package com.asx.mdx.lib.client.model.animations;

import com.asx.mdx.lib.client.util.models.Model;
import com.asx.mdx.lib.client.util.models.Model.Part;
import com.asx.mdx.lib.util.MDXMath;

import net.minecraft.util.math.MathHelper;

public class AnimationHelper
{
    /**
     * Rotates the given parts to face a given target
     *
     * @param yaw - Yaw to face
     * @param pitch - Pitch to face
     * @param divisionFactor - Amount to divide the rotation angles by
     * @param parts - Parts to face the given target
     */
    public static void face(float yaw, float pitch, float divisionFactor, Part... parts)
    {
        float factor = divisionFactor * parts.length;
        
        yaw = yaw / (180.0F / (float) MDXMath.PI) / factor;
        pitch = pitch / (180.0F / (float) MDXMath.PI) / factor;

        for (Part part : parts)
        {
            part.rotateAngleY += yaw;
            part.rotateAngleX += pitch;
        }
    }

    /**
     * Rotates (X) this part back and forth.
     *
     * @param part - Box to animate
     * @param speed - How fast the animation runs
     * @param rotation - The distance at which the part will rotate;
     * @param invert - Invert the rotation
     * @param offset - Offset the timing of the animation
     * @param weight - Make the animation favor one direction more based on how fast the mob is moving
     * @param walk - Walked distance
     * @param walkAmount - Walk speed
     */
    public static void oscillate(Part part, float speed, float rotation, boolean invert, float offset, float weight, float walk, float walkAmount)
    {
        part.oscillate(speed, rotation, invert, offset, weight, walk, walkAmount);
    }

    /**
     * Rotates (Z) this part up and down.
     *
     * @param part - Box to animate
     * @param speed - How fast the animation runs
     * @param rotation - Distance at which the part will rotate
     * @param invert - Invert the rotation
     * @param offset - Offset the timing of the animation
     * @param weight - Make the animation favor one direction more based on how fast the mob is moving
     * @param flap - Flapped distance
     * @param flapAmount - Flap speed
     */
    public static void flap(Part part, float speed, float rotation, boolean invert, float offset, float weight, float flap, float flapAmount)
    {
        part.flap(speed, rotation, invert, offset, weight, flap, flapAmount);
    }

    /**
     * Rotates (Y) this part side to side.
     *
     * @param part - Box to animate
     * @param speed - How fast the animation runs
     * @param rotation - Distance at which the part will rotate;
     * @param invert - Invert the rotation
     * @param offset - Offset the timing of the animation
     * @param weight - Make the animation favor one direction more based on how fast the mob is moving
     * @param swing - Swing distance
     * @param swingAmount - Swing speed
     */
    public static void swing(Part part, float speed, float rotation, boolean invert, float offset, float weight, float swing, float swingAmount)
    {
        part.swing(speed, rotation, invert, offset, weight, swing, swingAmount);
    }

    /**
     * Moves (Y) this part up and down.
     *
     * @param part - Part to animate
     * @param speed - How fast the animation runs
     * @param rotation - Distance at which the part will move
     * @param bounce - Make the part bounce
     * @param distanceWalked - Walked distance
     * @param walkSpeed - Walk speed
     */
    public static void bob(Part part, float speed, float rotation, boolean bounce, float distanceWalked, float walkSpeed)
    {
        part.bob(speed, rotation, bounce, distanceWalked, walkSpeed);
    }

    /**
     * Returns a float that can be used to move parts.
     *
     * @param speed - How fast the animation runs
     * @param rotation - The distance at which the part will move
     * @param bounce - Make the part bounce
     * @param distanceWalked - Walked distance
     * @param walkSpeed - Walk speed.
     * 
     * @return the distance at which this part will move.
     */
    public static float movePart(float speed, float rotation, boolean bounce, float distanceWalked, float walkSpeed)
    {
        if (bounce)
        {
            return -MathHelper.abs((MathHelper.sin(distanceWalked * speed) * walkSpeed * rotation));
        }
        else
        {
            return MathHelper.sin(distanceWalked * speed) * walkSpeed * rotation - walkSpeed * rotation;
        }
    }

    /**
     * Swings (Y) the given model parts in a wire-like manner.
     *
     * @param parts - Parts to swing
     * @param speed - Speed to swing this at
     * @param rotation - Amount to rotate by
     * @param baseOffset - the base rotation offset
     * @param swing - the swing rotation
     * @param swingAmount - the swing amount
     */
    public static void wireSwing(Model<?> model, Part[] parts, float speed, float rotation, double baseOffset, float swing, float swingAmount)
    {
        float offset = calculateWireOffset(baseOffset, parts);

        for (int index = 0; index < parts.length; index++)
        {
            parts[index].rotateAngleY += calculateWireRotation(model, speed, rotation, swing, swingAmount, offset, index);
        }
    }

    /**
     * Waves (X) the given model parts in a wire-like manner.
     *
     * @param parts - Parts to wave
     * @param speed - Speed to wave at
     * @param rotation - Amount to rotate by
     * @param baseOffset - The base rotation offset
     * @param swing - Swing rotation
     * @param swingAmount - Swing amount
     */
    public static void wireWave(Model<?> model, Part[] parts, float speed, float rotation, double baseOffset, float swing, float swingAmount)
    {
        float offset = calculateWireOffset(baseOffset, parts);

        for (int index = 0; index < parts.length; index++)
        {
            parts[index].rotateAngleX += calculateWireRotation(model, speed, rotation, swing, swingAmount, offset, index);
        }
    }

    /**
     * Flaps (Z) the given model parts in a wire-like manner.
     *
     * @param parts - Parts to flap
     * @param speed - Speed to flap this at
     * @param rotation - Amount to rotate by
     * @param baseOffset - Root rotation offset
     * @param swing - Swing rotation
     * @param swingAmount - Swing amount
     */
    public static void wireFlap(Model<?> model, Part[] parts, float speed, float rotation, double baseOffset, float swing, float swingAmount)
    {
        float offset = calculateWireOffset(baseOffset, parts);

        for (int index = 0; index < parts.length; index++)
        {
            parts[index].rotateAngleZ += calculateWireRotation(model, speed, rotation, swing, swingAmount, offset, index);
        }
    }

    private static float calculateWireRotation(Model<?> model, float speed, float rotation, float swing, float swingAmount, float offset, int partIndex)
    {
        return MathHelper.cos(swing * (speed * model.getMovementScale()) + offset * partIndex) * swingAmount * (rotation * model.getMovementScale());
    }

    public static float calculateWireOffset(double baseOffset, Part... parts)
    {
        return (float) ((baseOffset * MDXMath.PI) / (2 * parts.length));
    }
}
