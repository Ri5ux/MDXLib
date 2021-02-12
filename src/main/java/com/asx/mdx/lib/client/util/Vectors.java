package com.asx.mdx.lib.client.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class Vectors
{
    public static Vec3d[] buildVecs(EnumFacing facing, Vec3d[] vecs)
    {
        Set<Vec3d> set = new HashSet<Vec3d>(Arrays.asList(vecs));

        if (set.size() == 4)
        {
            return sortQuad(facing, vecs);
        }

        return null;
    }

    public static boolean isValid(Quad quad)
    {
        return quad != null && quad.getVectors() != null && quad.getVectors().length == 4;
    }

    public static Vec3d getNormal(Quad quad)
    {
        Vec3d[] vecs1 = new LinkedHashSet<Vec3d>(Arrays.asList(quad.getVectors())).toArray(new Vec3d[quad.getVectors().length]);
        return (vecs1[1].subtract(vecs1[0])).crossProduct(vecs1[2].subtract(vecs1[1])).normalize();
    }

    private static Vec3d[] sortQuad(EnumFacing facing, Vec3d[] vecs)
    {
        List<Vec3d> consumables = new LinkedList<Vec3d>(Arrays.asList(vecs));
        Vec3d[] newVecs = { vecs[0], vecs[1], vecs[2], vecs[3] };
        UV[] bounds = getBoundingPlane(facing, vecs);

        for (int i = 0; i < bounds.length; ++i)
        {
            Vec3d closest = null;
            float minDist = 0;

            for (Vec3d vec : consumables)
            {
                float dist = distSq(facing, bounds[i], vec);

                if (closest == null || dist < minDist)
                {
                    minDist = dist;
                    closest = vec;
                }
            }

            consumables.remove(closest);
            newVecs[i] = closest;
        }

        return newVecs;
    }

    private static UV[] getBoundingPlane(EnumFacing facing, Vec3d[] vecs)
    {
        float minU = 0;
        float maxU = 16;
        float minV = 0;
        float maxV = 16;

        for (Vec3d vec : vecs)
        {
            UV UV = new UV(facing, vec);

            minU = Math.min(minU, UV.getU());
            maxU = Math.max(maxU, UV.getU());
            minV = Math.min(minV, UV.getV());
            maxV = Math.max(maxV, UV.getV());
        }

        switch (facing)
        {
            case DOWN:
                return new UV[] { new UV(minU, maxV), new UV(minU, minV), new UV(maxU, minV), new UV(maxU, maxV) };
            case UP:
                return new UV[] { new UV(minU, minV), new UV(minU, maxV), new UV(maxU, maxV), new UV(maxU, minV) };
            case NORTH:
                return new UV[] { new UV(maxU, maxV), new UV(maxU, minV), new UV(minU, minV), new UV(minU, maxV) };
            case SOUTH:
                return new UV[] { new UV(minU, maxV), new UV(minU, minV), new UV(maxU, minV), new UV(maxU, maxV) };
            case WEST:
                return new UV[] { new UV(minU, maxV), new UV(minU, minV), new UV(maxU, minV), new UV(maxU, maxV) };
            case EAST:
                return new UV[] { new UV(maxU, maxV), new UV(maxU, minV), new UV(minU, minV), new UV(minU, maxV) };
            default:
                return null;
        }
    }

    private static float distSq(EnumFacing facing, UV to, Vec3d from)
    {
        UV tmp = new UV(facing, from);
        float u = tmp.getU() - to.getU();
        float v = tmp.getV() - to.getV();

        return u * u + v * v;
    }
}
