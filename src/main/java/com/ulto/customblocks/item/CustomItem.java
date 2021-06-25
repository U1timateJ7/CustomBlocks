package com.ulto.customblocks.item;

import com.ulto.customblocks.util.MiscConverter;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomItem extends Item {
    List<String> tooltips;

    public CustomItem(Settings settings, List<String> tooltip) {
        super(settings);
        tooltips = tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.addAll(MiscConverter.stringListToTextList(tooltips));
    }
}
