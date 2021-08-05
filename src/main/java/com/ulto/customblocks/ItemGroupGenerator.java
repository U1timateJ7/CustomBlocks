package com.ulto.customblocks;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemGroupGenerator {
    public static Map<ResourceLocation, CreativeModeTab> customTabs = new HashMap<>();

    public static boolean add(JsonObject itemGroup) {
        if (itemGroup.has("namespace") && itemGroup.has("id") && itemGroup.has("icon") && itemGroup.has("items")) {
            String namespace = itemGroup.get("namespace").getAsString();
            String id = itemGroup.get("id").getAsString();
            ResourceLocation icon = new ResourceLocation(itemGroup.get("icon").getAsString());
            List<JsonObject> items = JsonUtils.jsonArrayToJsonObjectList(itemGroup.getAsJsonArray("items"));
            CreativeModeTab GROUP = new CreativeModeTab(String.format("%s.%s", namespace, id)) {
                @Override
                public ItemStack makeIcon() {
                    return new ItemStack(ForgeRegistries.ITEMS.getValue(icon));
                }
            };
            customTabs.put(new ResourceLocation(namespace, id), GROUP);
            return true;
        }
        return false;
    }
}
