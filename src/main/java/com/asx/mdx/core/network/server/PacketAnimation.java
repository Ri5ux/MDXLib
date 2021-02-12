package com.asx.mdx.core.network.server;

import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.world.entity.animations.IAnimated;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketAnimation implements IMessage, IMessageHandler<PacketAnimation, PacketAnimation>
{
    private int entityID;
    private int index;

    public PacketAnimation()
    {

    }

    public PacketAnimation(int entityID, int index)
    {
        this.entityID = entityID;
        this.index = index;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public PacketAnimation onMessage(PacketAnimation packet, MessageContext ctx)
    {
        Game.minecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                IAnimated entity = (IAnimated) Game.minecraft().player.world.getEntityByID(packet.entityID);
                if (entity != null)
                {
                    if (packet.index == -1)
                    {
                        entity.setActiveAnimation(IAnimated.NO_ANIMATION);
                    }
                    else
                    {
                        entity.setActiveAnimation(entity.getAnimations()[packet.index]);
                    }
                    entity.setAnimationTick(0);
                }
            }
        });
        return null;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf)
    {
        this.entityID = byteBuf.readInt();
        this.index = byteBuf.readInt();
    }

    @Override
    public void toBytes(ByteBuf byteBuf)
    {
        byteBuf.writeInt(this.entityID);
        byteBuf.writeInt(this.index);
    }
}