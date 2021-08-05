package com.ulto.customblocks;

import com.google.common.base.Functions;
import com.ulto.customblocks.resource.CustomResourcePackFinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fmllegacy.packs.ResourcePackLoader;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("custom_blocks")
public class CustomBlocksMod {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger("Custom Blocks Mod");
    public static final String MOD_ID = "custom_blocks";
    public static final File customBlocksConfig = new File(FMLPaths.CONFIGDIR.get().toFile(), File.separator + MOD_ID);

    public static List<Block> blocks = new ArrayList<>();
    public static List<Item> blockItems = new ArrayList<>(), items = new ArrayList<>();

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
        event.getServer().getPackRepository().setSelected(Collections.singleton("Custom Block Resources"));
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            blockRegistryEvent.getRegistry().registerAll(blocks.toArray(new Block[]{}));
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            itemRegistryEvent.getRegistry().registerAll(items.toArray(new Item[]{}));
            itemRegistryEvent.getRegistry().registerAll(blockItems.toArray(new Item[]{}));
        }
    }
}
