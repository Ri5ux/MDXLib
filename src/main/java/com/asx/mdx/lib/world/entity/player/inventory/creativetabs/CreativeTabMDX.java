package com.asx.mdx.lib.world.entity.player.inventory.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CreativeTabMDX extends CreativeTabs
{
    public static final CreativeTabs INSTANCE = new CreativeTabMDX();

    public CreativeTabMDX()
    {
        super("MDX");
    }
    
    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(Items.REPEATER);
    }
}
