package com.ulto.customblocks.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CustomFoodItem extends CustomItem {
    private final int eatSpeed;

    public CustomFoodItem(Item.Settings settings, int eatSpeed, List<String> tooltip) {
        super(settings, tooltip);
        this.eatSpeed = eatSpeed;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return eatSpeed;
    }
}
