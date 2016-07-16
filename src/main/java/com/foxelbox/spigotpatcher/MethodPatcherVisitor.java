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
