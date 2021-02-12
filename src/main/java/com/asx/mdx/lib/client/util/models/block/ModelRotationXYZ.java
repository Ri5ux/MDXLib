package com.asx.mdx.lib.client.util.models.block;

import java.util.Map;
import java.util.Optional;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Maps;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ModelRotationXYZ implements net.minecraftforge.common.model.IModelState, net.minecraftforge.common.model.ITransformation
{
    X0_Y0_Z0(0, 0, 0),
    X0_Y90_Z0(0, 90, 0),
    X0_Y180_Z0(0, 180, 0),
    X0_Y270_Z0(0, 270, 0),
    
    X90_Y0_Z0(90, 0, 0),
    X90_Y90_Z0(90, 90, 0),
    X90_Y180_Z0(90, 180, 0),
    X90_Y270_Z0(90, 270, 0),
    
    X180_Y0_Z0(180, 0, 0),
    X180_Y90_Z0(180, 90, 0),
    X180_Y180_Z0(180, 180, 0),
    X180_Y270_Z0(180, 270, 0),
    
    X270_Y0_Z0(270, 0, 0),
    X270_Y90_Z0(270, 90, 0),
    X270_Y180_Z0(270, 180, 0),
    X270_Y270_Z0(270, 270, 0),

    X0_Y0_Z90(0, 0, 90),
    X0_Y90_Z90(0, 90, 90),
    X0_Y180_Z90(0, 180, 90),
    X0_Y270_Z90(0, 270, 90),
    
    X90_Y0_Z90(90, 0, 90),
    X90_Y90_Z90(90, 90, 90),
    X90_Y180_Z90(90, 180, 90),
    X90_Y270_Z90(90, 270, 90),
    
    X180_Y0_Z90(180, 0, 90),
    X180_Y90_Z90(180, 90, 90),
    X180_Y180_Z90(180, 180, 90),
    X180_Y270_Z90(180, 270, 90),
    
    X270_Y0_Z90(270, 0, 90),
    X270_Y90_Z90(270, 90, 90),
    X270_Y180_Z90(270, 180, 90),
    X270_Y270_Z90(270, 270, 90),

    X0_Y0_Z180(0, 0, 180),
    X0_Y90_Z180(0, 90, 180),
    X0_Y180_Z180(0, 180, 180),
    X0_Y270_Z180(0, 270, 180),
    
    X90_Y0_Z180(90, 0, 180),
    X90_Y90_Z180(90, 90, 180),
    X90_Y180_Z180(90, 180, 180),
    X90_Y270_Z180(90, 270, 180),
    
    X180_Y0_Z180(180, 0, 180),
    X180_Y90_Z180(180, 90, 180),
    X180_Y180_Z180(180, 180, 180),
    X180_Y270_Z180(180, 270, 180),
    
    X270_Y0_Z180(270, 0, 180),
    X270_Y90_Z180(270, 90, 180),
    X270_Y180_Z180(270, 180, 180),
    X270_Y270_Z180(270, 270, 180),

    X0_Y0_Z270(0, 0, 270),
    X0_Y90_Z270(0, 90, 270),
    X0_Y180_Z270(0, 180, 270),
    X0_Y270_Z270(0, 270, 270),
    
    X90_Y0_Z270(90, 0, 270),
    X90_Y90_Z270(90, 90, 270),
    X90_Y180_Z270(90, 180, 270),
    X90_Y270_Z270(90, 270, 270),
    
    X180_Y0_Z270(180, 0, 270),
    X180_Y90_Z270(180, 90, 270),
    X180_Y180_Z270(180, 180, 270),
    X180_Y270_Z270(180, 270, 270),
    
    X270_Y0_Z270(270, 0, 270),
    X270_Y90_Z270(270, 90, 270),
    X270_Y180_Z270(270, 180, 270),
    X270_Y270_Z270(270, 270, 270);

    private static final Map<Integer, ModelRotationXYZ> MAP_ROTATIONS = Maps.<Integer, ModelRotationXYZ>newHashMap();
    private final int combinedXY;
    private final Matrix4f matrix4d;
    private final int quartersX;
    private final int quartersY;
    private final int quartersZ;

    private static int combineXYZ(int x, int y, int z)
    {
        return x * 360 + y + z * 360;
    }

    private ModelRotationXYZ(int x, int y, int z)
    {
        this.combinedXY = combineXYZ(x, y, z);
        this.matrix4d = new Matrix4f();
        this.matrix4d.setIdentity();
        
        Matrix4f.rotate((float)(-x) * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4d, matrix4d);
        this.quartersX = MathHelper.abs(x / 90);
        
        Matrix4f.rotate((float)(-y) * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4d, matrix4d);
        this.quartersY = MathHelper.abs(y / 90);
        
        Matrix4f.rotate((float)(-z) * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), matrix4d, matrix4d);
        this.quartersZ = MathHelper.abs(z / 90);
        
    }

    public Matrix4f getMatrix4d()
    {
        return this.matrix4d;
    }

    public EnumFacing rotateFace(EnumFacing facing)
    {
        EnumFacing enumfacing = facing;

        for (int i = 0; i < this.quartersX; ++i)
        {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
        }

        if (enumfacing.getAxis() != EnumFacing.Axis.Y)
        {
            for (int j = 0; j < this.quartersY; ++j)
            {
                enumfacing = enumfacing.rotateAround(EnumFacing.Axis.Y);
            }
        }

        for (int i = 0; i < this.quartersZ; ++i)
        {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.Z);
        }

        return enumfacing;
    }

    public int rotateVertex(EnumFacing facing, int vertexIndex)
    {
        int i = vertexIndex;

        if (facing.getAxis() == EnumFacing.Axis.X)
        {
            i = (vertexIndex + this.quartersX) % 4;
        }

        EnumFacing enumfacing = facing;

        for (int j = 0; j < this.quartersX; ++j)
        {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
        }

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            i = (i + this.quartersY) % 4;
        }

        if (facing.getAxis() == EnumFacing.Axis.Z)
        {
            i = (vertexIndex + this.quartersZ) % 4;
        }

        EnumFacing enumfacingZ = facing;

        for (int j = 0; j < this.quartersZ; ++j)
        {
            enumfacingZ = enumfacingZ.rotateAround(EnumFacing.Axis.Z);
        }

        return i;
    }

    public static ModelRotationXYZ getModelRotation(int x, int y, int z)
    {
        return (ModelRotationXYZ) MAP_ROTATIONS.get(Integer.valueOf(combineXYZ(MathHelper.normalizeAngle(x, 360), MathHelper.normalizeAngle(y, 360), MathHelper.normalizeAngle(z, 360))));
    }

    static
    {
        for (ModelRotationXYZ modelrotation : values())
        {
            MAP_ROTATIONS.put(Integer.valueOf(modelrotation.combinedXY), modelrotation);
        }
    }

    @Override
    public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part)
    {
        return ForgeHooksClient.applyTransform(getMatrix(), part);
    }
    
    public javax.vecmath.Matrix4f getMatrix()
    {
    	javax.vecmath.Matrix4f ret = new javax.vecmath.Matrix4f(TRSRTransformation.toVecmath(this.getMatrix4d())), tmp = new javax.vecmath.Matrix4f();
        tmp.setIdentity();
        tmp.m03 = tmp.m13 = tmp.m23 = .5f;
        ret.mul(tmp, ret);
        tmp.invert();
        ret.mul(tmp);
        return ret;
    }
    
    public EnumFacing rotate(EnumFacing facing)
    {
    	return rotateFace(facing);
    }
    
    public int rotate(EnumFacing facing, int vertexIndex)
    {
    	return rotateVertex(facing, vertexIndex);
    }
}