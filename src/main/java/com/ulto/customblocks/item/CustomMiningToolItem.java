package com.ulto.customblocks.item;

import com.ulto.customblocks.util.MiscConverter;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomMiningToolItem extends DiggerItem {
    List<String> tooltips;

    public CustomMiningToolItem(float attackDamage, float attackSpeed, Tier material, Tag<Block> effectiveBlocks, Properties settings, List<String> tooltip) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
        tooltips = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.addAll(MiscConverter.stringListToComponentList(tooltips));
    }
}
