package com.asx.mdx.lib.client.model.loaders.tabula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asx.mdx.lib.client.model.loaders.tabula.container.TabulaCubeContainer;
import com.asx.mdx.lib.client.model.loaders.tabula.container.TabulaCubeGroupContainer;
import com.asx.mdx.lib.client.model.loaders.tabula.container.TabulaModelContainer;
import com.asx.mdx.lib.client.util.models.Model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TabulaModel extends Model
{
    protected Map<String, Part>    cubes         = new HashMap<>();
    protected List<Part>           rootBoxes     = new ArrayList<>();
    protected ITabulaModelAnimator tabulaAnimator;
    protected Map<String, Part>    identifierMap = new HashMap<>();
    protected double[]             scale;

    public TabulaModel(TabulaModelContainer container, ITabulaModelAnimator tabulaAnimator)
    {
        this.textureWidth = container.getTextureWidth();
        this.textureHeight = container.getTextureHeight();
        this.tabulaAnimator = tabulaAnimator;
        for (TabulaCubeContainer cube : container.getCubes())
        {
            this.parseCube(cube, null);
        }
        container.getCubeGroups().forEach(this::parseCubeGroup);
        this.updateDefaultPose();
        this.scale = container.getScale();
    }

    public TabulaModel(TabulaModelContainer container)
    {
        this(container, null);
    }

    private void parseCubeGroup(TabulaCubeGroupContainer container)
    {
        for (TabulaCubeContainer cube : container.getCubes())
        {
            this.parseCube(cube, null);
        }
        container.getCubeGroups().forEach(this::parseCubeGroup);
    }

    private void parseCube(TabulaCubeContainer cube, Part parent)
    {
        Part box = this.createBox(cube);
        this.cubes.put(cube.getName(), box);
        this.identifierMap.put(cube.getIdentifier(), box);
        if (parent != null)
        {
            parent.addChild(box);
        }
        else
        {
            this.rootBoxes.add(box);
        }
        for (TabulaCubeContainer child : cube.getChildren())
        {
            this.parseCube(child, box);
        }
    }

    private Part createBox(TabulaCubeContainer cube)
    {
        int[] textureOffset = cube.getTextureOffset();
        double[] position = cube.getPosition();
        double[] rotation = cube.getRotation();
        double[] offset = cube.getOffset();
        int[] dimensions = cube.getDimensions();
        Part box = new Part(this, cube.getName());
        box.setTextureOffset(textureOffset[0], textureOffset[1]);
        box.setRotationPoint((float) position[0], (float) position[1], (float) position[2]);
        box.addBox((float) offset[0], (float) offset[1], (float) offset[2], dimensions[0], dimensions[1], dimensions[2], 0.0F);
        box.rotateAngleX = (float) Math.toRadians(rotation[0]);
        box.rotateAngleY = (float) Math.toRadians(rotation[1]);
        box.rotateAngleZ = (float) Math.toRadians(rotation[2]);
        box.mirror = cube.isTextureMirrorEnabled();

        return box;
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale, Entity entity)
    {
        this.resetPose();

        if (this.tabulaAnimator != null)
        {
            this.tabulaAnimator.setRotationAngles(this, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        }
    }

    public Part getCube(String name)
    {
        return this.cubes.get(name);
    }

    public Part getCubeByIdentifier(String identifier)
    {
        return this.identifierMap.get(identifier);
    }

    public Map<String, Part> getCubes()
    {
        return this.cubes;
    }

    @Override
    public void render(Object obj)
    {
        if (obj instanceof Entity)
        {
            this.setRotationAngles(swingProgress(obj), swingProgressPrev(obj), idleProgress(obj), headYaw(obj), headPitch(obj), DEFAULT_SCALE, (Entity) obj);
        }
        
        GlStateManager.pushMatrix();
        GlStateManager.scale(this.scale[0], this.scale[1], this.scale[2]);

        for (Part box : this.rootBoxes)
        {
            draw(box);
        }

        GlStateManager.popMatrix();
    }
}
