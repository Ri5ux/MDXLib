package com.asx.mdx.lib.world.block.material;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public interface IMaterialPhysics
{
    public default void onCollision(Entity entity)
    {
        entity.fallDistance = 0.0F;
        entity.extinguish();
    }

    public default void handleForce(Entity entity, Vec3d motion)
    {
        if (this.getForceVelocity() == 0D)
        {
            return;
        }
        
        entity.motionX += motion.x * this.getForceVelocity();
        entity.motionY += motion.y * this.getForceVelocity();
        entity.motionZ += motion.z * this.getForceVelocity();
    }

    public default void handleMovement(Entity entity)
    {
        if (this.getVelocity() == 0D)
        {
            return;
        }

        entity.motionX *= this.getVelocity();
        entity.motionY *= this.getVelocity() * this.getSinkMultiplier();
        entity.motionZ *= this.getVelocity();

        if (entity.collidedHorizontally && entity.isOffsetPositionInLiquid(entity.motionX, entity.motionY + (this.getJumpOffset() * 2) - entity.posY + entity.posY, entity.motionZ))
        {
            entity.motionY = this.getJumpOffset();
        }
    }

    public default double getForceVelocity()
    {
        return 0.014D;
    }

    public default double getVelocity()
    {
        return 0.4D;
    }
    
    public default double getSinkMultiplier()
    {
        return 2.25F;
    }
    
    public default double getJumpOffset()
    {
        return 0.30000001192092896D;
    }
    
    public default boolean ignoresPushableCheck()
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public IMaterialRenderer getMaterialRenderer();
}