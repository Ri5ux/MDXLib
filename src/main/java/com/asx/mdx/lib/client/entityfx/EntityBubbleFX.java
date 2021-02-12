package com.asx.mdx.lib.client.entityfx;

import com.asx.mdx.lib.client.util.Draw;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityBubbleFX extends Particle
{
    public EntityBubbleFX(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ)
    {
        super(world, posX, posY, posZ, motionX, motionY, motionZ);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setParticleTextureIndex(32);
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.2F;
        this.motionX = motionX * 0.20000000298023224D + (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.02F);
        this.motionY = motionY * 0.20000000298023224D + (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.02F);
        this.motionZ = motionZ * 0.20000000298023224D + (double) ((float) (Math.random() * 2.0D - 1.0D) * 0.02F);
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY += 0.002D;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.8500000238418579D;
        this.motionY *= 0.8500000238418579D;
        this.motionZ *= 0.8500000238418579D;

        if (this.particleMaxAge-- <= 0)
        {
            this.setExpired();
        }
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rX, float rZ, float rYZ, float rXY, float rXZ)
    {
        super.renderParticle(buffer, entity, partialTicks, rX, rZ, rYZ, rXY, rXZ);
        Draw.drawParticle(32, 0, 0, 1, 1);
    }
    
    public void renderParticleNew(Tessellator tessellator, float partialTickTime, float rX, float rZ, float rYZ, float rXY, float rXZ)
    {
        float minU = (float) this.particleTextureIndexX / 16.0F;
        float maxU = minU + 0.0624375F;
        float minV = (float) this.particleTextureIndexY / 16.0F;
        float maxV = minV + 0.0624375F;
        float scale = 0.1F * this.particleScale;

        if (this.particleTexture != null)
        {
            minU = this.particleTexture.getMinU();
            maxU = this.particleTexture.getMaxU();
            minV = this.particleTexture.getMinV();
            maxV = this.particleTexture.getMaxV();
        }

        float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTickTime - interpPosX);
        float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTickTime - interpPosY);
        float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTickTime - interpPosZ);

        Draw.startQuads();
        Draw.vertex((double) (f11 - rX * scale - rXY * scale), (double) (f12 - rZ * scale), (double) (f13 - rYZ * scale - rXZ * scale), maxU, maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
        Draw.vertex((double) (f11 - rX * scale + rXY * scale), (double) (f12 + rZ * scale), (double) (f13 - rYZ * scale + rXZ * scale), maxU, minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
        Draw.vertex((double) (f11 + rX * scale + rXY * scale), (double) (f12 + rZ * scale), (double) (f13 + rYZ * scale + rXZ * scale), minU, minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
        Draw.vertex((double) (f11 + rX * scale - rXY * scale), (double) (f12 - rZ * scale), (double) (f13 + rYZ * scale - rXZ * scale), minU, maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).endVertex();
        Draw.tessellate();
        Draw.startQuads();
    }

    @Override
    public int getFXLayer()
    {
        return 3;
    }
}