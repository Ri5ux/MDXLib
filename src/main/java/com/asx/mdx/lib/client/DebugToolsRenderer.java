package com.asx.mdx.lib.client;

import java.util.ArrayList;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class DebugToolsRenderer
{
    public static final DebugToolsRenderer instance = new DebugToolsRenderer();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void renderWorldLastEvent(RenderWorldLastEvent event)
    {
        BlockScanner.renderWorldLastEvent(event);
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent event)
    {
        BlockScanner.tick(event);
        MDX.windows().onTick();
    }

    @SideOnly(Side.CLIENT)
    public static class BlockScanner
    {
        private static boolean              blockScannerEnabled;
        private static boolean              chunkBordersEnabled;
        private static boolean              chunkPlaneEnabled;
        private static ArrayList<Scannable> blockScanners = new ArrayList<Scannable>();;

        public static class Scannable
        {
            private Block               block;
            private ArrayList<BlockPos> blocksFound;
            private int                 scanRange;
            private float               r;
            private float               g;
            private float               b;
            private float               a;

            public Scannable(Block block)
            {
                this(block, 0.5F, 0F, 1F, 0.4F);
            }

            public Scannable(Block block, float r, float g, float b, float a)
            {
                this(block, 32, r, g, b, a);
            }

            public Scannable(Block block, int scanRange, float r, float g, float b, float a)
            {
                this.blocksFound = new ArrayList<BlockPos>();
                this.block = block;
                this.scanRange = scanRange;
                this.r = r;
                this.g = g;
                this.b = b;
                this.a = a;
            }
        }

        public static void scanForBlock(Scannable scanner)
        {
            boolean pass = true;

            for (Scannable s : blockScanners)
            {
                if (s.block == scanner.block)
                {
                    pass = false;
                }
            }

            if (pass)
            {
                blockScanners.add(scanner);
            }
        }

        public static void destroyBlockScanner(Scannable scanner)
        {
            for (Scannable s : (ArrayList<Scannable>) blockScanners.clone())
            {
                if (s.block == scanner.block)
                {
                    blockScanners.remove(s);
                }
            }
        }

        public static void destroyBlockScanners()
        {
            if (blockScanners != null)
            {
                blockScanners.clear();
            }
        }

        public static void renderWorldLastEvent(RenderWorldLastEvent event)
        {
            EntityPlayer p = Minecraft.getMinecraft().player;
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buff = tess.getBuffer();

            BlockScanner.drawBlockScannerBorders(event, p, tess, buff);
            BlockScanner.drawChunkBorders(event, p, tess, buff);
            BlockScanner.drawChunkPlane(event, p, tess, buff);
        }

        public static void tick(ClientTickEvent event)
        {
            if (BlockScanner.isBlockScannerEnabled())
            {
                if (event.type == Type.CLIENT)
                {
                    if (Game.minecraft().world != null && Game.minecraft().world.getWorldTime() % 40 == 0)
                    {
                        int posX = (int) Game.minecraft().player.posX;
                        int posY = (int) Game.minecraft().player.posY;
                        int posZ = (int) Game.minecraft().player.posZ;

                        for (Scannable scanner : blockScanners)
                        {
                            if (scanner.block != null)
                            {
                                scanner.blocksFound = com.asx.mdx.lib.world.block.Blocks.getBlocksInRangeIncluding(posX, posY, posZ, scanner.scanRange, Game.minecraft().world, scanner.block);
                            }
                        }
                    }
                }
            }
        }

        private static void drawBlockScannerBorders(RenderWorldLastEvent event, EntityPlayer p, Tessellator tess, BufferBuilder buff)
        {
            if (isBlockScannerEnabled() && Game.minecraft().gameSettings.thirdPersonView == 0)
            {
                double x = p.lastTickPosX + (p.posX - p.lastTickPosX) * (double) event.getPartialTicks();
                double y = p.lastTickPosY + (p.posY - p.lastTickPosY) * (double) event.getPartialTicks();
                double z = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * (double) event.getPartialTicks();

                GlStateManager.disableTexture2D();
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableBlend();
                OpenGL.disableLight();
                GlStateManager.disableDepth();

                if (blockScanners != null && !blockScanners.isEmpty())
                {
                    for (Scannable scanner : blockScanners)
                    {
                        if (scanner != null && scanner.blocksFound != null && !scanner.blocksFound.isEmpty())
                        {
                            for (BlockPos pos : scanner.blocksFound)
                            {
                                double cubeX = -(x - pos.getX());
                                double cubeY = -(y - pos.getY());
                                double cubeZ = -(z - pos.getZ());

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX, cubeY + 0.0D, cubeZ).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX, cubeY + 1.0D, cubeZ).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 1.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 0.0D, cubeY + 1.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 0.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();

                                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);
                                buff.pos(cubeX + 1.0D, cubeY + 0.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                buff.pos(cubeX + 0.0D, cubeY + 0.0D, cubeZ + 1.0D).color(scanner.r, scanner.g, scanner.b, scanner.a).endVertex();
                                tess.draw();
                            }
                        }
                    }
                }

                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
            }
        }

        private static void drawChunkBorders(RenderWorldLastEvent event, EntityPlayer p, Tessellator tess, BufferBuilder buff)
        {
            if (areChunkBordersEnabled())
            {
                double x = p.lastTickPosX + (p.posX - p.lastTickPosX) * (double) event.getPartialTicks();
                double y = p.lastTickPosY + (p.posY - p.lastTickPosY) * (double) event.getPartialTicks();
                double z = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * (double) event.getPartialTicks();
                double sY = 0.0D - y;
                double eY = 256.0D - y;
                double sX = (double) (p.chunkCoordX << 4) - x;
                double sZ = (double) (p.chunkCoordZ << 4) - z;

                GlStateManager.disableTexture2D();
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableBlend();
                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);

                for (int k = 1; k < 16; k += 1)
                {
                    buff.pos(sX + (double) k, sY, sZ).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    buff.pos(sX + (double) k, sY, sZ).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + (double) k, eY, sZ).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + (double) k, eY, sZ).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    buff.pos(sX + (double) k, sY, sZ + 16.0D).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    buff.pos(sX + (double) k, sY, sZ + 16.0D).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + (double) k, eY, sZ + 16.0D).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + (double) k, eY, sZ + 16.0D).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                }

                for (int l = 1; l < 16; l += 1)
                {
                    buff.pos(sX, sY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    buff.pos(sX, sY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX, eY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX, eY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    buff.pos(sX + 16.0D, sY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    buff.pos(sX + 16.0D, sY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + 16.0D, eY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + 16.0D, eY, sZ + (double) l).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                }

                for (int i1 = 0; i1 <= 256; i1 += 1)
                {
                    double d7 = (double) i1 - y;
                    buff.pos(sX, d7, sZ).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                    buff.pos(sX, d7, sZ).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX, d7, sZ + 16.0D).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + 16.0D, d7, sZ + 16.0D).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX + 16.0D, d7, sZ).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX, d7, sZ).color(0.0F, 1.0F, 0.0F, 0.6F).endVertex();
                    buff.pos(sX, d7, sZ).color(0.0F, 1.0F, 0.0F, 0.0F).endVertex();
                }

                tess.draw();
                GlStateManager.glLineWidth(3.0F);
                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);

                for (int j1 = 0; j1 <= 16; j1 += 16)
                {
                    for (int l1 = 0; l1 <= 16; l1 += 16)
                    {
                        buff.pos(sX + (double) j1, sY, sZ + (double) l1).color(0.25F, 0F, 1.0F, 0.0F).endVertex();
                        buff.pos(sX + (double) j1, sY, sZ + (double) l1).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                        buff.pos(sX + (double) j1, eY, sZ + (double) l1).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                        buff.pos(sX + (double) j1, eY, sZ + (double) l1).color(0.25F, 0F, 1.0F, 0.0F).endVertex();
                    }
                }

                for (int k1 = 0; k1 <= 256; k1 += 16)
                {
                    double d8 = (double) k1 - y;
                    buff.pos(sX, d8, sZ).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
                    buff.pos(sX, d8, sZ).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                    buff.pos(sX, d8, sZ + 16.0D).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                    buff.pos(sX + 16.0D, d8, sZ + 16.0D).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                    buff.pos(sX + 16.0D, d8, sZ).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                    buff.pos(sX, d8, sZ).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                    buff.pos(sX, d8, sZ).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
                }

                tess.draw();
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableTexture2D();
            }
        }

        private static void drawChunkPlane(RenderWorldLastEvent event, EntityPlayer p, Tessellator tess, BufferBuilder buff)
        {
            if (isChunkPlaneEnabled())
            {
                Entity e = Game.minecraft().renderViewEntity;
                double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) event.getPartialTicks();
                double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) event.getPartialTicks();
                double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) event.getPartialTicks();
                
                y = 0 - y;
                
                double sY = 0.0D - y;
                double eY = 256.0D - y;
                double sX = (double) (p.chunkCoordX << 4) - x;
                double sZ = (double) (p.chunkCoordZ << 4) - z;

                GlStateManager.disableTexture2D();
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableBlend();
                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);

