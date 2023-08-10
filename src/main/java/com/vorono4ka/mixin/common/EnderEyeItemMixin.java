package com.vorono4ka.mixin.common;

import com.vorono4ka.DimensionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;pushEntitiesUpBeforeBlockChange(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), method = "useOnBlock", cancellable = true)
	private void onUseOnPortalFrameBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		if (DimensionManager.handlePortalBuilding(World.END.getValue())) {
			cir.setReturnValue(ActionResult.FAIL);
			cir.cancel();

			PlayerEntity player = context.getPlayer();
			if (player == null) return;

			DimensionManager.notifyPortalBuildingBlocked(player);
		}
	}
}