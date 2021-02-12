package com.asx.mdx.lib.client;

import java.util.HashMap;

import com.asx.mdx.MDX;
import com.asx.mdx.core.mods.IPreInitEvent;
import com.asx.mdx.lib.client.model.loaders.DummyModelLoader;
import com.asx.mdx.lib.client.model.loaders.DummyModelLoader.Type;
import com.asx.mdx.lib.client.util.ItemIconRenderer;
import com.asx.mdx.lib.client.util.ItemRenderer;
import com.asx.mdx.lib.util.Game;

import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderers implements IPreInitEvent
{
    public static final Renderers                    INSTANCE       = new Renderers();
    private final HashMap<Item, ItemRenderer<?>>     ITEM_RENDERERS = new HashMap<Item, ItemRenderer<?>>();
    private final HashMap<Item, ItemIconRenderer<?>> ICON_RENDERERS = new HashMap<Item, ItemIconRenderer<?>>();

    public static ModelManager modelManager()
    {
        return Game.minecraft().modelManager;
    }

    public static BlockModelShapes modelProvider()
    {
        return modelManager().getBlockModelShapes();
    }

    /**
     * A better way of registering entity renderers.
     */
    public static <E extends Entity, R extends Render<? super E>> void registerRenderer(Class<E> entityClass, Class<R> renderer)
    {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<E>()
        {
            @Override
            public Render<E> createRenderFor(RenderManager m)
            {
                try
                {
                    Render<E> render = (Render<E>) (renderer.getConstructor(RenderManager.class)).newInstance(new Object[] { m });
                    return render;
                }
                catch (Exception e)
                {
                    MDX.log().warn("Failed to construct entity renderer: " + (renderer != null ? renderer.getName() : renderer));
                    e.printStackTrace();
                }

                return null;
            }
        });
    }
    
    public static void registerBlockItemRenderer(ItemBlock item, ItemRenderer<?> renderer)
    {
        registerItemRenderer(item, renderer);
    }

    /**
     * Working replacement for ItemRenderer, but should only be used in rare cases when
     * the built in rendering system is not sufficient.
     */
    public static void registerItemRenderer(Item item, ItemRenderer<?> renderer)
    {
        if (item != null && item.getRegistryName() == null || item == null || renderer == null || item == Items.AIR)
        {
            if (item == null)
            {
                MDX.log().warn("Failed to register item renderer: NULL");
                Exception e = new Exception();
                e.printStackTrace();
                return;
            }
            
            MDX.log().warn("Failed to register item renderer for item " + item.getRegistryName());
            return;
        }
        
        if (getItemRenderer(item) == null)
        {
            if (INSTANCE.ICON_RENDERERS.containsKey(item))
            {
                MDX.log().warn("%s has already been registered once. Removing the previous registration and proceeding.", item.getRegistryName());
                INSTANCE.ICON_RENDERERS.remove(item);
            }

            INSTANCE.ITEM_RENDERERS.put(item, renderer);
            //MDX.log().info("Registered item renderer for item " + item.getRegistryName());
        }
    }

    public static void registerIcon(Item item)
    {
        ItemIconRenderer<?> renderer = new ItemIconRenderer<>(item);

        if (item != null && item.getRegistryName() == null || renderer == null)
        {
            MDX.log().warn("Failed to register Item Icon Renderer for item: " + item.getRegistryName());
            return;
        }

        if (getItemRenderer(item) == null)
        {
            INSTANCE.ICON_RENDERERS.put(item, renderer);
            DummyModelLoader.INSTANCE.registerDummy(Type.ITEM, item.getRegistryName());
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            //MDX.log().info("Registered icon for " + item.getRegistryName());
        }
    }

    public static ItemRenderer<?> getItemRenderer(Item item)
    {
        if (INSTANCE.ITEM_RENDERERS.containsKey(item))
        {
            return INSTANCE.ITEM_RENDERERS.get(item);
        }

        return null;
    }

    public static ItemRenderer<?> getIconRenderer(Item item)
    {
        if (INSTANCE.ICON_RENDERERS.containsKey(item))
        {
            return INSTANCE.ICON_RENDERERS.get(item);
        }

        return null;
    }

    @Override
    public void pre(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelRegistry(ModelRegistryEvent event)
    {
        MDX.log().info("Registering models...");
        
        for (Item item : ITEM_RENDERERS.keySet())
        {
            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ItemRenderer<?> itemRenderer = ITEM_RENDERERS.get(item);

            if (itemRenderer != null)
            {
                ModelLoader.setCustomModelResourceLocation(item, 0, resource);
                //System.out.println("Registering model for " + item.getRegistryName() + ", " + itemRenderer);
            }
        }

        for (Item item : ICON_RENDERERS.keySet())
        {
            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ItemRenderer<?> itemRenderer = ICON_RENDERERS.get(item);

            if (itemRenderer != null)
            {
                DummyModelLoader.INSTANCE.registerDummy(Type.ITEM, item.getRegistryName());
                ModelLoader.setCustomModelResourceLocation(item, 0, resource);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        MDX.log().info("Injecting item renderers...");
        
        for (Item item : ITEM_RENDERERS.keySet())
        {
            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ItemRenderer<?> itemRenderer = ITEM_RENDERERS.get(item);

            if (itemRenderer != null)
            {
                event.getModelRegistry().putObject(resource, itemRenderer);
                //MDX.log().info("Injecting item renderer in place of default model for " + item.getRegistryName());
            }
            else
            {
                MDX.log().warn(String.format("Error injecting item renderer (%s): %s", itemRenderer, resource));
            }
        }

        for (Item item : ICON_RENDERERS.keySet())
        {
            ModelResourceLocation resource = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ItemRenderer<?> itemRenderer = ICON_RENDERERS.get(item);

            if (itemRenderer != null)
            {
                event.getModelRegistry().putObject(resource, itemRenderer);
            }
            else
            {
                MDX.log().warn(String.format("Error injecting icon renderer (%s): %s", itemRenderer, resource));
            }
        }
    }
}
