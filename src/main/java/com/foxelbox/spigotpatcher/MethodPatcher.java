package com.foxelbox.spigotpatcher;

import org.objectweb.asm.MethodVisitor;

public interface MethodPatcher {
    MethodVisitor getVisitor(int api, MethodVisitor parent);
}
