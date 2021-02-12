package com.asx.mdx.lib.world.entity.animations;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public abstract class AnimationAI<T extends EntityLiving & IAnimated> extends EntityAIBase
{
    protected T entity;
    protected Animation animation;

    public AnimationAI(T entity, Animation animation)
    {
        this(entity);
        this.animation = animation;
    }

    public AnimationAI(T entity, Animation animation, boolean interruptsAI)
    {
        this(entity);
        this.animation = animation;
        
        if (!interruptsAI)
        {
            setMutexBits(8);
        }
    }

    public AnimationAI(T entity)
    {
        this.entity = entity;
        this.setMutexBits(7);
    }

    public Animation getAnimation()
    {
        return this.animation;
    }

    public boolean isAutomatic()
    {
        return true;
    }

    public boolean shouldAnimate()
    {
        return false;
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.isAutomatic())
        {
            return this.entity.getActiveAnimation() == this.getAnimation();
        }
        return this.shouldAnimate();
    }

    @Override
    public void startExecuting()
    {
        if (!this.isAutomatic())
        {
            AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, this.getAnimation());
        }
        this.entity.setAnimationTick(0);

        if (entity instanceof IAnimated)
        {
            IAnimated animated = (IAnimated) entity;
            animated.setActiveAnimation(this.animation);
        }
    }

    @Override
    public void resetTask()
    {
        AnimationHandler.INSTANCE.sendAnimationMessage(this.entity, IAnimated.NO_ANIMATION);

        if (entity instanceof IAnimated)
        {
            IAnimated animated = (IAnimated) entity;
            animated.setActiveAnimation(null);
        }
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return this.entity.getAnimationTick() < this.getAnimation().getDuration();
    }
}
