package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.item.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemGenerator {
    public static boolean add(JsonObject item) {
        if (item.has("namespace") && item.has("id")) {
            String namespace = item.get("namespace").getAsString();
            String id = item.get("id").getAsString();
            JsonArray tooltip = new JsonArray();
            if (item.has("tooltips")) tooltip = item.getAsJsonArray("tooltips");
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
            String type;
            if (item.has("type")) type = item.get("type").getAsString();
            else type = "item";
            int protection;
            if (item.has("protection")) protection = item.get("protection").getAsInt();
            else protection = 0;
            SoundEvent equipSound;
            if (item.has("equip_sound")) equipSound = Registry.SOUND_EVENT.get(new Identifier(item.get("equip_sound").getAsString()));
            else equipSound = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            String armorName;
            if (item.has("armor_name")) armorName = item.get("armor_name").getAsString();
            else armorName = "iron";
            float toughness;
            if (item.has("efficiency")) toughness = item.get("efficiency").getAsFloat();
            else toughness = 0f;
            float knockbackResistance;
            if (item.has("knockback_resistance")) knockbackResistance = item.get("knockback_resistance").getAsFloat() / 10;
            else knockbackResistance = 0f;
            EquipmentSlot equipmentSlot;
            if (item.has("equipment_slot")) equipmentSlot = EquipmentSlot.byName(item.get("equipment_slot").getAsString());
            else equipmentSlot = EquipmentSlot.HEAD;
            Ingredient repairIngredient;
            if (Registry.ITEM.containsId(new Identifier(repairIngredientId))) repairIngredient = Ingredient.ofItems(Registry.ITEM.get(new Identifier(repairIngredientId)));
            else repairIngredient = null;
            ItemGroup itemGroup = switch (_itemGroup) {
                case "building_blocks" -> ItemGroup.BUILDING_BLOCKS;
                case "decorations" -> ItemGroup.DECORATIONS;
                case "redstone" -> ItemGroup.REDSTONE;
                case "transportation" -> ItemGroup.TRANSPORTATION;
                case "food" -> ItemGroup.FOOD;
                case "tools" -> ItemGroup.TOOLS;
                case "combat" -> ItemGroup.COMBAT;
                case "brewing" -> ItemGroup.BREWING;
                default -> ItemGroup.MISC;
            };
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
            ArmorMaterial armor = new ArmorMaterial() {
                @Override
                public int getDurability(EquipmentSlot slot) {
                    return durability;
                }

                @Override
                public int getProtectionAmount(EquipmentSlot slot) {
                    return protection;
                }

                @Override
                public int getEnchantability() {
                    return enchantability;
                }

                @Override
                public SoundEvent getEquipSound() {
                    return equipSound;
                }

                @Override
                public Ingredient getRepairIngredient() {
                    return repairIngredient;
                }

                @Override
                public String getName() {
                    return armorName;
                }

                @Override
                public float getToughness() {
                    return toughness;
                }

                @Override
                public float getKnockbackResistance() {
                    return knockbackResistance;
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
                ITEM = new CustomFoodItem(settings.food(foodBuilder.build()), eatingSpeed, tooltip, item);
            } else {
                switch (type) {
                    case "sword" -> {
                        ITEM = new CustomSwordItem(tool, -1, attackSpeed - 4, settings, tooltip, item);
                        TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "swords"), "items", new Identifier(namespace, id)));
                    }
                    case "pickaxe" -> {
                        ITEM = new CustomPickaxeItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                        TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "pickaxes"), "items", new Identifier(namespace, id)));
                    }
                    case "axe" -> {
                        ITEM = new CustomAxeItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                        TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "axes"), "items", new Identifier(namespace, id)));
                    }
                    case "shovel" -> {
                        ITEM = new CustomShovelItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                        TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "shovels"), "items", new Identifier(namespace, id)));
                    }
                    case "hoe" -> {
                        ITEM = new CustomHoeItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                        TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "hoes"), "items", new Identifier(namespace, id)));
                    }
                    case "shears" -> {
                        ITEM = new CustomShearsItem(settings, tooltip, item);
                        TagGenerator.add(TagGenerator.generateCustomTagObject(new Identifier("fabric", "shears"), "items", new Identifier(namespace, id)));
                    }
                    case "armor" -> ITEM = new CustomArmorItem(settings, armor, equipmentSlot, tooltip, item);
                    default -> ITEM = new CustomItem(settings, tooltip, item);
                }
            }
            Registry.register(Registry.ITEM, new Identifier(namespace, id), ITEM);
            if (compostLevel > 0) ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(ITEM, compostLevel);
            return true;
        }
        return false;
    }
}
