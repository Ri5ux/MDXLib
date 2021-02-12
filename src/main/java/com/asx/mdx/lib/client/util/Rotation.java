package com.asx.mdx.lib.client.util;

import io.netty.buffer.ByteBuf;

public class Rotation
{
    public float yaw;
    public float pitch;

    public Rotation(float yaw, float pitch)
    {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public Rotation setYaw(float yaw)
    {
        this.yaw = yaw;
        return this;
    }
    
    public Rotation setPitch(float pitch)
    {
        this.pitch = pitch;
        return this;
    }
    
    public Rotation readFromBuffer(ByteBuf buf)
    {
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        return this;
    }
    
    public Rotation writeToBuffer(ByteBuf buf)
    {
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        return this;
    }
}
