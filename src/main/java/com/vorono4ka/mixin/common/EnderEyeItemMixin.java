package com.vorono4ka.mixin.common;

import com.vorono4ka.DimensionManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin {
	@Inject(at = @At(value = "HEAD"), method = "useOnBlock", cancellable = true)
	private void onUseOnPortalFrameBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		cir.cancel();

		BlockPos blockPos;
		World world = context.getWorld();
		BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
		if (!blockState.isOf(Blocks.END_PORTAL_FRAME) || blockState.get(EndPortalFrameBlock.EYE)) {
			cir.setReturnValue(ActionResult.PASS);
			return;
		}

		if (world.isClient) {
			cir.setReturnValue(ActionResult.SUCCESS);
			return;
		}

		BlockState blockState2 = blockState.with(EndPortalFrameBlock.EYE, true);
		Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, world, blockPos);
		world.setBlockState(blockPos, blockState2, Block.NOTIFY_LISTENERS);
		world.updateComparators(blockPos, Blocks.END_PORTAL_FRAME);
		context.getStack().decrement(1);
		world.syncWorldEvent(WorldEvents.END_PORTAL_FRAME_FILLED, blockPos, 0);
		BlockPattern.Result result = EndPortalFrameBlock.getCompletedFramePattern().searchAround(world, blockPos);
		if (result != null) {
			BlockPos blockPos2 = result.getFrontTopLeft().add(-3, 0, -3);
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					world.setBlockState(blockPos2.add(i, 0, j), Blocks.END_PORTAL.getDefaultState(), Block.NOTIFY_LISTENERS);
				}
			}
			world.syncGlobalEvent(WorldEvents.END_PORTAL_OPENED, blockPos2.add(1, 0, 1), 0);

			if (DimensionManager.handleEndPortalBuilding((ServerWorld) world, result, context.getPlayer())) {
				cir.setReturnValue(ActionResult.FAIL);
				return;
			}
		}

		cir.setReturnValue(ActionResult.CONSUME);
	}
}