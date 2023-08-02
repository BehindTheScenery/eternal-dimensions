package com.vorono4ka;

import com.vorono4ka.config.ModConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoMoreDimensionsMod implements ModInitializer {
	public static final String MOD_ID = "no-more-dimensions";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initialized!");

		ModConfig.registerConfig();
		CommandManager.registerCommands();
	}
}