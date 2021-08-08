package com.ulto.customblocks;

import com.ulto.customblocks.resource.CustomResourcePackFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("custom_blocks")
public class CustomBlocksMod {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger("Custom Blocks Mod");
    public static final String MOD_ID = "custom_blocks";
    public static final File customBlocksConfig = new File(FMLPaths.CONFIGDIR.get().toFile(), File.separator + MOD_ID);

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

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist == Dist.CLIENT) {

            Minecraft.getInstance().getResourcePackRepository().addPackFinder(CustomResourcePackFinder.RESOUCE);
        }
    }

    private void construct(final FMLConstructModEvent event) {
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
        GenerateCustomElements.generate();
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
    }

    private void setupClient(final FMLClientSetupEvent event) {
        GenerateCustomElements.generateClient();
    }

    private void setupServer(final FMLDedicatedServerSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    @SubscribeEvent
    public void onServerStart (final FMLServerAboutToStartEvent event) {
        event.getServer().getPackRepository().addPackFinder(CustomResourcePackFinder.DATA);
        LOGGER.info("Added custom data pack finder to server pack repository.");
    }
}
