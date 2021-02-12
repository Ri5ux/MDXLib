package com.asx.mdx.lib.client.gui.notifications;

import java.util.ArrayList;

import com.asx.mdx.Settings;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Notifications
{
    public static final Notifications INSTANCE = new Notifications();

    /** A queue for notifications that will be displayed     on screen **/
    private ArrayList<Notification>   queue;

    public Notifications()
    {
        this.queue = new ArrayList<Notification>();
    }

    public ArrayList<Notification> queue()
    {
        return queue;
    }

    public static void sendNotification(Notification notification)
    {
        if (notification.allowMultiple() || !notification.allowMultiple() && !Notifications.INSTANCE.queue().contains(notification))
        {
            Notifications.INSTANCE.queue().add(notification);
        }
    }

    public void onStartup()
    {
        if (Settings.INSTANCE.isStartupNotificationEnabled())
        {
            Notifications.sendNotification(new Notification()
            {
                @Override
                public String getMessage()
                {
                    return "Notifications may pop up here throughout gameplay. These notifications may provide useful information as to how certain features work. This feature can be disabled in the configuration.";
                }
            });
            Settings.INSTANCE.disableStartupNotification();
        }
    }
}
