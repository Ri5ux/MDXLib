package com.asx.mdx.lib.world.block;

import java.util.ArrayList;
import java.util.Arrays;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.world.Pos;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Blocks
{
    public static ArrayList<Pos> getCoordDataInRange(int posX, int posY, int posZ, int range)
    {
        ArrayList<Pos> data = new ArrayList<Pos>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    data.add(new Pos(x, y, z));
                }
            }
        }

        return data;
    }
    
    public static ArrayList<BlockPos> getPositionsInRange(int posX, int posY, int posZ, int range)
    {
        ArrayList<BlockPos> data = new ArrayList<BlockPos>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    data.add(new BlockPos(x, y, z));
                }
            }
        }

        return data;
    }

    public static ArrayList<Pos> getCoordDataInRangeIncluding(int posX, int posY, int posZ, int range, World world, Block... types)
    {
        ArrayList<Pos> data = new ArrayList<Pos>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    Pos coordData = new Pos(x, y, z);
                    Block block = coordData.getBlock(world);

                    if (Arrays.asList(types).contains(block))
                    {
                        data.add(coordData);
                    }
                }
            }
        }

        return data;
    }

    public static ArrayList<BlockPos> getBlocksInRangeIncluding(int posX, int posY, int posZ, int range, World world, Block... types)
    {
        ArrayList<BlockPos> data = new ArrayList<BlockPos>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    BlockPos position = new BlockPos(x, y, z);
                    
                    if (Arrays.asList(types).contains(world.getBlockState(position).getBlock()))
                    {
                        data.add(position);
                    }
                }
            }
        }

        return data;
    }

    public static ArrayList<Pos> getCoordDataInRangeExcluding(int posX, int posY, int posZ, int range, World world, Block... types)
    {
        ArrayList<Pos> data = new ArrayList<Pos>();

        for (int x = posX - range; x < posX + range * 2; x++)
        {
            for (int y = posY - range; y < posY + range * 2; y++)
            {
                for (int z = posZ - range; z < posZ + range * 2; z++)
                {
                    Pos coordData = new Pos(x, y, z);
                    Block block = coordData.getBlock(world);

                    if (!Arrays.asList(types).contains(block))
                    {
                        data.add(coordData);
                    }
                }
            }
        }

        return data;
    }

    public static String getDomain(Block block)
    {
        String domain = "minecraft:";

        if (block.getTranslationKey().contains(":"))
        {
            domain = (block.getTranslationKey().split(":")[0] + ":").replace("tile.", "");
        }

        return domain;
    }

    public static void setCreativeTab(Block block, CreativeTabs tab)
    {
        if (tab != null)
        {
            block.setCreativeTab(tab);
        }
    }

    public static float getBlockResistance(Block blockParent)
    {
        return MDX.access().getBlockResistance(blockParent);
    }

    public static float getBlockHardness(Block blockParent)
    {
        return MDX.access().getBlockResistance(blockParent);
    }
}
