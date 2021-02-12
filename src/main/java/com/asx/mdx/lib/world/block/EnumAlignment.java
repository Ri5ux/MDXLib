package com.asx.mdx.lib.world.block;

import net.minecraft.util.IStringSerializable;

//TODO: This may have the same functionality as @BlockSide.java
public enum EnumAlignment implements IStringSerializable
{
    TOP("top"), BOTTOM("bottom"), SIDE("side");

    private final String NAME;

    private EnumAlignment(String name)
    {
        this.NAME = name;
    }

    public String toString()
    {
        return this.NAME;
    }

    public String getName()
    {
        return this.NAME;
    }
}