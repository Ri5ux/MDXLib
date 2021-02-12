package com.asx.mdx.lib.world.block;

import java.util.ArrayList;

import com.asx.mdx.lib.util.Game;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public interface IMultiBlock
{
    public BlockPos[] defaultSet();

    public default BlockPos[] setFor(EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return north();
            case SOUTH:
                return south();
            case EAST:
                return east();
            case WEST:
                return west();
            default:
                return defaultSet();
        }
    }

    public default BlockPos[] north()
    {
        if (defaultSet() != null)
        {
            BlockPos[] set = defaultSet();
            BlockPos[] newSet = new BlockPos[set.length];
            int idx = 0;

            for (BlockPos pos : set)
            {
                newSet[idx++] = invert(pos);
            }

            return newSet;
        }
        return null;
    }

    public default BlockPos[] south()
    {
        return defaultSet();
    }

    public default BlockPos[] east()
    {
        if (defaultSet() != null)
        {
            BlockPos[] set = defaultSet();
            BlockPos[] newSet = new BlockPos[set.length];
            int idx = 0;

            for (BlockPos pos : set)
            {
                newSet[idx++] = flip(invert(pos));
            }

            return newSet;
        }
        return null;
    }

    public default BlockPos[] west()
    {
        if (defaultSet() != null)
        {
            BlockPos[] set = defaultSet();
            BlockPos[] newSet = new BlockPos[set.length];
            int idx = 0;

            for (BlockPos pos : set)
            {
                newSet[idx++] = flip(pos);
            }

            return newSet;
        }
        return null;
    }

    public static BlockPos flip(BlockPos pos)
    {
        return new BlockPos(pos.getZ(), pos.getY(), pos.getX());
    }

    public static BlockPos invert(BlockPos pos)
    {
        return new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
    }
    
    public int getTicksExisted();
    
    public void tick();

    public boolean isParent();

    public TileEntity getParent();

    public void setParent(TileEntity parent);

    public void setTileParent();

    public boolean isChild();

    public void setTileChild();

    public ArrayList<TileEntity> getChildren();

    public void setPlacedByPlayer();

    public boolean isPlacedByPlayer();

    public EnumFacing getRotationYAxis();

    public Block getMultiBlockType();

    public String getMultiblockName();
    
    public default void updateMultiblock(World world)
    {
        this.tick();
        
        if (this.isParent() && this.getTicksExisted() > 1 && !this.isPlacedByPlayer())
        {
            this.setup(world, false);
        }
    }

    public default void addToParent(TileEntity parent)
    {
        if (parent instanceof IMultiBlock)
        {
            IMultiBlock multiblockParent = (IMultiBlock) parent;
            TileEntity thisTile = (TileEntity) this;
            
            if (!multiblockParent.getChildren().contains(thisTile))
            {
                multiblockParent.getChildren().add(thisTile);
            }

            this.setParent(parent);
        }
    }

    public default boolean setup(World world, boolean placedByPlayer)
    {
        TileEntity thisTile = (TileEntity) this;
        
        this.setTileParent();
        this.setPlacedByPlayer();

        if (this.getRotationYAxis() != null)
        {
            BlockPos[] set = this.setFor(this.getRotationYAxis());

            for (BlockPos offset : set)
            {
                BlockPos position = thisTile.getPos().add(offset);

                if (!this.setChildTile(world, position))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public default boolean setChildTile(World world, BlockPos position)
    {
        if (this instanceof TileEntity)
        {
            TileEntity thisTile = (TileEntity) this;
            IBlockState blockstate = world.getBlockState(position);
            TileEntity tileAtPos = (TileEntity) world.getTileEntity(position);
            Block block = blockstate.getBlock();

            boolean validBlastDoor = true;

            if (block == this.getMultiBlockType())
            {
                IMultiBlock multiblockAtPos = (IMultiBlock) tileAtPos;
                validBlastDoor = multiblockAtPos.getParent() != null ? false : true;
            }

            if (blockstate.getMaterial() != Material.AIR && block != this.getMultiBlockType() || !validBlastDoor)
            {
                if (world.isRemote)
                {
                    if (validBlastDoor == false)
                    {
                        Game.minecraft().player.sendMessage(new TextComponentString("Can't place a " + this.getMultiblockName() + " inside of another " + this.getMultiblockName() + "."));
                    }
                    else
                    {
                        Game.minecraft().player.sendMessage(new TextComponentString("Unable to place a " + this.getMultiblockName() + " here. Blocks are in the way."));
                    }
                }

                return false;
            }

            world.setBlockState(position, this.getMultiBlockType().getDefaultState());
            TileEntity tile = (TileEntity) world.getTileEntity(position);
            IMultiBlock multiblockTile = (IMultiBlock) tile;

            if (tile == null)
            {
                Game.minecraft().player.sendMessage(new TextComponentString("Internal Error: IMultiBlock.setChildTile()/multiblockTile = null"));
                return false;
            }

            if (tile != null)
            {
                multiblockTile.addToParent(thisTile);
                multiblockTile.setParent(thisTile);
            }

            return true;
        }

        return false;
    }

    public default void breakChildren(World world)
    {
        for (TileEntity child : this.getChildren())
        {
            world.setBlockToAir(child.getPos());
        }
    }
}
