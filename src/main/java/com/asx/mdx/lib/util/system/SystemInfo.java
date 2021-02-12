package com.asx.mdx.lib.util.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.opengl.GL11;

import com.asx.mdx.lib.client.util.OpenGL;

public class SystemInfo
{
    public static final SystemInfo              INSTANCE  = new SystemInfo();
    public static int                           bytesUnit = 1024;
    public static Enumeration<NetworkInterface> networkAdapters;
    private static String                       processorName;
    private static long                         memoryCapacity;
    private static OperatingSystem              osType;
    public static String                        dataCollectorResult;
    public static String                        vchk5;

    public void runtimeTasks()
    {
        osType = OperatingSystem.getCurrentPlatform();

        try
        {
            Runtime runtime = Runtime.getRuntime();
            Process process = null;

            switch (osType)
            {
                case WINDOWS:
                    process = runtime.exec("wmic cpu get name");
                    break;
                case LINUX:
                    process = runtime.exec("cat /proc/cpuinfo | grep \"model name\"");
                    break;
                case OSX:
                    process = runtime.exec("sysctl -n machdep.cpu.brand_string");
                    break;
                default:
                    process = runtime.exec("wmic cpu get name");
                    break;
            }

            if (process != null)
            {
                process.waitFor();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";

                while ((line = buffer.readLine()) != null)
                {
                    if (!line.isEmpty())
                    {
                        processorName = line.trim();
                    }
                }

                buffer.close();
            }

            switch (osType)
            {
                case WINDOWS:
                    process = runtime.exec("wmic memorychip get capacity");
                    break;
                default:
                    break;
            }

            if (process != null)
            {
                process.waitFor();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";

                if (buffer != null)
                {
                    while ((line = buffer.readLine()) != null)
                    {
                        if (!line.isEmpty())
                        {
                            line = StringUtils.deleteWhitespace(line);

                            if (StringUtils.isNumeric(line))
                            {
                                memoryCapacity = memoryCapacity + Long.parseLong(line);
                            }
                        }
                    }

                    buffer.close();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Enumeration<NetworkInterface> networkAdapters()
    {
        try
        {
            return networkAdapters == null ? networkAdapters = NetworkInterface.getNetworkInterfaces() : networkAdapters;
        }
        catch (SocketException e)
        {
            ;
        }

        return null;
    }

    public static long getMemoryCapacity()
    {
        return memoryCapacity;
    }

    public static String gpu()
    {
        return OpenGL.getString(GL11.GL_RENDERER);
    }

    public static String gpuVendor()
    {
        return OpenGL.getString(GL11.GL_VENDOR);
    }

    public static String cpu()
    {
        return processorName;
    }

    public static int cpuCores()
    {
        return Runtime.getRuntime().availableProcessors();
    }

    public static String javaVersion()
    {
        return SystemUtils.JAVA_VERSION;
    }

    public static OperatingSystem getOsType()
    {
        return osType;
    }

    public static String osName()
    {
        return System.getProperty("os.name");
    }

    public static String osVersion()
    {
        return System.getProperty("os.version");
    }

    public static String osArchitecture()
    {
        return System.getProperty("os.arch");
    }

    public static long vmMemoryTotalBytes()
    {
        return Runtime.getRuntime().totalMemory();
    }

    public static long vmMemoryMaxBytes()
    {
        return Runtime.getRuntime().maxMemory();
    }

    public static long vmMemoryFreeBytes()
    {
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * Convert Bytes to TeraBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of TeraBytes
     */
    public static double toTBFromB(long b)
    {
        return b / Math.pow(bytesUnit, 4);
    }

    /**
     * Convert Bytes to GigaBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of GigaBytes
     */
    public static double toGBFromB(long b)
    {
        return b / Math.pow(bytesUnit, 3);
    }

    /**
     * Convert Bytes to MegaBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of MegaBytes
     */
    public static double toMBFromB(long b)
    {
        return b / bytesUnit / bytesUnit;
    }

    /**
     * Convert Bytes to KiloBytes
     * 
     * @param b
     *            - Bytes to be converted
     * @return the amount of KiloBytes
     */
    public static double toKBFromB(long b)
    {
        return b / bytesUnit;
    }

    /**
     * Convert MegaBytes to TeraBytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of TeraBytes
     */
    public static double toTBFromMB(long mb)
    {
        return mb / Math.pow(bytesUnit, 2);
    }

    /**
     * Convert MegaBytes to GigaBytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of GigaBytes
     */
    public static double toGBFromMB(long mb)
    {
        return mb / bytesUnit;
    }

    /**
     * Convert MegaBytes to KiloBytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of KiloBytes
     */
    public static double toKBFromMB(long mb)
    {
        return mb * bytesUnit;
    }

    /**
     * Convert MegaBytes to Bytes
     * 
     * @param b
     *            - MegaBytes to be converted
     * @return the amount of Bytes
     */
    public static double toBFromMB(long mb)
    {
        return mb * Math.pow(bytesUnit, 2);
    }

    /**
     * Convert GigaBytes to TeraBytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of TeraBytes
     */
    public static double toTBFromGB(long gb)
    {
        return gb / bytesUnit;
    }

    /**
     * Convert GigaBytes to MegaBytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of MegaBytes
     */
    public static double toMBFromGB(long gb)
    {
        return gb * bytesUnit;
    }

    /**
     * Convert GigaBytes to KiloBytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of KiloBytes
     */
    public static double toKBFromGB(long gb)
    {
        return gb * Math.pow(bytesUnit, 2);
    }

    /**
     * Convert GigaBytes to Bytes
     * 
     * @param b
     *            - GigaBytes to be converted
     * @return the amount of Bytes
     */
    public static double toBFromGB(long gb)
    {
        return gb * Math.pow(bytesUnit, 3);
    }
}
