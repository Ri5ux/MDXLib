package com.asx.mdx.lib.client.util.wavegraph.ekg;

import java.util.ArrayList;

import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry;
import com.asx.mdx.lib.client.util.wavegraph.Wavegraph;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry.Interval;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry.Segment;
import com.asx.mdx.lib.client.util.wavegraph.ekg.DataEntryEKG.DisplayDataEKG;
import com.asx.mdx.lib.util.Game;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Electrocardiogram extends Wavegraph
{
    private ArrayList<DataEntryEKG> data;

    public Electrocardiogram()
    {
        this.data = new ArrayList<DataEntryEKG>();
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

    @Override
    public void update(World world)
    {
        this.newdata = false;

        if (world.getWorldTime() % Math.floor((60D / this.rate) * 20) == 0 && !Game.minecraft().isGamePaused())
        {
            this.newdata = true;
            long start = System.currentTimeMillis();

            int pri = 100;
            int prs = 50;
            int qrsi = 75;
            int sts = 100;
            int ti = 75;

            pri = (int) Math.floor(pri / ((float) this.rate / this.refRate));
            prs = (int) Math.floor(prs / ((float) this.rate / this.refRate));
            qrsi = (int) Math.floor(qrsi / ((float) this.rate / this.refRate));
            sts = (int) Math.floor(sts / ((float) this.rate / this.refRate));
            ti = (int) Math.floor(ti / ((float) this.rate / this.refRate));

            Interval prInterval = new Interval(start, start + pri, 0F, 0.25F, 0F);
            Segment prSegment = new Segment(prInterval.timeEnd, prInterval.timeEnd + prs);
            Interval qrsInterval = new Interval(prSegment.timeEnd, prSegment.timeEnd + qrsi, 0F, 0.8F, -0.25F);
            Segment stSegment = new Segment(qrsInterval.timeEnd, qrsInterval.timeEnd + sts);
            Interval tInterval = new Interval(stSegment.timeEnd, stSegment.timeEnd + ti, 0F, 0.15F, 0F);

            this.data.add(new DataEntryEKG(this, prInterval, prSegment, qrsInterval, stSegment, tInterval));
        }

        if (this.data.size() > 10)
        {
            for (DataEntry r : new ArrayList<DataEntry>(this.data))
            {
                if (this.data.indexOf(r) < this.data.size() - 30)
                {
                    this.data.remove(r);
                }
            }
        }
    }

    @Override
    public void render(float partialTicks)
    {
        flatlineHeight = height / 2 / heightScale;
        flatlineY = (y + height - flatlineHeight) - (height / 2);

        OpenGL.enableBlend();
        Draw.drawRect(x, y, width, height, newdata ? backlightColor : backgroundColor);
        this.drawBPMString(partialTicks);
        OpenGL.disableTexture2d();
        this.drawRecords(partialTicks);
        OpenGL.enableTexture2d();
        OpenGL.disableBlend();
    }

    @Override
    public void drawRecords(float partialTicks)
    {
        float depth = 10F;
        DataEntryEKG previousRecord = null;

        for (DataEntryEKG r : new ArrayList<DataEntryEKG>(this.data))
        {
            DisplayDataEKG data = r.displaydata();
            data.update(System.currentTimeMillis(), x, y, width, height, widthScale, heightScale);

            if (data.teX >= x)
            {
                if (previousRecord != null)
                {
                    if (data.prX <= x + width)
                    {
                        Draw.line(data.prX, data.prY, previousRecord.displaydata().teX, previousRecord.displaydata().teY, depth, lineWidth, color);
                    }
                    else
                    {
                        Draw.line(x + width, flatlineY, previousRecord.displaydata().teX, previousRecord.displaydata().teY, depth, lineWidth, color);
                    }
                }
                else if (data.preX >= x)
                {
                    Draw.line(data.prX, data.prY, x, flatlineY, depth, lineWidth, color);
                }

                if (!(this.data.size() > this.data.indexOf(r) + 1))
                {
                    if (data.teX <= x + width)
                    {
                        Draw.line(data.teX, data.teY, x + width, flatlineY, depth, lineWidth, color);
                    }
                }

                if (data.prX >= x && data.prX <= (x + width))
                    Draw.line(data.prX, data.prY, data.prpX, data.prpY, depth, lineWidth, color);

                if (data.prpX >= x && data.prpX <= (x + width))
                    Draw.line(data.prpX, data.prpY, data.preX, data.preY, depth, lineWidth, color);

                if (data.preX >= x && data.preX <= (x + width))
                    Draw.line(data.preX, data.preY, data.qrsX, data.qrsY, depth, lineWidth, color);

                if (data.qrsX >= x && data.qrsX <= (x + width))
                    Draw.line(data.qrsX, data.qrsY, data.qrspX, data.qrspY, depth, lineWidth, color);

                if (data.qrspX >= x && data.qrspX <= (x + width))
                    Draw.line(data.qrspX, data.qrspY, data.qrseX, data.qrseY, depth, lineWidth, color);

                if (data.qrseX >= x && data.qrseX <= (x + width))
                    Draw.line(data.qrseX, data.qrseY, data.tX, data.tY, depth, lineWidth, color);

                if (data.tX >= x && data.tX <= (x + width))
                    Draw.line(data.tX, data.tY, data.tpX, data.tpY, depth, lineWidth, color);

                if (data.tpX >= x && data.tpX <= (x + width))
                    Draw.line(data.tpX, data.tpY, data.teX, data.teY, depth, lineWidth, color);
            }
            else
            {
                this.data.remove(r);
            }

            previousRecord = r;
        }
    }

    public void drawBPMString(float partialTicks)
    {
        Draw.drawString(this.getBPMString(this.rate), x + 4, y + 4, color, false);
    }

    protected String getBPMString(int bpm)
    {
        return String.format("%sBPM", bpm);
    }

    @Override
    public int getRate()
    {
        return rate;
    }

    @Override
    public Electrocardiogram setRate(int bpm)
    {
        this.rate = bpm;
        return this;
    }
}