package com.arisux.mdx.lib.game;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IPreInitEvent
{
    @Mod.EventHandler
    public void pre(IMod mod, FMLPreInitializationEvent event);
}
