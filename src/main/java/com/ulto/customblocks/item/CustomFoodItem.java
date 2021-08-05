package com.ulto.customblocks.item;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CustomFoodItem extends CustomItem {
    private final int eatSpeed;

    public CustomFoodItem(Properties settings, int eatSpeed, List<String> tooltip) {
        super(settings, tooltip);
        this.eatSpeed = eatSpeed;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return eatSpeed;
    }
}
