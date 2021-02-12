package com.asx.mdx.lib.client;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.util.Texture;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Resources
{
    public static final Texture BLANK = new Texture(MDX.Properties.ID, "textures/misc/blank.png");
}
