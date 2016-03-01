package com.foxelbox.spigotpatcher.patchers;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GetOnlinePlayersPatcher extends ClassVisitor {
    public GetOnlinePlayersPatcher(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (name.equals("_INVALID_getOnlinePlayers")) {
            System.out.println("Patching GOPP: " + name);
            name = "getOnlinePlayers";
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}
