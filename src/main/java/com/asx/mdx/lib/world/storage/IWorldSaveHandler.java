package com.asx.mdx.lib.world.storage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface IWorldSaveHandler
{
    public boolean saveData(World world, CompoundNBT nbt);
    
    public boolean loadData(World world, CompoundNBT nbt);
}