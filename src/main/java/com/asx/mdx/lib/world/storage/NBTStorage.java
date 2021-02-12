package com.asx.mdx.lib.world.storage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.asx.mdx.core.Access;
import com.google.common.base.Throwables;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;

public class NBTStorage
{
    /**
     * Copies an ArrayList of Strings into an NBTTagList of Strings.
     * 
     * @param list - The ArrayList to be converted.
     * @return NBTTagList created from the ArrayList.
     */
    public static NBTTagList newStringNBTList(ArrayList<String> list)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int j = 0; j < list.size(); ++j)
        {
            String s1 = list.get(j);
            nbttaglist.appendTag(new NBTTagString(s1));
        }

        return nbttaglist;
    }

    /**
     * Copies an ArrayList of NBTTagCompounds into an NBTTagList of NBTTagCompounds.
     * 
     * @param list - The ArrayList to be converted.
     * @return NBTTagList created from the ArrayList.
     */
    public static NBTTagList newCompoundNBTList(ArrayList<NBTTagCompound> list)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int j = 0; j < list.size(); ++j)
        {
            NBTTagCompound nbt = list.get(j);
            nbttaglist.appendTag(nbt);
        }

        return nbttaglist;
    }

    /**
     * Writes an uncompressed NBTTagCompound to the specified File
     * 
     * @param nbt - The NBTTagCompound to be written.
     * @param file - The File to write to.
     */
    public static void write(NBTTagCompound nbt, File file)
    {
        try
        {
            CompressedStreamTools.write(nbt, file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param file - The File to read an NBTTagCompound from.
     * @return The uncompressed NBTTagCompound being read from the specified File.
     */
    public static NBTTagCompound read(File file)
    {
        NBTTagCompound nbtTagCompound = null;

        try
        {
            nbtTagCompound = CompressedStreamTools.read(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return nbtTagCompound;
    }

    /**
     * Writes a compressed NBTTagCompound to the specified File
     * 
     * @param nbt - The NBTTagCompound to be written.
     * @param file - The File to write to.
     * @throws IOException 
     */
    public static void writeCompressed(NBTTagCompound nbt, File file) throws IOException
    {
        FileOutputStream stream = new FileOutputStream(file);

        CompressedStreamTools.writeCompressed(nbt, stream);

        if (stream != null)
        {
            stream.close();
        }
    }

    /**
     * @param file - The File to read an NBTTagCompound from.
     * @return The compressed NBTTagCompound being read from the specified File.
     * @throws IOException 
     */
    public static NBTTagCompound readCompressed(File file) throws IOException
    {
        NBTTagCompound nbtTagCompound = null;
        FileInputStream stream = new FileInputStream(file);

        nbtTagCompound = CompressedStreamTools.readCompressed(stream);

        if (stream != null)
        {
            stream.close();
        }

        return nbtTagCompound;
    }

    /**
     * Write an {@link NBTBase} to the byte buffer using the Minecraft encoding.
     *
     * @param to The buffer to write to
     * @param nbt The NBT to write to buffer
     */
    public static void writeToBuffer(NBTBase nbt, ByteBuf to)
    {
        PacketBuffer buffer = new PacketBuffer(to);
        try
        {
            writeToPacketBuffer(nbt, buffer);
        }
        catch (IOException e)
        {
            throw Throwables.propagate(e);
        }
    }

    /**
     * Writes a compressed NBTTagCompound to this buffer
     */
    public static void writeToPacketBuffer(NBTBase nbt, PacketBuffer buffer) throws IOException
    {
        if (nbt == null)
        {
            buffer.writeShort(-1);
        }
        else
        {
            byte[] abyte = toBytes(nbt);
            buffer.writeShort((short) abyte.length);
            buffer.writeBytes(abyte);
        }
    }

    public static byte[] toBytes(NBTBase base) throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));

        try
        {
            Access.writeTag(base, dataoutputstream);
        }
        finally
        {
            dataoutputstream.close();
        }

        return bytearrayoutputstream.toByteArray();
    }

    /**
     * Read an {@link NBTTagCompound} from the byte buffer. It uses the minecraft encoding.
     *
     * @param from The buffer to read from
     * @return The read tag
     */
    public static NBTBase readFromBuffer(ByteBuf from)
    {
        PacketBuffer buffer = new PacketBuffer(from);
        try
        {
            return readFromPacketBuffer(buffer);
        }
        catch (IOException e)
        {
            throw Throwables.propagate(e);
        }
    }

    /**
     * Reads a compressed NBTTagCompound from this buffer
     */
    public static NBTBase readFromPacketBuffer(PacketBuffer buffer) throws IOException
    {
        short short1 = buffer.readShort();

        if (short1 < 0)
        {
            return null;
        }
        else
        {
            byte[] abyte = new byte[short1];
            buffer.readBytes(abyte);
            return fromBytes(abyte, new NBTSizeTracker(2097152L));
        }
    }

    public static NBTBase fromBytes(byte[] bytes, NBTSizeTracker tracker) throws IOException
    {
        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes))));
        NBTBase nbt;

        try
        {
            nbt = Access.readTag(datainputstream, 0, tracker);
        }
        finally
        {
            datainputstream.close();
        }

        return nbt;
    }
}
