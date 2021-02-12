package com.asx.mdx.lib.world.entity.animations;

public interface IAnimated
{
    public static final Animation NO_ANIMATION = Animation.create(0);

    public int getAnimationTick();

    public void setAnimationTick(int tick);

    public Animation getActiveAnimation();

    public void setActiveAnimation(Animation animation);

    public Animation[] getAnimations();
    
    public boolean isAnimationPaused();
    
    public void pauseAnimation();
    
    public void playAnimation();
}
