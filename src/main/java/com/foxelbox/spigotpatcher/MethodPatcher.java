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

public interface MethodPatcher {
    MethodVisitor getVisitor(int api, MethodVisitor parent);
}
