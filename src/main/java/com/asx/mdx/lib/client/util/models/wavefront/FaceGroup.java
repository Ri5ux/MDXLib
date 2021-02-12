package com.asx.mdx.lib.client.util.models.wavefront;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;

import net.minecraft.util.ResourceLocation;

public class FaceGroup
{
    public String material;
    public ResourceLocation resource;
    public ArrayList<Face> faces;
    public boolean listReady;
    public int glList;
    public Color color;
    
    public FaceGroup()
    {
        this.faces = new ArrayList<Face>();
        this.listReady = false;
    }

    public void bindTexture()
    {
        if (resource != null)
        {
            Draw.bindTexture(resource);
        }
    }

    public void draw()
    {
        if (resource != null)
        {
            bindTexture();
            drawNoBind();
        }
        else
        {
            OpenGL.disableTexture2d();
            drawNoBind();
            OpenGL.enableTexture2d();
        }
    }

    public void drawNoBind()
    {
//        if (listReady == false)
//        {
//            listReady = true;
//            glList = GL11.glGenLists(1);
//
//            OpenGL.newList(glList, GL11.GL_COMPILE);
            this.drawVertex();
//            OpenGL.endList();
//        }

//        OpenGL.callList(glList);
    }

    private void drawVertex()
    {
        int mode = 0;

        for (Face f : faces)
        {
            if (f.vertexNbr != mode)
            {
                if (mode != 0)
                {
                    GL11.glEnd();
                }

                switch (f.vertexNbr)
                {
                    case 3:
                        OpenGL.begin(GL11.GL_TRIANGLES);
                        break;
                    case 4:
                        OpenGL.begin(GL11.GL_QUADS);
                        break;
                    case 6:
                        OpenGL.begin(GL11.GL_TRIANGLE_STRIP);
                        break;
                    case 8:
                        OpenGL.begin(GL11.GL_TRIANGLE_STRIP);
                        break;
                }

                mode = f.vertexNbr;
            }

            if (this.color != null)
            {
                //OpenGL.color(this.color.r, this.color.g, this.color.b);
            }

            OpenGL.normal(f.normal.x, f.normal.y, f.normal.z);

            for (int idx = 0; idx < mode; idx++)
            {
                if (f.uv[idx] != null)
                {
                    OpenGL.texCoord(f.uv[idx].u, f.uv[idx].v);
                }

                OpenGL.vertex(f.vertex[idx].x, f.vertex[idx].y, f.vertex[idx].z);
            }
        }

        if (mode != 0)
        {
            OpenGL.end();
            OpenGL.color(1F, 1F, 1F);
        }
    }
}
