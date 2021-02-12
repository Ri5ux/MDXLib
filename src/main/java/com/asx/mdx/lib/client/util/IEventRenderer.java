package com.asx.mdx.lib.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public interface IEventRenderer
{
    public void update(Event event, Minecraft game, World world);
    
    public void render(Event event, float partialTicks);
}