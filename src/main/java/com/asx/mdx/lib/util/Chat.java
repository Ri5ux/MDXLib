package com.asx.mdx.lib.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class Chat
{
    public static class Chars
    {
        public static final char SECTION_SIGN = '\u00A7';
        public static final char START_OF_HEADING = '\u0001';
        public static final char END_OF_TEXT = '\u0003';
        public static final char SYNCHRONUS_IDLE = '\u0016';
        public static final char CANCEL = '\u0018';
        public static final char SUBSTITUTE = '\u001a';
    }
    
    public static String unicodeEscape(char c)
    {
        return ("\\u" + Integer.toHexString(c | 0x10000).substring(1));
    }
    
    public static String string(char c)
    {
        return String.valueOf(c);
    }
    
    public static String format(String message)
    {
        return message.replaceAll("&", string(Chars.SECTION_SIGN));
    }
    
    public static TextComponentString component(String message)
    {
        return new TextComponentString(format(message));
    }

    public static void sendTo(EntityPlayer player, String message)
    {
        player.sendMessage(component(message));
    }

    public static void sendTo(EntityPlayer player, TextComponentString component)
    {
        player.sendMessage(component);
    }

    public static void sendTo(ICommandSender sender, TextComponentString component)
    {
        sender.sendMessage(component);
    }
}
