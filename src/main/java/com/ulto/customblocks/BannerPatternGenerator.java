package com.ulto.customblocks;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.registries.ForgeRegistries;

public class BannerPatternGenerator {
    public static boolean add(JsonObject bannerPattern) {
        if (bannerPattern.has("namespace") && bannerPattern.has("id") && bannerPattern.has("hash_name")) {
            ResourceLocation id = new ResourceLocation(bannerPattern.get("namespace").getAsString(), bannerPattern.get("id").getAsString());
            String hashName = bannerPattern.get("hash_name").getAsString();
            boolean needsItem = false;
            if (bannerPattern.has("needs_item")) needsItem = bannerPattern.get("needs_item").getAsBoolean();
            ResourceKey<BannerPattern> pattern = ResourceKey.create(Registry.BANNER_PATTERN_REGISTRY, id);
            Registry.register(Registry.BANNER_PATTERN, pattern, new BannerPattern(hashName));
            if (needsItem) {
                Item bannerPatternItem = new BannerPatternItem(TagKey.create(Registry.BANNER_PATTERN_REGISTRY, new ResourceLocation(id.getNamespace(), "pattern_item/" + id.getPath())), new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1));
                ForgeRegistries.ITEMS.register(new ResourceLocation(id.getNamespace(), id.getPath() + "_banner_pattern"), bannerPatternItem);
                TagGenerator.add(TagGenerator.generateCustomTagObject(new ResourceLocation(id.getNamespace(), "pattern_item/" + id.getPath()), "banner_pattern", id));
            } else {
                TagGenerator.add(TagGenerator.generateCustomTagObject(BannerPatternTags.NO_ITEM_REQUIRED.location(), "banner_pattern", id));
            }
            return true;
        }
        return false;
    }
}
