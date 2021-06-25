package com.ulto.customblocks;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.fabricmc.api.ModInitializer;

public class CustomBlocksMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String MOD_ID = "custom_blocks";

	public void onInitialize() {
		LOGGER.info("Initializing Custom Blocks Mod");
		GenerateCustomElements.generate();
	}
}
