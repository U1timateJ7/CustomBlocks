package com.ulto.customblocks;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.BannerPatternTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class BannerPatternGenerator {
    public static boolean add(JsonObject bannerPattern) {
        if (bannerPattern.has("namespace") && bannerPattern.has("id") && bannerPattern.has("hash_name")) {
            Identifier id = new Identifier(bannerPattern.get("namespace").getAsString(), bannerPattern.get("id").getAsString());
            String hashName = bannerPattern.get("hash_name").getAsString();
            boolean needsItem = false;
            if (bannerPattern.has("needs_item")) needsItem = bannerPattern.get("needs_item").getAsBoolean();
            RegistryKey<BannerPattern> pattern = RegistryKey.of(Registry.BANNER_PATTERN_KEY, id);
            Registry.register(Registry.BANNER_PATTERN, pattern, new BannerPattern(hashName));
            if (needsItem) {
                Item bannerPatternItem = new BannerPatternItem(TagKey.of(Registry.BANNER_PATTERN_KEY, new Identifier(id.getNamespace(), "pattern_item/" + id.getPath())), new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
                Registry.register(Registry.ITEM, new Identifier(id.getNamespace(), id.getPath() + "_banner_pattern"), bannerPatternItem);
                TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier(id.getNamespace(), "pattern_item/" + id.getPath()), "banner_pattern", id));
            } else {
                TagGenerator.add(TagGenerator.generateCustomTagObject(BannerPatternTags.NO_ITEM_REQUIRED.id(), "banner_pattern", id));
            }
            return true;
        }
        return false;
    }
}
