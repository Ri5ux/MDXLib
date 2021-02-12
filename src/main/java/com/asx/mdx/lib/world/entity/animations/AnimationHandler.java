package com.asx.mdx.lib.world.entity.animations;

import org.apache.commons.lang3.ArrayUtils;

import com.asx.mdx.MDX;
import com.asx.mdx.core.network.server.PacketAnimation;
import com.asx.mdx.lib.event.AnimationEvent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

public enum AnimationHandler
{
    INSTANCE;

    /**
     * Sends an animation packet to all clients, notifying them of a changed animation
     *
     * @param entity the entity with an animation to be updated
     * @param animation the animation to be updated
     * @param <T> the entity type
     */
    public <T extends Entity & IAnimated> void sendAnimationMessage(T entity, Animation animation)
    {
        if (entity.world.isRemote)
        {
            return;
        }
        entity.setActiveAnimation(animation);
        for (EntityPlayer trackingPlayer : ((WorldServer) entity.world).getEntityTracker().getTrackingPlayers(entity))
        {
            MDX.network().sendTo(new PacketAnimation(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animation)), (EntityPlayerMP) trackingPlayer);
        }
    }

    /**
     * Updates all animations for a given entity
     *
     * @param entity the entity with an animation to be updated
     * @param <T> the entity type
     */
    public <T extends Entity & IAnimated> void updateAnimations(T entity)
    {
        if (entity.getActiveAnimation() == null)
        {
            entity.setActiveAnimation(IAnimated.NO_ANIMATION);
        }
        else
        {
            if (entity.getActiveAnimation() != IAnimated.NO_ANIMATION)
            {
                if (entity.getAnimationTick() == 0)
                {
                    AnimationEvent<T> event = new AnimationEvent.Start<>(entity, entity.getActiveAnimation());
                    
                    if (!MinecraftForge.EVENT_BUS.post(event))
                    {
                        this.sendAnimationMessage(entity, event.getAnimation());
                    }
                }
                if (entity.getAnimationTick() < entity.getActiveAnimation().getDuration())
                {
                    entity.setAnimationTick(entity.getAnimationTick() + 1);
                    MinecraftForge.EVENT_BUS.post(new AnimationEvent.Tick<>(entity, entity.getActiveAnimation(), entity.getAnimationTick()));
                }
                if (entity.getAnimationTick() == entity.getActiveAnimation().getDuration())
                {
                    entity.setAnimationTick(0);
                    entity.setActiveAnimation(IAnimated.NO_ANIMATION);
                }
            }
        }
    }
}
