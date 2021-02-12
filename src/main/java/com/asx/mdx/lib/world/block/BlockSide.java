package com.asx.mdx.lib.world.block;

public enum BlockSide
{
    DEFAULT(-1),
    BOTTOM(0) /** DOWN **/,
    TOP(1) /** UP **/,
    BACK(2) /** EAST **/,
    FRONT(3) /** WEST **/,
    LEFT(4) /** NORTH **/,
    RIGHT(5) /** SOUTH **/;

    private int id;

    BlockSide(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public static BlockSide getSide(int sideId)
    {
        for (BlockSide side : values())
        {
            if (side.id == sideId)
            {
                return side;
            }
        }

        return null;
    }
}
