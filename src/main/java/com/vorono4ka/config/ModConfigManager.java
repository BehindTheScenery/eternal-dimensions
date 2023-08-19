package com.vorono4ka.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class ModConfigManager {
    private static ConfigHolder<NoMoreDimensionsConfig> holder;

    public static void registerAutoConfig() {
        AutoConfig.register(NoMoreDimensionsConfig.class, Toml4jConfigSerializer::new);
        holder = AutoConfig.getConfigHolder(NoMoreDimensionsConfig.class);
    }

    public static NoMoreDimensionsConfig getConfig() {
        if (holder == null) {
            registerAutoConfig();
        }

        return holder.getConfig();
    }
}
