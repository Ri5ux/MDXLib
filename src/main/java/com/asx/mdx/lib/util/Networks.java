package com.asx.mdx.lib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.asx.mdx.MDX;

import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class Networks
{
    /**
     * Retrieves the contents of a page with the specified URL. NOTE: Networking
     * must be enabled for this method to function.
     * 
     * @param url
     *            - The URL to retrieve page contents from.
     * @return The contents of the remote page.
     */
    public static String query(String url)
    {
        return query(url, false, true);
    }

    /**
     * Retrieves the contents of a page with the specified URL. NOTE: Networking
     * must be enabled for this method to function.
     * 
     * @param url
     *            - The URL to retrieve page contents from.
     * @param insertNewLines
     *            - If set true, the method automatically inserts line breaks.
     * @return The contents of the remote page.
     */
    public static String query(String url, boolean insertNewLines, boolean quiet)
    {
        HttpURLConnection connection = null;

        try
        {
            connection = ((HttpURLConnection) (new URL(url)).openConnection());
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            if (connection.getResponseCode() / 100 != 2)
            {
                return null;
            }
            else
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String inputLine;

                while ((inputLine = reader.readLine()) != null)
                {
                    if (insertNewLines)
                        builder.append(inputLine + "\n");
                    else
                        builder.append(inputLine);
                }

                reader.close();
                return builder.toString();
            }
        }
        catch (Exception e)
        {
            if (!quiet)
            {
                MDX.log().warn(e.toString() + ": " + url);
            }

            if (connection != null)
            {
                connection.disconnect();
            }
            return null;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }

    /**
     * Downloads a file from the specified URL and saves it to the specified
     * location. NOTE: Networking must be enabled for this method to function.
     * 
     * @param fileUrl
     *            - The URL to download a file from.
     * @param saveLocation
     *            - The location where the downloaded file will be saved.
     * @throws IOException
     */
    public static void downloadFile(String fileUrl, String saveLocation) throws IOException
    {
        MDX.log().info("Downloading file from '" + fileUrl + "' and saving it to '" + saveLocation + "'");
        InputStream is = (new URL(fileUrl)).openStream();
        FileOutputStream os = new FileOutputStream(saveLocation);
        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1)
        {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    /**
     * Download a resource from the specified URL and convert it to a
     * ResourceLocation. Provide a fallback in case a network connection is not
     * present.
     * 
     * @param URL
     *            - URL of the resource to download
     * @param fallback
     *            - Fallback resource in case the download fails
     * @return Return the downloaded ResourceLocation
     */
    public static ResourceLocation downloadResource(String URL, ResourceLocation fallback)
    {
        return Networks.downloadResource(URL, fallback, false);
    }

    /**
     * Download a resource from the specified URL and convert it to a
     * ResourceLocation. Provide a fallback in case a network connection is not
     * present.
     * 
     * @param URL
     *            - URL of the resource to download
     * @param fallback
     *            - Fallback resource in case the download fails
     * @param forceDownload
     *            - Force re-downloading of the specified resource.
     * @return Return the downloaded ResourceLocation
     */
    public static ResourceLocation downloadResource(String URL, ResourceLocation fallback, boolean forceDownload)
    {
        ResourceLocation resource = new ResourceLocation(URL);
        TextureManager texturemanager = Game.minecraft().getTextureManager();
        Object object = forceDownload ? null : texturemanager.getTexture(resource);

        if (object == null)
        {
            object = new ThreadDownloadImageData((File) null, URL, fallback, null);
            texturemanager.loadTexture(resource, (ITextureObject) object);
        }

        return resource;
    }
}
