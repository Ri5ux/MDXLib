package com.asx.mdx.lib.world.item;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HookedItem extends Item
{
    public HookedItem()
    {
        super();
    }

    public Item setDescription(String desc)
    {
        return this;
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        String descriptionKey = String.format("%s.desc", this.getTranslationKey());

        if (descriptionKey != null)
        {
            String[] lines = I18n.format(descriptionKey).split("/n");

            for (String line : lines)
            {
                if (!line.equals(descriptionKey))
                {
                    tooltip.add(line);
                }
            }
        }
    }
}
