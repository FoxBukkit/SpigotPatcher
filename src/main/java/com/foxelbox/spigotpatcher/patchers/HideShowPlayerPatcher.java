package com.foxelbox.spigotpatcher.patchers;

import com.foxelbox.spigotpatcher.MethodPatcher;
import com.foxelbox.spigotpatcher.MethodPatcherVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class HideShowPlayerPatcher implements MethodPatcher {
    @Override
    public MethodVisitor getVisitor(int api, MethodVisitor parent) {
        return new MethodPatcherVisitor(api, parent) {
            @Override
            protected boolean checkMethodInsn(int opcode, String clazz, String name, String desc) {
                if(opcode == Opcodes.INVOKEVIRTUAL && clazz.endsWith("PlayerConnection") && name.equals("sendPacket")) {
                    visitInsn(Opcodes.POP);
                    visitInsn(Opcodes.ACONST_NULL);
                    System.out.println("HideShowPatcher: PATCHED (2 times = OK)");
                }
                return true;
            }
        };
    }
}
