package com.asx.mdx.lib.util.command;

import net.minecraft.entity.player.EntityPlayer;

public interface IClientCommand
{
    public void executeClient(EntityPlayer player, String[] args);
}
