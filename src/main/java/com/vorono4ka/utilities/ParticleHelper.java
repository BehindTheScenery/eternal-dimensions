package com.vorono4ka.utilities;

import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class ParticleHelper {
    public static void spawnDustParticle(ServerWorld world, int color, boolean force, Vec3f particlePosition, int particleCount, Vec3f delta, float particleSpeed) {
        spawnParticle(world, new DustParticleEffect(new Vec3f(Vec3d.unpackRgb(color)), 1.0f), force, particlePosition, particleCount, delta, particleSpeed);
    }

    public static void spawnParticle(ServerWorld world, ParticleEffect effect, boolean force, Vec3f particlePosition, int particleCount, Vec3f delta, float particleSpeed) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            world.spawnParticles(
                player,
                effect,
                force,
                particlePosition.getX(),
                particlePosition.getY(),
                particlePosition.getZ(),
                particleCount,
                delta.getX(),
                delta.getY(),
                delta.getZ(),
                particleSpeed
            );
        }
    }
}
