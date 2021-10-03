package com.ulto.customblocks.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.Map;

public class CustomFoodItem extends CustomItem {
    private final int eatSpeed;
    private Item eatenItem = null;

    public CustomFoodItem(Item.Settings settings, int eatSpeed, JsonArray tooltip, JsonObject item) {
        super(settings, tooltip, item);
        this.eatSpeed = eatSpeed;
        if (item.getAsJsonObject("food").has("eaten_item")) eatenItem = Registry.ITEM.get(new Identifier(item.getAsJsonObject("food").get("eaten_item").getAsString()));
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return eatSpeed;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (item.has("on_eaten")) Events.playItemEvent(stack, Map.of("x", user.getPos().x, "y", user.getPos().y, "z", user.getPos().z, "entity", user, "world", world), item.getAsJsonObject("on_eaten"));
        ItemStack itemStack = super.finishUsing(stack, world, user);
        return user instanceof PlayerEntity && ((PlayerEntity)user).getAbilities().creativeMode || eatenItem == null ? itemStack : new ItemStack(eatenItem);
    }
}
