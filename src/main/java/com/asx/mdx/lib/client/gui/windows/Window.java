package com.asx.mdx.lib.client.gui.windows;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.util.ScaledResolution;
import com.asx.mdx.lib.client.util.Screen;

public abstract class Window implements IWindow
{
    protected WindowManager                                 manager;
    protected int                                           xPos;
    protected int                                           yPos;
    protected int                                           width;
    protected int                                           height;
    private String                                          title;
    private String                                          id;
    private boolean                                         resizeEnabled       = false;
    private boolean                                         resizeIgnoreBorders = false;
    protected ArrayList<net.minecraft.client.gui.GuiButton> buttonList          = new ArrayList<net.minecraft.client.gui.GuiButton>();
    protected int                                           sizeLimitX          = 100;
    protected int                                           sizeLimitY          = 50;

    public Window(String id, WindowManager manager, String title, int xPos, int yPos, int width, int height)
    {
        this.id = id;
        this.manager = manager;
        this.title = title;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;

        MDX.windows().addWindow(this);
    }

    public abstract void drawWindowContents();

    public abstract void onButtonPress(net.minecraft.client.gui.GuiButton paramGuiButton);

    public abstract void keyTyped(char paramChar, int paramInt);

    public void onUpdate(int mouseX, int mouseY)
    {
        if (this.isResizeEnabled())
        {
            if (Mouse.isButtonDown(0))
            {
                /** Resize Zone Button **/
                int zoneButtonSize = 10;
                int zoneButtonX = this.getX() + this.getWidth() - (zoneButtonSize);
                int zoneButtonY = this.getY() + this.getHeight() - (zoneButtonSize);

                if (mouseX > zoneButtonX && mouseX < zoneButtonX + zoneButtonSize && mouseY > zoneButtonY && mouseY < zoneButtonY + zoneButtonSize || resizeIgnoreBorders)
                {
                    int newWidth = mouseX - this.getX() + 5;
                    int newHeight = mouseY - this.getY() + 5;

                    resizeIgnoreBorders = true;

                    if (newWidth >= sizeLimitX)
                    {
                        this.setDimensions(newWidth, this.getHeight());
                    }

                    if (newHeight >= sizeLimitY)
                    {
                        this.setDimensions(this.getWidth(), newHeight);
                    }
                }
            }
            else
            {
                resizeIgnoreBorders = false;
            }
        }
    }

    public void onClose()
    {
        this.manager.getWindowAPI().getWindows().remove(this);
    }

    public void maximize()
    {
        ScaledResolution res = Screen.scaledDisplayResolution();
        this.setPosition(0, 0 + 16);
        this.setDimensions(res.getScaledWidth(), res.getScaledHeight() - 16);
    }

    public void setPosition(int x, int y)
    {
        this.xPos = x;
        this.yPos = y;
    }

    public void setDimensions(int w, int h)
    {
        this.width = w;
        this.height = h;
    }

    public void setTitle(String t)
    {
        this.title = t;
    }

    public int getX()
    {
        return this.xPos;
    }

    public int getY()
    {
        return this.yPos;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public String getTitle()
    {
        return this.title;
    }

    public ArrayList<net.minecraft.client.gui.GuiButton> getButtonList()
    {
        return this.buttonList;
    }

    public boolean isTopWindow()
    {
        if (this.manager.getWindowAPI().getWindows().size() > 0)
        {
            return ((Window) this.manager.getWindowAPI().getWindows().get(0)).equals(this);
        }

        return false;
    }

    public String getID()
    {
        return this.id;
    }

    public void setResizeEnabled(boolean resizeEnabled)
    {
        this.resizeEnabled = resizeEnabled;
    }

    public boolean isResizeEnabled()
    {
        return resizeEnabled;
    }

    public int getSizeLimitX()
    {
        return sizeLimitX;
    }

    public int getSizeLimitY()
    {
        return sizeLimitY;
    }

    public void setSizeLimit(int sizeLimitX, int sizeLimitY)
    {
        this.sizeLimitX = sizeLimitX;
        this.sizeLimitY = sizeLimitY;
    }
}