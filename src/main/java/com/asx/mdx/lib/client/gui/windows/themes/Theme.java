package com.asx.mdx.lib.client.gui.windows.themes;

import org.lwjgl.opengl.GL11;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.gui.windows.Window;
import com.asx.mdx.lib.client.gui.windows.WindowManager;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;

import net.minecraft.client.gui.GuiButton;

public class Theme implements ITheme
{
    private String themeName;

    public Theme(String name)
    {
        this.themeName = name;
    }

    public void drawWindow(Window window, int mouseX, int mouseY)
    {
        OpenGL.enableBlend();
        drawBackground(window, mouseX, mouseY);
        drawTitleBar(window, mouseX, mouseY);
        drawCloseButton(window, mouseX, mouseY);
        drawContents(window, mouseX, mouseY);

        /** Maximize button **/
        int maximizeButtonColor = 0x1F121212;
        int maximizeButtonSize = 16;
        int maximizeButtonX = window.getX() + window.getWidth() - (maximizeButtonSize * 2);
        int maximizeButtonY = window.getY() - maximizeButtonSize;

        if (mouseX > maximizeButtonX && mouseX < maximizeButtonX + maximizeButtonSize && mouseY > maximizeButtonY && mouseY < maximizeButtonY + maximizeButtonSize)
        {
            maximizeButtonColor = 0x44121212;
        }

        Draw.drawRect(maximizeButtonX, maximizeButtonY, maximizeButtonSize, maximizeButtonSize, maximizeButtonColor);
        Draw.drawString("\u29E0", maximizeButtonX + 5, window.getY() - 12, 0xFFFFFFFF, false);

        /** Resize button **/
        int resizeButtonColor = window.isResizeEnabled() ? 0x4412FF12 : 0x1F121212;
        int resizeButtonSize = 16;
        int resizeButtonX = window.getX() + window.getWidth() - (resizeButtonSize * 3);
        int resizeButtonY = window.getY() - resizeButtonSize;

        if (mouseX > resizeButtonX && mouseX < resizeButtonX + resizeButtonSize && mouseY > resizeButtonY && mouseY < resizeButtonY + resizeButtonSize)
        {
            resizeButtonColor = 0x44121212;
        }

        Draw.drawRect(resizeButtonX, resizeButtonY, resizeButtonSize, resizeButtonSize, resizeButtonColor);
        Draw.drawString("\u2921", resizeButtonX + 5, window.getY() - 12, 0xFFFFFFFF, false);

        if (window.isResizeEnabled())
        {
            /** Zone **/
            int zoneButtonColor = 0x6612FF12;
            int zoneButtonSize = 10;
            int zoneButtonX = window.getX() + window.getWidth() - (zoneButtonSize);
            int zoneButtonY = window.getY() + window.getHeight() - (zoneButtonSize);

            if (mouseX > zoneButtonX && mouseX < zoneButtonX + zoneButtonSize && mouseY > zoneButtonY && mouseY < zoneButtonY + zoneButtonSize)
            {
                zoneButtonColor = 0x8812FF12;
            }

            Draw.drawRect(zoneButtonX, zoneButtonY, zoneButtonSize, zoneButtonSize, zoneButtonColor);
            Draw.drawString("\u2921", zoneButtonX + 2, zoneButtonY + 1, 0xFFFFFFFF, false);
        }
    }

    public void drawBackground(Window window, int mouseX, int mouseY)
    {
        ;
    }

    public void drawTitleBar(Window window, int mouseX, int mouseY)
    {
        ;
    }

    public void drawCloseButton(Window window, int mouseX, int mouseY)
    {
        ;
    }

    public void drawContents(Window window, int mouseX, int mouseY)
    {
        int x;

        int y;

        if (getWindowManager() != null)
        {
            OpenGL.pushMatrix();
            float s = 0.5F;
            OpenGL.scale(s, s, s);
            Draw.drawString(window.getTitle(), (window.getX() + 5) * 2, (window.getY() - 10) * 2, 0xFF000000, false);
            OpenGL.popMatrix();

            window.drawWindowContents();

            x = -1;
            y = -1;

            if (getWindowManager().getTopWindow(mouseX, mouseY) == window)
            {
                x = mouseX;
                y = mouseY;
            }

            for (GuiButton button : window.getButtonList())
            {
                button.drawButton(Game.minecraft(), x, y, Game.partialTicks());
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
            }

        }
        else
        {
            MDX.log().info("Window Manager returned null.");
        }
    }

    public String getName()
    {
        return this.themeName;
    }

    public void setName(String themeName)
    {
        this.themeName = themeName;
    }

    public WindowManager getWindowManager()
    {
        return MDX.windows().getWindowManager();
    }
}