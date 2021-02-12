package com.asx.mdx.lib.world.entity.animations;

/** Credit to the developers of LLibrary, for which without this would not be possible. **/
public class Animation
{
    private int duration;

    private Animation(int duration)
    {
        this.duration = duration;
    }

    public static Animation create(int duration)
    {
        return new Animation(duration);
    }

    public int getDuration()
    {
        return this.duration;
    }
}