package com.asx.mdx.lib.client;

import java.util.ArrayList;

import com.asx.mdx.lib.client.util.entity.PlayerResource;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerResourceStorage
{
    private ArrayList<PlayerResource> playerResources = new ArrayList<PlayerResource>();

    public PlayerResource create(String username)
    {
        if (lookup(username) == null)
        {
            this.playerResources.add(new PlayerResource(username));
        }

        return lookup(username);
    }

    public void removeResource(String username)
    {
        this.playerResources.remove(lookup(username));
    }

    public ArrayList<PlayerResource> getResourceStore()
    {
        return this.playerResources;
    }

    public PlayerResource lookup(String username)
    {
        for (PlayerResource player : this.playerResources)
        {
            if (player.playerName().equalsIgnoreCase(username))
            {
                return player;
            }
        }

        return null;
    }
}