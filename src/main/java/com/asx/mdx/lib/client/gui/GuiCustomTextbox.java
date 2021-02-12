package com.asx.mdx.lib.client.gui;

import javax.vecmath.Vector2d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.client.util.OpenGL;
import com.asx.mdx.lib.util.Game;
import com.asx.mdx.lib.util.Chat.Chars;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCustomTextbox implements IGuiElement
{
    protected IAction         action;
    protected GuiCustomScreen parentScreen;
    protected String          text;
    protected String          textPrev;
    protected String          tooltip;
    protected boolean         trackInput;
    protected boolean         isRendered;
    protected boolean         enableBackgroundDrawing;
    protected boolean         canLoseFocus;
    protected boolean         isFocused;
    protected boolean         isEnabled;
    protected boolean         visible;
    protected long            lastRendered;
    protected int             x;
    protected int             y;
    protected int             width;
    protected int             height;
    protected int             maxStringLength;
    protected int             cursorCounter;
    protected int             lineScrollOffset;
    protected int             cursorPosition;
    protected int             selectionEnd;
    protected int             textColor;
    protected int             textColorDisabled;
    protected int             backgroundColor;
    protected int             borderColor;

    public GuiCustomTextbox(GuiCustomScreen parentScreen, int x, int y, int width, int height)
    {
        this(x, y, width, height);
        this.parentScreen = parentScreen;
        this.parentScreen.customTextfieldList.add(this);
    }

    public GuiCustomTextbox(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.trackInput = true;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxStringLength = 32;
        this.enableBackgroundDrawing = true;
        this.canLoseFocus = true;
        this.textColor = 0xE0E0E0;
        this.textColorDisabled = 0x707070;
        this.backgroundColor = 0x44000000;
        this.borderColor = 0x00000000;
        this.visible = true;
        this.isEnabled = true;
        this.text = "";
        this.trackElement();
    }

    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter()
    {
        ++this.cursorCounter;
    }

    /**
     * Returns the contents of the textbox
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * Sets the text of the textbox
     */
    public void setText(String text)
    {
        if (text.length() > this.maxStringLength)
        {
            this.text = text.substring(0, this.maxStringLength);
        }
        else
        {
            this.text = text;
        }

        this.setCursorPositionEnd();

        int pos = this.x + this.width;

        if (this.getEnableBackgroundDrawing())
        {
            pos -= 4;
        }

        int lineScrollOffset = 0;

        String s = Minecraft.getMinecraft().fontRenderer.trimStringToWidth(text.substring(lineScrollOffset), this.width);
        this.setCursorPosition(Minecraft.getMinecraft().fontRenderer.trimStringToWidth(s, pos).length());
    }

    @Override
    public void updateElement()
    {
        if (this.isRendered())
        {
            this.setVisible(true);
        }
        else
        {
            this.setVisible(false);
        }
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText()
    {
        int start = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int end = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(start, end);
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String text)
    {
        String s1 = "";
        String s2 = ChatAllowedCharacters.filterAllowedCharacters(text);
        int start = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int end = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int r = this.maxStringLength - this.text.length() - (start - this.selectionEnd);

        if (this.text.length() > 0)
        {
            s1 = s1 + this.text.substring(0, start);
        }

        int l;

        if (r < s2.length())
        {
            s1 = s1 + s2.substring(0, r);
            l = r;
        }
        else
        {
            s1 = s1 + s2;
            l = s2.length();
        }

        if (this.text.length() > 0 && end < this.text.length())
        {
            s1 = s1 + this.text.substring(end);
        }

        this.text = s1;
        this.moveCursorBy(start - this.selectionEnd + l);
    }

    /**
     * Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of
     * the cursor.
     */
    public void deleteWords(int words)
    {
        if (this.text.length() != 0)
        {
            if (this.selectionEnd != this.cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                this.deleteFromCursor(this.getNthWordFromCursor(words) - this.cursorPosition);
            }
        }
    }

    /**
     * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
     */
    public void deleteFromCursor(int offset)
    {
        if (this.text.length() != 0)
        {
            if (this.selectionEnd != this.cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                boolean flag = offset < 0;
                int j = flag ? this.cursorPosition + offset : this.cursorPosition;
                int k = flag ? this.cursorPosition : this.cursorPosition + offset;
                String s = "";

                if (j >= 0)
                {
                    s = this.text.substring(0, j);
                }

                if (k < this.text.length())
                {
                    s = s + this.text.substring(k);
                }

                this.text = s;

                if (flag)
                {
                    this.moveCursorBy(offset);
                }
            }
        }
    }

    /**
     * see @getNthNextWordFromPos() params: N, position
     */
    public int getNthWordFromCursor(int position)
    {
        return this.getNthWordFromPos(position, this.getCursorPosition());
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int position, int position2)
    {
        return this.getWord(position, position2, true);
    }

    public int getWord(int position, int cursorPos, boolean p_146197_3_)
    {
        int pos = cursorPos;
        boolean flag1 = position < 0;

        for (int i1 = 0; i1 < Math.abs(position); ++i1)
        {
            if (flag1)
            {
                while (p_146197_3_ && pos > 0 && this.text.charAt(pos - 1) == 32)
                {
                    --pos;
                }

                while (pos > 0 && this.text.charAt(pos - 1) != 32)
                {
                    --pos;
                }
            }
            else
            {
                int j1 = this.text.length();
                pos = this.text.indexOf(32, pos);

                if (pos == -1)
                {
                    pos = j1;
                }
                else
                {
                    while (p_146197_3_ && pos < j1 && this.text.charAt(pos) == 32)
                    {
                        ++pos;
                    }
                }
            }
        }

        return pos;
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int offset)
    {
        this.setCursorPosition(this.selectionEnd + offset);
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int position)
    {
        this.cursorPosition = position;
        int j = this.text.length();

        if (this.cursorPosition < 0)
        {
            this.cursorPosition = 0;
        }

        if (this.cursorPosition > j)
        {
            this.cursorPosition = j;
        }

        this.setSelectionPos(this.cursorPosition);
    }

    /**
     * sets the cursors position to the beginning
     */
    public void setCursorPositionZero()
    {
        this.setCursorPosition(0);
    }

    /**
     * sets the cursors position to after the text
     */
    public void setCursorPositionEnd()
    {
        this.setCursorPosition(this.text.length());
    }

    /**
     * Select all of this textbox's text.
     */
    protected void selectAll()
    {
        this.setCursorPositionEnd();
        this.setSelectionPos(0);
    }

    /**
     * Copy everything currently selected
     */
    protected void copy()
    {
        GuiScreen.setClipboardString(this.getSelectedText());
    }

    /**
     * Paste everything in the clipboard into the current selection
     */
    protected void paste()
    {
        this.textPrev = this.text;
        if (this.isEnabled)
        {
            this.writeText(GuiScreen.getClipboardString());
        }
    }

    /**
     * Copy everything in the current selection and empty the textbox
     */
    protected void cut()
    {
        this.textPrev = this.text;
        GuiScreen.setClipboardString(this.getSelectedText());

        if (this.isEnabled)
        {
            this.writeText("");
        }
    }

    /**
     * Set the current text to the text of the last major state change. (paste, cut, undo, delete, backspace)
     */
    protected void undo()
    {
        if (this.textPrev != null && !this.textPrev.isEmpty())
        {
            String newText = this.textPrev;
            this.textPrev = this.text;
            this.setText(newText);
        }
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    public boolean textboxKeyTyped(char c, int key)
    {
        if (!this.isFocused)
        {
            return false;
        }
        else
        {
            switch (c)
            {
                case Chars.START_OF_HEADING:
                    this.selectAll();
                    return true;
                case Chars.END_OF_TEXT:
                    this.copy();
                    return true;
                case Chars.SYNCHRONUS_IDLE:
                    this.paste();
                    return true;
                case Chars.CANCEL:
                    this.cut();
                    return true;
                case Chars.SUBSTITUTE:
                    this.undo();
                    return true;
                default:
                    switch (key)
                    {
                        case Keyboard.KEY_BACK:
                            this.textPrev = text;
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                if (this.isEnabled)
                                {
                                    this.deleteWords(-1);
                                }
                            }
                            else if (this.isEnabled)
                            {
                                this.deleteFromCursor(-1);
                            }

                            return true;
                        case Keyboard.KEY_HOME:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(0);
                            }
                            else
                            {
                                this.setCursorPositionZero();
                            }

                            return true;
                        case Keyboard.KEY_LEFT:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                                }
                                else
                                {
                                    this.setSelectionPos(this.getSelectionEnd() - 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                this.setCursorPosition(this.getNthWordFromCursor(-1));
                            }
                            else
                            {
                                this.moveCursorBy(-1);
                            }

                            return true;
                        case Keyboard.KEY_RIGHT:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                                }
                                else
                                {
                                    this.setSelectionPos(this.getSelectionEnd() + 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                this.setCursorPosition(this.getNthWordFromCursor(1));
                            }
                            else
                            {
                                this.moveCursorBy(1);
                            }

                            return true;
                        case Keyboard.KEY_END:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(this.text.length());
                            }
                            else
                            {
                                this.setCursorPositionEnd();
                            }

                            return true;
                        case Keyboard.KEY_DELETE:
                            this.textPrev = text;
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                if (this.isEnabled)
                                {
                                    this.deleteWords(1);
                                }
                            }
                            else if (this.isEnabled)
                            {
                                this.deleteFromCursor(1);
                            }

                            return true;
                        default:
                            if (ChatAllowedCharacters.isAllowedCharacter(c))
                            {
                                if (this.isEnabled)
                                {
                                    this.writeText(Character.toString(c));
                                }

                                return true;
                            }
                            else
                            {
                                return false;
                            }
                    }
            }
        }
    }

    /**
     * Args: x, y, buttonClicked
     */
    public void mouseClicked(int mouseX, int mouseY, int i)
    {
        boolean flag = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;

        if (this.canLoseFocus)
        {
            this.setFocused(flag);
        }

        if (this.isFocused && i == 0)
        {
            int l = mouseX - this.x;

            if (this.enableBackgroundDrawing)
            {
                l -= 4;
            }

            String s = Game.fontRenderer().trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(Game.fontRenderer().trimStringToWidth(s, l).length() + this.lineScrollOffset);
        }
    }

    /**
     * Draws the textbox
     */
    public void drawTextBox()
    {
        if (this.getVisible())
        {
            if (this.getEnableBackgroundDrawing())
            {
                Draw.drawRectWithOutline(this.x, this.y, this.width, this.height, 1, this.backgroundColor, this.borderColor);
            }

            int color = this.isEnabled ? this.textColor : this.textColorDisabled;
            int pos = this.cursorPosition - this.lineScrollOffset;
            int end = this.selectionEnd - this.lineScrollOffset;
            String textboxText = Game.fontRenderer().trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            boolean flag = pos >= 0 && pos <= textboxText.length();
            boolean cursorActive = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int padding = this.enableBackgroundDrawing ? this.x + 4 : this.x;
            int cursorHeight = this.enableBackgroundDrawing ? this.y + (this.height - 8) / 2 : this.y;
            int cursorPosX = padding;

            if (end > textboxText.length())
            {
                end = textboxText.length();
            }

            if (textboxText.length() > 0)
            {
                String s1 = flag ? textboxText.substring(0, pos) : textboxText;
                cursorPosX = Game.fontRenderer().drawStringWithShadow(s1, padding, cursorHeight, color);
            }

            boolean selectCursor = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            int cursorWidth = cursorPosX;

            if (!flag)
            {
                cursorWidth = pos > 0 ? padding + this.width : padding;
            }
            else if (selectCursor)
            {
                cursorWidth = cursorPosX - 1;
                --cursorPosX;
            }

            if (textboxText.length() > 0 && flag && pos < textboxText.length())
            {
                Game.fontRenderer().drawStringWithShadow(textboxText.substring(pos), cursorPosX, cursorHeight, color);
            }

            if (cursorActive)
            {
                if (selectCursor)
                {
                    Gui.drawRect(cursorWidth, cursorHeight - 1, cursorWidth + 1, cursorHeight + 1 + Game.fontRenderer().FONT_HEIGHT, 0xFFD0D0D0);
                }
                else
                {
                    Game.fontRenderer().drawStringWithShadow("_", cursorWidth, cursorHeight, color);
                }
            }

            if (end != pos)
            {
                int l1 = padding + Game.fontRenderer().getStringWidth(textboxText.substring(0, end));
                this.drawCursorVertical(cursorWidth, cursorHeight - 1, l1 - 1, cursorHeight + 1 + Game.fontRenderer().FONT_HEIGHT);
            }
        }

        this.lastRendered = System.currentTimeMillis();
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    protected void drawCursorVertical(int x, int y, int w, int h)
    {
        int i1;

        if (x < w)
        {
            i1 = x;
            x = w;
            w = i1;
        }

        if (y < h)
        {
            i1 = y;
            y = h;
            h = i1;
        }

        if (w > this.x + this.width)
        {
            w = this.x + this.width;
        }

        if (x > this.x + this.width)
        {
            x = this.x + this.width;
        }

        OpenGL.color(0.0F, 0.0F, 255.0F, 255.0F);
        OpenGL.disableTexture2d();
        GlStateManager.enableColorLogic();
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        Draw.startQuads();
        Draw.vertex(x, h, 0).endVertex();
        Draw.vertex(w, h, 0).endVertex();
        Draw.vertex(w, y, 0).endVertex();
        Draw.vertex(x, y, 0).endVertex();
        Draw.tessellate();
        GlStateManager.disableColorLogic();
        OpenGL.enableTexture2d();
    }

    public void setMaxStringLength(int length)
    {
        this.maxStringLength = length;

        if (this.text.length() > length)
        {
            this.text = this.text.substring(0, length);
        }
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int getMaxStringLength()
    {
        return this.maxStringLength;
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition()
    {
        return this.cursorPosition;
    }

    /**
     * get enable drawing background and outline
     */
    public boolean getEnableBackgroundDrawing()
    {
        return this.enableBackgroundDrawing;
    }

    /**
     * enable drawing background and outline
     */
    public void setEnableBackgroundDrawing(boolean backgroundDrawing)
    {
        this.enableBackgroundDrawing = backgroundDrawing;
    }

    /**
     * Sets the text color for this textbox
     */
    public void setTextColor(int color)
    {
        this.textColor = color;
    }

    public void setDisabledTextColour(int color)
    {
        this.textColorDisabled = color;
    }
    
    public void setBackgroundColor(int backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }
    
    public void setBorderColor(int borderColor)
    {
        this.borderColor = borderColor;
    }

    /**
     * Sets focus to this gui element
     */
    public void setFocused(boolean focused)
    {
        if (focused && !this.isFocused)
        {
            this.cursorCounter = 0;
        }

        this.isFocused = focused;
    }

    /**
     * Getter for the focused field
     */
    public boolean isFocused()
    {
        return this.isFocused;
    }

    public void setEnabled(boolean enabled)
    {
        this.isEnabled = enabled;
    }

    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    public int getSelectionEnd()
    {
        return this.selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getWidth()
    {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    /**
     * Sets the position of the selection anchor (i.e. position the selection was started at)
     */
    public void setSelectionPos(int pos)
    {
        int textLength = this.text.length();

        if (pos > textLength)
        {
            pos = textLength;
        }

        if (pos < 0)
        {
            pos = 0;
        }

        this.selectionEnd = pos;

        if (Game.fontRenderer() != null)
        {
            if (this.lineScrollOffset > textLength)
            {
                this.lineScrollOffset = textLength;
            }

            String trimmedString = Game.fontRenderer().trimStringToWidth(this.text.substring(this.lineScrollOffset), this.width);
            int trimmedPos = trimmedString.length() + this.lineScrollOffset;

            if (pos == this.lineScrollOffset)
            {
                this.lineScrollOffset -= Game.fontRenderer().trimStringToWidth(this.text, this.width, true).length();
            }

            if (pos > trimmedPos)
            {
                this.lineScrollOffset += pos - trimmedPos;
            }
            else if (pos <= this.lineScrollOffset)
            {
                this.lineScrollOffset -= this.lineScrollOffset - pos;
            }

            if (this.lineScrollOffset < 0)
            {
                this.lineScrollOffset = 0;
            }

            if (this.lineScrollOffset > textLength)
            {
                this.lineScrollOffset = textLength;
            }
        }
    }

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    public void setCanLoseFocus(boolean canLoseFocus)
    {
        this.canLoseFocus = canLoseFocus;
    }

    /**
     * returns true if this textbox is visible
     */
    public boolean getVisible()
    {
        return this.visible;
    }

    /**
     * Sets whether or not this textbox is visible
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    @Override
    public void mousePressed(Vector2d mousePosition)
    {
        this.mouseClicked((int) mousePosition.x, (int) mousePosition.y, 0);
    }

    @Override
    public void mouseReleased(Vector2d mousePosition)
    {
        ;
    }

    @Override
    public void mouseDragged(Vector2d mousePosition)
    {
        ;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
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
    public void trackElement()
    {
        GUIElementTracker.INSTANCE.track(this);
    }

    @Override
    public void stopTracking()
    {
        GUIElementTracker.INSTANCE.stopTracking(this);
    }

    @Override
    public boolean isMouseInElement(Vector2d mousePosition)
    {
        int mouseX = (int) mousePosition.x;
        int mouseY = (int) mousePosition.y;

        return this.isEnabled() && this.getVisible() && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    @Override
    public boolean canTrackInput()
    {
        return this.isEnabled() && trackInput && getVisible();
    }

    @Override
    public boolean setTrackInput(boolean trackInput)
    {
        return this.trackInput = trackInput;
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

    @Override
    public void setX(int x)
    {
        this.x = x;
    }

    @Override
    public void setY(int y)
    {
        this.y = y;
    }

    @Override
    public void setWidth(int width)
    {
        this.width = width;
    }

    @Override
    public void setHeight(int height)
    {
        this.height = height;
    }
}
