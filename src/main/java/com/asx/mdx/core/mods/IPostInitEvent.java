package com.asx.mdx.core.mods;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public interface IPostInitEvent
{
    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event);
}
