package com.ulto.customblocks.item;

import com.ulto.customblocks.util.MiscConverter;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class CustomMiningToolItem extends MiningToolItem {
    List<String> tooltips;

    public CustomMiningToolItem(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings, List<String> tooltip) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
        tooltips = tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.addAll(MiscConverter.stringListToLiteralTextList(tooltips));
    }
}
