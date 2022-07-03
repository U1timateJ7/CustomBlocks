package com.ulto.customblocks.resource;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.realmsclient.util.JsonUtils;
import com.ulto.customblocks.CustomBlocksMod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomResourcePackFinder implements RepositorySource {
    public static File customBlocksPath = new File(CustomBlocksMod.customBlocksConfig, File.separator + "custom_blocks_resources");
    private static final MetadataSectionSerializer<PackMeta> META_SERIALIZER = new PackMetaSerializer();

    public static final CustomResourcePackFinder DATA = new CustomResourcePackFinder(Type.DATA);
    public static final CustomResourcePackFinder RESOUCE = new CustomResourcePackFinder(Type.RESOURCES);

    private final Type type;
    private final File packDirectory;
    
    public CustomResourcePackFinder(Type type) {
        this.type = type;
        this.packDirectory = type.getDirectory();
        try {
            Files.createDirectories(this.packDirectory.toPath());
        }
        catch (IOException e) {
            CustomBlocksMod.LOGGER.error("Failed to initialize loader.", e);
        }
    }
    
    @Override
    public void loadPacks (Consumer<Pack> consumer, Pack.PackConstructor factory) {
        CustomBlocksMod.LOGGER.info("Loading {} Custom Blocks Resources.", this.type.displayName);
        Pack packInfo = Pack.create("Custom Blocks Resources", true, getAsPack(Type.RESOURCES.getDirectory()), factory, Pack.Position.TOP, decorating("pack.source.mod"));
        if (packInfo != null) {
            consumer.accept(packInfo);
        }
    }

    private Supplier<PackResources> getAsPack (File file) {
        return file.isDirectory() ? () -> new FolderPackResources(file) : () -> new FilePackResources(file);
    }

    static PackSource decorating(String p_10534_) {
        Component component = Component.translatable(p_10534_);
        return (p_10539_) -> (Component.translatable("pack.nameAndSource", p_10539_, component)).withStyle(ChatFormatting.GRAY);
    }
    
    public enum Type {
        DATA("Data Pack", "custom_blocks/custom_blocks_resources"),
        RESOURCES("Resource Pack", "custom_blocks/custom_blocks_resources");
        
        String displayName;
        String path;
        
        Type(String name, String path) {
            displayName = name;
            this.path = path;
        }
        
        public File getDirectory () {
            File directory = FMLPaths.CONFIGDIR.get().resolve(path).toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            return directory;
        }

        @Nullable
        public static Type getPackType (Pack pack) {
            final String packName = pack.getTitle().getString();
            for (final Type type : Type.values()) {
                if (packName.startsWith(type.path)) {
                    return type;
                }
            }
            return null;
        }

        public static boolean isCustomBlocksPack (Pack pack) {
            return getPackType(pack) != null;
        }
    }

    static class PackMetaSerializer implements MetadataSectionSerializer<PackMeta> {
        @Override
        public String getMetadataSectionName () {
            return CustomBlocksMod.MOD_ID;
        }

        @Override
        public PackMeta fromJson(JsonObject json) {
            boolean builtin = JsonUtils.getBooleanOr("builtin", json, true);
            String priorityName = JsonUtils.getStringOr("priority", json, "top").toLowerCase();
            Pack.Position priority = "top".equalsIgnoreCase(priorityName) ? Pack.Position.TOP : "bottom".equalsIgnoreCase(priorityName) ? Pack.Position.BOTTOM : null;
            boolean defaultEnabled = JsonUtils.getBooleanOr("defaultEnabled", json, true);
            if (priority == null) {
                throw new JsonParseException("Expected priority to be \"top\" or \"bottom\". " + priorityName + " is not a valid value!");
            }
            return new PackMeta(builtin, priority, defaultEnabled);
        }
    }

    public static class PackMeta {
        public final boolean isBuiltIn;
        public final Pack.Position priority;
        public final boolean defaultEnabled;
        public PackMeta(boolean isBuiltIn, Pack.Position priority, boolean defaultEnabled) {
            this.isBuiltIn = isBuiltIn;
            this.priority = priority;
            this.defaultEnabled = defaultEnabled;
        }

        @Override
        public String toString () {
            return "PackMeta [isBuiltIn=" + this.isBuiltIn + ", priority=" + this.priority + ", defaultEnabled=" + this.defaultEnabled + "]";
        }
    }
}