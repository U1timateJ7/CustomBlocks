package com.ulto.customblocks;

import com.ulto.customblocks.resource.CustomResourcePackProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CustomBlocksMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Custom Blocks Mod");

	public static final String MOD_ID = "custom_blocks";
	public static final File customBlocksConfig = new File(FabricLoader.getInstance().getConfigDir().toFile(), File.separator + MOD_ID);

	public void onInitialize() {
		LOGGER.info("Initializing Custom Blocks Mod");
		File assets = new File(customBlocksConfig, File.separator + "assets");
		File packMcmeta = new File(customBlocksConfig, File.separator + "pack.mcmeta");
		try {
			if (!CustomResourcePackProvider.customBlocksPath.exists()) CustomResourcePackProvider.customBlocksPath.mkdirs();
			Files.move(assets.toPath(), CustomResourceCreator.assets.toPath());
			Files.move(packMcmeta.toPath(), new File(CustomResourcePackProvider.customBlocksPath, File.separator + "pack.mcmeta").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		GenerateCustomElements.generate();
	}
}
