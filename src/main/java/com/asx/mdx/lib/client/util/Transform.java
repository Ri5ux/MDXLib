package com.asx.mdx.lib.client.util;

public class Transform
{
    private float rotationX;
    private float rotationY;
    private float rotationZ;
    
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    
    private float positionX;
    private float positionY;
    private float positionZ;

    public float getRotationX()
    {
        return this.rotationX;
    }

    public float getRotationY()
    {
        return this.rotationY;
    }

    public float getRotationZ()
    {
        return this.rotationZ;
    }

    public float getOffsetX()
    {
        return this.offsetX;
    }

    public float getOffsetY()
    {
        return this.offsetY;
    }

    public float getOffsetZ()
    {
        return this.offsetZ;
    }
    
    public float getPositionX()
    {
        return positionX;
    }
    
    public float getPositionY()
    {
        return positionY;
    }
    
    public float getPositionZ()
    {
        return positionZ;
    }

    public void addRotation(float x, float y, float z)
    {
        this.rotationX += x;
        this.rotationY += y;
        this.rotationZ += z;
    }

    public void addOffset(float x, float y, float z)
    {
        this.offsetX += x;
        this.offsetY += y;
        this.offsetZ += z;
    }

    public void addPosition(float x, float y, float z)
    {
        this.positionX += x;
        this.positionY += y;
        this.positionZ += z;
    }

    public void resetRotation()
    {
        this.rotationX = 0.0F;
        this.rotationY = 0.0F;
        this.rotationZ = 0.0F;
    }

    public void resetOffset()
    {
        this.offsetX = 0.0F;
        this.offsetY = 0.0F;
        this.offsetZ = 0.0F;
    }

    public void resetPosition()
    {
        this.positionX = 0.0F;
        this.positionY = 0.0F;
        this.positionZ = 0.0F;
    }

    public void setRotation(float x, float y, float z)
    {
        this.resetRotation();
        this.addRotation(x, y, z);
    }

    public void setOffset(float x, float y, float z)
    {
        this.resetOffset();
        this.addOffset(x, y, z);
    }
    
    public void setPosition(float x, float y, float z)
    {
        this.resetPosition();
        this.addPosition(x, y, z);
    }
}