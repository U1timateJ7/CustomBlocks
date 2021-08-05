package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class CustomSlabBlock extends SlabBlock {
    List<JsonObject> drops = new ArrayList<>();
    JsonObject block;

    public CustomSlabBlock(Properties settings, List<JsonObject> dropsIn, JsonObject blockIn) {
        super(settings);
        drops.addAll(dropsIn);
        block = blockIn;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt() > 0;
        }
        return false;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        if (block.has("redstone_power")) {
            return block.get("redstone_power").getAsInt();
        }
        return 0;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        List<ItemStack> realDrops = new ArrayList<>();
        for (JsonObject item : drops) {
            ItemStack stack = JsonUtils.itemStackFromJsonObject(item);
            stack.setCount(stack.getCount() * (state.getValue(TYPE) == SlabType.DOUBLE ? 2 : 1));
            realDrops.add(stack);
        }
        return realDrops;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (block.has("render_type")) {
            if (!block.get("render_type").getAsString().equals("opaque")) {
                return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
            }
        }
        return super.skipRendering(state, stateFrom, direction);
    }
}
