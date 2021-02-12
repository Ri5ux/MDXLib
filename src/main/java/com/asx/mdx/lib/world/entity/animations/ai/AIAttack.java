package com.asx.mdx.lib.world.entity.animations.ai;

import com.asx.mdx.lib.world.entity.animations.Animation;
import com.asx.mdx.lib.world.entity.animations.AnimationAI;
import com.asx.mdx.lib.world.entity.animations.IAnimated;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

public class AIAttack<T extends EntityLiving & IAnimated> extends AnimationAI<T>
{
    protected EntityLivingBase entityTarget;
    protected SoundEvent       attackSound;
    protected float            knockback = 1;
    protected float            range;
    protected float            damageMultiplier;
    protected int              damageFrame;
    protected SoundEvent       hitSound;
    
    public AIAttack(T entity, Animation animation, SoundEvent attackSound, SoundEvent hitSound, float knockback, float range, float damageMultiplier, int damageFrame)
    {
        super(entity, animation);
        setMutexBits(8);
        this.entity = entity;
        this.entityTarget = null;
        this.attackSound = attackSound;
        this.knockback = knockback;
        this.range = range;
        this.damageMultiplier = damageMultiplier;
        this.damageFrame = damageFrame;
        this.hitSound = hitSound;
    }

    @Override
    public void startExecuting()
    {
        super.startExecuting();

        if (entity instanceof EntityMob)
        {
            EntityMob mob = (EntityMob) entity;
            entityTarget = mob.getAttackTarget();
        }
    }

    @Override
    public void updateTask()
    {
        super.updateTask();

        if (entity.getAnimationTick() < damageFrame && entityTarget != null)
        {
            entity.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
        }

        if (entity.getAnimationTick() == damageFrame)
        {
            if (entity instanceof EntityMob)
            {
                EntityMob mob = (EntityMob) entity;
                float damage = (float) entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();

                if (entityTarget != null && mob.getDistance(entityTarget) <= range)
                {
                    entityTarget.attackEntityFrom(DamageSource.causeMobDamage(entity), damage * damageMultiplier);
                    entityTarget.motionX *= knockback;
                    entityTarget.motionZ *= knockback;
                    
                    if (hitSound != null)
                    {
                        entity.playSound(hitSound, 1, 1);
                    }
                }
                
                if (attackSound != null)
                {
                    entity.playSound(attackSound, 1, 1);
                }
            }
        }
    }
}