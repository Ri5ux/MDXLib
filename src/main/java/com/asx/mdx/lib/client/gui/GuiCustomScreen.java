package com.asx.mdx.lib.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;

public class GuiCustomScreen extends GuiScreen
{
    public ArrayList<GuiCustomButton> customButtonList = new ArrayList<GuiCustomButton>();
    public ArrayList<GuiCustomTextbox> customTextfieldList = new ArrayList<GuiCustomTextbox>();

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        try
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (GuiCustomButton button : customButtonList)
        {
            button.mouseReleased(mouseX, mouseY);
        }
    }

    @Override
    public void drawGradientRect(int x1, int y1, int x2, int y2, int topColor, int bottomColor)
    {
        super.drawGradientRect(x1, y1, x2, y2, topColor, bottomColor);
    }

    public void setZLevel(float level)
    {
        this.zLevel = level;
    }

    public float getZLevel()
    {
        return this.zLevel;
    }

    public void incZLevel(float level)
    {
        this.zLevel += level;
    }
}