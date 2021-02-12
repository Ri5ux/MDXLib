package com.asx.mdx.lib.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMaterial extends Block
{
    private BlockRenderLayer renderLayer;
    
    public BlockMaterial(Material material)
    {
        super(material);
        this.renderLayer = BlockRenderLayer.SOLID;
        this.setLightOpacity(255);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return this.renderLayer;
    }

    public BlockMaterial setLayer(BlockRenderLayer renderLayer)
    {
        this.renderLayer = renderLayer;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        if (this.renderLayer == BlockRenderLayer.TRANSLUCENT)
        {
            if (blockState != iblockstate)
            {
                return true;
            }

            if (block == this)
            {
                return false;
            }
        }

        return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return this.renderLayer == BlockRenderLayer.TRANSLUCENT ? false : super.isOpaqueCube(state);
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return this.renderLayer == BlockRenderLayer.TRANSLUCENT ? false : super.isFullCube(state);
    }
}
