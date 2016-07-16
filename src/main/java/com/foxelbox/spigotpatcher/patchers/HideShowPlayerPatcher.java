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
import com.foxelbox.spigotpatcher.MethodPatcherVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class HideShowPlayerPatcher implements MethodPatcher {
    @Override
    public MethodVisitor getVisitor(int api, MethodVisitor parent) {
        return new MethodPatcherVisitor(api, parent) {
            @Override
            protected boolean checkMethodInsn(int opcode, String clazz, String name, String desc) {
                if(opcode == Opcodes.INVOKEVIRTUAL && clazz.endsWith("/PlayerConnection") && name.equals("sendPacket")) {
                    visitInsn(Opcodes.POP);
                    visitInsn(Opcodes.ACONST_NULL);
                    System.out.println("HideShowPatcher: PATCHED (2 times = OK)");
                }
                return true;
            }
        };
    }
}
