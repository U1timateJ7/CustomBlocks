package com.ulto.customblocks.item;

import com.ulto.customblocks.util.MiscConverter;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomSwordItem extends SwordItem {
    List<String> tooltips;

    public CustomSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, List<String> tooltip) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        tooltips = tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.addAll(MiscConverter.stringListToLiteralTextList(tooltips));
    }
}
