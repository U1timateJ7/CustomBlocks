package com.ulto.customblocks.item;

import com.ulto.customblocks.util.MiscConverter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomSwordItem extends SwordItem {
    List<String> tooltips;

    public CustomSwordItem(Tier toolMaterial, int attackDamage, float attackSpeed, Properties settings, List<String> tooltip) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        tooltips = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.addAll(MiscConverter.stringListToComponentList(tooltips));
    }
}
