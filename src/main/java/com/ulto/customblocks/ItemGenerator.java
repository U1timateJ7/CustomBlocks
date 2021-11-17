package com.ulto.customblocks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.ForgeRegistries;

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
            else type = "none";
            int protection;
            if (item.has("protection")) protection = item.get("protection").getAsInt();
            else protection = 0;
            SoundEvent equipSound;
            if (item.has("equip_sound")) equipSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(item.get("equip_sound").getAsString()));
            else equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
            String armorName;
            if (item.has("armor_name")) armorName = item.get("armor_name").getAsString();
            else armorName = "iron";
            float toughness;
            if (item.has("toughness")) toughness = item.get("toughness").getAsFloat();
            else toughness = 0f;
            float knockbackResistance;
            if (item.has("knockback_resistance")) knockbackResistance = item.get("knockback_resistance").getAsFloat() / 10;
            else knockbackResistance = 0f;
            EquipmentSlot equipmentSlot;
            if (item.has("equipment_slot")) equipmentSlot = EquipmentSlot.byName(item.get("equipment_slot").getAsString());
            else equipmentSlot = EquipmentSlot.HEAD;
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
            ArmorMaterial armor = new ArmorMaterial() {
                @Override
                public int getDurabilityForSlot(EquipmentSlot slot) {
                    return durability;
                }

                @Override
                public int getDefenseForSlot(EquipmentSlot slot) {
                    return protection;
                }

                @Override
                public int getEnchantmentValue() {
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
                ITEM = new CustomFoodItem(settings.food(foodBuilder.build()), eatingSpeed, tooltip, item);
            } else {
                switch (type) {
                    case "sword" -> ITEM = new CustomSwordItem(tool, -1, attackSpeed - 4, settings, tooltip, item);
                    case "pickaxe" -> ITEM = new CustomPickaxeItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                    case "axe" -> ITEM = new CustomAxeItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                    case "shovel" -> ITEM = new CustomShovelItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                    case "hoe" -> ITEM = new CustomHoeItem(-1, attackSpeed - 4, tool, settings, tooltip, item);
                    case "shears" -> ITEM = new CustomShearsItem(settings, tooltip, item);
                    case "armor" -> ITEM = new CustomArmorItem(settings, armor, equipmentSlot, tooltip, item);
                    default -> ITEM = new CustomItem(settings, tooltip, item);
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
