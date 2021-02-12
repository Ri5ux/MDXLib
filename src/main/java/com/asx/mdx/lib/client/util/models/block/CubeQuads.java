package com.asx.mdx.lib.client.util.models.block;

import com.asx.mdx.lib.client.util.Quad;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class CubeQuads implements ICubeQuads
{
    @Override
    public Quad getYNeg()
    {
        return Quad.get(EnumFacing.DOWN, new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 1.0D));
    }

    @Override
    public Quad getYPos()
    {
        return Quad.get(EnumFacing.UP, new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(1.0D, 1.0D, 0.0D));
    }

    @Override
    public Quad getZNeg()
    {
        return Quad.get(EnumFacing.NORTH, new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, 0.0D));
    }

    @Override
    public Quad getZPos()
    {
        return Quad.get(EnumFacing.SOUTH, new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 1.0D, 1.0D));
    }

    @Override
    public Quad getXNeg()
    {
        return Quad.get(EnumFacing.WEST, new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 1.0D, 1.0D));
    }

    @Override
    public Quad getXPos()
    {
        return Quad.get(EnumFacing.EAST, new Vec3d(1.0D, 1.0D, 1.0D), new Vec3d(1.0D, 0.0D, 1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 1.0D, 0.0D));
    }
}
