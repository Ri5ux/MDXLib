package com.asx.mdx.lib.util;

import java.lang.reflect.Field;

import com.asx.mdx.MDX;

public class Reflect
{
    public static double getDouble(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Double) get(obj, deobfName, obfName)).doubleValue();
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to change the access of double %s. This is a severe problem and needs to be reported: %s", obfName, e);
        }

        return 0D;
    }

    public static float getFloat(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Float) get(obj, deobfName, obfName)).floatValue();
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to change the access of float %s. This is a severe problem and needs to be reported: %s", obfName, e);
        }

        return 0F;
    }

    public static int getInt(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Integer) get(obj, deobfName, obfName)).intValue();
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to change the access of integer %s. This is a severe problem and needs to be reported: %s", obfName, e);
        }

        return 0;
    }

    public static boolean getBoolean(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Boolean) get(obj, deobfName, obfName)).booleanValue();
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to change the access of boolean %s. This is a severe problem and needs to be reported: %s", obfName, e);
        }

        return false;
    }

    public static long getLong(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Long) get(obj, deobfName, obfName)).longValue();
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to change the access of long %s. This is a severe problem and needs to be reported: %s", obfName, e);
        }

        return 0L;
    }

    public static byte getByte(Object obj, String deobfName, String obfName)
    {
        try
        {
            return ((Byte) get(obj, deobfName, obfName)).byteValue();
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to change the access of byte %s. This is a severe problem and needs to be reported: %s", obfName, e);
        }

        return 0;
    }

    public static String getString(Object obj, String deobfName, String obfName)
    {
        try
        {
            return (String) get(obj, deobfName, obfName);
        }
        catch (Exception e)
        {
            MDX.log().warn("Failed to change the access of string %s. This is a severe problem and needs to be reported: %s", obfName, e);
        }

        return "";
    }

    public static void set(Object obj, String deobfName, String obfName, Object value)
    {
        set(obj.getClass(), obj, deobfName, obfName, value);
    }

    public static void set(Class<?> clazz, Object obj, String deobfName, String obfName, Object value)
    {
        try
        {
            String fieldName = Game.isDevEnvironment() ? deobfName : obfName;
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Object get(Object obj, String deobfName, String obfName)
    {
        return get(obj.getClass(), obj, deobfName, obfName);
    }

    public static Object get(Class<?> clazz, Object obj, String deobfName, String obfName)
    {
        try
        {
            String fieldName = Game.isDevEnvironment() ? deobfName : obfName;
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
