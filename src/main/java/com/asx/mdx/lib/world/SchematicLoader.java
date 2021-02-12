package com.asx.mdx.lib.world;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.world.storage.Schematic;

import net.minecraft.nbt.CompressedStreamTools;

public class SchematicLoader
{
    public static final SchematicLoader INSTANCE = new SchematicLoader();

    /** The registry that contains all currently registered schematics **/
    private ArrayList<Schematic>        schematicRegistry;

    public SchematicLoader()
    {
        this.schematicRegistry = new ArrayList<Schematic>();
    }

    public static ArrayList<Schematic> getSchematicRegistry()
    {
        return SchematicLoader.INSTANCE.schematicRegistry;
    }

    /**
     * @return - The File instance of the schematics directory.
     */
    public static File getSchematicsDirectory()
    {
        return new File("./", "schematics");
    }

    /**
     * @return - A Collection of Files containing File instances to each schematic
     * found in the schematics directory.
     */
    public static Collection<File> getSchematicsInDirectory()
    {
        return FileUtils.listFiles(SchematicLoader.getSchematicsDirectory(), new String[] { "schematic" }, true);
    }

    /**
     * @return - An Array of Strings containing the names of all schematics in
     * the schematics directory.
     */
    public static String[] getSchematicNames()
    {
        Collection<File> files = SchematicLoader.getSchematicsInDirectory();
        String[] filenames = new String[files.size()];
        int i = 0;

        for (File file : files)
        {
            filenames[i++] = FilenameUtils.getBaseName(file.getName());
        }

        return filenames;
    }

    /**
     * Load a schematic from the schematics directory by using a direct File path.
     * 
     * @param path - The File path we are loading the schematic from.
     * @return - The schematic that was loaded. Null if the schematic was not loaded.
     */
    public static Schematic load(File path)
    {
        try
        {
            Schematic schematic = new Schematic(path, CompressedStreamTools.readCompressed(new FileInputStream(path)));
            SchematicLoader.getSchematicRegistry().add(schematic);

            return schematic;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Load a schematic from the schematics directory simply by using its name.
     * 
     * @param name - The name of the schematic we are loading.
     * @return - The schematic that was loaded. Null if the schematic was not loaded.
     */
    public static Schematic load(String name)
    {
        name = FilenameUtils.getExtension(name).length() == 0 ? name + ".schematic" : name;

        for (File file : SchematicLoader.getSchematicsInDirectory())
        {
            if (file.getPath().endsWith(name) && name.endsWith(file.getName()))
            {
                return SchematicLoader.load(file);
            }
        }

        return null;
    }

    /**
     * Extract a schematic from the provided URL to the provided File path and then load it.
     * The schematic will not be extracted if it is already present in the provided path.
     * 
     * @param path - The path we are extracting to and loading the schematic from.
     * @param resource - The URL path we are extracting the resource from.
     * @return - The schematic that was loaded. Null if the schematic was not loaded.
     */
    public static Schematic load(File path, URL resource)
    {
        if (!SchematicLoader.getSchematicsDirectory().exists())
        {
            SchematicLoader.getSchematicsDirectory().mkdirs();
        }

        if (!path.exists())
        {
            try
            {
                FileUtils.copyURLToFile(resource, path);
                MDX.log().info(String.format("Extracted %s", path.getAbsoluteFile().getPath()));
            }
            catch (Exception e)
            {
                MDX.log().info(String.format("Error while extracting %s: %s", path, e));
            }
        }

        return SchematicLoader.load(path);
    }
}
