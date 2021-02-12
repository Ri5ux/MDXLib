package com.asx.mdx.lib.hotfix;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class JoinWorldEvent
{
    public static final JoinWorldEvent INSTANCE = new JoinWorldEvent();

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityPlayer) && event.getEntity() instanceof EntityCreature)
        {
            EntityCreature entity = (EntityCreature) event.getEntity();
            
            double entitySpeed = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
            
            if(entitySpeed != 0)
            {
                entity.tasks.addTask(1, new EntityAIWanderPatch(entity, entitySpeed));
            }
        }
    }
}
