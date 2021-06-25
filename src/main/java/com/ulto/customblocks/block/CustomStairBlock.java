package com.ulto.customblocks.block;

import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonConverter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomStairBlock extends StairsBlock {
    List<JsonObject> drops = new ArrayList<>();
    JsonObject block;

    public CustomStairBlock(BlockState baseBlock, Settings settings, List<JsonObject> dropsIn, JsonObject blockIn) {
        super(baseBlock, settings);
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
            realDrops.add(JsonConverter.itemStackFromJsonObject(item));
        }
        return realDrops;
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (block.has("render_type")) {
            if (!block.get("render_type").getAsString().equals("opaque")) {
                return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
            }
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }
}
