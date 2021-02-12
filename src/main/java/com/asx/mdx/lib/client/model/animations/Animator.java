package com.asx.mdx.lib.client.model.animations;

import java.util.HashMap;

import com.asx.mdx.lib.client.util.Transform;
import com.asx.mdx.lib.client.util.models.Model;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.entity.animations.Animation;
import com.asx.mdx.lib.world.entity.animations.IAnimated;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Animator
{
    private IAnimated                         entity;
    private boolean                           correct;
    private int                               tick;
    private int                               tickPrev;
    private HashMap<ModelRenderer, Transform> transforms;
    private HashMap<ModelRenderer, Transform> transformsPrev;

    private Animator()
    {
        this.tick = 0;
        this.correct = false;
        this.transforms = new HashMap<>();
        this.transformsPrev = new HashMap<>();
    }

    public static Animator create()
    {
        return new Animator();
    }

    public IAnimated getEntity()
    {
        return this.entity;
    }

    public void update(IAnimated entity)
    {
        this.tick = this.tickPrev = 0;
        this.correct = false;
        this.entity = entity;
        this.transforms.clear();
        this.transformsPrev.clear();
    }

    public boolean setAnimation(Animation animation)
    {
        this.tick = this.tickPrev = 0;
        this.correct = this.entity.getActiveAnimation() == animation;

        return this.correct;
    }

    /**
     * Start the keyframe and set the duration of the current keyframe
     * 
     * @param duration
     */
    public void startKeyframe(int duration)
    {
        if (!this.correct)
        {
            return;
        }

        this.tickPrev = this.tick;
        this.tick += duration;
    }

    /**
     * Hold the keyframe for the provided duration
     * 
     * @param duration
     */
    public void setStaticKeyframe(int duration)
    {
        this.startKeyframe(duration);
        this.endKeyframe(true);
    }

    /**
     * Resets the keyframe to the default position over a period of the provided duration
     * 
     * @param duration
     */
    public void resetKeyframe(int duration)
    {
        this.startKeyframe(duration);
        this.endKeyframe();
    }

    /**
     * Rotates the cube to a specified rotation in degrees.
     * 
     * @param box
     * @param x
     * @param y
     * @param z
     */
    public void rotateTo(ModelRenderer box, float x, float y, float z)
    {
        this.rotateTo(box, x, y, z, false);
    }

    /**
     * Rotates the cube to a specified rotation in degrees.
     * 
     * @param box
     * @param x
     * @param y
     * @param z
     * @param s - Whether this is a static pose or not
     */
    public void rotateTo(ModelRenderer box, float x, float y, float z, boolean s)
    {
        x = (float) Math.toRadians(x);
        y = (float) Math.toRadians(y);
        z = (float) Math.toRadians(z);

        if (s)
        {
            box.rotateAngleX += x;
            box.rotateAngleY += y;
            box.rotateAngleZ += z;
        }
        else
        {
            rotate(box, x, y, z);
        }
    }

    /**
     * Rotates the cube progressively using radians.
     * 
     * @param box
     * @param x
     * @param y
     * @param z
     */
    public void rotate(ModelRenderer box, float x, float y, float z)
    {
        if (!this.correct)
        {
            return;
        }

        this.getTransform(box).addRotation(x, y, z);
    }
    
    /**
     * Moves the cube to a specified point.
     * 
     * @param box
     * @param x
     * @param y
     * @param z
     */
    public void moveTo(ModelRenderer box, float x, float y, float z)
    {
        float f = Model.DEFAULT_SCALE;
        x = x * f;
        y = y * f;
        z = z * f;

        move(box, x, y, z);
    }

    public void move(ModelRenderer box, float x, float y, float z)
    {
        if (!this.correct)
        {
            return;
        }

        this.getTransform(box).addOffset(x, y, z);
    }

    public void reposition(ModelRenderer box, float x, float y, float z)
    {
        if (!this.correct)
        {
            return;
        }

        this.getTransform(box).addPosition(x, y, z);
    }

    private Transform getTransform(ModelRenderer box)
    {
        Transform t = this.transforms.get(box);

        if (t == null)
        {
            t = new Transform();
            this.transforms.put(box, t);
        }

        return t;
    }

    public void endKeyframe()
    {
        this.endKeyframe(false);
    }

    private void endKeyframe(boolean stationary)
    {
        if (!this.correct)
        {
            return;
        }

        int animationTick = this.entity.getAnimationTick();

        if (animationTick >= this.tickPrev && animationTick < this.tick)
        {
            if (stationary)
            {
                for (ModelRenderer box : this.transformsPrev.keySet())
                {
                    Transform t = this.transformsPrev.get(box);

                    box.rotateAngleX += t.getRotationX();
                    box.rotateAngleY += t.getRotationY();
                    box.rotateAngleZ += t.getRotationZ();
                    box.offsetX += t.getOffsetX();
                    box.offsetY += t.getOffsetY();
                    box.offsetZ += t.getOffsetZ();
                    box.rotationPointX += t.getPositionX();
                    box.rotationPointY += t.getPositionY();
                    box.rotationPointZ += t.getPositionZ();
                }
            }
            else
            {
                float partialTicks = Game.partialTicks();
                
                if (Game.minecraft().isGamePaused() || this.entity.isAnimationPaused())
                {
                    partialTicks = 0;
                }
                
                float tick = (animationTick - this.tickPrev + partialTicks) / (this.tick - this.tickPrev);
                float inc = MathHelper.sin((float) (tick * Math.PI / 2.0F)), dec = 1.0F - inc;

                for (ModelRenderer box : this.transformsPrev.keySet())
                {
                    Transform t = this.transformsPrev.get(box);

                    box.rotateAngleX += dec * t.getRotationX();
                    box.rotateAngleY += dec * t.getRotationY();
                    box.rotateAngleZ += dec * t.getRotationZ();
                    box.offsetX += dec * t.getOffsetX();
                    box.offsetY += dec * t.getOffsetY();
                    box.offsetZ += dec * t.getOffsetZ();
                    box.rotationPointX += t.getPositionX();
                    box.rotationPointY += t.getPositionY();
                    box.rotationPointZ += t.getPositionZ();
                }

                for (ModelRenderer box : this.transforms.keySet())
                {
                    Transform t = this.transforms.get(box);

                    box.rotateAngleX += inc * t.getRotationX();
                    box.rotateAngleY += inc * t.getRotationY();
                    box.rotateAngleZ += inc * t.getRotationZ();
                    box.offsetX += inc * t.getOffsetX();
                    box.offsetY += inc * t.getOffsetY();
                    box.offsetZ += inc * t.getOffsetZ();
                    box.rotationPointX += t.getPositionX();
                    box.rotationPointY += t.getPositionY();
                    box.rotationPointZ += t.getPositionZ();
                }
            }
        }

        if (!stationary)
        {
            this.transformsPrev.clear();
            this.transformsPrev.putAll(this.transforms);
            this.transforms.clear();
        }
    }
}