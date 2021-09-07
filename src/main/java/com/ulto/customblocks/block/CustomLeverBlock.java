package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;

import java.util.ArrayList;
import java.util.List;

public class CustomLeverBlock extends LeverBlock {
    List<JsonObject> drops = new ArrayList<>();
    JsonObject block;

    public CustomLeverBlock(Settings settings, List<JsonObject> dropsIn, JsonObject blockIn) {
        super(settings);
        drops.addAll(dropsIn);
        block = blockIn;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDroppedStacks(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        List<ItemStack> realDrops = new ArrayList<>();
        for (JsonObject item : drops) {
            realDrops.add(JsonUtils.itemStackFromJsonObject(item));
        }
        return realDrops;
    }
}
