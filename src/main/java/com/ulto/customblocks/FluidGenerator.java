package com.ulto.customblocks;

import com.google.gson.JsonObject;
import com.ulto.customblocks.fluid.CustomFluid;
import com.ulto.customblocks.util.BooleanUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FluidGenerator {
    public static boolean add(JsonObject fluid) {
        if (BooleanUtils.isValidFluid(fluid)) {
            int luminance = 0;
            if (fluid.has("luminance")) luminance = fluid.get("luminance").getAsInt();
            if (luminance > 15) luminance = 15;
            if (luminance < 0) luminance = 0;
            Identifier id = new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString());
            Identifier flowingId = new Identifier(fluid.get("namespace").getAsString(), "flowing_" + fluid.get("id").getAsString());
            Identifier bucketId = new Identifier(fluid.get("namespace").getAsString(), fluid.get("id").getAsString() + "_bucket");
            FlowableFluid STILL = Registry.register(Registry.FLUID, id, new CustomFluid.Still(fluid));
            FlowableFluid FLOWING = Registry.register(Registry.FLUID, flowingId, new CustomFluid.Flowing(fluid));
            Item BUCKET = Registry.register(Registry.ITEM, bucketId, new BucketItem(STILL, new FabricItemSettings().group(ItemGroup.MISC).recipeRemainder(Items.BUCKET).maxCount(1)));
            Block BLOCK = Registry.register(Registry.BLOCK, id, new FluidBlock(STILL, FabricBlockSettings.copyOf(Blocks.WATER).luminance(luminance)){});
            TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("water"), "fluids", id, flowingId));
            return true;
        }
        return false;
    }
}
