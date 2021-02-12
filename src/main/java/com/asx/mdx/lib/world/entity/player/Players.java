package com.asx.mdx.lib.world.entity.player;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import com.asx.mdx.lib.util.Networks;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Players
{
    /**
     * @param player - The EntityPlayer to check for XP.
     * @return the amount of XP required to reach the next level.
     */
    public static float getXPToNextLevel(EntityPlayer player)
    {
        return getXPCurrentLevelMax(player) - getXPCurrentLevel(player);
    }

    /**
     * @param player - The EntityPlayer to check for XP of the current level.
     * @return the amount of XP the player has on the current level.
     */
    public static float getXPCurrentLevel(EntityPlayer player)
    {
        return player.experience * getXPCurrentLevelMax(player);
    }

    /**
     * @param player - The EntityPlayer to check for XP.
     * @return the max amount of XP required for this level.
     */
    public static float getXPCurrentLevelMax(EntityPlayer player)
    {
        return player.xpBarCap();
    }

    /**
     * @param player - The EntityPlayer to check for XP totals.
     * @return the total amount of XP gained through all the player's levels.
     */
    public static float getXPTotal(EntityPlayer player)
    {
        return player.experienceTotal;
    }

    /**
     * @param player - The EntityPlayer to check the level of.
     * @return the player's level.
     */
    public static float getXPLevel(EntityPlayer player)
    {
        return player.experienceLevel;
    }

    /**
     * @param player - Instance of the receiving player
     * @param message - The string to be sent to the player
     */
    public static void sendToChat(EntityPlayer player, String message)
    {
        player.sendMessage(new TextComponentString(message));
    }

    /**
     * This method sends an HTTP request to the Arisux web servers, which retrieve 
     * the UUID from Mojang's auth servers, and relays it back to this method.
     * 
     * @param username - The username of the player to retrieve a UUID from
     * @return The UUID of the specified player.
     */
    public static String getUUID(String username)
    {
        String retrieved = Networks.query(String.format("http://aliensvspredator.org/login/api.mojang.php?function=uuid&user=%s", username));
        return retrieved != null && retrieved.length() >= 32 ? retrieved : username;
    }
    
    public static UUID toUUID(String uuid)
    {
        BigInteger bigInteger = new BigInteger(uuid, 16);
        return new UUID(bigInteger.shiftRight(64).longValue(), bigInteger.longValue());
    }
    
    public static ResourceLocation getPlayerSkin(String username)
    {
        UUID id = toUUID(Players.getUUID(username));
        NetworkPlayerInfo playerInfo = new NetworkPlayerInfo(new GameProfile(id, username));
        
        return playerInfo.getLocationSkin();
    }
    
    /**
     * Returns an instance of EntityPlayer for the first player found with the specified username.
     * 
     * @param world - World instance to scan for players in.
     * @param username - Username to scan players for.
     * @return EntityPlayer instance of the specified player's username.
     */
    public static EntityPlayer getPlayerForUsername(World world, String username)
    {
        for (Object player : world.playerEntities)
        {
            if (((EntityPlayer) player).getName().equalsIgnoreCase(username))
            {
                return (EntityPlayer) player;
            }
        }

        return null;
    }

    /**
     * Returns an instance of EntityPlayer for the first player found with the specified ICommandSender details
     * 
     * @param commandSender - The ICommandSender instance to get details from
     * @return EntityPlayer instance of the specified ICommandSender
     */
    public static EntityPlayer getPlayerForCommandSender(ICommandSender commandSender)
    {
        return getPlayerForUsername(commandSender.getEntityWorld(), commandSender.getName());
    }

    public static List<EntityPlayer> getPlayersInWorld(World world)
    {
        return world.playerEntities;
    }
}