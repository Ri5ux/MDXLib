package com.asx.mdx.lib.client.util.models;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.model.animations.Animator;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.util.MDXMath;
import com.asx.mdx.lib.world.entity.animations.IAnimated;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Model<TYPE extends Object> extends ModelBase
{
    @SideOnly(Side.CLIENT)
    public static class Part extends ModelRenderer
    {
        public float    defaultRotationX;
        public float    defaultRotationY;
        public float    defaultRotationZ;

        public float    defaultOffsetX;
        public float    defaultOffsetY;
        public float    defaultOffsetZ;

        public float    defaultPositionX;
        public float    defaultPositionY;
        public float    defaultPositionZ;

        public float    scaleX = 1.0F;
        public float    scaleY = 1.0F;
        public float    scaleZ = 1.0F;

        public int      textureOffsetX;
        public int      textureOffsetY;

        public boolean  scaleChildren;
        private Model   model;
        private Part    parent;
        private int     displayList;
        private boolean compiled;

        public Part(Model model, String name)
        {
            super(model, name);
            this.model = model;
        }

        public Part(Model model)
        {
            this(model, null);
        }

        public Part(Model model, int textureOffsetX, int textureOffsetY)
        {
            this(model);
            this.setTextureOffset(textureOffsetX, textureOffsetY);
        }

        @Override
        public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth)
        {
            partName = this.boxName + "." + partName;
            TextureOffset textureoffset = this.model.getTextureOffset(partName);
            this.setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
            this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F)).setBoxName(partName));

            return this;
        }

        @Override
        public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth)
        {
            this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
            return this;
        }

        @Override
        public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored)
        {
            this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F, mirrored));
            return this;
        }

        /**
         * Creates a textured box.
         */
        @Override
        public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor)
        {
            this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, scaleFactor));
        }

        /**
         * If true, when using setScale, the children of this model part will be scaled as well as just this part. If false, just this part will be scaled.
         *
         * @param scaleChildren true if this parent should scale the children
         */
        public void setShouldScaleChildren(boolean scaleChildren)
        {
            this.scaleChildren = scaleChildren;
        }

        /**
         * Sets the scale for this AdvancedModelRenderer to be rendered at. (Performs a call to GLStateManager.scale()).
         *
         * @param scaleX the x scale
         * @param scaleY the y scale
         * @param scaleZ the z scale
         */
        public void setScale(float scaleX, float scaleY, float scaleZ)
        {
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }

        public void setScaleX(float scaleX)
        {
            this.scaleX = scaleX;
        }

        public void setScaleY(float scaleY)
        {
            this.scaleY = scaleY;
        }

        public void setScaleZ(float scaleZ)
        {
            this.scaleZ = scaleZ;
        }

        /**
         * Sets this ModelRenderer's default pose to the current pose.
         */
        public void updateDefaultPose()
        {
            this.defaultRotationX = this.rotateAngleX;
            this.defaultRotationY = this.rotateAngleY;
            this.defaultRotationZ = this.rotateAngleZ;

            this.defaultOffsetX = this.offsetX;
            this.defaultOffsetY = this.offsetY;
            this.defaultOffsetZ = this.offsetZ;

            this.defaultPositionX = this.rotationPointX;
            this.defaultPositionY = this.rotationPointY;
            this.defaultPositionZ = this.rotationPointZ;
        }

        /**
         * Sets the current pose to the previously set default pose.
         */
        public void resetToDefaultPose()
        {
            this.rotateAngleX = this.defaultRotationX;
            this.rotateAngleY = this.defaultRotationY;
            this.rotateAngleZ = this.defaultRotationZ;

            this.offsetX = this.defaultOffsetX;
            this.offsetY = this.defaultOffsetY;
            this.offsetZ = this.defaultOffsetZ;

            this.rotationPointX = this.defaultPositionX;
            this.rotationPointY = this.defaultPositionY;
            this.rotationPointZ = this.defaultPositionZ;
        }

        @Override
        public void addChild(ModelRenderer child)
        {
            super.addChild(child);

            if (child instanceof Part)
            {
                Part advancedChild = (Part) child;
                advancedChild.setParent(this);
            }
        }

        /**
         * @return the parent of this box
         */
        public Part getParent()
        {
            return this.parent;
        }

        /**
         * Sets the parent of this box
         *
         * @param parent the new parent
         */
        public void setParent(Part parent)
        {
            this.parent = parent;
        }

        /**
         * Post renders this box with all its parents
         *
         * @param scale the render scale
         */
        public void parentedPostRender(float scale)
        {
            if (this.parent != null)
            {
                this.parent.parentedPostRender(scale);
            }

            this.postRender(scale);
        }

        /**
         * Renders this box with all it's parents
         *
         * @param scale the render scale
         */
        public void renderWithParents(float scale)
        {
            if (this.parent != null)
            {
                this.parent.renderWithParents(scale);
            }

            this.render(scale);
        }

        @Override
        public void render(float scale)
        {
            if (!this.isHidden)
            {
                if (this.showModel)
                {
                    GlStateManager.pushMatrix();

                    if (!this.compiled)
                    {
                        this.compileDisplayList(scale);
                    }

                    GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

                    if (this.rotateAngleZ != 0.0F)
                    {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F)
                    {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F)
                    {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                    }

                    if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F)
                    {
                        GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
                    }

                    GlStateManager.callList(this.displayList);

                    if (!this.scaleChildren && (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F))
                    {
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
                        GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

                        if (this.rotateAngleZ != 0.0F)
                        {
                            GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                        }

                        if (this.rotateAngleY != 0.0F)
                        {
                            GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                        }

                        if (this.rotateAngleX != 0.0F)
                        {
                            GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                        }
                    }

                    if (this.childModels != null)
                    {
                        for (ModelRenderer childModel : this.childModels)
                        {
                            childModel.render(scale);
                        }
                    }
                    GlStateManager.popMatrix();
                }
            }
        }

        private void compileDisplayList(float scale)
        {
            this.displayList = GLAllocation.generateDisplayLists(1);
            GlStateManager.glNewList(this.displayList, 4864);
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();

            for (ModelBox box : this.cubeList)
            {
                box.render(buffer, scale);
            }

            GlStateManager.glEndList();
            this.compiled = true;
        }

        public Model getModel()
        {
            return this.model;
        }

        private float calculateRotation(float speed, float degree, boolean invert, float offset, float weight, float f, float f1)
        {
            float movementScale = this.model.getMovementScale();
            float rotation = (MathHelper.cos(f * (speed * movementScale) + offset) * (degree * movementScale) * f1) + (weight * f1);

            return invert ? -rotation : rotation;
        }

        /**
         * Rotates this box back and forth (rotateAngleX). Useful for arms and legs.
         *
         * @param speed is how fast the animation runs
         * @param degree is how far the box will rotate;
         * @param invert will invert the rotation
         * @param offset will offset the timing of the animation
         * @param weight will make the animation favor one direction more based on how fast the mob is moving
         * @param walk is the walked distance
         * @param walkAmount is the walk speed
         */
        public void oscillate(float speed, float degree, boolean invert, float offset, float weight, float walk, float walkAmount)
        {
            this.rotateAngleX += this.calculateRotation(speed, degree, invert, offset, weight, walk, walkAmount);
        }

        /**
         * Rotates this box up and down (rotateAngleZ). Useful for wing and ears.
         *
         * @param speed is how fast the animation runs
         * @param degree is how far the box will rotate;
         * @param invert will invert the rotation
         * @param offset will offset the timing of the animation
         * @param weight will make the animation favor one direction more based on how fast the mob is moving
         * @param flap is the flapped distance
         * @param flapAmount is the flap speed
         */
        public void flap(float speed, float degree, boolean invert, float offset, float weight, float flap, float flapAmount)
        {
            this.rotateAngleZ += this.calculateRotation(speed, degree, invert, offset, weight, flap, flapAmount);
        }

        /**
         * Rotates this box side to side (rotateAngleY).
         *
         * @param speed is how fast the animation runs
         * @param degree is how far the box will rotate;
         * @param invert will invert the rotation
         * @param offset will offset the timing of the animation
         * @param weight will make the animation favor one direction more based on how fast the mob is moving
         * @param swing is the swung distance
         * @param swingAmount is the swing speed
         */
        public void swing(float speed, float degree, boolean invert, float offset, float weight, float swing, float swingAmount)
        {
            this.rotateAngleY += this.calculateRotation(speed, degree, invert, offset, weight, swing, swingAmount);
        }

        /**
         * Moves this box up and down (rotationPointY). Useful for bodies.
         *
         * @param speed is how fast the animation runs;
         * @param degree is how far the box will move;
         * @param bounce will make the box bounce;
         * @param f is the walked distance;
         * @param f1 is the walk speed.
         */
        public void bob(float speed, float degree, boolean bounce, float f, float f1)
        {
            float movementScale = this.model.getMovementScale();
            degree *= movementScale;
            speed *= movementScale;
            float bob = (float) (Math.sin(f * speed) * f1 * degree - f1 * degree);

            if (bounce)
            {
                bob = (float) -Math.abs((Math.sin(f * speed) * f1 * degree));
            }

            this.rotationPointY += bob;
        }

        @Override
        public Part setTextureOffset(int textureOffsetX, int textureOffsetY)
        {
            this.textureOffsetX = textureOffsetX;
            this.textureOffsetY = textureOffsetY;

            return this;
        }

        public void transitionTo(Part to, float timer, float maxTime)
        {
            this.rotateAngleX += ((to.rotateAngleX - this.rotateAngleX) / maxTime) * timer;
            this.rotateAngleY += ((to.rotateAngleY - this.rotateAngleY) / maxTime) * timer;
            this.rotateAngleZ += ((to.rotateAngleZ - this.rotateAngleZ) / maxTime) * timer;

            this.rotationPointX += ((to.rotationPointX - this.rotationPointX) / maxTime) * timer;
            this.rotationPointY += ((to.rotationPointY - this.rotationPointY) / maxTime) * timer;
            this.rotationPointZ += ((to.rotationPointZ - this.rotationPointZ) / maxTime) * timer;

            this.offsetX += ((to.offsetX - this.offsetX) / maxTime) * timer;
            this.offsetY += ((to.offsetY - this.offsetY) / maxTime) * timer;
            this.offsetZ += ((to.offsetZ - this.offsetZ) / maxTime) * timer;
        }
    }

    public static final float DEFAULT_SCALE = 1F / 16F;
    private float             movementScale = 1.0F;
    protected Animator        animator;

    public Animator getAnimationHandler()
    {
        return this.animator;
    }
    
    public Model()
    {
        this.animator = Animator.create();
    }
    
    public static float partialTicks()
    {
        return Game.minecraft().isGamePaused() ? 0 : Animation.getPartialTickTime();
    }

    /**
     * Set the width and height of this ModelBaseExtension's texture.
     * 
     * @param textureWidth - The texture width in pixels
     * @param textureHeight - The texture height in pixels
     */
    public void setTextureDimensions(int textureWidth, int textureHeight)
    {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    /**
     * Set the rotation angles of the specified ModelRenderer instance.
     * 
     * @param model - The model rotations are being set for.
     * @param rotateAngleX - Angle by which model will rotate in the X direction.
     * @param rotateAngleY - Angle by which the model will rotate in the Y direction.
     * @param rotateAngleZ - Angle by which the model will rotate in the Z direction.
     */
    public void setRotation(ModelRenderer model, float rotateAngleX, float rotateAngleY, float rotateAngleZ)
    {
        model.rotateAngleX = rotateAngleX;
        model.rotateAngleY = rotateAngleY;
        model.rotateAngleZ = rotateAngleZ;
    }
    
    public void updateAnimations(TYPE t)
    {
        if (this.getAnimationHandler() != null && t instanceof IAnimated)
        {
            IAnimated animated = (IAnimated) t;
            this.getAnimationHandler().update(animated);
        }
        resetPose();
    }

    /**
     * Creates an array or group of ModelRenderers.
     * 
     * @param children - The ModelRenderers we're adding to this group.
     * @return The array or group created.
     */
    public static ModelRenderer[] group(ModelRenderer... children)
    {
        return children;
    }

    /**
     * Renders a model.
     * 
     * @param modelRenderer - The ModelRenderer being used.
     */
    public static void draw(ModelRenderer modelRenderer)
    {
        modelRenderer.render(DEFAULT_SCALE);
    }

    /**
     * Renders a group of models.
     * 
     * @param group - A group of models for which to be rendered.
     */
    public static void draw(ModelRenderer[] group)
    {
        for (ModelRenderer child : group)
        {
            draw(child);
        }
    }

    /**
     * A shortcut for calling render(Object) without the parameter.
     */
    public void render()
    {
        this.render(null);
    }

    /**
     * The render method that should be used with Model. An Entity or TileEntity can be passed through the function in place of the Object parameter.
     */
    public abstract void render(TYPE obj);

    /**
     * Gets the main model of a rendering class.
     * 
     * @param renderer - The render class of the model.
     * @return The model for the renderer.
     */
    public static ModelBase getMainModel(RenderLivingBase<EntityLivingBase> renderer)
    {
        return MDX.access().getMainModel(renderer);
    }

    /**
     * ===================================================================================================================
     * EntityLivingBase specific functions
     * ===================================================================================================================
     */

    /**
     * Gets the idle progress of a generic Object.
     * Basically float-precise timing of ticksExisted, instead of an integer value.
     * 
     * @param o - The object for which to get the idle progress from. Should be an instance of EntityLivingBase.
     * @return ticksExisted + partialTicks of the object.
     */
    public static float idleProgress(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            EntityLivingBase base = (EntityLivingBase) o;
            return base.ticksExisted + partialTicks();
        }

        return 0F;
    }

    /**
     * Gets the swing process of a generic Object.
     * 
     * @param o - The object to get the swing progress of. Should be an instance of EntityLivingBase.
     * @return How far along the object is from completing its swing.
     */
    public static float swingProgress(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            EntityLivingBase base = (EntityLivingBase) o;
            return base.limbSwing - base.limbSwingAmount * (1.0F - partialTicks());
        }

        return 0F;
    }

    /**
     * Gets the previous swing progress of a generic Object.
     * 
     * @param o - The object to get the previous swing progress of. Should be an instance of EntityLivingBase.
     * @return The time since the object's last swing was completed.
     */
    public static float swingProgressPrev(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            EntityLivingBase base = (EntityLivingBase) o;
            return base.prevLimbSwingAmount + (base.limbSwingAmount - base.prevLimbSwingAmount) * partialTicks();
        }

        return 0F;
    }

    /**
     * Gets the yaw rotation of a generic Object.
     * 
     * @param o - The object from which to get the yaw of. Should be an instance of EntityLivingBase.
     * @return The yaw rotation of the object.
     */
    public static float headYaw(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            EntityLivingBase base = (EntityLivingBase) o;
            float yawOffset = MDXMath.interpolateRotation(base.prevRenderYawOffset, base.renderYawOffset, partialTicks());
            float yawHead = MDXMath.interpolateRotation(base.prevRotationYawHead, base.rotationYawHead, partialTicks());
            return yawHead - yawOffset;
        }

        return 0F;
    }

    /**
     * Gets the pitch rotation of a generic Object.
     * 
     * @param o - The object from which to get the pitch of. Should be an instance of EntityLivingBase.
     * @return The pitch rotation of the object.
     */
    public static float headPitch(Object o)
    {
        if (o != null && o instanceof EntityLivingBase)
        {
            EntityLivingBase base = (EntityLivingBase) o;
            return (base.prevRotationPitch + (base.rotationPitch - base.prevRotationPitch) * partialTicks());
        }

        return 0F;
    }

    /**
     * ===================================================================================================================
     * Animations
     * ===================================================================================================================
     */

    /**
     * Sets the default pose to the current pose of this model
     */
    public void updateDefaultPose()
    {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof Part).forEach(modelRenderer -> {
            Part advancedModelRenderer = (Part) modelRenderer;
            advancedModelRenderer.updateDefaultPose();
        });
    }

    /**
     * Sets the current pose to the previously set default pose
     */
    public void resetPose()
    {
        this.boxList.stream().filter(modelRenderer -> modelRenderer instanceof Part).forEach(modelRenderer -> {
            Part advancedModelRenderer = (Part) modelRenderer;
            advancedModelRenderer.resetToDefaultPose();
        });
    }

    /**
     * @return the current movement scale
     */
    public float getMovementScale()
    {
        return this.movementScale;
    }

    /**
     * Multiplies all rotation and position changes by this value
     *
     * @param movementScale the movement scale
     */
    public void setMovementScale(float movementScale)
    {
        this.movementScale = movementScale;
    }

    /**
     * ===================================================================================================================
     * Obsolete functions - Using these may result in unexpected behavior
     * ===================================================================================================================
     */

    /**
     * The vanilla entity render method from ModelBase with correct parameter mappings. Should not be using this if you're using Model
     *
     * @param entity - The Entity instance being rendered.
     * @param swing - The arm swing progress of the Entity being rendered.
     * @param swingPrev - The previous tick's arm swing progress of the Entity being rendered.
     * @param idle - The entity age in ticks
     * @param headYaw - The head rotation yaw of the Entity being rendered.
     * @param headPitch - The head rotation pitch of the Entity being rendered.
     * @param scale - The scale this model will render at.
     */
    @Deprecated
    public void render(Entity entity, float swing, float swingPrev, float idle, float headYaw, float headPitch, float scale)
    {
        this.render((TYPE) entity);
    }

    /**
     * The standard setRotationAngles method from ModelBase with correct parameter mappings. Used for animation.
     *
     * @param swing - The arm swing progress of the Entity being rendered.
     * @param swingPrev - The previous tick's arm swing progress of the Entity being rendered.
     * @param idle - The idle arm swing progress of the Entity being rendered.
     * @param headYaw - The head rotation yaw of the Entity being rendered.
     * @param headPitch - The head rotation pitch of the Entity being rendered.
     * @param scale - The scale this model will render at.
     * @param entity - The Entity instance being rendered.
     */
    @Deprecated
    public void setRotationAngles(float swing, float swingPrev, float idle, float headYaw, float headPitch, float scale, Entity entity)
    {
        ;
    }

    /**
     * The standard setLivingAnimations method from ModelBase with correct parameter mappings. Used for animation. Should not be using this with Model.
     *
     * @param entityLiving - The EntityLiving instance currently being rendered.
     * @param swingProgress - The arm swing progress of the Entity being rendered.
     * @param swingProgressPrev - The previous tick's arm swing progress of the Entity being rendered.
     * @param renderPartialTicks - Render partial ticks
     */
    @Deprecated
    public void setLivingAnimations(EntityLivingBase entityLiving, float swingProgress, float swingProgressPrev, float renderPartialTicks)
    {
        ;
    }
}
