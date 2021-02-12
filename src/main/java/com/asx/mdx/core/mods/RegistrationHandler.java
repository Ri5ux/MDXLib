package com.asx.mdx.core.mods;

import com.asx.mdx.lib.client.Renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class RegistrationHandler<MOD extends IMod> implements IPreInitEvent, IInitEvent, IPostInitEvent
{
    protected MOD mod;
    
    public RegistrationHandler(MOD mod)
    {
        this.mod = mod;
    }

    @Override
    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent event)
    {
        ;
    }

    @Override
    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ;
    }
    
    @Override
    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event)
    {
        ;
    }

    public <T extends IForgeRegistryEntry<T>> T register(RegistryEvent.Register<T> registryEvent, String registryName, T registryObject)
    {
        ResourceLocation resource = new ResourceLocation(mod.container().getModId(), registryName);

        if (registryObject instanceof Block)
        {
            Block block = (Block) registryObject;
            block.setRegistryName(registryName);
            block.setTranslationKey(resource.toString());
        }

        if (registryObject instanceof Item)
        {
            Item item = (Item) registryObject;
            item.setRegistryName(registryName);
            item.setTranslationKey(resource.toString());

            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            {
                if (item instanceof ItemBlock)
                {
                    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
                }
                else if (item instanceof Item)
                {
                    Renderers.registerIcon(item);
                }
            }
        }

        registryEvent.getRegistry().register(registryObject);

        return registryObject;
    }
}
