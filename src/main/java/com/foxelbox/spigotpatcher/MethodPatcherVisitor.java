package com.foxelbox.spigotpatcher;

import org.objectweb.asm.MethodVisitor;

public abstract class MethodPatcherVisitor extends MethodVisitor {
    public MethodPatcherVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    protected abstract boolean checkMethodInsn(int opcode, String clazz, String name, String desc);

    @Override
    public void visitMethodInsn(int opcode, String clazz, String name, String desc, boolean isInterface) {
        if(checkMethodInsn(opcode, clazz, name, desc)) {
            super.visitMethodInsn(opcode, clazz, name, desc, isInterface);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String clazz, String name, String desc) {
        if(checkMethodInsn(opcode, clazz, name, desc)) {
            super.visitMethodInsn(opcode, clazz, name, desc);
        }
    }
}
