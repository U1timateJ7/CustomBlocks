package com.ulto.customblocks;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.ulto.customblocks.item.CustomFoodItem;
import com.ulto.customblocks.item.CustomItem;
import com.ulto.customblocks.item.CustomMiningToolItem;
import com.ulto.customblocks.item.CustomSwordItem;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ItemGenerator {
    public static boolean add(JsonObject item) {
        if (item.has("namespace") && item.has("id")) {
            String namespace = item.get("namespace").getAsString();
            String id = item.get("id").getAsString();
            List<String> tooltip = new ArrayList<>();
            if (item.has("tooltips")) tooltip = JsonUtils.jsonArrayToStringList(item.getAsJsonArray("tooltips"));
            int maxStackSize;
            if (item.has("max_stack_size")) maxStackSize = item.get("max_stack_size").getAsInt();
            else maxStackSize = 64;
            if (maxStackSize > 64) maxStackSize = 64;
            if (maxStackSize < 1) maxStackSize = 1;
            String _itemGroup;
            if (item.has("item_group")) _itemGroup = item.get("item_group").getAsString();
            else _itemGroup = "misc";
            boolean fireproof;
            if (item.has("fireproof")) fireproof = item.get("fireproof").getAsBoolean();
            else fireproof = false;
            float compostLevel = 0f;
            if (item.has("compost_level")) compostLevel = item.get("compost_level").getAsFloat();
            if (compostLevel > 1f) compostLevel = 1f;
            if (compostLevel < 0f) compostLevel = 0f;
            int durability;
            if (item.has("durability")) durability = item.get("durability").getAsInt();
            else durability = 0;
            float damage;
            if (item.has("damage")) damage = item.get("damage").getAsFloat();
            else damage = 1f;
            float attackSpeed;
            if (item.has("attack_speed")) attackSpeed = item.get("attack_speed").getAsFloat();
            else attackSpeed = 1.6f;
            float efficiency;
            if (item.has("efficiency")) efficiency = item.get("efficiency").getAsFloat();
            else efficiency = 1f;
            int harvestLevel;
            if (item.has("harvest_level")) harvestLevel = item.get("harvest_level").getAsInt();
            else harvestLevel = 2;
            int enchantability;
            if (item.has("enchantability")) enchantability = item.get("enchantability").getAsInt();
            else enchantability = 9;
            String repairIngredientId;
            if (item.has("repair_ingredient")) repairIngredientId = item.get("repair_ingredient").getAsString();
            else repairIngredientId = "none";
            String toolType;
            if (item.has("tool_type")) toolType = item.get("tool_type").getAsString();
            else toolType = "none";
            Ingredient repairIngredient;
            if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(repairIngredientId))) repairIngredient = Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation(repairIngredientId)));
            else repairIngredient = null;
            CreativeModeTab itemGroup = switch (_itemGroup) {
                case "decorations" -> CreativeModeTab.TAB_DECORATIONS;
                case "redstone" -> CreativeModeTab.TAB_REDSTONE;
                case "transportation" -> CreativeModeTab.TAB_TRANSPORTATION;
                case "misc" -> CreativeModeTab.TAB_MISC;
                case "food" -> CreativeModeTab.TAB_FOOD;
                case "tools" -> CreativeModeTab.TAB_TOOLS;
                case "combat" -> CreativeModeTab.TAB_COMBAT;
                case "brewing" -> CreativeModeTab.TAB_BREWING;
                default -> CreativeModeTab.TAB_BUILDING_BLOCKS;
            };

            List<Block> blocks = new ArrayList<>();
            for (ResourceLocation block : ForgeRegistries.BLOCKS.getKeys()) {
                blocks.add(ForgeRegistries.BLOCKS.getValue(block));
            }
            Tag<Block> allBlocks = Tag.fromSet(ImmutableSet.copyOf(blocks));

            Item ITEM;
            Item.Properties settings = new Item.Properties().tab(itemGroup).stacksTo(maxStackSize);
            Tier tool = new Tier() {
                @Override
                public int getUses() {
                    return durability;
                }

                @Override
                public float getSpeed() {
                    return efficiency;
                }

                @Override
                public float getAttackDamageBonus() {
                    return damage;
                }

                @Override
                public int getLevel() {
                    return harvestLevel;
                }

                @Override
                public int getEnchantmentValue() {
                    return enchantability;
                }

                @Override
                public Ingredient getRepairIngredient() {
                    return repairIngredient;
                }
            };
            if (fireproof) settings = settings.fireResistant();
            if (durability > 0) settings = settings.stacksTo(1).durability(durability);
            if (item.has("food")) {
                JsonObject food = item.get("food").getAsJsonObject();
                int nutrition;
                if (food.has("nutrition")) nutrition = food.get("nutrition").getAsInt();
                else nutrition = 3;
                float saturationModifier;
                if (food.has("saturation")) saturationModifier = food.get("saturation").getAsFloat();
                else saturationModifier = 1.2f;
                boolean isMeat;
                if (food.has("meat")) isMeat = food.get("meat").getAsBoolean();
                else isMeat = false;
                boolean alwaysEdible;
                if (food.has("always_edible")) alwaysEdible = food.get("always_edible").getAsBoolean();
                else alwaysEdible = false;
                int eatingSpeed;
                if (food.has("eating_speed")) eatingSpeed = food.get("eating_speed").getAsInt();
                else eatingSpeed = 32;

                FoodProperties.Builder foodBuilder = new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationModifier);
                if (isMeat) {
                    foodBuilder.meat();
                }
                if (alwaysEdible) {
                    foodBuilder.alwaysEat();
                }

                if (!item.has("item_group")) itemGroup = CreativeModeTab.TAB_FOOD;
                settings.tab(itemGroup);
                ITEM = new CustomFoodItem(settings.food(foodBuilder.build()), eatingSpeed, tooltip);
            } else {
                if (!toolType.equals("none")) {
                    ITEM = toolType.equals("sword") ? new CustomSwordItem(tool, -1, attackSpeed - 4, settings, tooltip) : new CustomMiningToolItem(-1, attackSpeed - 4, tool, allBlocks, settings, tooltip);
                } else {
                    ITEM = new CustomItem(settings, tooltip);
                }
            }
            ITEM.setRegistryName(new ResourceLocation(namespace, id));
            ForgeRegistries.ITEMS.register(ITEM);
            if (compostLevel > 0) ComposterBlock.COMPOSTABLES.put(ITEM, compostLevel);
            return true;
        }
        return false;
    }
}
