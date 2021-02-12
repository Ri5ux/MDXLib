package com.asx.mdx.lib.client.model.loaders.tabula.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabulaAnimationContainer
{
    private String                                               name;
    private String                                               identifier;

    private boolean                                              loops;

    private Map<String, List<TabulaAnimationComponentContainer>> sets = new HashMap<>();

    public String getName()
    {
        return this.name;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public boolean doesLoop()
    {
        return this.loops;
    }

    public Map<String, List<TabulaAnimationComponentContainer>> getComponents()
    {
        return this.sets;
    }
}
