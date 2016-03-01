package com.foxelbox.spigotpatcher;

import com.foxelbox.spigotpatcher.patchers.GetOnlinePlayersPatcher;
import com.foxelbox.spigotpatcher.patchers.HideShowPlayerPatcher;
import com.foxelbox.spigotpatcher.patchers.OnPlayerJoinDisconnectPatcher;
import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;

public class ClassVisitorPatcher extends ClassVisitor {
    static HashMap<String, HashMap<String, MethodPatcher>> methodVisitors = new HashMap<>();
    static int haveCraftServer = 0;

    static void addPatcher(String clazz, String method, MethodPatcher patcher) {
        HashMap<String, MethodPatcher> patcherMap = methodVisitors.get(clazz);
        if(patcherMap == null) {
            patcherMap = new HashMap<>();
            methodVisitors.put(clazz, patcherMap);
        }
        patcherMap.put(method, patcher);
    }

    static {
        addPatcher("CraftPlayer", "hidePlayer", new HideShowPlayerPatcher());
        addPatcher("CraftPlayer", "showPlayer", new HideShowPlayerPatcher());
        addPatcher("PlayerList", "onPlayerJoin", new OnPlayerJoinDisconnectPatcher());
        addPatcher("PlayerList", "disconnect", new OnPlayerJoinDisconnectPatcher());
    }

    static class ClassTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            String myClassName = className.replaceFirst("^.+[\\./]", "");
            if(methodVisitors.containsKey(myClassName)) {
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                ClassVisitorPatcher patcher = new ClassVisitorPatcher(classWriter, methodVisitors.remove(myClassName));
                classReader.accept(patcher, 0);
                return classWriter.toByteArray();
            } else if(myClassName.equals("CraftServer") || myClassName.equals("Server")) {
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classReader.accept(new GetOnlinePlayersPatcher(classWriter), 0);
                haveCraftServer++;
                return classWriter.toByteArray();
            } else if(methodVisitors.isEmpty() && haveCraftServer >= 2) {
                SpigotPatcherPremain.instrumentation.removeTransformer(this);
                SpigotPatcherPremain.instrumentation = null;
                methodVisitors = null;
                System.out.println("All patched up");
            }

            return classfileBuffer;
        }
    }

    private final HashMap<String, MethodPatcher> methodPatchers;

    ClassVisitorPatcher(ClassVisitor classVisitor, HashMap<String, MethodPatcher> methodPatchers) {
        super(Opcodes.ASM5, classVisitor);
        this.methodPatchers = methodPatchers;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if(methodPatchers.containsKey(name)) {
            return methodPatchers.remove(name).getVisitor(api, super.visitMethod(access, name, desc, signature, exceptions));
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}
