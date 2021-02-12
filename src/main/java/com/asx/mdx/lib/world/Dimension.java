package com.asx.mdx.lib.world;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.world.entity.Entities;

import net.minecraft.entity.Entity;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class Dimension
{
    protected String                         name;
    protected String                         suffix;
    protected Class<? extends WorldProvider> provider;
    protected boolean                        keepLoaded;
    protected int                            id;
    private DimensionType                    type;
    private boolean                          registered;
    private int                              chunkLoadRadius;

    public Dimension(String name, String suffix, Class<? extends WorldProvider> provider, boolean keepLoaded)
    {
        this.name = name;
        this.suffix = suffix;
        this.provider = provider;
        this.keepLoaded = keepLoaded;
        this.chunkLoadRadius = 256;
    }

    public void findNextAvailableID()
    {
        this.id = DimensionManager.getNextFreeDimId();
    }

    public Dimension register()
    {
        if (!this.registered)
        {
            this.findNextAvailableID();

            if (this.id > 0)
            {
                this.type = DimensionType.register(this.name, this.suffix, this.id, this.provider, this.keepLoaded);
                DimensionManager.registerDimension(this.id, this.type);
                this.registered = true;
                MDX.log().info(String.format("Registered dimension %s with ID %s", this.name, this.id));
            }
        }
        else
        {
            MDX.log().warn("Attempted to register dimension with name %s more than once. Registration attempt blocked.", name);
        }
        return this;
    }

    @Deprecated
    private void findAvailableID()
    {
        for (int i = 200; i < Integer.MAX_VALUE; i++)
        {
            if (!DimensionManager.isDimensionRegistered(i))
            {
                MDX.log().info(String.format("Registering dimension %s with ID %s", this.name, i));
                this.id = i;
                return;
            }
        }

        MDX.log().warn(String.format("Could not find an available dimension ID for dimension %s", this.name));
        this.id = -1;
    }

    public DimensionType getType()
    {
        return type;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public Class<? extends WorldProvider> getProvider()
    {
        return provider;
    }

    public boolean shouldKeepLoaded()
    {
        return keepLoaded;
    }

    public Teleporter getTeleporter(WorldServer worldServer)
    {
        return getDefaultTeleporter(worldServer);
    }

    public static Teleporter getDefaultTeleporter(WorldServer worldServer)
    {
        return new Teleporter(worldServer) {
            @Override
            public void placeInPortal(Entity entityIn, float rotationYaw)
            {
                ;
            }

            @Override
            public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
            {
                return false;
            }

            @Override
            public boolean makePortal(Entity entity)
            {
                return true;
            }
        };
    }

    public int getInitialChunkLoadRadius()
    {
        return this.chunkLoadRadius;
    }

    public boolean shouldRegisterWithForge()
    {
        return true;
    }

    public boolean shouldAutoLoad()
    {
        return false;
    }
    
    public void transferEntityTo(Entity e)
    {
        transferEntityTo(e, this.getId());
    }
    
    public static void transferEntityTo(Entity e, int dimensionId)
    {
        WorldServer worldServer = e.getEntityWorld().getMinecraftServer().getWorld(dimensionId);
        Teleporter teleporter = new Teleporter(worldServer) {
            @Override
            public void placeInPortal(Entity entityIn, float rotationYaw)
            {
                Pos coord = Entities.getSafePositionAboveBelow(new Pos(entityIn.posX, worldServer.provider.getAverageGroundLevel(), entityIn.posZ), worldServer);
                entityIn.setLocationAndAngles(coord.x, coord.y, coord.z, entityIn.rotationYaw, entityIn.rotationPitch);
            }

            @Override
            public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
            {
                return false;
            }

            @Override
            public boolean makePortal(Entity entity)
            {
                return false;
            }
        };
        e.changeDimension(dimensionId, teleporter);
    }
}
