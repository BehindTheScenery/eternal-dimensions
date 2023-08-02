package com.vorono4ka.mixin;

import com.vorono4ka.DimensionManager;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/AreaHelper;createPortal()V"), method = "onBlockAdded", cancellable = true)
	private void onCreatePortal(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) {
		if (DimensionManager.handlePortalBuilding(World.NETHER.getValue())) {
			ci.cancel();

			if (!state.canPlaceAt(world, pos)) {
				world.removeBlock(pos, false);
			}

			for (PlayerEntity player : world.getPlayers()) {
				DimensionManager.sendPortalBuildingBlockedMessage(player);
			}
		}
	}
}