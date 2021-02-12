package com.asx.mdx.lib.client.gui;

import javax.vecmath.Vector2d;

import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.util.Game;

import net.minecraft.client.Minecraft;

public class GuiCustomSlider extends GuiCustomButton
{
    public String label;
    public float sliderValue = 1.0F;
    public float sliderMaxValue = 1.0F;
    public boolean dragging = false;
    public int sliderVariable;
    public int sliderButtonColor;

    public GuiCustomSlider(int id, int x, int y, String label, float startingValue, float maxValue)
    {
        super(id, x, y, 150, 20, label);
        this.sliderValue = startingValue;
        this.sliderMaxValue = maxValue;
        this.label = label;
        this.sliderButtonColor = 0xCC00DDFF;
        this.overlayColorNormal = 0x00000000;
        this.baseColor = 0x44000000;
    }

    @Override
    public int getHoverState(boolean par1)
    {
        return 0;
    }

    @Override
    public void mouseDragged(Minecraft minecraft, int mouseX, int mouseY)
    {
        super.mouseDragged(minecraft, mouseX, mouseY);

        if (this.visible)
        {
            if (this.dragging)
            {
                this.displayString = label + ": " + (int) (sliderValue * sliderMaxValue);
                this.sliderValue = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }

                this.sliderVariable = (int) (this.sliderValue * this.sliderMaxValue);
            }
        }
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        
        if (this.visible)
        {
            Draw.drawRect(this.x + (int) (this.sliderValue * (this.width - 8)), this.y, 8, this.height, this.sliderButtonColor);
        }
    }

    @Override
    public boolean mousePressed(Minecraft minecraft, int mouseX, int mouseY)
    {
        if (super.mousePressed(minecraft, mouseX, mouseY))
        {
            this.sliderValue = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
    }

    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        this.mousePressed(Game.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
    }

    @Override
    public void mouseReleased(Vector2d mousePosition)
    {
        this.mouseReleased((int) mousePosition.x, (int) mousePosition.y);
    }

    @Override
    public void mouseDragged(Vector2d mousePosition)
    {
        this.mouseDragged(Game.minecraft(), (int) mousePosition.x, (int) mousePosition.y);
    }
}