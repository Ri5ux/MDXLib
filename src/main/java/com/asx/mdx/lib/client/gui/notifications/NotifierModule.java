package com.asx.mdx.lib.client.gui.notifications;

import com.asx.mdx.MDX;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NotifierModule
{
    public static NotifierModule instance            = new NotifierModule();
    public static Notification   currentNotification = null;

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        if (currentNotification == null && MDX.notifications().queue().size() > 0)
        {
            currentNotification = MDX.notifications().queue().get(0);
        }

        if (System.currentTimeMillis() % 2 == 0)
        {
            if (currentNotification != null && currentNotification.tick() >= currentNotification.displayTimeout())
            {
                currentNotification.timeoutAction();
            }
        }
    }

    @SubscribeEvent
    public void render(TickEvent.RenderTickEvent event)
    {
        if (currentNotification != null)
        {
            currentNotification.preDraw();
        }
    }
}
