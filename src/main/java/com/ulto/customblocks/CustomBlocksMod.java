package com.ulto.customblocks;

import com.ulto.customblocks.client.CustomBlocksClient;
import com.ulto.customblocks.resource.CustomResourcePackFinder;
import net.minecraft.core.Registry;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Mod("custom_blocks")
public class CustomBlocksMod {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger("Custom Blocks Mod");
    public static final String MOD_ID = "custom_blocks";
    public static final File customBlocksConfig = new File(FMLPaths.CONFIGDIR.get().toFile(), File.separator + MOD_ID);

    static {
        customBlocksConfig.mkdirs();
        File assets = new File(customBlocksConfig, File.separator + "assets");
        File packMcmeta = new File(customBlocksConfig, File.separator + "pack.mcmeta");
        try {
            if (!CustomResourcePackFinder.customBlocksPath.exists()) CustomResourcePackFinder.customBlocksPath.mkdirs();
            if (assets.exists()) Files.move(assets.toPath(), CustomResourceCreator.assets.toPath());
            if (packMcmeta.exists()) Files.move(packMcmeta.toPath(), new File(CustomResourcePackFinder.customBlocksPath, File.separator + "pack.mcmeta").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Registry.BLOCK.unfreeze();
        Registry.ITEM.unfreeze();
        Registry.ENTITY_TYPE.unfreeze();
        Registry.PAINTING_VARIANT.unfreeze();
        GenerateCustomElements.generate();
        Registry.BLOCK.freeze();
        Registry.ITEM.freeze();
        Registry.ENTITY_TYPE.freeze();
        Registry.PAINTING_VARIANT.freeze();
    }

    public CustomBlocksMod() {
        // Register the construct method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::construct);
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the setupClient method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        // Register the setupServer method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupServer);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the addPackFinder method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addPackFinder);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void construct(final FMLConstructModEvent event) {

    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    private void setupClient(final FMLClientSetupEvent event) {
        CustomBlocksClient.copyOldFiles();
        GenerateCustomElements.generateClient();
    }

    private void setupServer(final FMLDedicatedServerSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    private void addPackFinder(final AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) event.addRepositorySource(CustomResourcePackFinder.DATA);
        else event.addRepositorySource(CustomResourcePackFinder.RESOUCE);
    }
}
