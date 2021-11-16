package com.ulto.customblocks.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.event.Events;
import com.ulto.customblocks.util.JsonUtils;
import com.ulto.customblocks.util.MiscUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomArmorItem extends ArmorItem {
    JsonArray tooltips;
    JsonObject item;

    public CustomArmorItem(Settings settings, ArmorMaterial material, EquipmentSlot slot, JsonArray tooltip, JsonObject itemIn) {
        super(material, slot, settings);
        tooltips = tooltip;
        item = itemIn;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.addAll(JsonUtils.jsonArrayToTextList(tooltips));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TypedActionResult<ItemStack> result = super.use(world, user, hand);
        if (item.has("on_use")) {
            boolean swingsHand = false;
            if (item.getAsJsonObject("on_use").has("swing_hand")) swingsHand = item.getAsJsonObject("on_use").get("swing_hand").getAsBoolean();
            return Events.playItemTypedActionEvent(user.getStackInHand(hand), swingsHand, MiscUtils.mapOf("x", user.getPos().x, "y", user.getPos().y, "z", user.getPos().z, "entity", user, "hand", hand, "world", world), item.getAsJsonObject("on_use"));
        }
        return result;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult result = super.useOnBlock(context);
        if (item.has("on_use_on_block")) {
            if (context.getPlayer() != null) return Events.playItemActionEvent(context.getStack(), MiscUtils.mapOf("x", context.getHitPos().x, "y", context.getHitPos().y, "z", context.getHitPos().z, "entity", context.getPlayer(), "hand", context.getHand(), "world", context.getWorld()), item.getAsJsonObject("on_use"));
            else return Events.playItemActionEvent(context.getStack(), MiscUtils.mapOf("hand", context.getHand(), "world", context.getWorld()), item.getAsJsonObject("on_use"));
        }
        return result;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        if (item.has("on_craft")) Events.playItemEvent(stack, MiscUtils.mapOf("x", player.getPos().x, "y", player.getPos().y, "z", player.getPos().z, "entity", player, "world", world), item.getAsJsonObject("on_craft"));
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (item.has("on_stopped_using")) Events.playItemEvent(stack, MiscUtils.mapOf("x", user.getPos().x, "y", user.getPos().y, "z", user.getPos().z, "entity", user, "world", world), item.getAsJsonObject("on_stopped_using"));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (item.has("on_inventory_tick")) Events.playItemEvent(stack, MiscUtils.mapOf("x", entity.getPos().x, "y", entity.getPos().y, "z", entity.getPos().z, "entity", entity, "world", world), item.getAsJsonObject("on_inventory_tick"));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
        if (item.has("on_usage_tick")) Events.playItemEvent(stack, MiscUtils.mapOf("x", user.getPos().x, "y", user.getPos().y, "z", user.getPos().z, "entity", user, "world", world), item.getAsJsonObject("on_usage_tick"));
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (item.has("on_mine_block")) Events.playBlockEvent(state, pos, world, MiscUtils.mapOf("entity", miner, "itemstack", stack), item.getAsJsonObject("on_mine_block"));
        return super.postMine(stack, world, state, pos, miner);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (item.has("on_hit")) Events.playItemEvent(stack, MiscUtils.mapOf("x", target.getPos().x, "y", target.getPos().y, "z", target.getPos().z, "entity", target, "sourceentity", attacker, "world", target.world), item.getAsJsonObject("on_hit"));
        return super.postHit(stack, target, attacker);
    }
}
