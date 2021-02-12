package com.asx.mdx.lib.client.gui.notifications;

import java.util.ArrayList;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;

public abstract class Notification
{
    private int ticksExisted;
    private int x;
    private int y;
    private int width;
    private int height;
    private int pad;
    private int lineSpacing;
    private float textScale;

    public static class DynamicNotification extends Notification
    {
        private String message;
        private int displayTimeout;

        @Override
        public boolean allowMultiple()
        {
            return false;
        }

        @Override
        public String getMessage()
        {
            return this.message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }
        
        @Override
        public int displayTimeout()
        {
            return this.displayTimeout == 0 ? super.displayTimeout() : this.displayTimeout;
        }
        
        public void setDisplayTimeout(int seconds)
        {
            this.displayTimeout = seconds * 20;
        }
    }
    
    public Notification()
    {
        this.x = 5;
        this.y = 5;
        this.width = 128;
        this.height = 32;
        this.pad = 5;
        this.lineSpacing = 10;
        this.textScale = 0.75F;
    }
    
    public abstract String getMessage();

    public int displayTimeout()
    {
        return 20 * 10;
    }

    public int tick()
    {
        return this.ticksExisted++;
    }

    public void timeoutAction()
    {
        MDX.notifications().queue().remove(this);
        NotifierModule.currentNotification = null;
        this.ticksExisted = 0;
    }

    protected void preDraw()
    {
        this.draw();
    }

    public void draw()
    {
        this.textScale = 0.5F;
        int lineIndex = 0;
        ArrayList<String> lines = Draw.wrapString(this.getMessage(), (int) ((width - (pad * 2)) / textScale));
        String tickString = String.format("%s", this.displayTimeout() / 20 - this.ticksExisted / 20);
        int tickStringWidth = Draw.getStringRenderWidth(tickString);
        
        Draw.drawRect(x, y, width, height, 0xAA000000);

        OpenGL.pushMatrix();
        {
            OpenGL.scale(textScale, textScale, textScale);
            Draw.drawString(String.format("Notification %s of %s", MDX.notifications().queue().indexOf(NotifierModule.currentNotification) + 1, MDX.notifications().queue().size()), (int) ((x + pad - 3) / textScale), (int) ((y + pad - 3) / textScale) + (lineSpacing * lineIndex), 0x66FFFFFF, false);
            Draw.drawString(tickString, (int) ((x + width - pad + 3 - (tickStringWidth * textScale)) / textScale), (int) ((y + pad - 3) / textScale) + (lineSpacing * lineIndex), 0x66FFFFFF, false);

            for (String line : lines)
            {
                Draw.drawString(line, (int) ((x + pad) / textScale), (int) ((int) ((y + pad) / textScale) + (lineSpacing * lineIndex) + lineSpacing), 0xFFFFFFFF, false);
                lineIndex++;
            }
            
            this.height = (int) ((pad * 2) + (lineIndex * (lineSpacing * textScale)) + (lineSpacing * textScale));
        }
        OpenGL.popMatrix();
    }

    public int getTicksExisted()
    {
        return ticksExisted;
    }
    
    public boolean allowMultiple()
    {
        return true;
    }
}
