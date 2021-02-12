package com.asx.mdx.lib.client.gui.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.asx.mdx.MDX;
import com.asx.mdx.lib.client.gui.windows.themes.Theme;
import com.asx.mdx.lib.client.gui.windows.themes.ThemeDefault;
import com.asx.mdx.lib.client.gui.windows.themes.ThemeMinecraft;

import net.minecraft.client.Minecraft;

public class WindowAPI
{
    public static final WindowAPI  INSTANCE = new WindowAPI();

    private WindowManager          windowManager;
    private Theme                  currentTheme;
    private ArrayList<Window>      windows  = new ArrayList<Window>();
    private HashMap<String, Theme> themes   = new HashMap<String, Theme>();
    private Theme                  themeDefault;
    private Theme                  themeMinecraft;

    public WindowAPI()
    {
        this.windowManager = new WindowManager(this, null);
        this.themeDefault = new ThemeDefault("default");
        this.themeMinecraft = new ThemeMinecraft("minecraft");
        registerTheme(this.themeDefault);
        registerTheme(this.themeMinecraft);
        this.currentTheme = getThemeForName("default");
    }

    public void onTick()
    {
        if ((getWindows().size() <= 0) && ((Minecraft.getMinecraft().currentScreen instanceof WindowManager)))
        {
            Minecraft.getMinecraft().displayGuiScreen(getWindowManager().parentScreen);
        }

        if ((getWindowManager().parentScreen != Minecraft.getMinecraft().currentScreen) && (!(Minecraft.getMinecraft().currentScreen instanceof WindowManager)))
        {
            getWindowManager().parentScreen = Minecraft.getMinecraft().currentScreen;
        }
    }

    public WindowManager getWindowManager()
    {
        return this.windowManager;
    }

    public void drawWindow(Window window, int mouseX, int mouseY)
    {
        getCurrentTheme().drawWindow(window, mouseX, mouseY);
    }

    public void registerTheme(Theme theme)
    {
        this.themes.put(theme.getName(), theme);
    }

    public void addWindow(Window window)
    {
        if (!isWindowRegistered(window))
        {
            this.windows.add(window);
        }
        else
        {
            MDX.log().info("Tried registerring a window with an ID that already exists: " + window.getID());
        }
    }

    public void removeWindowWithID(String id)
    {
        this.windows.remove(id);
    }

    public void removeWindow(Window window)
    {
        this.windows.remove(window);
    }

    public Theme getThemeForName(String name)
    {
        return (Theme) this.themes.get(name);
    }

    public Theme getThemeNameForTheme(Theme theme)
    {
        return (Theme) this.themes.get(theme);
    }

    public Theme getCurrentTheme()
    {
        return this.currentTheme;
    }

    public void setCurrentTheme(Theme currentTheme)
    {
        this.currentTheme = currentTheme;
    }

    public ArrayList<Theme> getThemes()
    {
        return new ArrayList<Theme>(this.themes.values());
    }

    public ArrayList<Window> getWindows()
    {
        return this.windows;
    }

    public HashMap<String, Theme> getThemeRegistry()
    {
        return this.themes;
    }

    public boolean canWindowManagerOpen()
    {
        return (Minecraft.getMinecraft().currentScreen != null) && (!(Minecraft.getMinecraft().currentScreen instanceof WindowManager));
    }

    public void showWindowManager()
    {
        showWindowManager(false);
    }

    public void showWindowManager(boolean force)
    {
        if ((canWindowManagerOpen()) || (force))
        {
            getWindowManager().setParentScreen(null);
            Minecraft.getMinecraft().displayGuiScreen(getWindowManager());
        }
    }

    public boolean isWindowRegistered(Window windowObj)
    {
        Iterator<Window> iterator = this.windows.iterator();

        Window window;
        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            window = (Window) iterator.next();
        }
        while (window != windowObj);

        return true;
    }
}