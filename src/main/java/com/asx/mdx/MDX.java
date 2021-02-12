package com.asx.mdx;

import com.sun.scenario.Settings;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;

import com.asx.mdx.lib.client.Renderers;

import java.io.Console;

@Mod("mdxlib")
public class MDX {
    public static final String ID = "mdxlib";


    /** Provides access to core Minecraft methods that have restricted access **/
    private static Access access;

   public MDX()
   {
        access = new Access();
    }

    public static MDX instance()
   {
        return MDXModule.instance();
    }

    public static Access access()
   {
        return access;
   }

    public static Console console()
   {
        return Console.INSTANCE;
    }

    public static Logger log()
    {
    return Console.LOGGER;
   }

    public static Settings settings()
    {
        return Settings.INSTANCE;
    }

    public static PacketHandler network()
   {
       return PacketHandler.instance;
    }

   @SideOnly(Side.CLIENT)
   public static Notifications notifications()
   {
        return Notifications.INSTANCE;
    }

    @SideOnly(Side.CLIENT)
    public static WindowAPI windows()
    {
        return WindowAPI.INSTANCE;
    }

    @SideOnly(Side.CLIENT)
   public static Renderers renders()
   {
       return Renderers.INSTANCE;
   }
}
