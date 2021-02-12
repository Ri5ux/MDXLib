package com.asx.mdx.lib.client.util.models.block;

import com.asx.mdx.lib.client.util.Quad;

public interface ICubeQuads
{
    /**
     * Provides a quad of vectors for rendering the bottom face of a block.
     */
    public Quad getYNeg();

    /**
     * Provides a quad of vectors for rendering the top face of a block.
     */
    public Quad getYPos();

    /**
     * Provides a quad of vectors for rendering the North face of a block. 
     */
    public Quad getZNeg();

    /**
     * Provides a quad of vectors for rendering the South face of a block. 
     */
    public Quad getZPos();

    /**
     * Provides a quad of vectors for rendering the West face of a block. 
     */
    public Quad getXNeg();

    /**
     * Provides a quad of vectors for rendering the East face of a block. 
     */
    public Quad getXPos();
}
