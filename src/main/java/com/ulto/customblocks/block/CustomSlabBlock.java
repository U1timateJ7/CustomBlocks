package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.List;

public class CustomSlabBlock extends SlabBlock {
    List<JsonObject> drops = new ArrayList<>();
    JsonObject block;

    public CustomSlabBlock(Settings settings, List<JsonObject> dropsIn, JsonObject blockIn) {
        super(settings);
        drops.addAll(dropsIn);
        block = blockIn;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt() > 0;
        }
        return false;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt();
        }
        return 0;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDroppedStacks(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        List<ItemStack> realDrops = new ArrayList<>();
        for (JsonObject item : drops) {
            ItemStack stack = JsonUtils.itemStackFromJsonObject(item);
            stack.setCount(stack.getCount() * (state.get(TYPE) == SlabType.DOUBLE ? 2 : 1));
            realDrops.add(stack);
        }
        return realDrops;
    }
}
