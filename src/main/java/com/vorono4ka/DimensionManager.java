package com.vorono4ka;

import com.vorono4ka.config.ModConfig;
import com.vorono4ka.utilities.ArrayUtils;
import com.vorono4ka.utilities.MathHelper;
import com.vorono4ka.utilities.ParticleHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class DimensionManager {
    private static final int FAILED_PARTICLE_COLOR = 0x9714cf;
    public static final DustParticleEffect END_PORTAL_BUILDING_FAILED_PARTICLE_EFFECT = new DustParticleEffect(new Vec3f(Vec3d.unpackRgb(FAILED_PARTICLE_COLOR)), 1.0f);
    private static final boolean FORCE_PARTICLES = true;
    private static final float FAILED_PARTICLE_SPEED = 0.01f;
    private static final int FAILED_PARTICLE_DENSITY = 100;
    public static final SoundEvent PORTAL_BUILDING_BLOCKED_SOUND = SoundEvents.ENTITY_GENERIC_EXPLODE;

    public static boolean handleEndPortalBuilding(ServerWorld world, BlockPattern.Result result, PlayerEntity player) {
        Vec3f portalMiddlePosition = MathHelper.add(MathHelper.createVec3f(result.translate(
            result.getWidth() / 2,
            result.getHeight() / 2,
            result.getDepth() / 2
        ).getBlockPos()), MathHelper.BLOCK_HALF);

        boolean dimensionBlocked = handlePortalBuilding(world, World.END.getValue(), portalMiddlePosition);
        if (dimensionBlocked) {
            destroyEndPortal(world, result);
            clearEndPortalFrames(world, result);
            createFailedParticles(
                world,
                END_PORTAL_BUILDING_FAILED_PARTICLE_EFFECT,
                MathHelper.translate(MathHelper.createVec3f(result.getFrontTopLeft()), Direction.WEST, Direction.UP, 0.5f, 0.5f, 0.5f),
                Direction.NORTH,
                Direction.WEST,
                result.getHeight() - 2,
                result.getWidth() - 2,
                result.getDepth(),
                FAILED_PARTICLE_DENSITY,
                new Vec3f(1, 0.25f, 1)
            );

            if (player != null) {
                DimensionManager.notifyPortalBuildingBlocked(player);
            }
        }

        return dimensionBlocked;
    }

    public static void handleNetherPortalBuilding(ServerWorld world, BlockPos lowerCorner, int height, Direction negativeDir, int width) {
        Vec3f vec3f = MathHelper.createVec3f(lowerCorner);

        Vec3f portalMiddlePosition = MathHelper.translate(vec3f, negativeDir, Direction.UP, -0.5f, height / 2f, width / 2f);

        boolean dimensionBlocked = handlePortalBuilding(world, World.NETHER.getValue(), portalMiddlePosition);
        if (dimensionBlocked) {
            destroyNetherPortal(world, lowerCorner, height, negativeDir, width);

            float offsetForwards = -0.5f;
            if (negativeDir == Direction.WEST) {
                offsetForwards *= -1;
            }

            createFailedParticles(
                world,
                ParticleTypes.FLAME,
                MathHelper.translate(vec3f, negativeDir.getOpposite(), Direction.UP, 0.5f, 0.5f, offsetForwards),
                Direction.UP,
                negativeDir,
                height,
                width,
                1,
                FAILED_PARTICLE_DENSITY,
                MathHelper.translate(new Vec3f(), negativeDir.getOpposite(), Direction.UP, 0.25f, 0.5f, 0.5f)
            );

            for (PlayerEntity player : world.getPlayers()) {
                DimensionManager.notifyPortalBuildingBlocked(player);
            }
        }

    }

    public static boolean handlePortalBuilding(ServerWorld world, Identifier dimension, Vec3f soundPosition) {
        boolean dimensionBlocked = isDimensionBlocked(dimension);
        if (dimensionBlocked) {
            playFailedSound(world, soundPosition);
            giveFailedEffect(world);
        }
        return dimensionBlocked;
    }

    private static void giveFailedEffect(ServerWorld world) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 1, false, false), player);
        }
    }

    private static void playFailedSound(ServerWorld world, Vec3f soundPosition) {
        world.playSound(null, soundPosition.getX(), soundPosition.getY(), soundPosition.getZ(), PORTAL_BUILDING_BLOCKED_SOUND, SoundCategory.MASTER, 1, 0, world.getRandom().nextLong());
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

    private static void createFailedParticles(ServerWorld world, ParticleEffect particleEffect, Vec3f cornerVector, Direction direction, Direction direction1, int height, int width, int depth, int particleCount, Vec3f delta) {
        for (int i = 0; i < height; i++) {
            Vec3f modifiedVector = MathHelper.add(cornerVector, direction.getVector().multiply(i));
            for (int j = 0; j < width; j++) {
                ParticleHelper.spawnParticle(
                    world,
                    particleEffect,
                    FORCE_PARTICLES,
                    MathHelper.add(modifiedVector, direction1.getVector().multiply(j)),
                    particleCount,
                    delta,
                    FAILED_PARTICLE_SPEED
                );
            }
        }
    }

    private static void clearEndPortalFrames(ServerWorld world, BlockPattern.Result result) {
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

    private static void destroyNetherPortal(WorldAccess world, BlockPos corner, int height, Direction negativeDir, int width) {
        BlockPos.iterate(corner, corner.offset(Direction.UP, height - 1).offset(negativeDir, width - 1)).forEach(blockPos -> world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE));
    }

    private static void destroyEndPortal(WorldAccess world, BlockPattern.Result result) {
        int height = 3, width = 3;
        BlockPos corner = result.getFrontTopLeft();
        BlockPos.iterate(corner, corner.offset(Direction.NORTH, height).offset(Direction.WEST, width)).forEach(blockPos -> world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE));
    }
}
