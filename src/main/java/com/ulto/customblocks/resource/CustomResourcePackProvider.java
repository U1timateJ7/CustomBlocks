package com.ulto.customblocks.resource;

import com.ulto.customblocks.CustomBlocksMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomResourcePackProvider implements ResourcePackProvider {
    public static File customBlocksPath = new File(CustomBlocksMod.customBlocksConfig, File.separator + "custom_blocks_resources");
    private final Type type;
    
    public CustomResourcePackProvider(Type type) {
        this.type = type;
    }
    
    @Override
    public void register (Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        File candidate = type.getDirectory();
        String packName = "Custom Block Resources";
        type.getLogger().info("Loading pack {} from {}.", packName, candidate.getAbsolutePath());
        Supplier<ResourcePack> packSupplier = candidate.isDirectory() ? () -> new DirectoryResourcePack(candidate) : () -> new ZipResourcePack(candidate);
        ResourcePackProfile profile = ResourcePackProfile.of(packName, true, packSupplier, factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
        if (profile != null) {
            consumer.accept(profile);
            type.getLogger().info("Loaded pack {}.", packName);
        }
        else {
            type.getLogger().error("Failed to build pack profile {} from {}.", packName, candidate.getAbsolutePath());
        }
    }
    
    public enum Type {
        DATA("Data Pack", "custom_blocks/custom_blocks_resources"),
        RESOURCES("Resource Pack", "custom_blocks/custom_blocks_resources");
        
        String displayName;
        String path;
        Logger logger;
        
        Type(String name, String path) {
            displayName = name;
            this.path = path;
            logger = LogManager.getLogger("Custom Blocks Mod");
        }
        
        public Logger getLogger () {
            return this.logger;
        }
        
        public File getDirectory () {
            File directory = FabricLoader.getInstance().getConfigDir().resolve(path).toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            return directory;
        }
    }
}