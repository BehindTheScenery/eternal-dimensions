package com.vorono4ka.mixin.edenring;

import com.vorono4ka.DimensionManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.EdenRing;
import paulevs.edenring.world.EdenPortal;

@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelItemMixin {
    @Inject(
        method = {"useOnBlock"},
        at = {@At("HEAD")},
        cancellable = true
    )
    private void eden_portalOpen(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getSide() == Direction.UP) {
            BlockPos pos = context.getBlockPos();
            World level = context.getWorld();
            RegistryKey<World> dimension = level.getRegistryKey();
            if (this.correctDimension(dimension) && level.getBlockState(pos).isOf(Blocks.GOLD_BLOCK) && EdenPortal.checkNewPortal(level, pos.up())) {
                if (dimension.equals(World.OVERWORLD) && DimensionManager.handlePortalBuilding(EdenRing.EDEN_RING_KEY.getValue())) {
                    cir.setReturnValue(ActionResult.FAIL);
                    cir.cancel();

                    PlayerEntity player = context.getPlayer();
                    if (player == null) return;

                    DimensionManager.notifyPortalBuildingBlocked(player);
                    return;
                }

                cir.setReturnValue(ActionResult.success(level.isClient()));
                EdenPortal.buildPortal(level, pos.up());
            }
        }
    }

    @Unique
    private boolean correctDimension(RegistryKey<World> dimension) {
        return dimension.equals(World.OVERWORLD) || dimension.equals(EdenRing.EDEN_RING_KEY);
    }
}
