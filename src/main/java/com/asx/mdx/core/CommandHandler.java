package com.asx.mdx.core;

import com.asx.mdx.commands.CommandBlockScanner;
import com.asx.mdx.commands.CommandBlockUpdate;
import com.asx.mdx.commands.CommandChunkBorders;
import com.asx.mdx.commands.CommandChunkPlane;
import com.asx.mdx.commands.CommandGenerate;
import com.asx.mdx.commands.CommandKeyframeDifferenceCalculator;
import com.asx.mdx.commands.CommandTeleportDimension;
import com.asx.mdx.core.mods.IInitEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

public class CommandHandler implements IInitEvent
{
    public static final CommandHandler INSTANCE = new CommandHandler();

    public CommandGenerate             generate;
    public CommandBlockUpdate          blockUpdate;
    public CommandBlockScanner         blockScanner;
    public CommandChunkBorders         chunkBorders;
    public CommandChunkPlane           chunkPlane;

    @Override
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(this.generate = new CommandGenerate());
        event.registerServerCommand(this.blockUpdate = new CommandBlockUpdate());
        event.registerServerCommand(this.blockScanner = new CommandBlockScanner());
        event.registerServerCommand(this.chunkBorders = new CommandChunkBorders());
        event.registerServerCommand(this.chunkPlane = new CommandChunkPlane());
        event.registerServerCommand(new CommandTeleportDimension());

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
        {
            event.registerServerCommand(new CommandKeyframeDifferenceCalculator());
        }
    }
}
