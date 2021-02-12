package com.asx.mdx.lib.world.tile;

import com.asx.mdx.lib.world.Pos;

import net.minecraft.tileentity.TileEntity;

public class TileEntities
{
    /**
     * Move the specified TileEntity to the specified CoordData coordinates.
     * 
     * @param tileEntity - The TileEntity to change the position of.
     * @param pos - The CoordData instance containing the new set of coordinates.
     */
    public static void setTileEntityPosition(TileEntity tileEntity, Pos pos)
    {
        tileEntity.setPos(pos.blockPos());
    }

    /**
     * Move the specified TileEntity by the specified amount of coordinates.
     * 
     * @param tileEntity - The TileEntity to be moved.
     * @param coord - The CoordData instance containing the coordinates to add.
     */
    public static void moveTileEntity(TileEntity tileEntity, Pos coord)
    {
        setTileEntityPosition(tileEntity, (new Pos(tileEntity)).add(coord));
    }

}