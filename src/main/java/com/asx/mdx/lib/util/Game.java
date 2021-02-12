package com.asx.mdx.lib.util;

import java.util.ArrayList;
import java.util.List;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.world.entity.player.inventory.Inventories;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A class designed to provide easy access to a variety of Minecraft core methods, some
 * of which may naturally have restricted access. The names of these methods will never change.
 */
public class Game
{
    /** A wrapper method to get the Minecraft instance. **/
    @SideOnly(Side.CLIENT)
    public static Minecraft minecraft()
    {
        return Minecraft.getMinecraft();
    }

    public static MinecraftServer server()
    {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    /** A wrapper method for the RenderManager instance. **/
    @SideOnly(Side.CLIENT)
    public static RenderManager renderManager()
    {
        return minecraft().getRenderManager();
    }

    /** A wrapper method for the FontRenderer instance. **/
    @SideOnly(Side.CLIENT)
    public static FontRenderer fontRenderer()
    {
        return Game.minecraft().fontRenderer;
    }

    /** Easy access to the partialTickTime variable. **/
    @SideOnly(Side.CLIENT)
    public static float partialTicks()
    {
        return Animation.getPartialTickTime();
    }

    /** Access to the Session instance. Please do not abuse this. **/
    @SideOnly(Side.CLIENT)
    public static Session session()
    {
        return MDX.access().getSession();
    }

    /** Easy way to set the right click delay timer variable. **/
    @SideOnly(Side.CLIENT)
    public static void setRightClickDelayTimer(int i)
    {
        MDX.access().setRightClickDelayTimer(i);
    }

    /** Easy way to set the current equipped progress variable. **/
    @SideOnly(Side.CLIENT)
    public static void setEquippedProgress(float f)
    {
        MDX.access().setEquippedProgress(f);
    }

    /** Easy way to gain access to the lightmap texture. **/
    public static DynamicTexture getLightmapTexture()
    {
        return MDX.access().getLightmapTexture();
    }

    /** Easy way to gain access to the lightmap colors array. **/
    public static int[] getLightmapColors()
    {
        return MDX.access().getLightmapColors();
    }

    /** Allows for the developer to specify if a lightmap update is required. **/
    public static void setLightmapUpdateNeeded(boolean b)
    {
        MDX.access().setLightmapUpdateNeeded(b);
    }

    /** Get the boss color modifier. **/
    public static float getBossColorModifier()
    {
        return MDX.access().getBossColorModifier();
    }

    /** Get the torch flicker X variable. **/
    public static float getTorchFlickerX()
    {
        return MDX.access().getTorchFlickerX();
    }

    /** Get the boss color modifier previous variable. **/
    public static float getBossColorModifierPrev()
    {
        return MDX.access().getBossColorModifierPrev();
    }

    /**
     * Returns if the current Minecraft installation is running 
     * in a development environment or normal environment.
     * 
     * @return Returns true if in a dev environment. Returns false if other.
     */
    public static boolean isDevEnvironment()
    {
        return (Boolean) net.minecraft.launchwrapper.Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    /** Register class instances through this method instead of the individual forge event busses. **/
    public static void registerEventHandler(Object handler)
    {
        //FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    /**
     * Finds all IRecipe instances registered to a specific Item or Block instance.
     * 
     * @param obj - Item or Block instance to scan for recipes.
     * @return All found instances of IRecipes registered to the specified Item or Block.
     */
    public static List<IRecipe> getRecipes(Object obj)
    {
        ItemStack stack = Inventories.newStack(obj);
        List<IRecipe> foundRecipes = new ArrayList<IRecipe>();

        if (stack != null)
        {
            for (ResourceLocation res : CraftingManager.REGISTRY.getKeys())
            {
                IRecipe recipe = CraftingManager.REGISTRY.getObject(res);
                
                if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == stack.getItem())
                {
                    foundRecipes.add(recipe);
                }
            }
        }

        return foundRecipes;
    }

    /**
     * Finds the first IRecipe instance registered to a specific Item or Block instance.
     * 
     * @param obj - Item or Block instance to scan for recipes.
     * @return First found instance of an IRecipe registered to the specified Item or Block.
     */
    public static IRecipe getRecipe(Object obj)
    {
        List<IRecipe> recipes = getRecipes(obj);
        
        if (recipes != null && recipes.size() > 0)
        {
            return recipes.get(0);
        }
        
        return null;
    }

    /**
     * Wrapper method for the registerKeyBinding method found in ClientRegistry. Allows for
     * more efficient registration of a KeyBinding. 
     * 
     * @param keyName - Name of the KeyBinding to be registered.
     * @param key - Integer assigned to each individual keyboard key in the Keyboard class.
     * @param keyGroup - Group of KeyBindings to assign this KeyBinding to.
     * @return The KeyBinding Instance created from the provided parameters.
     */
    @SideOnly(Side.CLIENT)
    public static KeyBinding registerKeybinding(String keyName, int key, String keyGroup)
    {
        KeyBinding keybind = new KeyBinding(String.format("key.%s", keyName), key, keyGroup);
        ClientRegistry.registerKeyBinding(keybind);
        return keybind;
    }

    /** 
     * Retrieve the ModContainer instance for a mod with the specified ID.
     * 
     * @param id - ID of the mod retrieving an instance from.
     * @return An instance of ModContainer that is assigned to this ID.
     */
    public static ModContainer getModContainer(String id)
    {
        for (ModContainer container : Loader.instance().getModList())
        {
            if (container.getModId().equalsIgnoreCase(id))
            {
                return container;
            }
        }

        return null;
    }

    /** Retrieve the mod id of a mod class from the @Mod annotation. **/
    public static final String getAnnotatedModId(Class<?> clazz)
    {
        if (clazz.isAnnotationPresent(Mod.class))
        {
            Mod mod = clazz.getAnnotation(Mod.class);

            return mod.modid();
        }

        return null;
    }

//    public static Item register(String modid, String identifier, Item item)
//    {
//        item.setTranslationKey(String.format("%s:%s", modid, identifier));
//        GameRegistry.register(item, new ResourceLocation(modid, identifier));
//
//        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
//        {
//            Renderers.registerIcon(item);
//        }
//
//        return item;
//    }

//    public static Block register(String modid, String identifier, Block block)
//    {
//        block.setTranslationKey(String.format("%s:%s", modid, identifier));
//        
//        GameRegistry.register(block, new ResourceLocation(modid, identifier));
//        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
//
//        if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
//        {
//            Item item = Item.getItemFromBlock(block);
//            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
//        }
//
//        return block;
//    }

    public static Item getItem(Block block)
    {
        return Item.getItemFromBlock(block);
    }

//    public static void register(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
//    {
//        EntityRegistry.registerModEntity(entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
//    }
//
//    public static void register(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimaryColor, int eggSecondaryColor)
//    {
//        EntityRegistry.registerModEntity(entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimaryColor, eggSecondaryColor);
//    }
}
