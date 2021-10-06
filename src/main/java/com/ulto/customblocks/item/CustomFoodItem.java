package com.ulto.customblocks.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class CustomFoodItem extends CustomItem {
    private final int eatSpeed;
    private Item eatenItem = null;

    public CustomFoodItem(Properties settings, int eatSpeed, JsonArray tooltip, JsonObject item) {
        super(settings, tooltip, item);
        this.eatSpeed = eatSpeed;
        if (item.getAsJsonObject("food").has("eaten_item")) eatenItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item.getAsJsonObject("food").get("eaten_item").getAsString()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return eatSpeed;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (item.has("on_eaten")) Events.playItemEvent(stack, Map.of("x", user.position().x, "y", user.position().y, "z", user.position().z, "entity", user, "world", world), item.getAsJsonObject("on_eaten"));
        ItemStack itemStack = super.finishUsingItem(stack, world, user);
        return user instanceof Player && ((Player)user).getAbilities().instabuild || eatenItem == null ? itemStack : new ItemStack(eatenItem);
    }
}
