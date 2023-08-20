package com.vorono4ka;

import com.vorono4ka.config.ModConfigManager;
import com.vorono4ka.config.NoMoreDimensionsConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoMoreDimensions implements ModInitializer {
	public static final String MOD_ID = "no_more_dimensions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static NoMoreDimensionsConfig config;

	@Override
	public void onInitialize() {
		LOGGER.info("Initialized!");

		NoMoreDimensions.config = ModConfigManager.getConfig();
		CommandManager.registerCommands();
	}
}