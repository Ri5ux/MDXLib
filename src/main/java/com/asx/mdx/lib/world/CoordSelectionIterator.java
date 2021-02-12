package com.asx.mdx.lib.world;

import java.util.Iterator;

public class CoordSelectionIterator implements Iterator<Pos>
{
    private Pos min;
    private Pos max;

    private int curX;
    private int curY;
    private int curZ;

    public CoordSelectionIterator(Pos min, Pos max)
    {
        this.min = min;
        this.max = max;
        this.curX = (int) min.x;
        this.curY = (int) min.y;
        this.curZ = (int) min.z;
    }

    @Override
    public boolean hasNext()
    {
        return this.curX <= this.max.x && this.curY <= this.max.y && this.curZ <= this.max.z;
    }

    @Override
    public Pos next()
    {
        Pos coord = this.hasNext() ? new Pos(this.curX, this.curY, this.curZ) : null;

        this.curX++;

        if (this.curX > this.max.x)
        {
            this.curX = (int) this.min.x;
            this.curY++;

            if (this.curY > this.max.y)
            {
                this.curY = (int) this.min.y;
                this.curZ++;
            }
        }

        return coord;
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}