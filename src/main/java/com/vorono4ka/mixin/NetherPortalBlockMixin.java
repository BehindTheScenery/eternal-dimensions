package com.vorono4ka.mixin;

import com.vorono4ka.DimensionManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {
	@Inject(at = @At(value = "HEAD"), method = "onEntityCollision", cancellable = true)
	private void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		// Allows to exit nether world
		if (world.getRegistryKey() == World.NETHER) return;

		PlayerEntity player = null;
		if (entity.isPlayer()) {
			player = (PlayerEntity) entity;
		}

		// Prevents nausea
		if (DimensionManager.handlePortalCollision(World.NETHER.getValue(), player)) {
			ci.cancel();
		}
	}
}