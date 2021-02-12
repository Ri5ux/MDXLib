package com.asx.mdx.core.mods;

import com.asx.mdx.lib.util.Game;

public class ModIdentityMap
{
    public IdentityMap identityMap;

    public ModIdentityMap(String modIdentity)
    {
        this.identityMap = new IdentityMap(modIdentity, modIdentity, this);
    }

    public ModIdentityMap(String invalidIdentity, String validIdentity, String classLocation)
    {
        this.identityMap = new IdentityMap(invalidIdentity, validIdentity, this);
    }

    public String getClassLocation()
    {
        return Game.getModContainer(this.getValidIdentity()).getMod().getClass().getName();
    }

    public String getValidIdentity()
    {
        return this.identityMap.validIdentity;
    }

    public String getInvalidIdentity()
    {
        return this.identityMap.invalidIdentity;
    }

    public static class IdentityMap
    {
        public ModIdentityMap modIdentityMap;
        public String invalidIdentity;
        public String validIdentity;

        public IdentityMap(String invalidIdentity, String validIdentity, ModIdentityMap mod)
        {
            this.modIdentityMap = mod;
            this.invalidIdentity = invalidIdentity;
            this.validIdentity = validIdentity;
        }

        public String getValidIdentity()
        {
            return validIdentity;
        }

        public String getInvalidIdentity()
        {
            return invalidIdentity;
        }

        public ModIdentityMap getModIdentityMap()
        {
            return modIdentityMap;
        }
    }
}
