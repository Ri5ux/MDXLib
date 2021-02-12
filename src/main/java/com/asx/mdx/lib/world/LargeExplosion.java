package com.asx.mdx.lib.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.asx.mdx.lib.world.entity.Entities;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class LargeExplosion
{
    private int                           x;
    private int                           y;
    private int                           z;
    private int                           rX;
    private int                           rY;
    private int                           rZ;
    private int                           erb;
    private int                           erm;
    private float                         damage;
    private World                         world;
    private Random                        random;
    private ArrayList<Block>              excludedBlocks;
    private ArrayList<Material>           excludedMaterials;
    private static final ArrayList<Block> EXCLUDE_DEFAULT = new ArrayList<Block>();

    static
    {
        EXCLUDE_DEFAULT.add(Blocks.BEDROCK);
    }

    public LargeExplosion(World world, double rX, double rY, double rZ, int x, int y, int z, long seed)
    {
        this(world, rX, rY, rZ, x, y, z, 1000F, seed, EXCLUDE_DEFAULT);
    }

    public LargeExplosion(World world, double rX, double rY, double rZ, int x, int y, int z, float damage, long seed)
    {
        this(world, rX, rY, rZ, x, y, z, damage, seed, EXCLUDE_DEFAULT);
    }

    public LargeExplosion(World world, double rX, double rY, double rZ, int x, int y, int z, float damage, long seed, ArrayList<Block> exclude)
    {
        this(world, rX, rY, rZ, x, y, z, damage, seed, EXCLUDE_DEFAULT, null, 0, 0);
    }

    public LargeExplosion(World world, double rX, double rY, double rZ, int x, int y, int z, float damage, long seed, ArrayList<Block> excludedBlocks, ArrayList<Material> excludedMaterials, int erb, int erm)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rX = (int) Math.ceil(rX);
        this.rY = (int) Math.ceil(rY);
        this.rZ = (int) Math.ceil(rZ);
        this.damage = damage;
        this.random = new Random(seed);
        this.excludedBlocks = excludedBlocks;
        this.excludedMaterials = excludedMaterials;
        this.erb = erb;
        this.erm = erm;
    }

    public void process()
    {
        rX += 0.5D;
        rY += 0.5D;
        rZ += 0.5D;

        int size = ((rX + rY + rZ) / 3) / 2;

        for (int idx = 0; idx <= size; ++idx)
        {
            float scale = 1F - (1F / size);
            rX = Math.round(rX * scale);
            rY = Math.round(rY * scale);
            rZ = Math.round(rZ * scale);
            double x1 = 0.0D;

            for (int posX = 0; posX <= rX; ++posX)
            {
                double x2 = x1;
                x1 = (posX + 1) * (1D / rX);

                for (int posY = 0; posY <= rY; ++posY)
                {
                    double y1 = 0.0D;
                    double y2 = (posY + 1) * (1D / rY);
                    double z2 = 0.0D;

                    for (int posZ = 0; posZ <= rZ; ++posZ)
                    {
                        double z1 = z2;
                        z2 = (posZ + 1) * (1D / rZ);

                        if (this.sq(x2, y1, z1) > 1.0D && posZ == 0 && posY == 0)
                        {
                            break;
                        }

                        if ((this.sq(x1, y1, z1) <= 1.0D && this.sq(x2, y2, z1) <= 1.0D && this.sq(x2, y1, z2) <= 1.0D))
                        {
                            pos(posX, posY, posZ);
                            pos(-posX, posY, posZ);
                            pos(posX, -posY, posZ);
                            pos(posX, posY, -posZ);
                            pos(-posX, -posY, posZ);
                            pos(posX, -posY, -posZ);
                            pos(-posX, posY, -posZ);
                            pos(-posX, -posY, -posZ);
                        }
                    }
                }
            }
        }
    }

    public void start()
    {
        this.process();

        // TODO: FIX SOUND
        // world.playSoundEffect(x, y, z, "random.old_explode", 4.0F, (1.0F +
        // (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);

        List<Entity> entities = Entities.getEntitiesInCoordsRange(world, Entity.class, new Pos(x, y, z), this.rX, this.rY);
        
        for (int idx = 0; idx < entities.size(); ++idx)
        {
            if (entities.get(idx) instanceof EntityLivingBase)
            {
                EntityLivingBase living = (EntityLivingBase) entities.get(idx);
                living.attackEntityFrom(DamageSource.causeExplosionDamage(living), this.damage);
            }
        }
    }

    private final double sq(double posX, double posY, double posZ)
    {
        return (posX * posX) + (posY * posY) + (posZ * posZ);
    }

    private final void pos(double posX, double posY, double posZ)
    {
        int dX = (int) posX + this.x;
        int dY = (int) posY + this.y;
        int dZ = (int) posZ + this.z;
        BlockPos pos = new BlockPos(dX, dY, dZ);
        IBlockState state = this.world.getBlockState(pos);

        if (excludedBlocks != null && (excludedBlocks.contains(state.getBlock()) && (erb > 0 && this.random.nextInt(this.erb) == 0 || erb == 0)) || state.getBlock() == Blocks.AIR)
        {
            return;
        }

        if (excludedMaterials != null && excludedMaterials.contains(state.getMaterial()) && (erm > 0 && this.random.nextInt(this.erm) == 0 || erm == 0))
        {
            return;
        }

        state.getBlock().onExplosionDestroy(this.world, pos, new Explosion(this.world, null, dX, dY, dZ, 1F, false, false));
        this.world.setBlockToAir(pos);
    }
}
