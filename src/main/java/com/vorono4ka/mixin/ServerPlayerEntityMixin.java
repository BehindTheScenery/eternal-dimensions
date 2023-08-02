package com.vorono4ka.mixin;

import com.vorono4ka.DimensionManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Inject(at = @At(value = "HEAD"), method = "moveToWorld", cancellable = true)
	private void onMoveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
		if (destination.getRegistryKey() == World.OVERWORLD) return;

		ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

		if (DimensionManager.handlePortalCollision(destination.getRegistryKey().getValue(), player)) {
			cir.setReturnValue(null);
			cir.cancel();
		}
	}
}