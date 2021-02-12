package com.asx.mdx.lib.client.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ImageBuffer extends ImageBufferDownload implements IImageBuffer
{
    private int[] imageData;
    private int imageWidth;
    private int imageHeight;

    public ImageBuffer(int width, int height)
    {
        this.imageWidth = width;
        this.imageHeight = height;
    }

    public BufferedImage parseUserSkin(BufferedImage image)
    {
        if (image == null)
        {
            return null;
        }
        else
        {
            BufferedImage bufferedimage1 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
            Graphics graphics = bufferedimage1.getGraphics();
            graphics.drawImage(image, 0, 0, (ImageObserver) null);
            graphics.dispose();
            this.imageData = ((DataBufferInt) bufferedimage1.getRaster().getDataBuffer()).getData();
            this.setAreaOpaque(0, 0, 32, 16);
            this.setAreaTransparent(32, 0, 64, 32);
            this.setAreaOpaque(0, 16, 64, 32);
            return bufferedimage1;
        }
    }

    /**
     * Makes the given area of the image transparent if it was previously
     * completely opaque (used to remove the outer layer of a skin around
     * the head if it was saved all opaque; this would be redundant so it's
     * assumed that the skin maker is just using an image editor without an
     * alpha channel)
     */
    private void setAreaTransparent(int x, int y, int width, int height)
    {
        if (!this.hasTransparency(x, y, width, height))
        {
            for (int x1 = x; x1 < width; ++x1)
            {
                for (int y1 = y; y1 < height; ++y1)
                {
                    this.imageData[x1 + y1 * this.imageWidth] &= 16777215;
                }
            }
        }
    }

    /**
     * Makes the given area of the image opaque
     */
    private void setAreaOpaque(int x, int y, int width, int height)
    {
        for (int x1 = x; x1 < width; ++x1)
        {
            for (int y1 = y; y1 < height; ++y1)
            {
                this.imageData[x1 + y1 * this.imageWidth] |= -16777216;
            }
        }
    }

    /**
     * Returns true if the given area of the image contains transparent
     * pixels
     */
    private boolean hasTransparency(int x, int y, int width, int height)
    {
        for (int x1 = x; x1 < width; ++x1)
        {
            for (int y1 = y; y1 < height; ++y1)
            {
                if (((this.imageData[x1 + y1 * this.imageWidth]) >> 24 & 255) < 128)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void func_152634_a()
    {
        ;
    }
}
