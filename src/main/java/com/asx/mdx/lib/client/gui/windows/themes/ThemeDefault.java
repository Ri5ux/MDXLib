package com.asx.mdx.lib.client.gui.windows.themes;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.gui.windows.Window;
import com.asx.mdx.lib.client.gui.windows.WindowManager;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;

public class ThemeDefault extends Theme implements ITheme
{
    public ThemeDefault(String name)
    {
        super(name);
        setName(name);
    }

    public void drawWindow(Window window, int mouseX, int mouseY)
    {
        super.drawWindow(window, mouseX, mouseY);
    }

    public void drawBackground(Window window, int mouseX, int mouseY)
    {
        Draw.drawGradientRect(window.getX(), window.getY(), window.getWidth(), window.getHeight(), 0xFF676767, 0xFF888888);
    }

    public void drawTitleBar(Window window, int mouseX, int mouseY)
    {
        int titlebarColor = 0xFF888888;
        
        if (MDX.windows().getWindowManager().getActiveWindow() == window)
        {
            titlebarColor = 0xFF999999;
        }
        
        WindowManager.drawRect(window.getX(), window.getY() - 16, window.getX() + window.getWidth(), window.getY(), titlebarColor);
    }

    public void drawCloseButton(Window window, int mouseX, int mouseY)
    {
        OpenGL.enableBlend();
        int closeButtonColor = 0x1F121212;
        int closeButtonSize = 16;
        int closeButtonX = window.getX() + window.getWidth() - closeButtonSize;
        int closeButtonY = window.getY() - closeButtonSize;

        if (mouseX > closeButtonX && mouseX < closeButtonX + closeButtonSize && mouseY > closeButtonY && mouseY < closeButtonY + closeButtonSize)
        {
            closeButtonColor = 0xAAFF1212;
        }

        Draw.drawRect(closeButtonX, closeButtonY, closeButtonSize, closeButtonSize, closeButtonColor);
        Draw.drawString("x", window.getX() + window.getWidth() - 10, window.getY() - 12, 0xFFFFFFFF, false);
    }
}