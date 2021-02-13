package com.asx.mdx.lib.util.command;

import net.minecraft.entity.player.PlayerEntity;

public interface IClientCommand
{
    public void executeClient(PlayerEntity player, String[] args);
}
