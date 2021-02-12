package com.asx.mdx.core;

import com.asx.mdx.MDX;
import com.asx.mdx.core.mods.IInitEvent;
import com.asx.mdx.core.network.server.PacketAnimation;
import com.asx.mdx.core.network.server.PacketAnimationPause;
import com.asx.mdx.core.network.server.PacketCommandBlockScanner;
import com.asx.mdx.core.network.server.PacketCommandChunkBorders;
import com.asx.mdx.core.network.server.PacketCommandChunkPlane;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler extends SimpleNetworkWrapper implements IInitEvent
{
    public static final PacketHandler instance      = new PacketHandler();
    private int                       descriminator = 0;

    public PacketHandler()
    {
        super(MDX.Properties.ID.toUpperCase());
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        this.registerMessage(Side.CLIENT, PacketCommandBlockScanner.class);
        this.registerMessage(Side.CLIENT, PacketCommandChunkBorders.class);
        this.registerMessage(Side.CLIENT, PacketCommandChunkPlane.class);
        this.registerMessage(Side.CLIENT, PacketAnimation.class);
        this.registerMessage(Side.CLIENT, PacketAnimationPause.class);
    }

    /**
     * @param side - The side this packet will be sent to.
     * @param packet - The packet being registered.
     */
    @SuppressWarnings("unchecked")
    private <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Side side, Class<?> packet)
    {
        this.registerMessage((Class<? extends IMessageHandler<REQ, REPLY>>) packet, (Class<REQ>) packet, descriminator++, side);
    }
}
