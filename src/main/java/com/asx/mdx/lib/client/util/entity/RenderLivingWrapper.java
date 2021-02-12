package com.asx.mdx.lib.client.util.entity;

import com.asx.mdx.lib.client.util.models.MapModelTexture;
import com.asx.mdx.lib.client.util.models.Model;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderLivingWrapper<T extends EntityLiving, MODEL extends Model> extends RenderLiving<T>
{
    protected MapModelTexture<MODEL> model;
    
    public RenderLivingWrapper(RenderManager m, MapModelTexture<MODEL> model)
    {
        this(m, model, 0F);
    }
    
    public RenderLivingWrapper(RenderManager m, MapModelTexture<MODEL> model, float shadowSize)
    {
        super(m, model.getModel(), shadowSize);
        this.model = model;
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity)
    {
        return this.model.getTexture();
    }
    
    public MapModelTexture<MODEL> getModel()
    {
        return model;
    }
}
