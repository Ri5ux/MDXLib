package com.asx.mdx.lib.client.util.entity;

import com.asx.mdx.lib.world.entity.player.Players;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerResource
{
    private String name, uuid;
    private ResourceLocation resource;

    public PlayerResource(String username)
    {
        this.name = username;
        this.uuid = username;

        Thread uuidThread = new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                uuid = Players.getUUID(name);
            }
        };
        uuidThread.start();
    }

    public String playerName()
    {
        return name;
    }

    public String playerUUID()
    {
        return uuid;
    }

    public ResourceLocation get()
    {
        return this.resource;
    }

    public ResourceLocation store(ResourceLocation resource)
    {
        return this.resource = resource;
    }
}
