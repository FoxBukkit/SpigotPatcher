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
