package com.asx.mdx.lib.client.util.entity;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class EntityRenderTransforms
{
    private ArrayList<Class<? extends Entity>> entities;

    @SuppressWarnings("all")
    public EntityRenderTransforms(Class<?>... entities)
    {
        this.entities = new ArrayList(Arrays.asList(entities));
    }

    /** 
     * Returns a list of entity types that this entity render transformation is applied to. 
     **/
    public ArrayList<Class<? extends Entity>> getHandledEntities()
    {
        return this.entities;
    }

    public boolean isApplicable(Entity entity)
    {
        if (entities.contains(entity.getClass()))
        {
            return true;
        }
        
        for (Class<?> c : getSuperClasses(entity))
        {
            if (c != null && entities.contains(c))
            {
                return true;
            }
        }
        
        return false;
    }

    public static ArrayList<Class<?>> getSuperClasses(Object o)
    {
        ArrayList<Class<?>> list = new ArrayList<Class<?>>();
        Class<?> c = o.getClass();
        Class<?> superclass = c.getSuperclass();
        list.add(superclass);
        
        while (superclass != null)
        {
            c = superclass;
            superclass = c.getSuperclass();
            list.add(superclass);
        }
        
        return list;
    }

    public abstract void pre(Entity entity, float partialTicks);

    public abstract void post(Entity entity, float partialTicks);
}