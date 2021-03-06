package com.ulto.customblocks.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.ulto.customblocks.CustomBlocksMod;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.biome.Biome;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    public static List<String> jsonArrayToStringList(JsonArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsString());
        }
        return list;
    }

    public static List<Character> jsonArrayToCharacterList(JsonArray jsonArray) {
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsCharacter());
        }
        return list;
    }

    public static List<Number> jsonArrayToNumberList(JsonArray jsonArray) {
        List<Number> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsNumber());
        }
        return list;
    }

    public static List<Integer> jsonArrayToIntList(JsonArray jsonArray) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsInt());
        }
        return list;
    }

    public static List<Float> jsonArrayToFloatList(JsonArray jsonArray) {
        List<Float> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsFloat());
        }
        return list;
    }

    public static List<Long> jsonArrayToLongList(JsonArray jsonArray) {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsLong());
        }
        return list;
    }

    public static List<Short> jsonArrayToShortList(JsonArray jsonArray) {
        List<Short> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsShort());
        }
        return list;
    }

    public static List<Double> jsonArrayToDoubleList(JsonArray jsonArray) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsDouble());
        }
        return list;
    }

    public static List<Byte> jsonArrayToByteList(JsonArray jsonArray) {
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsByte());
        }
        return list;
    }

    public static List<BigDecimal> jsonArrayToBigDecimalList(JsonArray jsonArray) {
        List<BigDecimal> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsBigDecimal());
        }
        return list;
    }

    public static List<BigInteger> jsonArrayToBigIntegerList(JsonArray jsonArray) {
        List<BigInteger> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsBigInteger());
        }
        return list;
    }

    public static List<Boolean> jsonArrayToBooleanList(JsonArray jsonArray) {
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsBoolean());
        }
        return list;
    }

    public static List<JsonElement> jsonArrayToJsonElementList(JsonArray jsonArray) {
        List<JsonElement> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i));
        }
        return list;
    }

    public static List<JsonObject> jsonArrayToJsonObjectList(JsonArray jsonArray) {
        List<JsonObject> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsJsonObject());
        }
        return list;
    }

    public static List<JsonPrimitive> jsonArrayToJsonPrimitiveList(JsonArray jsonArray) {
        List<JsonPrimitive> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsJsonPrimitive());
        }
        return list;
    }

    public static List<JsonNull> jsonArrayToJsonNullList(JsonArray jsonArray) {
        List<JsonNull> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i).getAsJsonNull());
        }
        return list;
    }

    public static List<Identifier> jsonArrayToIdentifierList(JsonArray jsonArray) {
        List<Identifier> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(new Identifier(jsonArray.get(i).getAsString()));
        }
        return list;
    }

    public static List<VoxelShape> jsonObjectListToVoxelShapeList(List<JsonObject> jsonObjectList) {
        List<JsonObject> shape = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjectList) {
            shape.add(jsonObject.getAsJsonObject());
        }
        List<VoxelShape> list = new ArrayList<>();
        for (JsonObject obj : shape) {
            list.add(VoxelShapes.cuboid(obj.get("min_x").getAsDouble() / 16, obj.get("min_y").getAsDouble() / 16, obj.get("min_z").getAsDouble() / 16, obj.get("max_x").getAsDouble() / 16, obj.get("max_y").getAsDouble() / 16, obj.get("max_z").getAsDouble() / 16));
        }
        return list;
    }

    public static List<RegistryKey<Biome>> jsonArrayToBiomeRegistryKeyList(JsonArray jsonArray) {
        List<RegistryKey<Biome>> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(RegistryKey.of(Registry.BIOME_KEY, new Identifier(jsonArray.get(i).getAsString())));
        }
        return list;
    }

    public static NbtCompound jsonObjectListToNbtCompound(List<JsonObject> jsonObjectList) {
        NbtCompound nbt = new NbtCompound();
        for (JsonObject nbtObject : jsonObjectList) {
            if (nbtObject.has("key") && nbtObject.has("value")) {
                String key = nbtObject.get("key").getAsString();
                JsonElement value = nbtObject.get("value");
                if (value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isString()) {
                        nbt.putString(key, primitive.getAsString());
                    } else if (primitive.isNumber()) {
                        if (primitive.getAsNumber() instanceof Integer) nbt.putInt(key, primitive.getAsInt());
                        else if (primitive.getAsNumber() instanceof Short) nbt.putShort(key, primitive.getAsShort());
                        else if (primitive.getAsNumber() instanceof Long) nbt.putLong(key, primitive.getAsLong());
                        else if (primitive.getAsNumber() instanceof Byte) nbt.putByte(key, primitive.getAsByte());
                        else if (primitive.getAsNumber() instanceof Float) nbt.putFloat(key, primitive.getAsFloat());
                        else if (primitive.getAsNumber() instanceof Double) nbt.putDouble(key, primitive.getAsDouble());
                    } else if (primitive.isBoolean()) {
                        nbt.putBoolean(key, primitive.getAsBoolean());
                    }
                } else if (value.isJsonArray()) {
                    nbt.put(key, jsonObjectListToNbtCompound(jsonArrayToJsonObjectList(value.getAsJsonArray())));
                }
            }
        }
        return nbt;
    }

    public static ItemStack itemStackFromJsonObject(JsonObject itemIn) {
        if (itemIn.has("id")) {
            if (itemIn.get("id").getAsString().equals("empty")) {
                return ItemStack.EMPTY;
            } else {
                Identifier id = new Identifier(itemIn.get("id").getAsString());
                int count = 1;
                if (itemIn.has("count")) count = itemIn.get("count").getAsInt();
                ItemStack item = new ItemStack(Registry.ITEM.get(id), count);
                if (itemIn.has("modifiers")) {
                    JsonObject modifiers = itemIn.getAsJsonObject("modifiers");
                    if (modifiers.has("enchantments")) {
                        for (JsonObject enchantment : jsonArrayToJsonObjectList(modifiers.getAsJsonArray("enchantments"))) {
                            if (enchantment.has("id")) {
                                Identifier enchantId = new Identifier(enchantment.get("id").getAsString());
                                int level = 1;
                                if (enchantment.has("level")) level = enchantment.get("level").getAsInt();
                                item.addEnchantment(Registry.ENCHANTMENT.get(enchantId), level);
                            }
                        }
                    }
                    if (modifiers.has("potion_type") && BooleanUtils.isIdentifierPotion(id)) {
                        PotionUtil.setPotion(item, Registry.POTION.get(new Identifier(modifiers.get("potion_type").getAsString())));
                    }
                    if (modifiers.has("custom_potion") && BooleanUtils.isIdentifierPotion(id)) {
                        List<StatusEffectInstance> effects = new ArrayList<>();
                        JsonObject customPotion = modifiers.getAsJsonObject("custom_potion");
                        for (JsonObject effect : jsonArrayToJsonObjectList(customPotion.getAsJsonArray("effects"))) {
                            if (effect.has("id")) {
                                Identifier effectId = new Identifier(effect.get("id").getAsString());
                                int amplifier = 0;
                                if (effect.has("amplifier")) amplifier = effect.get("amplifier").getAsInt();
                                if (amplifier > 128) amplifier = 128;
                                if (amplifier < 1) amplifier = 1;
                                int duration = 3600;
                                if (effect.has("duration")) duration = (int) (effect.get("duration").getAsFloat() * 20);
                                if (duration > 20000000) duration = 20000000;
                                if (duration < 0) duration = 0;
                                effects.add(new StatusEffectInstance(Registry.STATUS_EFFECT.get(effectId), duration, amplifier));
                            }
                        }
                        PotionUtil.setCustomPotionEffects(item, effects);
                        if (customPotion.has("color")) {
                            item.getOrCreateNbt().putInt("CustomPotionColor", MiscConverter.jsonObjectToColor(customPotion.getAsJsonObject("color")));
                        } else {
                            item.getOrCreateNbt().putInt("CustomPotionColor", PotionUtil.getColor(effects));
                        }
                    }
                    if (modifiers.has("damage")) {
                        item.setDamage(modifiers.get("damage").getAsInt());
                    }
                    if (modifiers.has("unbreakable")) {
                        boolean unbreakable = modifiers.get("unbreakable").getAsBoolean();
                        item.getOrCreateNbt().putBoolean("Unbreakable", unbreakable);
                    }
                    if (modifiers.has("display")) {
                        JsonObject display = modifiers.getAsJsonObject("display");
                        NbtCompound displayTag = new NbtCompound();
                        if (display.has("color")) {
                            displayTag.putInt("color", MiscConverter.jsonObjectToColor(display.getAsJsonObject("color")));
                        }
                        if (display.has("name")) {
                            String name;
                            if (display.get("name").isJsonObject()) {
                                name = display.getAsJsonObject("name").toString();
                            } else if (display.get("lore").isJsonArray()) {
                                name = display.getAsJsonArray("name").toString();
                            } else {
                                JsonObject nameObj = new JsonObject();
                                nameObj.addProperty("text", display.get("name").getAsString());
                                name = nameObj.toString();
                            }
                            displayTag.putString("Name", name);
                        }
                        if (display.has("lore")) {
                            NbtList lore = new NbtList();
                            String loreValue;
                            if (display.get("lore").isJsonObject()) {
                                loreValue = display.getAsJsonObject("lore").toString();
                            } else if (display.get("lore").isJsonArray()) {
                                loreValue = display.getAsJsonArray("lore").toString();
                            } else {
                                JsonObject loreObj = new JsonObject();
                                loreObj.addProperty("text", display.get("lore").getAsString());
                                loreValue = loreObj.toString();
                            }
                            lore.add(NbtString.of(loreValue));
                            displayTag.put("Lore", lore);
                        }
                        item.getOrCreateNbt().put("display", displayTag);
                    }
                    if (modifiers.has("nbt")) {
                        List<JsonObject> nbtObjects = jsonArrayToJsonObjectList(modifiers.getAsJsonArray("nbt"));
                        item.setNbt(jsonObjectListToNbtCompound(nbtObjects));
                    }
                }
                if (itemIn.has("nbt")) {
                    if (itemIn.get("nbt").isJsonArray()) item.setNbt(jsonObjectListToNbtCompound(jsonArrayToJsonObjectList(itemIn.getAsJsonArray("nbt"))));
                    else if (itemIn.get("nbt").isJsonPrimitive()) {
                        try {
                            item.setNbt(StringNbtReader.parse(itemIn.get("nbt").getAsString()));
                        } catch (CommandSyntaxException e) {
                            CustomBlocksMod.LOGGER.error("Failed to set Nbt of an item stack!");
                        }
                    }
                }
                return item;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static StatusEffectInstance statusEffectInstanceFromJsonObject(JsonObject jsonObject) {
        if (jsonObject.has("id")) {
            Identifier effectId = new Identifier(jsonObject.get("id").getAsString());
            int amplifier = 0;
            if (jsonObject.has("amplifier")) amplifier = jsonObject.get("amplifier").getAsInt();
            if (amplifier > 128) amplifier = 128;
            if (amplifier < 1) amplifier = 1;
            int duration = 3600;
            if (jsonObject.has("duration")) duration = (int) (jsonObject.get("duration").getAsFloat() * 20);
            if (duration > 20000000) duration = 20000000;
            if (duration < 0) duration = 0;
            return new StatusEffectInstance(Registry.STATUS_EFFECT.get(effectId), duration, amplifier);
        }
        return new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 0, 0);
    }

    public static NbtCompound jsonElementToNbtCompound(JsonElement element) {
        if (element.isJsonArray()) return jsonObjectListToNbtCompound(jsonArrayToJsonObjectList(element.getAsJsonArray()));
        else if (element.isJsonPrimitive()) {
            try {
                return StringNbtReader.parse(element.getAsString());
            } catch (CommandSyntaxException e) {
                CustomBlocksMod.LOGGER.error("Failed to set Nbt of an item stack!");
            }
        }
        return new NbtCompound();
    }

    public static Text jsonElementToText(JsonElement element) {
        if (element.isJsonPrimitive()) return Text.literal(element.getAsString());
        else if (element.isJsonArray() || element.isJsonObject()) return Text.Serializer.fromJson(element);
        return Text.literal("");
    }

    public static List<Text> jsonArrayToTextList(JsonArray jsonArray) {
        List<Text> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonElementToText(jsonArray.get(i)));
        }
        return list;
    }

    public static JsonObject copy(JsonObject objectIn) {
        JsonObject newObject = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : objectIn.entrySet()) {
            newObject.add(entry.getKey(), entry.getValue());
        }
        return newObject;
    }

    public static boolean isJsonObjectValid(String json) {
        return isJsonValid(json, JsonObject.class);
    }

    public static boolean isJsonArrayValid(String json) {
        return isJsonValid(json, JsonArray.class);
    }

    public static boolean isJsonPrimitiveValid(String json) {
        return isJsonValid(json, JsonPrimitive.class);
    }

    public static boolean isJsonValid(String json, Class<? extends JsonElement> clazz) {
        try {
            new Gson().fromJson(json, clazz);
        } catch (JsonSyntaxException ex) {
            return false;
        }
        return true;
    }
}
