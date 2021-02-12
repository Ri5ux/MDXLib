package com.asx.mdx.core.network.server;

import com.asx.mdx.commands.CommandBlockScanner;
import com.asx.mdx.lib.util.Game;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCommandBlockScanner implements IMessage, IMessageHandler<PacketCommandBlockScanner, PacketCommandBlockScanner>
{
    private int size;
    private String[] args;

    public PacketCommandBlockScanner()
    {
        ;
    }

    public PacketCommandBlockScanner(String args[])
    {
        this.size = args.length;
        this.args = args;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.size = buf.readInt();
        this.args = new String[this.size];

        for (int i = this.size - 1; i >= 0; i--)
        {
            this.args[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.size);
        
        for (int i = this.size - 1; i >= 0; i--)
        {
            ByteBufUtils.writeUTF8String(buf, args[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public PacketCommandBlockScanner onMessage(PacketCommandBlockScanner packet, MessageContext ctx)
    {
        Game.minecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                new CommandBlockScanner().executeClient(Game.minecraft().player, packet.args);
            }
        });
        return null;
    }
}
