package com.asx.mdx.lib.client.util.models.wavefront;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.model.loaders.WavefrontModelLoader;
import com.asx.mdx.lib.client.util.Color;
import com.asx.mdx.lib.client.util.Normal;
import com.asx.mdx.lib.client.util.UV;
import com.asx.mdx.lib.client.util.Vertex;

import net.minecraft.util.ResourceLocation;

public class TriangulatedWavefrontModel
{
    private String pathName;
    private String mtlName;
    private String modid;
    private String directory;

    public ArrayList<Vertex> vertex;
    public ArrayList<UV> uv;
    public Hashtable<String, Part> parts;
    public Hashtable<String, String> strings;

    public float xDim, yDim, zDim;
    public float xMin, yMin, zMin;
    public float xMax, yMax, zMax;
    public float dimMax, dimMaxInv;
    
    public TriangulatedWavefrontModel()
    {
        this.vertex = new ArrayList<Vertex>();
        this.uv = new ArrayList<UV>();
        this.parts = new Hashtable<String, Part>();
        this.strings = new Hashtable<String, String>();
    }

    public ResourceLocation getAlternativeTexture(String name)
    {
        ResourceLocation resource = new ResourceLocation(modid, directory.substring(1) + name);
        return resource;
    }

    public boolean load(String modid, String path)
    {
        File fileModel = new File(path + ".obj");
        File fileTexture = new File(path + ".mtl");
        File fileOrigins = new File(path + ".ori");
        int lastSlashId = path.lastIndexOf('/');
        this.modid = modid;
        this.directory = path.substring(0, lastSlashId + 1);
        this.pathName = path.substring(lastSlashId + 1, path.length());
        Part part = null;
        FaceGroup fg = null;

        try
        {
            this.loadModel(fileModel, path, part, fg);
            part = null;
            this.loadMaterials(fileTexture, path);
            part = null;
        }
        catch (Exception e)
        {
            MDX.log().warn(String.format("[WavefrontAPI] Error loading model for mod with id %s: %s", modid, path));
            e.printStackTrace();
            return false;
        }

        try
        {
            this.loadOrigins(fileOrigins, part);
        }
        catch (Exception e)
        {
            MDX.log().warn(String.format("[WavefrontAPI] Error (%s) loading origins for model for mod with id %s: %s", e, modid, path));
        }

        xDim = xMax - xMin;
        yDim = yMax - yMin;
        zDim = zMax - zMin;

        dimMax = Math.max(Math.max(xMax, yMax), zMax);
        dimMaxInv = 1.0f / dimMax;

        MDX.log().info(String.format("[WavefrontAPI] Loaded wavefront model for mod with id %s: %s", this.modid, this.pathName));

        return true;
    }

    private boolean loadModel(File fileModel, String path, Part part, FaceGroup group) throws Exception
    {
        InputStream stream = new FileInputStream(fileModel);

        if (stream == null || fileModel == null || path == null)
        {
            MDX.log().warn("OBJ Loading Failed: " + path);
            stream.close();
            return false;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String line;

        while ((line = bufferedReader.readLine()) != null)
        {
            String[] words = line.split(" ");
            String command = words[0];

            if (command.equals("o"))
            {
                part = new Part(vertex, uv);
                parts.put(words[1], part);
            }
            else if (command.equals("v"))
            {
                Vertex v;
                part.addVertex(v = new Vertex(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3])));

                xMin = Math.min(xMin, v.x);
                yMin = Math.min(yMin, v.y);
                zMin = Math.min(zMin, v.z);
                xMax = Math.max(xMax, v.x);
                yMax = Math.max(yMax, v.y);
                zMax = Math.max(zMax, v.z);
            }
            else if (command.equals("vt"))
            {
                part.getUV().add(new UV(Float.parseFloat(words[1]), 1 - Float.parseFloat(words[2])));
            }
            else if (command.equals("f"))
            {
                int vertexNbr = words.length - 1;
                if (vertexNbr == 3)
                {
                    Vertex[] verticeId = new Vertex[vertexNbr];
                    UV[] uvId = new UV[vertexNbr];
                    for (int idx = 0; idx < vertexNbr; idx++)
                    {
                        String[] id = words[idx + 1].split("/");

                        verticeId[idx] = part.getVertices().get(Integer.parseInt(id[0]) - 1);
                        if (id.length > 1 && !id[1].equals(""))
                        {
                            uvId[idx] = part.getUV().get(Integer.parseInt(id[1]) - 1);
                        }
                        else
                        {
                            uvId[idx] = null;
                        }
                    }

                    group.faces.add(new Face(verticeId, uvId, new Normal(verticeId[0], verticeId[1], verticeId[2])));
                }
            }
            else if (command.equals("mtllib"))
            {
                mtlName = words[1];
            }
            else if (command.equals("usemtl"))
            {
                group = new FaceGroup();
                group.material = words[1];

                if (part != null && part.groups != null)
                {
                    part.groups.add(group);
                }
            }
        }
        bufferedReader.close();

