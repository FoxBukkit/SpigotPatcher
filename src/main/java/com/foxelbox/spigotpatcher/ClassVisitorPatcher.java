/**
 * This file is part of SpigotPatcher.
 *
 * SpigotPatcher is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpigotPatcher is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SpigotPatcher.  If not, see <http://www.gnu.org/licenses/>.
 */
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
