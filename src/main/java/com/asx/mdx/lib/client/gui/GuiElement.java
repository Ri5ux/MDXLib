package com.asx.mdx.lib.client.gui;

import javax.vecmath.Vector2d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public abstract class GuiElement extends GuiButton implements IGuiElement
{
    protected IAction action;
    protected boolean trackInput;
    protected long    lastRendered;
    public String     tooltip;

    public GuiElement(int id, int x, int y, int width, int height, String displayName)
    {
        super(id, x, y, width, height, displayName);
        this.trackInput = true;
        this.tooltip = "";
    }

    @Override
    public void updateElement()
    {
        if (this.isRendered())
        {
            this.visible = true;
        }
        else
        {
            this.visible = false;
        }
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        this.lastRendered = System.currentTimeMillis();
    }

    @Override
    public boolean isEnabled()
    {
        return this.enabled;
    }

    @Override
    public long lastRenderTime()
    {
        return this.lastRendered;
    }

    @Override
    public boolean isRendered()
    {
        long currentTime = System.currentTimeMillis();

        if (currentTime > 100 && this.lastRenderTime() > 100)
        {
            return currentTime - this.lastRenderTime() < 500;
        }

        return false;
    }

    @Override
    public boolean canTrackInput()
    {
        return this.isEnabled() && trackInput && visible;
    }

    @Override
    public boolean setTrackInput(boolean trackInput)
    {
        return this.trackInput = trackInput;
    }

    public void trackElement()
    {
        GUIElementTracker.INSTANCE.track(this);
    }

    public void stopTracking()
    {
        GUIElementTracker.INSTANCE.stopTracking(this);
    }

    public boolean isMouseInElement(Vector2d mousePosition)
    {
        int mouseX = (int) mousePosition.x;
        int mouseY = (int) mousePosition.y;

        return this.isEnabled() && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    @Override
    public IAction getAction()
    {
        return action;
    }

    @Override
    public IGuiElement setAction(IAction action)
    {
        this.action = action;
        return this;
    }
    
    @Override
    public String getTooltip()
    {
        return this.tooltip;
    }
    
    @Override
    public void setTooltip(String tooltip)
    {
        this.tooltip = tooltip;
    }
    
    @Override
    public int x()
    {
        return this.x;
    }
    
    @Override
    public int y()
    {
        return this.y;
    }
    
    @Override
    public int width()
    {
        return this.width;
    }
    
    @Override
    public int height()
    {
        return this.height;
    }
}
