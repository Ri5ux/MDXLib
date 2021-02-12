package com.asx.mdx.core.mods;

import net.minecraftforge.fml.common.ModContainer;

public interface IMod
{
    /** Return the mod's ModContainer instance **/
    public ModContainer container();
}
