package com.ulto.customblocks;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.ulto.customblocks.item.CustomFoodItem;
import com.ulto.customblocks.item.CustomItem;
import com.ulto.customblocks.item.CustomMiningToolItem;
import com.ulto.customblocks.item.CustomSwordItem;
import com.ulto.customblocks.util.JsonConverter;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ItemGenerator {
    public static boolean add(JsonObject item) {
        if (item.has("namespace") && item.has("id")) {
            String namespace = item.get("namespace").getAsString();
            String id = item.get("id").getAsString();
            List<String> tooltip = new ArrayList<>();
            if (item.has("tooltips")) tooltip = JsonConverter.jsonArrayToStringList(item.getAsJsonArray("tooltips"));
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
            if (Registry.ITEM.containsId(new Identifier(repairIngredientId))) repairIngredient = Ingredient.ofItems(Registry.ITEM.get(new Identifier(repairIngredientId)));
            else repairIngredient = null;
            ItemGroup itemGroup;
            switch (_itemGroup) {
                case "building_blocks":
                    itemGroup = ItemGroup.BUILDING_BLOCKS;
                    break;
                case "decorations":
                    itemGroup = ItemGroup.DECORATIONS;
                    break;
                case "redstone":
                    itemGroup = ItemGroup.REDSTONE;
                    break;
                case "transportation":
                    itemGroup = ItemGroup.TRANSPORTATION;
                    break;
                case "food":
                    itemGroup = ItemGroup.FOOD;
                    break;
                case "tools":
                    itemGroup = ItemGroup.TOOLS;
                    break;
                case "combat":
                    itemGroup = ItemGroup.COMBAT;
                    break;
                case "brewing":
                    itemGroup = ItemGroup.BREWING;
                    break;
                default:
                    itemGroup = ItemGroup.MISC;
                    break;
            }

            List<Block> blocks = new ArrayList<>();
            for (Identifier block : Registry.BLOCK.getIds()) {
                blocks.add(Registry.BLOCK.get(block));
            }
            Set<Block> allBlocks = ImmutableSet.copyOf(blocks);

            Item ITEM;
            FabricItemSettings settings = new FabricItemSettings().group(itemGroup).maxCount(maxStackSize);
            ToolMaterial tool = new ToolMaterial() {
                @Override
                public int getDurability() {
                    return durability;
                }

                @Override
                public float getMiningSpeedMultiplier() {
                    return efficiency;
                }

                @Override
                public float getAttackDamage() {
                    return damage;
                }

                @Override
                public int getMiningLevel() {
                    return harvestLevel;
                }

                @Override
                public int getEnchantability() {
                    return enchantability;
                }

                @Override
                public Ingredient getRepairIngredient() {
                    return repairIngredient;
                }
            };
            if (fireproof) settings = settings.fireproof();
            if (durability > 0) settings = settings.maxCount(1).maxDamage(durability);
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

                FoodComponent.Builder foodBuilder = new FoodComponent.Builder().hunger(nutrition).saturationModifier(saturationModifier);
                if (isMeat) {
                    foodBuilder.meat();
                }
                if (alwaysEdible) {
                    foodBuilder.alwaysEdible();
                }

                if (!item.has("item_group")) itemGroup = ItemGroup.FOOD;
                settings.group(itemGroup);
                ITEM = new CustomFoodItem(settings.food(foodBuilder.build()), eatingSpeed, tooltip);
            } else {
                if (!toolType.equals("none")) {
                    ITEM = toolType.equals("sword") ? new CustomSwordItem(tool, -1, attackSpeed - 4, settings, tooltip) : new CustomMiningToolItem(-1, attackSpeed - 4, tool, allBlocks, settings, tooltip);
                } else {
                    ITEM = new CustomItem(settings, tooltip);
                }
            }
            Registry.register(Registry.ITEM, new Identifier(namespace, id), ITEM);
            if (compostLevel > 0) ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(ITEM, compostLevel);
            return true;
        }
        return false;
    }
}
