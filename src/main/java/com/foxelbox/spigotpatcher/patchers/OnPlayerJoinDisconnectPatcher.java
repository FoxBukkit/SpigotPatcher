package com.foxelbox.spigotpatcher.patchers;

import com.foxelbox.spigotpatcher.MethodPatcher;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class OnPlayerJoinDisconnectPatcher implements MethodPatcher {
    @Override
    public MethodVisitor getVisitor(int api, MethodVisitor parent) {
        return new MethodVisitor(api, parent) {
            protected void checkMethodInsn(int opcode, String clazz, String name, String desc) {
                if(opcode == Opcodes.INVOKEVIRTUAL && clazz.endsWith("/CraftPlayer") && name.equals("canSee")) {
                    visitInsn(Opcodes.POP);
                    visitInsn(Opcodes.ICONST_1);
                    System.out.println("OnJoinPatcher: PATCHED (3 times = OK)");
                }
            }

            @Override
            public void visitMethodInsn(int opcode, String clazz, String name, String desc, boolean isInterface) {
                super.visitMethodInsn(opcode, clazz, name, desc, isInterface);
                checkMethodInsn(opcode, clazz, name, desc);
            }

            @Override
            public void visitMethodInsn(int opcode, String clazz, String name, String desc) {
                super.visitMethodInsn(opcode, clazz, name, desc);
                checkMethodInsn(opcode, clazz, name, desc);
            }
        };
    }
}
