package com.asx.mdx.lib.client.util.wavegraph;

import java.util.ArrayList;
import java.util.Random;

import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry.DisplayData;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry.Interval;
import com.asx.mdx.lib.util.Game;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Wavegraph
{
    protected ArrayList<DataEntry> data;
    protected boolean              newdata;
    protected int                  rate;
    public int                     refRate;
    public int                     x;
    public int                     y;
    public int                     width;
    public int                     height;
    public int                     widthScale;
    public int                     heightScale;
    public int                     color;
    public int                     backlightColor;
    public int                     backgroundColor;
    public int                     timespan;
    public int                     flatlineHeight;
    public int                     flatlineY;
    public float                   lineWidth;

    public Wavegraph()
    {
        this.data = new ArrayList<DataEntry>();
        this.rate = 60;
        this.refRate = 80;
        this.width = 300;
        this.height = 80;
        this.widthScale = 16;
        this.heightScale = 16;
        this.color = 0xFF00CCFF;
        this.backgroundColor = 0x44000000;
        this.backlightColor = 0x44003344;
        this.lineWidth = 1F;
    }

    public void update(World world)
    {
        this.newdata = false;

        if (world.getWorldTime() % Math.floor((60D / this.rate) * 20) == 0 && !Game.minecraft().isGamePaused())
        {
            this.newdata = true;
            long start = System.currentTimeMillis();

            int pri = 100;

            pri = (int) Math.floor(pri / ((float) this.rate / this.refRate));

            Interval interval = new Interval(start, start + pri, 0F, new Random().nextFloat() / 2, -(new Random().nextFloat() / 2));

            this.data.add(new DataEntry(this, interval));
        }

        if (this.data.size() > 10)
        {
            for (DataEntry r : new ArrayList<DataEntry>(this.data))
            {
                if (this.data.indexOf(r) < this.data.size() - 45)
                {
                    this.data.remove(r);
                }
            }
        }
    }

    public void render(float partialTicks)
    {
        flatlineHeight = height / 2 / heightScale;
        flatlineY = (y + height - flatlineHeight) - (height / 2);

        OpenGL.enableBlend();
        Draw.drawRect(x, y, width, height, newdata ? backlightColor : backgroundColor);
        OpenGL.disableTexture2d();
        this.drawRecords(partialTicks);
        OpenGL.enableTexture2d();
        OpenGL.disableBlend();
    }

    public void drawRecords(float partialTicks)
    {
        float depth = 10F;
        DataEntry previousRecord = null;

        for (DataEntry r : new ArrayList<DataEntry>(this.data))
        {
            DisplayData data = r.displaydata();
            data.update(System.currentTimeMillis(), x, y, width, height, widthScale, heightScale);

            if (data.iEndX >= x)
            {
                if (previousRecord != null)
                {
                    if (data.iX <= x + width)
                    {
                        Draw.line(data.iX, data.iY, previousRecord.displaydata().iEndX, previousRecord.displaydata().iEndY, depth, lineWidth, color);
                    }
                    else
                    {
                        Draw.line(x + width, flatlineY, previousRecord.displaydata().iEndX, previousRecord.displaydata().iEndY, depth, lineWidth, color);
                    }
                }
                else if (data.iEndX >= x)
                {
                    Draw.line(data.iX, data.iY, x, flatlineY, depth, lineWidth, color);
                }

                if (!(this.data.size() > this.data.indexOf(r) + 1))
                {
                    if (data.iEndX <= x + width)
                    {
                        Draw.line(data.iEndX, data.iEndY, x + width, flatlineY, depth, lineWidth, color);
                    }
                }

                if (data.iX >= x && data.iX <= (x + width))
                    Draw.line(data.iX, data.iY, data.iPeakX, data.iPeakY, depth, lineWidth, color);

                if (data.iPeakX >= x && data.iPeakX <= (x + width))
                    Draw.line(data.iPeakX, data.iPeakY, data.iEndX, data.iEndY, depth, lineWidth, color);
            }
            else
            {
                this.data.remove(r);
            }

            previousRecord = r;
        }
    }

    public ArrayList<DataEntry> getData()
    {
        return data;
    }

    public int getRate()
    {
        return rate;
    }

    public Wavegraph setRate(int rate)
    {
        this.rate = rate;
        return this;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Wavegraph setColor(int color)
    {
        this.color = color;
        return this;
    }

    public Wavegraph setX(int x)
    {
        this.x = x;
        return this;
    }

    public Wavegraph setY(int y)
    {
        this.y = y;
        return this;
    }

    public Wavegraph setWidth(int width)
    {
        this.width = width;
        return this;
    }

    public Wavegraph setHeight(int height)
    {
        this.height = height;
        return this;
    }

    public Wavegraph setScale(int widthScale, int heightScale)
    {
        this.widthScale = widthScale;
        this.heightScale = heightScale;
        return this;
    }

    public Wavegraph setBackgroundColor(int backgroundColor)
    {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public Wavegraph setBacklightColor(int backlightColor)
    {
        this.backlightColor = backlightColor;
        return this;
    }

    public Wavegraph setLineWidth(float lineWidth)
    {
        this.lineWidth = lineWidth;
        return this;
    }
}