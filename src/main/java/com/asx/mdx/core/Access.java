package com.asx.mdx.core;

import java.io.DataInput;
import java.io.DataOutput;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.util.Reflect;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Access
{
    @SideOnly(Side.CLIENT)
    public float getRenderPartialTicks()
    {
        return ((Timer) Reflect.get(Game.minecraft(), "timer", "field_71428_T")).renderPartialTicks;
    }

    @SideOnly(Side.CLIENT)
    public int getRightClickDelayTimer()
    {
        return Game.minecraft().rightClickDelayTimer;
    }

    @SideOnly(Side.CLIENT)
    public void setRightClickDelayTimer(int value)
    {
        Game.minecraft().rightClickDelayTimer = value;
    }

    @SideOnly(Side.CLIENT)
    public Session getSession()
    {
        return (Session) Reflect.get(Game.minecraft(), "session", "field_71449_j");
    }

    @SideOnly(Side.CLIENT)
    public void setEquippedProgress(float value)
    {
        Game.minecraft().entityRenderer.itemRenderer.equippedProgressMainHand = value;
    }

    @SideOnly(Side.CLIENT)
    public float getEquippedProgress()
    {
        return Game.minecraft().entityRenderer.itemRenderer.equippedProgressMainHand;
    }

    @SideOnly(Side.CLIENT)
    public float getEquippedProgressPrev()
    {
        return Game.minecraft().entityRenderer.itemRenderer.prevEquippedProgressMainHand;
    }

    @SideOnly(Side.CLIENT)
    public float getEquippedProgressOffHand()
    {
        return Game.minecraft().entityRenderer.itemRenderer.equippedProgressOffHand;
    }

    @SideOnly(Side.CLIENT)
    public float getEquippedProgressOffHandPrev()
    {
        return Game.minecraft().entityRenderer.itemRenderer.prevEquippedProgressOffHand;
    }

    @SideOnly(Side.CLIENT)
    public float getTorchFlickerX()
    {
        return Game.minecraft().entityRenderer.torchFlickerX;
    }

    @SideOnly(Side.CLIENT)
    public float getTorchFlickerDX()
    {
        return Game.minecraft().entityRenderer.torchFlickerDX;
    }

    @SideOnly(Side.CLIENT)
    public void setTorchFlickerX(float value)
    {
        Game.minecraft().entityRenderer.torchFlickerX = value;
    }
    
    @SideOnly(Side.CLIENT)
    public void setTorchFlickerDX(float value)
    {
        Game.minecraft().entityRenderer.torchFlickerDX = value;
    }

    @SideOnly(Side.CLIENT)
    public float getBossColorModifier()
    {
        return Game.minecraft().entityRenderer.bossColorModifier;
    }

    @SideOnly(Side.CLIENT)
    public void setBossColorModifier(float value)
    {
        Game.minecraft().entityRenderer.bossColorModifier = value;
    }

    @SideOnly(Side.CLIENT)
    public float getBossColorModifierPrev()
    {
        return Game.minecraft().entityRenderer.bossColorModifierPrev;
    }

    @SideOnly(Side.CLIENT)
    public void getBossColorModifierPrev(float value)
    {
        Game.minecraft().entityRenderer.bossColorModifierPrev = value;
    }

    @SideOnly(Side.CLIENT)
    public double getCameraZoom()
    {
        return Game.minecraft().entityRenderer.cameraZoom;
    }

    @SideOnly(Side.CLIENT)
    public double getCameraPitch()
    {
        return Game.minecraft().entityRenderer.cameraPitch;
    }

    @SideOnly(Side.CLIENT)
    public double getCameraYaw()
    {
        return Game.minecraft().entityRenderer.cameraYaw;
    }

    @SideOnly(Side.CLIENT)
    public int[] getLightmapColors()
    {
        return Game.minecraft().entityRenderer.lightmapColors;
    }

    @SideOnly(Side.CLIENT)
    public float getDebugViewDirection()
    {
        return Game.minecraft().entityRenderer.debugViewDirection;
    }

    @SideOnly(Side.CLIENT)
    public DynamicTexture getLightmapTexture()
    {
        return Game.minecraft().entityRenderer.lightmapTexture;
    }

    @SideOnly(Side.CLIENT)
    public boolean isLightmapUpdateNeeded()
    {
        return Game.minecraft().entityRenderer.lightmapUpdateNeeded;
    }

    @SideOnly(Side.CLIENT)
    public float getFarPlaneDistance()
    {
        return Game.minecraft().entityRenderer.farPlaneDistance;
    }

    @SideOnly(Side.CLIENT)
    public void setLightmapUpdateNeeded(boolean value)
    {
        Game.minecraft().entityRenderer.lightmapUpdateNeeded = value;
    }

    @SideOnly(Side.CLIENT)
    public float getFOVModifier(float partialTicks, boolean b)
    {
        return Game.minecraft().entityRenderer.getFOVModifier(partialTicks, b);
    }

    @SideOnly(Side.CLIENT)
    public void setupViewBobbing(float partialTicks)
    {
        Game.minecraft().entityRenderer.applyBobbing(partialTicks);        
    }

    public void setMoveHelper(EntityLiving living, EntityMoveHelper moveHelper)
    {
        Reflect.set(EntityLiving.class, living, "moveHelper", "field_70765_h", moveHelper);
    }

    public void setNavigator(EntityLiving living, PathNavigate navigator)
    {
        Reflect.set(EntityLiving.class, living, "navigator", "field_70699_by", navigator);
    }

    public void setLookHelper(EntityLiving living, EntityLookHelper lookHelper)
    {
        Reflect.set(EntityLiving.class, living, "lookHelper", "field_70749_g", lookHelper);
    }

    public double getMoveHelperPosX(EntityMoveHelper moveHelper)
    {
        return moveHelper.posX;
    }

    public double getMoveHelperPosY(EntityMoveHelper moveHelper)
    {
        return moveHelper.posY;
    }

    public double getMoveHelperPosZ(EntityMoveHelper moveHelper)
    {
        return moveHelper.posZ;
    }

    public double getMoveHelperSpeed(EntityMoveHelper moveHelper)
    {
        return moveHelper.speed;
    }

    public double getLookHelperPosX(EntityLookHelper lookHelper)
    {
        return lookHelper.posX;
    }

    public double getLookHelperPosY(EntityLookHelper lookHelper)
    {
        return lookHelper.posY;
    }

    public double getLookHelperPosZ(EntityLookHelper lookHelper)
    {
        return lookHelper.posZ;
    }

    public boolean getLookHelperIsLooking(EntityLookHelper lookHelper)
    {
        return lookHelper.isLooking;
    }

    public float getBlockResistance(Block block)
    {
        return block.blockResistance;
    }

    public float getBlockHardness(Block block)
    {
        return block.blockHardness;
    }

    @SideOnly(Side.CLIENT)
    public ModelBase getMainModel(RenderLivingBase<EntityLivingBase> renderLiving)
    {
        return renderLiving.mainModel;
    }

    private static final MethodHandle COMPRESSED_STREAM_TOOLS_WRITE_TAG;
    private static final MethodHandle COMPRESSED_STREAM_TOOLS_READ_TAG;

    static
    {
        try
        {
            Method method = null;
            
            method = CompressedStreamTools.class.getDeclaredMethod(Game.isDevEnvironment() ? "writeTag" : "func_150663_a", NBTBase.class, DataOutput.class);
            method.setAccessible(true);
            COMPRESSED_STREAM_TOOLS_WRITE_TAG = MethodHandles.publicLookup().unreflect(method);

            method = CompressedStreamTools.class.getDeclaredMethod(Game.isDevEnvironment() ? "read" : "func_152455_a", DataInput.class, int.class, NBTSizeTracker.class);
            method.setAccessible(true);
            COMPRESSED_STREAM_TOOLS_READ_TAG = MethodHandles.publicLookup().unreflect(method);
        }
        catch (Exception exception)
        {
            throw new RuntimeException();
        }

    }

    /**
     * Provides access to CompressedStreamTools.writeTag(NBTBase, DataOutput)
     */
    public static void writeTag(NBTBase base, DataOutput dataoutput)
    {
        try
        {
            Access.COMPRESSED_STREAM_TOOLS_WRITE_TAG.invokeExact(base, dataoutput);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Provides access to CompressedStreamTools.readTag(DataInput, int, NBTSizeTracker)
     */
    public static NBTBase readTag(DataInput datainput, int depth, NBTSizeTracker sizeTracker)
    {
        try
        {
            return (NBTBase) Access.COMPRESSED_STREAM_TOOLS_READ_TAG.invokeExact(datainput, depth, sizeTracker);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static class ClientAccess
    {
        private static final MethodHandle GET_ENTITY_TEXTURE;

        static
        {
            try
            {
                Method method = Render.class.getDeclaredMethod(Game.isDevEnvironment() ? "getEntityTexture" : "func_110775_a", Entity.class);
                method.setAccessible(true);
                GET_ENTITY_TEXTURE = MethodHandles.publicLookup().unreflect(method);
            }
            catch (Exception exception)
            {
                throw new RuntimeException();
            }

        }
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getEntityTexture(Render render, Entity entity)
    {
        try
        {
            return ((ResourceLocation) ClientAccess.GET_ENTITY_TEXTURE.invokeExact(render, (Entity) entity));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
