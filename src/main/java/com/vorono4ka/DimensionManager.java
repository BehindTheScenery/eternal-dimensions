package com.vorono4ka;

import com.vorono4ka.config.ModConfig;
import com.vorono4ka.utilities.ArrayUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class DimensionManager {
    public static boolean handlePortalBuilding(Identifier dimension) {
        return isDimensionBlocked(dimension);
    }

    public static boolean handlePortalCollision(Identifier dimension, PlayerEntity player) {
        boolean dimensionBlocked = isDimensionBlocked(dimension);
        if (dimensionBlocked && player != null) {
            notifyDimensionBlocked(player);
        }

        return dimensionBlocked;
    }

    public static boolean isDimensionBlocked(Identifier dimension) {
        return ArrayUtils.contains(ModConfig.blockedDimensions, dimension.toString());
    }

    public static void notifyPortalBuildingBlocked(PlayerEntity player) {
        player.sendMessage(ModConfig.portalBuildingBlocked, true);
    }

    public static void notifyDimensionBlocked(PlayerEntity player) {
        player.sendMessage(ModConfig.dimensionBlocked, true);
    }
}
