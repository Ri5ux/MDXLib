package com.asx.mdx.lib.client.util;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.lib.client.util.models.Model;
import com.asx.mdx.lib.util.Game;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemIconRenderer<M extends Model> extends ItemRenderer<M>
{
    public static enum IconType
    {
        ITEM("items");

        private String type;

        IconType(String type)
        {
            this.type = type;
        }

        public ResourceLocation iconResourceLocation(Item item)
        {
            ResourceLocation registryName = item.getRegistryName();
            String domain = registryName.getNamespace();
            String path = String.format("%s/%s.png", getIconDirectory(), registryName.getPath());
            //System.out.println(String.format("DOMAIN(%s) PATH(%s) RESOURCE(%s)", domain, registryName.getPath(), new ResourceLocation(domain, path)));

            return new ResourceLocation(domain, path);
        }

        public String getIconDirectory()
        {
            return String.format("textures/%s", this.type);
        }
    }

    private ResourceLocation icon;

    public ItemIconRenderer(Item item)
    {
        super(null);
        this.icon = IconType.ITEM.iconResourceLocation(item);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public void renderThirdPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.5F, -0.75F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderThirdPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.5F, -0.75F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        OpenGL.scale(1F, -1F, 1F);
        OpenGL.translate(0, -1F, 0);
        renderItemIn2D(Tessellator.getInstance(), 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderFirstPersonLeft(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.5F, -0.75F, 0F);
        OpenGL.rotate(120F, 0, 1, 0);
        OpenGL.rotate(20F, 0, 0, 1);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderFirstPersonRight(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.1F, -0.5F, -0.7F);
        OpenGL.rotate(260F, 0, 1, 0);
        OpenGL.rotate(-20F, 0, 0, 1);
//        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        OpenGL.scale(1F, -1F, -1F);
        OpenGL.translate(0, -1F, 0);
        renderItemIn2D(Tessellator.getInstance(), 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
//        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderInInventory(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-1F, -1F, 1F);
        OpenGL.translate(0.5F, -0.5F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderInWorld(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        OpenGL.pushMatrix();
        OpenGL.scale(-0.75F, -0.75F, 1F);
        OpenGL.translate(0.5F, -0.75F, 0F);
        OpenGL.rotate(180F, 0, 1, 0);
        OpenGL.disableStandardItemLighting();
        Draw.bindTexture(this.icon);
        OpenGL.disableCullFace();
        Draw.drawQuad(0, 0, 1, 1, 0, 0F, 1F, 0F, 1F);
        OpenGL.enableCullFace();
        OpenGL.enableStandardItemLighting();
        OpenGL.popMatrix();
    }

    @Override
    public void renderPre(ItemStack itemstack, EntityLivingBase entity, TransformType cameraTransformType)
    {
        ;
    }
    
    public static void renderItemIn2D(Tessellator t, float u1, float v1, float u2, float v2, int w, int h, float scale)
    {
        Draw.buffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        Draw.vertex(0.0D, 0.0D, 0.0D, (double) u1, (double) v2).normal(0.0F, 0.0F, 1.0F).endVertex();
        Draw.vertex(1.0D, 0.0D, 0.0D, (double) u2, (double) v2).normal(0.0F, 0.0F, 1.0F).endVertex();
        Draw.vertex(1.0D, 1.0D, 0.0D, (double) u2, (double) v1).normal(0.0F, 0.0F, 1.0F).endVertex();
        Draw.vertex(0.0D, 1.0D, 0.0D, (double) u1, (double) v1).normal(0.0F, 0.0F, 1.0F).endVertex();
        Draw.tessellate();

        Draw.buffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        Draw.vertex(0.0D, 1.0D, (double) (0.0F - scale), (double) u1, (double) v1).normal(0.0F, 0.0F, -1.0F).endVertex();
        Draw.vertex(1.0D, 1.0D, (double) (0.0F - scale), (double) u2, (double) v1).normal(0.0F, 0.0F, -1.0F).endVertex();
        Draw.vertex(1.0D, 0.0D, (double) (0.0F - scale), (double) u2, (double) v2).normal(0.0F, 0.0F, -1.0F).endVertex();
        Draw.vertex(0.0D, 0.0D, (double) (0.0F - scale), (double) u1, (double) v2).normal(0.0F, 0.0F, -1.0F).endVertex();
        Draw.tessellate();

        float f5 = 0.5F * (u1 - u2) / (float) w;
        float f6 = 0.5F * (v2 - v1) / (float) h;

        Draw.buffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        float x;
        float z;

        for (int k = 0; k < w; ++k)
        {
            x = (float) k / (float) w;
            z = u1 + (u2 - u1) * x - f5;
            Draw.vertex((double) x, 0.0D, (double) (0.0F - scale), (double) z, (double) v2).normal(-1.0F, 0.0F, 0.0F).endVertex();
            Draw.vertex((double) x, 0.0D, 0.0D, (double) z, (double) v2).normal(-1.0F, 0.0F, 0.0F).endVertex();
            Draw.vertex((double) x, 1.0D, 0.0D, (double) z, (double) v1).normal(-1.0F, 0.0F, 0.0F).endVertex();
            Draw.vertex((double) x, 1.0D, (double) (0.0F - scale), (double) z, (double) v1).normal(-1.0F, 0.0F, 0.0F).endVertex();
        }

        Draw.tessellate();
        Draw.buffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        float y;

        for (int k = 0; k < w; ++k)
        {
            x = (float) k / (float) w;
            z = u1 + (u2 - u1) * x - f5;
            y = x + 1.0F / (float) w;
            Draw.vertex((double) y, 1.0D, (double) (0.0F - scale), (double) z, (double) v1).normal(1.0F, 0.0F, 0.0F).endVertex();
            Draw.vertex((double) y, 1.0D, 0.0D, (double) z, (double) v1).normal(1.0F, 0.0F, 0.0F).endVertex();
            Draw.vertex((double) y, 0.0D, 0.0D, (double) z, (double) v2).normal(1.0F, 0.0F, 0.0F).endVertex();
            Draw.vertex((double) y, 0.0D, (double) (0.0F - scale), (double) z, (double) v2).normal(1.0F, 0.0F, 0.0F).endVertex();
        }

        Draw.tessellate();
        Draw.buffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        for (int k = 0; k < h; ++k)
        {
            x = (float) k / (float) h;
            z = v2 + (v1 - v2) * x - f6;
            y = x + 1.0F / (float) h;
            Draw.vertex(0.0D, (double) y, 0.0D, (double) u1, (double) z).normal(0.0F, 1.0F, 0.0F).endVertex();
            Draw.vertex(1.0D, (double) y, 0.0D, (double) u2, (double) z).normal(0.0F, 1.0F, 0.0F).endVertex();
            Draw.vertex(1.0D, (double) y, (double) (0.0F - scale), (double) u2, (double) z).normal(0.0F, 1.0F, 0.0F).endVertex();
            Draw.vertex(0.0D, (double) y, (double) (0.0F - scale), (double) u1, (double) z).normal(0.0F, 1.0F, 0.0F).endVertex();
        }

        Draw.tessellate();
        Draw.buffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        for (int k = 0; k < h; ++k)
        {
            x = (float) k / (float) h;
            z = v2 + (v1 - v2) * x - f6;
            Draw.vertex(1.0D, (double) x, 0.0D, (double) u2, (double) z).normal(0.0F, -1.0F, 0.0F).endVertex();
            Draw.vertex(0.0D, (double) x, 0.0D, (double) u1, (double) z).normal(0.0F, -1.0F, 0.0F).endVertex();
            Draw.vertex(0.0D, (double) x, (double) (0.0F - scale), (double) u1, (double) z).normal(0.0F, -1.0F, 0.0F).endVertex();
            Draw.vertex(1.0D, (double) x, (double) (0.0F - scale), (double) u2, (double) z).normal(0.0F, -1.0F, 0.0F).endVertex();
        }

        Draw.tessellate();
    }
}
