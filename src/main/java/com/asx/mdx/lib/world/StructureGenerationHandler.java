package com.asx.mdx.lib.world;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class StructureGenerationHandler
{
    public static final StructureGenerationHandler INSTANCE          = new StructureGenerationHandler();
    private ArrayList<Structure>                   structuresInQueue = new ArrayList<Structure>();

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event)
    {
        Iterator<Structure> iter = structuresInQueue.iterator();

        while (iter.hasNext())
        {
            Structure structure = iter.next();

            if (structure.process())
            {
                iter.remove();
            }
        }
    }

    public static void addStructureToQueue(Structure structure)
    {
        getStructuresInQueue().add(structure);
    }

    public static ArrayList<Structure> getStructuresInQueue()
    {
        return INSTANCE.structuresInQueue;
    }
}
