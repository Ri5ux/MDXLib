package com.asx.mdx.lib.client.entityfx;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBloodFX extends Particle
{
    public static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    
    private int                          bobTimer;
    private boolean                      glow;

    public EntityBloodFX(World worldIn, double posX, double posY, double posZ, int color, int maxAge, boolean glow)
    {
        super(worldIn, posX, posY, posZ, 0, 0, 0);
        this.particleMaxAge = maxAge + this.rand.nextInt((maxAge / 4) + 1);
        this.glow = glow;
        this.particleGravity = 0.36F;
        this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F);
        this.bobTimer = 40;
        float particleSpread = 0.15F;
        this.motionY = (double) (this.rand.nextFloat() * 0.4F + 0.05F);
        this.motionZ = (double) (this.rand.nextFloat() * particleSpread) - (this.rand.nextFloat() * particleSpread);
        this.motionX = (double) (this.rand.nextFloat() * particleSpread) - (this.rand.nextFloat() * particleSpread);
        this.particleAlpha = ((float) ((color & 0xFF000000) >> 24)) / 255F;
        this.particleRed = ((float) ((color & 0xFF0000) >> 16)) / 255F;
        this.particleGreen = ((float) ((color & 0xFF00) >> 8)) / 255F;
        this.particleBlue = ((float) (color & 0xFF)) / 255F;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= (double) this.particleGravity;

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.bobTimer-- > 0)
        {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
        }

        if (this.particleMaxAge-- <= 0)
        {
            this.setExpired();
        }

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos pos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
        IBlockState blockstate = this.world.getBlockState(pos);
        Material material = blockstate.getMaterial();

        if (material.isLiquid() || material.isSolid())
        {
            double d0 = (double) ((float) (MathHelper.floor(this.posY) + 1) - BlockLiquid.getLiquidHeightPercent(blockstate.getBlock().getMetaFromState(blockstate)));

            if (this.posY < d0)
            {
                this.setExpired();
            }
        }
    }

    @Override
    public void renderParticle(BufferBuilder buffer, Entity entity, float partialTicks, float rX, float rZ, float rYZ, float rXY, float rXZ)
    {
        OpenGL.enableBlend();
        OpenGL.blendClear();
        Draw.bindTexture(PARTICLE_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

        float r = this.particleRed;
        float g = this.particleGreen;
        float b = this.particleBlue;
        float a = this.particleAlpha;

        if (glow)
        {
            OpenGL.disableLightMapping();
        }

        float u2 = (float) this.particleTextureIndexX / 16.0F;
        float u1 = u2 + 0.0624375F;
        float v2 = (float) this.particleTextureIndexY / 16.0F;
        float v1 = v2 + 0.0624375F;
        float pScale = 1F * this.particleScale;
        float pyScale = 0.1F * this.particleScale;

        if (this.particleTexture != null)
        {
            u2 = this.particleTexture.getMinU();
            u1 = this.particleTexture.getMaxU();
            v2 = this.particleTexture.getMinV();
            v1 = this.particleTexture.getMaxV();
        }
        
        Entity rve = Game.minecraft().getRenderViewEntity();
        
        double ipx = rve.lastTickPosX + (rve.posX - rve.lastTickPosX) * (double)partialTicks;
        double ipy = rve.lastTickPosY + (rve.posY - rve.lastTickPosY) * (double)partialTicks;
        double ipz = rve.lastTickPosZ + (rve.posZ - rve.lastTickPosZ) * (double)partialTicks;
        
        float x = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - ipx);
        float y = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - ipy + 0.02F);
        float z = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - ipz);

        
        int brightness = this.getBrightnessForRender(partialTicks);
        int skyLight = brightness >> 16 & 65535;
        int blocKLight = brightness & 65535;
        Vec3d[] vec = new Vec3d[] { new Vec3d((double) (-rX * pScale - rXY * pScale), (double) (-rZ * pyScale), (double) (-rYZ * pScale - rXZ * pScale)), new Vec3d((double) (-rX * pScale + rXY * pScale), (double) (rZ * pyScale), (double) (-rYZ * pScale + rXZ * pScale)), new Vec3d((double) (rX * pScale + rXY * pScale), (double) (rZ * pyScale), (double) (rYZ * pScale + rXZ * pScale)), new Vec3d((double) (rX * pScale - rXY * pScale), (double) (-rZ * pyScale), (double) (rYZ * pScale - rXZ * pScale)) };

        if (this.particleAngle != 0.0F)
        {
            float angle = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            float scale = MathHelper.cos(angle * 0.5F);
            float viewX = MathHelper.sin(angle * 0.5F) * (float) cameraViewDir.x;
            float viewY = MathHelper.sin(angle * 0.5F) * (float) cameraViewDir.y;
            float viewZ = MathHelper.sin(angle * 0.5F) * (float) cameraViewDir.z;
            Vec3d vec3d = new Vec3d((double) viewX, (double) viewY, (double) viewZ);

            for (int v = 0; v < 4; ++v)
            {
                vec[v] = vec3d.scale(2.0D * vec[v].dotProduct(vec3d)).add(vec[v].scale((double) (scale * scale) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(vec[v]).scale((double) (2.0F * scale)));
            }
        }

        buffer.pos((double) x + vec[0].x, (double) y + vec[0].y, (double) z + vec[0].z).tex((double) u1, (double) v1).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();
        buffer.pos((double) x + vec[1].x, (double) y + vec[1].y, (double) z + vec[1].z).tex((double) u1, (double) v2).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();
        buffer.pos((double) x + vec[2].x, (double) y + vec[2].y, (double) z + vec[2].z).tex((double) u2, (double) v2).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();
        buffer.pos((double) x + vec[3].x, (double) y + vec[3].y, (double) z + vec[3].z).tex((double) u2, (double) v1).color(r, g, b, a).lightmap(skyLight, blocKLight).endVertex();

        Tessellator.getInstance().draw();

        if (glow)
        {
            OpenGL.enableLightMapping();
        }
    }

    @Override
    public int getFXLayer()
    {
        return 3;
    }
}
