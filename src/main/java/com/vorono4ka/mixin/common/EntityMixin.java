package com.vorono4ka.mixin.common;

import com.vorono4ka.DimensionManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(at = @At(value = "HEAD"), method = "moveToWorld", cancellable = true)
	private void onMoveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
		if (destination.getRegistryKey() == World.OVERWORLD) return;

		if (DimensionManager.handlePortalCollision(destination.getRegistryKey().getValue(), null)) {
			cir.setReturnValue(null);
			cir.cancel();
		}
	}
}