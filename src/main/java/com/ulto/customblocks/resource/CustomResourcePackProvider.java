package com.ulto.customblocks.resource;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomResourcePackProvider implements ResourcePackProvider {
    public static final File customBlocksPath = new File(FabricLoader.getInstance().getConfigDir().toFile().getAbsolutePath(), File.separator + "custom_blocks");
    private final Type type;
    
    public CustomResourcePackProvider(Type type) {
        this.type = type;
    }
    
    @Override
    public void register (Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        File candidate = type.getDirectory();
        final String packName = candidate.getName();
        type.getLogger().info("Loading pack {} from {}.", packName, candidate.getAbsolutePath());
        final Supplier<ResourcePack> packSupplier = candidate.isDirectory() ? () -> new DirectoryResourcePack(candidate) : () -> new ZipResourcePack(candidate);
        final ResourcePackProfile profile = ResourcePackProfile.of(packName, true, packSupplier, factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.field_25347);
        if (profile != null) {
            consumer.accept(profile);
            type.getLogger().info("Loaded pack {}.", packName);
        }
        else {
            type.getLogger().error("Failed to build pack profile {} from {}.", packName, candidate.getAbsolutePath());
        }
    }
    
    public enum Type {
        DATA("Data Pack", "custom_blocks"),
        RESOURCES("Resource Pack", "custom_blocks");
        
        final String displayName;
        final String path;
        final Logger logger;
        
        Type(String name, String path) {
            displayName = name;
            this.path = path;
            logger = LogManager.getLogger("Custom Blocks Mod");
        }
        
        public Logger getLogger () {
            return this.logger;
        }
        
        public File getDirectory () {
            final File directory = new File(FabricLoader.getInstance().getConfigDir().toFile().getAbsolutePath(), File.separator + path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            return directory;
        }
    }
}