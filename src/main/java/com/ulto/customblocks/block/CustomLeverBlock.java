package com.ulto.customblocks.block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomLeverBlock extends LeverBlock {
    JsonObject block;

    public CustomLeverBlock(Settings settings, JsonObject blockIn) {
        super(settings);
        block = blockIn;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        if (block.has("drops")) {
            if (block.get("drops").isJsonArray()) {
                List<ItemStack> dropsOriginal = super.getDroppedStacks(state, builder);
                if (!dropsOriginal.isEmpty())
                    return dropsOriginal;
                List<ItemStack> realDrops = new ArrayList<>();
                for (JsonElement item : block.getAsJsonArray("drops")) {
                    realDrops.add(JsonUtils.itemStackFromJsonObject((JsonObject) item));
                }
                return realDrops;
            } else if (block.get("drops").isJsonPrimitive()) {
                if (block.getAsJsonPrimitive("drops").isString()) {
                    Identifier identifier = new Identifier(block.get("drops").getAsString());
                    if (identifier.equals(LootTables.EMPTY)) {
                        return Collections.emptyList();
                    } else {
                        LootContext lootContext = builder.parameter(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK);
                        ServerWorld serverWorld = lootContext.getWorld();
                        LootTable lootTable = serverWorld.getServer().getLootManager().getTable(identifier);
                        return lootTable.generateLoot(lootContext);
                    }
                }
            }
        }
        List<ItemStack> dropsOriginal = super.getDroppedStacks(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this));
    }
}
