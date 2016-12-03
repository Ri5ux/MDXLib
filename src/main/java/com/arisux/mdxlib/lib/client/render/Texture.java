package com.arisux.mdxlib.lib.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

public class Texture extends ResourceLocation
{
    public Texture(ResourceLocation resource)
    {
        this(resource.getResourceDomain(), resource.getResourcePath());
    }

    public Texture(String location)
    {
        super(location);
    }

    public Texture(String domain, String location)
    {
        super(domain, location);
    }

    @SideOnly(Side.CLIENT)
    public void bind()
    {
        Draw.bindTexture(this);
    }
}