//                for (int yDist = -16 * 16; yDist < 16 * 16; yDist += 1)
//                {
//                    for (int xDist = -16 * 16; xDist < 16 * 16; xDist += 1)
//                    {
//                        double yLevel = (double) y;
//                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//                        buff.pos(sX + xDist, yLevel, sZ + 1.0D + yDist).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//                        buff.pos(sX + xDist + 1.0D, yLevel, sZ + 1.0D + yDist).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//                        buff.pos(sX + xDist + 1.0D, yLevel, sZ + yDist).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
//                    }
//                }
                

                tess.draw();
                GlStateManager.glLineWidth(1F);
                buff.begin(3, DefaultVertexFormats.POSITION_COLOR);

                for (int yDist = -16 * 100; yDist <= 16 * 100; yDist += 16)
                {
                    for (int xDist = -16 * 100; xDist <= 16 * 100; xDist += 16)
                    {
                        double yLevel = (double) y;
                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                        buff.pos(sX + xDist, yLevel, sZ + 16.0D + yDist).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                        buff.pos(sX + xDist + 16.0D, yLevel, sZ + 16.0D + yDist).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                        buff.pos(sX + xDist + 16.0D, yLevel, sZ + yDist).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.25F, 0F, 1.0F, 1.0F).endVertex();
                        buff.pos(sX + xDist, yLevel, sZ + yDist).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
                    }
                }

                tess.draw();
                GlStateManager.glLineWidth(1.0F);
                GlStateManager.enableTexture2D();
                OpenGL.disableBlend();
            }
        }

        public static boolean areChunkBordersEnabled()
        {
            return chunkBordersEnabled;
        }

        public static void setChunkBordersEnabled(boolean enabled)
        {
            chunkBordersEnabled = enabled;
        }

        public static boolean isChunkPlaneEnabled()
        {
            return chunkPlaneEnabled;
        }

        public static void setChunkPlaneEnabled(boolean enabled)
        {
            chunkPlaneEnabled = enabled;
        }

        public static boolean isBlockScannerEnabled()
        {
            return blockScannerEnabled;
        }

        public static void setBlockScannerEnabled(boolean enabled)
        {
            blockScannerEnabled = enabled;
        }
    }
}
