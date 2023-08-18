package com.vorono4ka.mixin.common;

import com.vorono4ka.DimensionManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.AreaHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaHelper.class)
public abstract class AreaHelperMixin {
	@Inject(at = @At(value = "RETURN"), method = "createPortal")
	private void onCreatePortal(CallbackInfo ci) {
		AreaHelper areaHelper = (AreaHelper) (Object) this;

        DimensionManager.handleNetherPortalBuilding(((ServerWorld) areaHelper.world), areaHelper.lowerCorner, areaHelper.height, areaHelper.negativeDir, areaHelper.width);
    }
}