package com.vorono4ka.utilities;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.NotNull;

public class BlockPosHelper {
    public static Vec3f translate(BlockPattern.Result result, float offsetLeft, float offsetDown, float offsetForwards) {
        return translate(
            createVec3f(result.getFrontTopLeft()),
            result.getForwards(),
            result.getUp(),
            offsetLeft,
            offsetDown,
            offsetForwards
        );
    }

    public static Vec3f translate(Vec3f pos, Direction forwards, Direction up, float offsetLeft, float offsetDown, float offsetForwards) {
        if (forwards == up || forwards == up.getOpposite()) {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
        Vec3f vec3i = new Vec3f(forwards.getOffsetX(), forwards.getOffsetY(), forwards.getOffsetZ());
        Vec3f vec3i2 = new Vec3f(up.getOffsetX(), up.getOffsetY(), up.getOffsetZ());
        Vec3f vec3i3 = vec3i.copy();
        vec3i3.cross(vec3i2);

        float xOffset = vec3i2.getX() * -offsetDown + vec3i3.getX() * offsetLeft + vec3i.getX() * offsetForwards;
        float yOffset = vec3i2.getY() * -offsetDown + vec3i3.getY() * offsetLeft + vec3i.getY() * offsetForwards;
        float zOffset = vec3i2.getZ() * -offsetDown + vec3i3.getZ() * offsetLeft + vec3i.getZ() * offsetForwards;
        pos.add(xOffset, yOffset, zOffset);
        return pos;
    }

    @NotNull
    public static Vec3f createVec3f(BlockPos blockPos) {
        return new Vec3f(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}
