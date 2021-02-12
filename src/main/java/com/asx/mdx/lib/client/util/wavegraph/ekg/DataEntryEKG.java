package com.asx.mdx.lib.client.util.wavegraph.ekg;

import com.asx.mdx.lib.client.util.wavegraph.DataEntry;
import com.asx.mdx.lib.client.util.wavegraph.Wavegraph;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry.DisplayData;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry.Interval;
import com.asx.mdx.lib.client.util.wavegraph.DataEntry.Segment;

public class DataEntryEKG extends DataEntry
{
    public Interval       prInterval;
    public Segment        prSegment;
    public Interval       qrsInterval;
    public Segment        stSegment;
    public Interval       tInterval;

    public DataEntryEKG(Wavegraph graph, Interval prInterval, Segment prSegment, Interval qrsInterval, Segment stSegment, Interval tInterval)
    {
        super(graph);
        this.displaydata = new DisplayDataEKG(graph, this);
        this.prInterval = prInterval;
        this.prSegment = prSegment;
        this.qrsInterval = qrsInterval;
        this.stSegment = stSegment;
        this.tInterval = tInterval;
    }

    @Override
    public int lengthMillis()
    {
        return (int) ((prInterval.timeEnd - prInterval.timeStart) + (prSegment.timeEnd - prSegment.timeStart) + (qrsInterval.timeEnd - qrsInterval.timeStart) + (stSegment.timeEnd - stSegment.timeStart) + (tInterval.timeEnd - tInterval.timeStart));
    }

    public static class DisplayDataEKG extends DisplayData
    {
        public int             prTime;
        public int             prWidth;
        public int             prX;
        public int             preX;
        public int             prpX;
        public int             prY;
        public int             prpY;
        public int             preY;

        public int             prsTime;
        public int             prsWidth;
        public int             prsX;
        public int             prseX;

        public int             qrsTime;
        public int             qrsWidth;
        public int             qrsX;
        public int             qrseX;
        public int             qrspX;
        public int             qrsY;
        public int             qrspY;
        public int             qrseY;

        public int             stsTime;
        public int             stsWidth;
        public int             stsX;
        public int             stseX;

        public int             tTime;
        public int             tWidth;
        public int             tX;
        public int             teX;
        public int             tpX;
        public int             tY;
        public int             tpY;
        public int             teY;

        public DisplayDataEKG(Wavegraph e, DataEntry r)
        {
            super(e, r);
        }

        @Override
        public void update(long curTime, int x, int y, int width, int height, int widthScale, int heightScale)
        {
            super.update(curTime, x, y, width, height, widthScale, heightScale);
            
            DataEntryEKG entry = (DataEntryEKG) this.r;
            
            prTime = (int) (((entry.prInterval.timeEnd - entry.prInterval.timeStart)));
            prWidth = prTime / widthScale;
            prX = recordX;
            preX = prX + prWidth;
            prpX = preX - (prWidth / 2);
            prY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.prInterval.startValue);
            prpY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.prInterval.peakValue);
            preY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.prInterval.endValue);

            prsTime = (int) (entry.prSegment.timeEnd - entry.prSegment.timeStart);
            prsWidth = prsTime / widthScale;
            prsX = preX;
            prseX = prsX + prsWidth;

            qrsTime = (int) (entry.qrsInterval.timeEnd - entry.qrsInterval.timeStart);
            qrsWidth = qrsTime / widthScale;
            qrsX = prseX;
            qrseX = qrsX + qrsWidth;
            qrspX = qrseX - (qrsWidth / 2);
            qrsY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.qrsInterval.startValue);
            qrspY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.qrsInterval.peakValue);
            qrseY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.qrsInterval.endValue);

            stsTime = (int) (entry.stSegment.timeEnd - entry.stSegment.timeStart);
            stsWidth = stsTime / widthScale;
            stsX = qrseX;
            stseX = stsX + stsWidth;

            tTime = (int) (entry.tInterval.timeEnd - entry.tInterval.timeStart);
            tWidth = tTime / widthScale;
            tX = stseX;
            teX = tX + tWidth;
            tpX = teX - (tWidth / 2);
            tY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.tInterval.startValue);
            tpY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.tInterval.peakValue);
            teY = this.e.flatlineY - (int) Math.floor((height / 2) * entry.tInterval.endValue);
        }
    }

    @Override
    public DisplayDataEKG displaydata()
    {
        return (DisplayDataEKG) displaydata;
    }

    @Override
    public Wavegraph getWavegraph()
    {
        return graph;
    }
}