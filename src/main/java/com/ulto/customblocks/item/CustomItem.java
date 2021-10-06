package com.ulto.customblocks.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CustomItem extends Item {
    JsonArray tooltips;
    JsonObject item;

    public CustomItem(Properties settings, JsonArray tooltip, JsonObject item) {
        super(settings);
        tooltips = tooltip;
        this.item = item;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        tooltip.addAll(JsonUtils.jsonArrayToComponentList(tooltips));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        InteractionResultHolder<ItemStack> result = super.use(world, user, hand);
        if (item.has("on_use")) {
            boolean swingsHand = false;
            if (item.getAsJsonObject("on_use").has("swing_hand")) swingsHand = item.getAsJsonObject("on_use").get("swing_hand").getAsBoolean();
            return Events.playItemTypedActionEvent(user.getItemInHand(hand), swingsHand, Map.of("x", user.position().x, "y", user.position().y, "z", user.position().z, "entity", user, "hand", hand, "world", world), item.getAsJsonObject("on_use"));
        }
        return result;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = super.useOn(context);
        if (item.has("on_use_on_block")) {
            if (context.getPlayer() != null) return Events.playItemActionEvent(context.getItemInHand(), Map.of("x", context.getClickLocation().x, "y", context.getClickLocation().y, "z", context.getClickLocation().z, "entity", context.getPlayer(), "hand", context.getHand(), "world", context.getLevel()), item.getAsJsonObject("on_use"));
            else return Events.playItemActionEvent(context.getItemInHand(), Map.of("hand", context.getHand(), "world", context.getLevel()), item.getAsJsonObject("on_use"));
        }
        return result;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level world, Player player) {
        super.onCraftedBy(stack, world, player);
        if (item.has("on_craft")) Events.playItemEvent(stack, Map.of("x", player.position().x, "y", player.position().y, "z", player.position().z, "entity", player, "world", world), item.getAsJsonObject("on_craft"));
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        super.onDestroyed(entity);
        if (item.has("on_destroyed")) Events.playItemEvent(entity.getItem(), Map.of("x", entity.position().x, "y", entity.position().y, "z", entity.position().z, "entity", entity, "world", entity.level), item.getAsJsonObject("on_destroyed"));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        super.releaseUsing(stack, world, user, remainingUseTicks);
        if (item.has("on_stopped_using")) Events.playItemEvent(stack, Map.of("x", user.position().x, "y", user.position().y, "z", user.position().z, "entity", user, "world", world), item.getAsJsonObject("on_stopped_using"));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (item.has("on_inventory_tick")) Events.playItemEvent(stack, Map.of("x", entity.position().x, "y", entity.position().y, "z", entity.position().z, "entity", entity, "world", world), item.getAsJsonObject("on_inventory_tick"));
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.onUseTick(world, user, stack, remainingUseTicks);
        if (item.has("on_usage_tick")) Events.playItemEvent(stack, Map.of("x", user.position().x, "y", user.position().y, "z", user.position().z, "entity", user, "world", world), item.getAsJsonObject("on_usage_tick"));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (item.has("on_mine_block")) Events.playBlockEvent(state, pos, world, Map.of("entity", miner, "itemstack", stack), item.getAsJsonObject("on_mine_block"));
        return super.mineBlock(stack, world, state, pos, miner);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (item.has("on_hit")) Events.playItemEvent(stack, Map.of("x", target.position().x, "y", target.position().y, "z", target.position().z, "entity", target, "sourceentity", attacker, "world", target.level), item.getAsJsonObject("on_hit"));
        return super.hurtEnemy(stack, target, attacker);
    }
}
