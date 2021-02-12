package com.asx.mdx.lib.world;

import java.util.ArrayList;

import com.asx.mdx.lib.world.Pos.BlockDataStore;
import com.asx.mdx.lib.world.storage.Schematic;

import net.minecraft.init.Blocks;
import net.minecraft.world.WorldServer;

public abstract class Structure
{
    private Schematic      schematic;
    private WorldServer    world;
    private Pos            data;
    private ArrayList<Pos> blockQueue;

    public Structure(Schematic schematic, WorldServer world, Pos data)
    {
        this.schematic = schematic;
        this.world = world;
        this.data = data;
        this.blockQueue = new ArrayList<Pos>();
        this.queueBlocks();
    }

    public Schematic getSchematic()
    {
        return schematic;
    }

    public WorldServer getWorld()
    {
        return world;
    }

    public Pos getData()
    {
        return data;
    }

    public ArrayList<Pos> getBlockQueue()
    {
        return blockQueue;
    }

    public void queueBlocks()
    {
        this.schematic.addBlocksToQueue(this);
    }

    public boolean process()
    {
        this.onProcessing();

        if (world.getWorldInfo().getWorldTime() % 80 == 0)
        {
            System.out.println("Generating " + this.getName() + ": " + this.blockQueue.size() + " blocks left in queue.");
        }

        try
        {
            int sectionSize = 10;
            int queueSize = this.blockQueue.size();

            if (queueSize < sectionSize)
            {
                sectionSize = queueSize;
            }

            for (int i = 0; i < sectionSize; i++)
            {
                Pos pos = this.blockQueue.get((this.blockQueue.size() - 1) - i);
                
                if (pos.store() instanceof BlockDataStore)
                {
                    BlockDataStore data = (BlockDataStore) pos.store();

                    if (data.asBlock() != Blocks.AIR)
                    {
                        this.world.setBlockState(pos.blockPos(), data.asBlock().getStateFromMeta(data.metadata), 2);
                    }
                }

                this.blockQueue.remove(pos);
            }

            if (this.blockQueue.size() <= 0)
            {
                this.onProcessingComplete();
                System.out.println("Generation of " + this.getName() + " completed.");

                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.onProcessingComplete();
            System.out.println("Generation of " + this.getName() + " completed with an error: " + e);

            return true;
        }

        return false;
    }

    public abstract String getName();

    public abstract void onProcessing();

    public abstract void onProcessingComplete();
}
