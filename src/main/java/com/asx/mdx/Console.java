package com.asx.mdx;


import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.core.mods.IPostInitEvent;
import com.asx.mdx.core.mods.IPreInitEvent;



import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Console implements IPreInitEvent, IInitEvent, IPostInitEvent
{
    public static final Console INSTANCE = new Console();
    public static final Logger LOGGER   = LogManager.getLogManager().getLogger("MDX");

    @Override
    public void pre(FMLPreInitializationEvent event)
    {
        LOGGER.info("Preparing...");
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        LOGGER.info("Minecraft Development Library X Copyright \u00A9 2016-2025 ASX");
        LOGGER.info("Initializing...");
    }

    @Override
    public void post(FMLPostInitializationEvent event)
    {
        LOGGER.info("Initialized. Running post initialization tasks...");
    }

    public static void modificationWarning()
    {
        LOGGER.warning("Somebody has been tinkering with functionality that shouldn't be tinkered with!");
    }
}
