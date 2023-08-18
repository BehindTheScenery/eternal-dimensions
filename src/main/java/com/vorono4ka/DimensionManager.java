package com.vorono4ka;

import com.vorono4ka.config.ModConfig;
import com.vorono4ka.utilities.ArrayUtils;
import com.vorono4ka.utilities.BlockPosHelper;
import com.vorono4ka.utilities.ParticleHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;

public class DimensionManager {
    private static final int FAILED_PARTICLE_COLOR = 0x9714cf;
    private static final boolean FORCE_PARTICLES = true;
    private static final float FAILED_PARTICLE_SPEED = 0.01f;
    private static final int FAILED_PARTICLE_COUNT = 1000;
    private static final Vec3f END_PORTAL_FAILED_PARTICLE_DELTA = new Vec3f(1, 0.25f, 1);

    public static boolean handleEndPortalBuilding(ServerWorld world, BlockPattern.Result result) {
        boolean dimensionBlocked = isDimensionBlocked(World.END.getValue());
        if (dimensionBlocked) {
            destroyPortal(world, result);
            clearPortalFrames(world, result);
            createFailedParticles(
                world,
                BlockPosHelper.createVec3f(result.translate(
                    result.getWidth() / 2,
                    result.getHeight() / 2,
                    result.getDepth() / 2
                ).getBlockPos()),
                END_PORTAL_FAILED_PARTICLE_DELTA
            );
        }

        return dimensionBlocked;
    }

    public static boolean handlePortalBuilding(Identifier dimension) {
        return isDimensionBlocked(dimension);
    }

    public static boolean handlePortalCollision(Identifier dimension, PlayerEntity player) {
        boolean dimensionBlocked = isDimensionBlocked(dimension);
        if (dimensionBlocked && player != null) {
            notifyDimensionBlocked(player);
        }

        return dimensionBlocked;
    }


    public static void notifyPortalBuildingBlocked(PlayerEntity player) {
        player.sendMessage(Text.translatable("custom_text.no_more_dimensions.portal_building_blocked"), true);
    }

    public static void notifyDimensionBlocked(PlayerEntity player) {
        player.sendMessage(Text.translatable("custom_text.no_more_dimensions.dimension_blocked"), true);
    }

    private static boolean isDimensionBlocked(Identifier dimension) {
        return ArrayUtils.contains(ModConfig.blockedDimensions, dimension.toString());
    }

    private static void createFailedParticles(ServerWorld world, Vec3f portalMiddlePosition, Vec3f delta) {
        ParticleHelper.spawnDustParticle(
            world,
            FAILED_PARTICLE_COLOR,
            FORCE_PARTICLES,
            portalMiddlePosition,
            FAILED_PARTICLE_COUNT,
            delta,
            FAILED_PARTICLE_SPEED
        );
    }

    private static void clearPortalFrames(ServerWorld world, BlockPattern.Result result) {
        for (int i = 0; i < result.getWidth(); ++i) {
            for (int j = 0; j < result.getHeight(); ++j) {
                for (int k = 0; k < result.getDepth(); ++k) {
                    CachedBlockPosition cachedBlockPosition = result.translate(i, j, k);
                    BlockPos blockPos = cachedBlockPosition.getBlockPos();

                    BlockState blockState = cachedBlockPosition.getBlockState();
                    if (!blockState.isOf(Blocks.END_PORTAL_FRAME)) continue;

                    world.setBlockState(blockPos, blockState.with(Properties.EYE, false));
                }
            }
        }
    }

    private static void destroyPortal(ServerWorld world, BlockPattern.Result result) {
        BlockPos blockPos2 = result.getFrontTopLeft().add(-3, 0, -3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                world.removeBlock(blockPos2.add(i, 0, j), false);
            }
        }
    }
}
