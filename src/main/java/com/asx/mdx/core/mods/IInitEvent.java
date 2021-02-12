package com.asx.mdx.core.mods;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public interface IInitEvent
{
    @Mod.EventHandler
    public void init(FMLInitializationEvent event);
}
