package com.asx.mdx.lib.client.model.loaders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.asx.mdx.lib.client.util.Draw;
import com.asx.mdx.lib.util.Game;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class DummyModelLoader implements ICustomModelLoader
{
    public static final DummyModelLoader INSTANCE = new DummyModelLoader();
    private ArrayList<String> dummyIDs = new ArrayList<String>();

    public static enum Type
    {
        ITEM("models/item/%s"), BLOCK("models/block/%s"), STANDARD("%s");

        private String location;

        Type(String location)
        {
            this.location = location;
        }

        public String location(ResourceLocation resource)
        {
            return String.format("%s:" + location, resource.getNamespace(), resource.getPath());
        }
    }
    
    public void registerDummy(DummyModelLoader.Type type, ResourceLocation resource)
    {
        dummyIDs.add(type.location(resource));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        ;
    }
    
    public static String resourceNameFor(ResourceLocation r)
    {
        return String.format("%s:%s", r.getNamespace(), r.getPath());
    }

    @Override
    public boolean accepts(ResourceLocation r)
    {
        if (this.dummyIDs.contains(resourceNameFor(r)))
        {
            return true;
        }

        return false;
    }

    @Override
    public IModel loadModel(ResourceLocation r) throws Exception
    {
        if (this.dummyIDs.contains(resourceNameFor(r)))
        {
            return new DummyModel();
        }

        return null;
    }

    public static class DummyModel implements IModel
    {
        @Override
        public Collection<ResourceLocation> getDependencies()
        {
            return ImmutableSet.of(CachedDummyModel.MODEL_LOCATION);
        }

        @Override
        public Collection<ResourceLocation> getTextures()
        {
            return ImmutableSet.of(Draw.getMissingTexture());
        }

        @Override
        public IModelState getDefaultState()
        {
            return TRSRTransformation.identity();
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
        {
            return new CachedDummyModel();
        }
    }

    public static class CachedDummyModel implements IBakedModel
    {
        public static final ResourceLocation MODEL_LOCATION = new ResourceLocation("minecraft", "item/generated");

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
        {
            try
            {
                return ModelLoaderRegistry.getModel(MODEL_LOCATION).bake(TRSRTransformation.from(EnumFacing.NORTH), DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL, ModelLoader.defaultTextureGetter()).getQuads(state, side, rand);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return Collections.emptyList();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms()
        {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return ItemOverrideList.NONE;
        }

        @Override
        public boolean isAmbientOcclusion()
        {
            return false;
        }

        @Override
        public boolean isGui3d()
        {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer()
        {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture()
        {
            return Game.minecraft().getTextureMapBlocks().getMissingSprite();
        }
    }
}
