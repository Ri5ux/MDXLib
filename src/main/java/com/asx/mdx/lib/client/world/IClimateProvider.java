package com.asx.mdx.lib.client.world;

public interface IClimateProvider
{
    public ICloudProvider getCloudProvider();
    
    public IStormProvider getStormProvider();
}