        return true;
    }

    private boolean loadMaterials(File fileTexture, String path) throws Exception
    {
        InputStream stream = new FileInputStream(fileTexture);

        System.out.println("Loading MTL: " + fileTexture);

        if (stream == null || fileTexture == null || path == null)
        {
            MDX.log().warn("MTL Loading failed: " + path);
            stream.close();
            return false;
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        String mtlName = "";
        String line;
        
        while ((line = bufferedReader.readLine()) != null)
        {
            String[] args = line.split(" ");
            String command = args[0];

            if (command.equals("newmtl"))
            {
                mtlName = args[1];

            }
            else if (command.equals("map_Kd"))
            {
                for (Part partPtr : parts.values())
                {
                    for (FaceGroup faceGroup : partPtr.groups)
                    {
                        if (faceGroup.material != null && faceGroup.material.equals(mtlName))
                        {
                            faceGroup.resource = new ResourceLocation(modid, "models/" + directory + args[1]);
                        }
                    }
                }
            }
            else if (command.equals("Kd"))
            {
                for (Part partPtr : parts.values())
                {
                    for (FaceGroup faceGroup : partPtr.groups)
                    {
                        if (faceGroup.material != null && faceGroup.material.equals(mtlName))
                        {
                            faceGroup.color = new Color(Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3]), 1F);
                        }
                    }
                }
            }
        }
        bufferedReader.close();

        return true;
    }

    private void loadOrigins(File fileOrigins, Part part) throws Exception
    {
        InputStream stream = new FileInputStream(fileOrigins);

        if (stream != null)
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                String[] words = line.split(" ");
                String command = words[0];

                if (command.equals("o"))
                {
                    part = parts.get(words[1]);
                }
                else if (command.equals("f"))
                {
                    String key = words[1];
                    float value = Float.valueOf(words[2]);

                    if (key.equals("originX"))
                    {
                        part.setOriginX(value);
                    }
                    else if (key.equals("originY"))
                    {
                        part.setOriginY(value);
                    }
                    else if (key.equals("originZ"))
                    {
                        part.setOriginZ(value);
                    }
                    else if (key.equals("originX2"))
                    {
                        part.setOriginX2(value);
                    }
                    else if (key.equals("originY2"))
                    {
                        part.setOriginY2(value);
                    }
                    else if (key.equals("originZ2"))
                    {
                        part.setOriginZ2(value);
                    }
                    else
                    {
                        part.floats.put(key, Float.valueOf(value));
                    }
                }
                else if (command.equals("s"))
                {
                    strings.put(words[1], words[2]);
                }
            }

            bufferedReader.close();
        }
    }

    public void draw(String part)
    {
        Part partPtr = getPart(part);

        if (partPtr != null)
        {
            partPtr.draw();
        }
    }

    public Part getPart(String part)
    {
        return this.parts.get(part);
    }

    public String getString(String name)
    {
        return this.strings.get(name);
    }

    public String getMtlName()
    {
        return this.mtlName;
    }

    /**
     * Render a part from a model obtained using the model name.
     * 
     * @param modelName - The name of the model we are rendering a part from.
     * @param partName - The name of the part we are rendering.
     * @return Returns true if the part was rendered, false if it was not.
     */
    public static boolean renderPart(String modelName, String partName)
    {
        Part part = getPart(modelName, partName);
    
        if (part != null)
        {
            part.draw();
            return true;
        }
    
        return false;
    }

    /**
     * Return a Part instance of a part from the model with the specified name.
     * 
     * @param modelName - The name of the model we are getting a Part instance from.
     * @param partName - The name of the part we are getting.
     * @return The Part instance that was found in the specified model.
     */
    public static Part getPart(String modelName, String partName)
    {
        TriangulatedWavefrontModel model = WavefrontModelLoader.get(modelName);
        return model != null ? model.getPart(partName) : null;
    }
}
