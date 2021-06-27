package com.ulto.customblocks;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class ItemGroupGenerator {
    public static boolean add(JsonObject itemGroup) {
        if (itemGroup.has("namespace") && itemGroup.has("id") && itemGroup.has("icon") && itemGroup.has("items")) {
            String namespace = itemGroup.get("namespace").getAsString();
            String id = itemGroup.get("id").getAsString();
            Identifier icon = new Identifier(itemGroup.get("icon").getAsString());
            List<JsonObject> items = JsonUtils.jsonArrayToJsonObjectList(itemGroup.getAsJsonArray("items"));
            ItemGroup GROUP = FabricItemGroupBuilder.create(
                new Identifier(namespace, id))
                .icon(() -> new ItemStack(Registry.ITEM.get(icon)))
                .appendItems(stacks -> {
                    for (JsonObject item : items) {
                        stacks.add(JsonUtils.itemStackFromJsonObject(item));
                    }
                })
                .build();
            return true;
        }
        return false;
    }
}
