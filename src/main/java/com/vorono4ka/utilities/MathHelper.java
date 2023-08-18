package com.vorono4ka.utilities;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

public class MathHelper {
    public static final Vec3f BLOCK_HALF = new Vec3f(0.5f, 0.5f, 0.5f);

    public static Vec3f translate(Vec3f pos, Direction forwards, Direction up, float offsetLeft, float offsetUp, float offsetForwards) {
        if (forwards == up || forwards == up.getOpposite()) {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
        Vec3f vec3i = new Vec3f(forwards.getOffsetX(), forwards.getOffsetY(), forwards.getOffsetZ());
        Vec3f vec3i2 = new Vec3f(up.getOffsetX(), up.getOffsetY(), up.getOffsetZ());
        Vec3f vec3i3 = vec3i.copy();
        vec3i3.cross(vec3i2);

        float xOffset = vec3i2.getX() * offsetUp + vec3i3.getX() * offsetLeft + vec3i.getX() * offsetForwards;
        float yOffset = vec3i2.getY() * offsetUp + vec3i3.getY() * offsetLeft + vec3i.getY() * offsetForwards;
        float zOffset = vec3i2.getZ() * offsetUp + vec3i3.getZ() * offsetLeft + vec3i.getZ() * offsetForwards;
        Vec3f result = pos.copy();
        result.add(xOffset, yOffset, zOffset);
        return result;
    }

    public static Vec3f add(Vec3f pos, Vec3f offset) {
        Vec3f result = pos.copy();
        result.add(offset);
        return result;
    }

    public static Vec3f add(Vec3f pos, Vec3i offset) {
        Vec3f result = pos.copy();
        result.add(offset.getX(), offset.getY(), offset.getZ());
        return result;
    }

    @NotNull
    public static Vec3f createVec3f(BlockPos blockPos) {
        return new Vec3f(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}
