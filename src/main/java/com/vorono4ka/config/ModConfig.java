package com.vorono4ka.config;

import com.vorono4ka.NoMoreDimensionsMod;
import net.minecraft.text.Text;

import java.util.Arrays;

public class ModConfig {
    private static final ModConfigProvider provider = new ModConfigProvider();
    private static SimpleConfig config;

    public static String[] blockedDimensions;
    public static Text portalBuildingBlocked;
    public static Text dimensionBlocked;

    public static void registerConfig() {
        initDefaults();
        loadConfig();
    }

    public static void loadConfig() {
        config = SimpleConfig.of(NoMoreDimensionsMod.MOD_ID).provider(provider).request();

        assignValues();
    }

    private static void initDefaults() {
        provider.add("blocked_dimensions", "");
        provider.add("text.portal_building_blocked", "Этот портал тебе сейчас не доступен!");
        provider.add("text.dimension_blocked", "Этот мир тебе сейчас не доступен!");
    }

    private static void assignValues() {
        blockedDimensions = Arrays.stream(config.getOrDefault("blocked_dimensions", "").split(",")).map(String::trim).toArray(String[]::new);
        portalBuildingBlocked = Text.literal(config.getOrDefault("text.portal_building_blocked", "NO TEXT FOUND"));
        dimensionBlocked = Text.literal(config.getOrDefault("text.dimension_blocked", "NO TEXT FOUND"));
    }
}
