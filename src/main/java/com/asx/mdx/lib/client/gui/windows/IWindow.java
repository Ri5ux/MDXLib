package com.asx.mdx.lib.client.gui.windows;

import net.minecraft.client.gui.GuiButton;

public abstract interface IWindow
{
    public abstract void drawWindowContents();

    public abstract void onClose();

    public abstract void onButtonPress(GuiButton paramGuiButton);

    public abstract void keyTyped(char paramChar, int paramInt);
}