package com.asx.mdx.lib.client.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class Quad
{
    private Vec3d[]    vectors;
    private EnumFacing facing;

    private Quad()
    {
        super();
    }

    private Quad(EnumFacing facing, Vec3d ... vectors)
    {
        this.facing = facing;
        this.vectors = vectors;
    }

    public static Quad get(EnumFacing facing, Vec3d ... vecs)
    {
        Vec3d[] builtVecs = Vectors.buildVecs(facing, vecs);

        if (builtVecs == null || builtVecs.length != 4)
        {
            return null;
        }

        return new Quad(facing, builtVecs);
    }

    public Quad offset(double x, double y, double z)
    {
        for (int i = 0; i < this.vectors.length; ++i)
        {
            this.vectors[i] = this.vectors[i].add(x, y, z);
        }
        
        return this;
    }

    public EnumFacing getFacing()
    {
        return this.facing;
    }

    public void setFacing(EnumFacing facing)
    {
        this.facing = facing;
    }

    public Vec3d[] getVectors()
    {
        return this.vectors;
    }
}
