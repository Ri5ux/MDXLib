package com.asx.mdx.lib.client.util.wavegraph;

public class DataEntry
{
    protected Wavegraph   graph;
    protected DisplayData displaydata;
    public long           time;
    private Interval      interval;

    public DataEntry(Wavegraph graph, Interval interval)
    {
        this(graph);
        this.interval = interval;
    }

    public DataEntry(Wavegraph graph)
    {
        this.graph = graph;
        this.time = System.currentTimeMillis();
        this.displaydata = new DisplayData(graph, this);
    }

    public int lengthMillis()
    {
        return (int) (interval.timeEnd - interval.timeStart);
    }

    public static class Segment
    {
        public long timeStart;
        public long timeEnd;

        public Segment(long timeStart, long timeEnd)
        {
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
        }
    }

    public static class Interval
    {
        public long  timeStart;
        public long  timeEnd;
        public float startValue;
        public float peakValue;
        public float endValue;

        public Interval(long timeStart, long timeEnd, float startValue, float peakValue, float endValue)
        {
            this.timeStart = timeStart;
            this.timeEnd = timeEnd;
            this.startValue = startValue;
            this.peakValue = peakValue;
            this.endValue = endValue;
        }
    }

    public static class DisplayData
    {
        protected Wavegraph e;
        protected DataEntry r;

        public int          recordTime;
        public int          recordElapseTime;
        public int          recordWidth;
        public int          recordHeight;
        public int          recordX;
        public int          recordY;

        public int          iTime;
        public int          iWidth;
        public int          iX;
        public int          iEndX;
        public int          iPeakX;
        public int          iY;
        public int          iPeakY;
        public int          iEndY;

        public DisplayData(Wavegraph e, DataEntry r)
        {
            this.e = e;
            this.r = r;
        }

        public void update(long curTime, int x, int y, int width, int height, int widthScale, int heightScale)
        {
            if (this.r == null)
            {
                return;
            }

            recordTime = (int) (curTime - r.time) / 16;
            recordElapseTime = this.r.lengthMillis();
            recordWidth = recordElapseTime / widthScale;
            recordHeight = height / heightScale;
            recordX = x + (width - recordTime) + recordWidth;
            recordY = y;

            if (this.r.interval != null)
            {
                iTime = (int) (((this.r.interval.timeEnd - this.r.interval.timeStart)));
                iWidth = iTime / widthScale;
                iX = recordX;
                iEndX = iX + iWidth;
                iPeakX = iEndX - (iWidth / 2);
                iY = this.e.flatlineY - (int) Math.floor((height / 2) * this.r.interval.startValue);
                iPeakY = this.e.flatlineY - (int) Math.floor((height / 2) * this.r.interval.peakValue);
                iEndY = this.e.flatlineY - (int) Math.floor((height / 2) * this.r.interval.endValue);
            }
        }
    }

    public DisplayData displaydata()
    {
        return displaydata;
    }

    public Wavegraph getWavegraph()
    {
        return graph;
    }
}