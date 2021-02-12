package com.asx.mdx.lib.client.model.loaders.tabula.container;

import java.util.ArrayList;
import java.util.List;

public class TabulaCubeContainer
{
    private String                    name;
    private String                    identifier;
    private String                    parentIdentifier;

    private int[]                     dimensions = new int[3];
    private double[]                  position   = new double[3];
    private double[]                  offset     = new double[3];
    private double[]                  rotation   = new double[3];
    private double[]                  scale      = new double[3];

    private int[]                     txOffset   = new int[2];
    private boolean                   txMirror;

    private double                    mcScale    = 1.0;
    private double                    opacity    = 100.0;
    private boolean                   hidden;

    private List<TabulaCubeContainer> children   = new ArrayList<>();

    public TabulaCubeContainer(String name, String identifier, String parentIdentifier, int[] dimensions, double[] position, double[] offset, double[] rotation, double[] scale, int[] textureOffset, boolean textureMirror, double opacity, double mcScale, boolean hidden)
    {
        this.name = name;
        this.identifier = identifier;
        this.parentIdentifier = parentIdentifier;
        this.dimensions = dimensions;
        this.position = position;
        this.offset = offset;
        this.rotation = rotation;
        this.scale = scale;
        this.txOffset = textureOffset;
        this.txMirror = textureMirror;
        this.opacity = opacity;
        this.mcScale = mcScale;
        this.hidden = hidden;
    }

    public String getName()
    {
        return this.name;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public String getParentIdentifier()
    {
        return this.parentIdentifier;
    }

    public int[] getDimensions()
    {
        return this.dimensions;
    }
    
    public void setDimensions(int[] dimensions)
    {
        this.dimensions = dimensions;
    }

    public double[] getPosition()
    {
        return this.position;
    }
    
    public void setPosition(double[] position)
    {
        this.position = position;
    }

    public double[] getOffset()
    {
        return this.offset;
    }
    
    public void setOffset(double[] offset)
    {
        this.offset = offset;
    }

    public double[] getRotation()
    {
        return this.rotation;
    }
    
    public void setRotation(double[] rotation)
    {
        this.rotation = rotation;
    }

    public double[] getScale()
    {
        return this.scale;
    }
    
    public void setScale(double[] scale)
    {
        this.scale = scale;
    }

    public int[] getTextureOffset()
    {
        return this.txOffset;
    }
    
    public void setTxOffset(int[] txOffset)
    {
        this.txOffset = txOffset;
    }

    public boolean isTextureMirrorEnabled()
    {
        return this.txMirror;
    }

    public double getMCScale()
    {
        return this.mcScale;
    }

    public double getOpacity()
    {
        return this.opacity;
    }

    public boolean isHidden()
    {
        return this.hidden;
    }

    public List<TabulaCubeContainer> getChildren()
    {
        return this.children;
    }
}
