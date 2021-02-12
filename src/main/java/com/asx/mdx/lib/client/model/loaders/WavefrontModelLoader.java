package com.asx.mdx.lib.client.model.loaders;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.util.models.wavefront.TriangulatedWavefrontModel;

public class WavefrontModelLoader
{
    public static final WavefrontModelLoader            INSTANCE = new WavefrontModelLoader();

    /** The registry that contains all of the triangulated wavefront models currently registered **/
    private HashMap<String, TriangulatedWavefrontModel> modelRegistry;

    public WavefrontModelLoader()
    {
        this.modelRegistry = new HashMap<String, TriangulatedWavefrontModel>();
    }

    /**
     * @return - A registry that containst instances of all registered wavefront models.
     */
    public static HashMap<String, TriangulatedWavefrontModel> getModelRegistry()
    {
        return WavefrontModelLoader.INSTANCE.modelRegistry;
    }

    /**
     * @return - The File instance of the models directory.
     */
    public static File getModelsDirectory()
    {
        return new File("models");
    }

    /**
     * Extract a wavefront model from the provided URL to the provided File path and then load it.
     * Note: The model MUST be triangulated.
     * 
     * @param c - The main class of the mod we are loading models for.
     * @param modid - The mod id of the mod we are loading models for.
     * @param model - The name of the model we are loading.
     * @param assetsPath - The location of the model in the assets.
     * @return - The model that was loaded. Null if the model was not loaded.
     */
    public static TriangulatedWavefrontModel load(Class<?> c, String modid, String model, String assetsPath)
    {
        File baseDir = new File(WavefrontModelLoader.getModelsDirectory(), String.format("%s/", modid));
        File path = new File(baseDir, model);

        if (!WavefrontModelLoader.getModelsDirectory().exists())
        {
            WavefrontModelLoader.getModelsDirectory().mkdirs();
        }

        if (!baseDir.exists())
        {
            baseDir.mkdirs();
        }

        try
        {
            URL urlOBJ = c.getResource(assetsPath + ".obj");
            File fileOBJ = new File(path.getAbsolutePath() + ".obj");
            URL urlMTL = c.getResource(assetsPath + ".mtl");
            File fileMTL = new File(path.getAbsolutePath() + ".mtl");

            if (!fileOBJ.exists())
            {
                FileUtils.copyURLToFile(urlOBJ, fileOBJ);
                MDX.log().info(String.format("Extracted wavefront model: %s", fileOBJ.getAbsoluteFile().getPath()));
            }

            if (!fileMTL.exists())
            {
                FileUtils.copyURLToFile(urlMTL, fileMTL);
                MDX.log().info(String.format("Extracted wavefront texture: %s", fileMTL.getAbsoluteFile().getPath()));
            }
        }
        catch (Exception e)
        {
            MDX.log().info(String.format("Error while extracting %s from %s: %s", path, assetsPath, e));
            e.printStackTrace();
        }

        return load(modid, path);
    }

    /**
     * Load a wavefront model from the provided path. 
     * Note: The model MUST be triangulated.
     * 
     * @param modid - The mod id of the mod we are loading models for.
     * @param path - The path we are extracting to and loading the model from.
     * @return - The model that was loaded. Null if the model was not loaded.
     */
    public static TriangulatedWavefrontModel load(String modid, File path)
    {
        TriangulatedWavefrontModel model = new TriangulatedWavefrontModel();

        if (model.load(modid, path.getAbsolutePath()))
        {
            String tag = path.getAbsolutePath().replaceAll(".obj", "").replaceAll(".OBJ", "");
            tag = tag.substring(tag.lastIndexOf('/') + 1, tag.length());
            WavefrontModelLoader.getModelRegistry().put(tag, model);

            MDX.log().info("[WavefrontModelLoader] Loaded wavefront model: " + path);
        }
        else
        {
            MDX.log().info("[WavefrontModelLoader] Unable to load wavefront model: " + path);
        }

        return model;
    }

    /**
     * Get the instance of the model with the specified name.
     * 
     * @param modelName - The name of the model we are getting.
     * @return The instance of the model that was found.
     */
    public static TriangulatedWavefrontModel get(String modelName)
    {
        return WavefrontModelLoader.getModelRegistry().get(modelName);
    }
}
