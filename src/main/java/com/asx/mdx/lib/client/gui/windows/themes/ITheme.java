package com.asx.mdx.lib.client.gui.windows.themes;

import com.asx.mdx.lib.client.gui.windows.Window;

public abstract interface ITheme
{
    public abstract void drawWindow(Window paramWindow, int paramInt1, int paramInt2);

    public abstract void drawBackground(Window paramWindow, int paramInt1, int paramInt2);

    public abstract void drawTitleBar(Window paramWindow, int paramInt1, int paramInt2);

    public abstract void drawCloseButton(Window paramWindow, int paramInt1, int paramInt2);
}
