package com.vorono4ka.config;

import com.vorono4ka.NoMoreDimensionsMod;

import java.util.Arrays;

public class ModConfig {
    private static final ModConfigProvider provider = new ModConfigProvider();
    private static SimpleConfig config;

    public static String[] blockedDimensions;

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
    }

    private static void assignValues() {
        blockedDimensions = Arrays.stream(config.getOrDefault("blocked_dimensions", "").split(",")).map(String::trim).toArray(String[]::new);
    }
}
