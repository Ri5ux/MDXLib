package com.asx.mdx.lib.world;

import java.util.Iterator;

import net.minecraft.util.math.AxisAlignedBB;

public class CoordSelection implements Iterable<Pos>
{
    private Pos pos1;
    private Pos pos2;

    public CoordSelection(Pos pos1, Pos pos2)
    {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public static CoordSelection areaFromSize(Pos coord, int[] size)
    {
        if (size[0] <= 0 || size[1] <= 0 || size[2] <= 0)
        {
            throw new IllegalArgumentException();
        }

        return new CoordSelection(coord, new Pos(coord.x + size[0] - 1, coord.y + size[1] - 1, coord.z + size[2] - 1));
    }

    public Pos getPos1()
    {
        return pos1;
    }

    public void setPos1(Pos point1)
    {
        this.pos1 = point1;
    }

    public Pos getPos2()
    {
        return pos2;
    }

    public void setPos2(Pos point2)
    {
        this.pos2 = point2;
    }

    public Pos min()
    {
        return pos1.min(pos2);
    }

    public Pos max()
    {
        return pos1.max(pos2);
    }

    public double[] areaSize()
    {
        return new double[] { max().subtract(min()).x + 1, max().subtract(min()).y + 1, max().subtract(min()).z + 1 };
    }

    public boolean contains(Pos coord)
    {
        return coord.x >= min().x && coord.y >= min().y && coord.z >= min().z && coord.x <= max().x && coord.y <= max().y && coord.z <= max().z;
    }

    public AxisAlignedBB asAxisAlignedBB()
    {
        return new AxisAlignedBB(min().x, min().y, min().z, max().x, max().y, max().z);
    }

    @Override
    public Iterator<Pos> iterator()
    {
        return new CoordSelectionIterator(min(), max());
    }
}