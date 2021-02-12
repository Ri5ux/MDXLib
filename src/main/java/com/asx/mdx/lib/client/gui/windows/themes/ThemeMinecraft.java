package com.asx.mdx.lib.client.gui.windows.themes;

import com.asx.mdx.lib.client.gui.windows.Window;
import com.asx.mdx.lib.client.util.Draw;

import net.minecraft.init.Blocks;

public class ThemeMinecraft extends Theme implements ITheme
{
    public ThemeMinecraft(String name)
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
        Draw.drawBlock(Blocks.SANDSTONE.getDefaultState(), window.getX(), window.getY() - 16, window.getWidth(), 16, window.getWidth() / 16F, 1F);
    }

    public void drawTitleBar(Window window, int mouseX, int mouseY)
    {
        Draw.drawBlock(Blocks.SAND.getDefaultState(), window.getX(), window.getY(), window.getWidth(), window.getHeight(), window.getWidth() / 16F, window.getHeight() / 16F);
    }

    public void drawCloseButton(Window window, int mouseX, int mouseY)
    {
        Draw.drawBlock(Blocks.TNT.getDefaultState(), window.getX() + window.getWidth() - 16, window.getY() - 16, 16, 16);
    }
}
