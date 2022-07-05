package com.ulto.customblocks;

import com.google.gson.*;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.*;

public class TagGenerator {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static JsonObject fileToJsonObject(File value) {
        JsonObject jsonObject = new JsonObject();
        try {
            BufferedReader packReader = new BufferedReader(new FileReader(value));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = packReader.readLine()) != null) {
                json.append(line);
            }
            jsonObject = new Gson().fromJson(json.toString(), JsonObject.class);
            packReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static boolean add(JsonObject tag) {
        if (tag.has("custom")) {
            JsonObject custom = tag.getAsJsonObject("custom");
            if (custom.has("namespace") && custom.has("id") && custom.has("type")) {
                String type = custom.get("type").getAsString();
                File namespaceDir = new File(CustomResourceCreator.data, File.separator + custom.get("namespace").getAsString());
                namespaceDir.mkdirs();
                File tags = new File(namespaceDir, File.separator + "tags");
                tags.mkdirs();
                File typeDir = new File(tags, File.separator + type);
                typeDir.mkdirs();
                File tagFile = getTagFile(typeDir, custom.get("id").getAsString());
                JsonArray values = new JsonArray();
                if (!tagFile.exists()) {
                    try {
                        tagFile.createNewFile();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                } else values = fileToJsonObject(tagFile).getAsJsonArray("values");
                JsonObject tagToWrite = JsonUtils.copy(tag);
                tagToWrite.remove("custom");
                for (int i = 0; i < tagToWrite.getAsJsonArray("values").size(); i++) {
                    JsonElement value = tagToWrite.getAsJsonArray("values").get(i);
                    if (values.contains(value)) values.remove(value);
                }
                values.addAll(tagToWrite.getAsJsonArray("values"));
                for (int i = 0; i < values.size(); i++) {
                    JsonElement value = values.get(i);
                    switch (type) {
                        case "items":
                            if (!Registry.ITEM.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "fluids":
                            if (!Registry.FLUID.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "entity_types":
                            if (!Registry.ENTITY_TYPE.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "game_events":
                            if (!Registry.GAME_EVENT.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "painting_variant":
                            if (!Registry.PAINTING_VARIANT.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "banner_pattern":
                            if (!Registry.BANNER_PATTERN.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "cat_variant":
                            if (!Registry.CAT_VARIANT.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "instrument":
                            if (!Registry.INSTRUMENT.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        case "point_of_interest_type":
                            if (!Registry.POINT_OF_INTEREST_TYPE.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                        default:
                            if (!Registry.BLOCK.getIds().contains(new Identifier(value.getAsString())))
                                values.remove(value);
                            break;
                    }
                }
                tagToWrite.remove("values");
                tagToWrite.add("values", values);
                try {
                    FileWriter tagfw = new FileWriter(tagFile);
                    tagfw.write(gson.toJson(tagToWrite));
                    tagfw.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                return true;
            }
            CustomBlocksMod.LOGGER.error("Failed to add tag! Had namespace: {}, had id: {}, had type: {}", custom.has("namespace"), custom.has("id"), custom.has("type"));
        }
        return false;
    }

    public static JsonObject generateTagObject(Block... valuesIn) {
        JsonObject tag = new JsonObject();
        tag.addProperty("replace", false);
        JsonArray values = new JsonArray();
        for (Block block : valuesIn) values.add(Registry.BLOCK.getId(block).toString());
        tag.add("values", values);
        return tag;
    }

    public static JsonObject generateTagObject(ItemConvertible... valuesIn) {
        JsonObject tag = new JsonObject();
        tag.addProperty("replace", false);
        JsonArray values = new JsonArray();
        for (ItemConvertible item : valuesIn) values.add(Registry.ITEM.getId(item.asItem()).toString());
        tag.add("values", values);
        return tag;
    }

    public static JsonObject generateTagObject(Fluid... valuesIn) {
        JsonObject tag = new JsonObject();
        tag.addProperty("replace", false);
        JsonArray values = new JsonArray();
        for (Fluid fluid : valuesIn) values.add(Registry.FLUID.getId(fluid).toString());
        tag.add("values", values);
        return tag;
    }

    public static JsonObject generateTagObject(Identifier... valuesIn) {
        JsonObject tag = new JsonObject();
        tag.addProperty("replace", false);
        JsonArray values = new JsonArray();
        for (Identifier id : valuesIn) values.add(id.toString());
        tag.add("values", values);
        return tag;
    }

    public static JsonObject generateCustomTagObject(Identifier name, String type, Identifier... valuesIn) {
        JsonObject tag = new JsonObject();
        JsonObject custom = new JsonObject();
        custom.addProperty("namespace", name.getNamespace());
        custom.addProperty("id", name.getPath());
        custom.addProperty("type", type);
        tag.add("custom", custom);
        tag.addProperty("replace", false);
        JsonArray values = new JsonArray();
        for (Identifier id : valuesIn) values.add(id.toString());
        tag.add("values", values);
        return tag;
    }

    private static File getTagFile(File typeDir, String id) {
        if (id.contains("/")) {
            String[] files = id.split("/");
            File[] latestFiles = new File[files.length];
            for (int i = 0; i < files.length; i++) {
                if (i == 0) {
                    latestFiles[0] = new File(typeDir, File.separator + files[0]);
                    latestFiles[0].mkdirs();
                }
                else if (i < files.length - 1) {
                    latestFiles[i] = new File(latestFiles[i - 1], File.separator + files[i]);
                    latestFiles[i].mkdirs();
                }
                else return new File(latestFiles[latestFiles.length - 2], File.separator + files[files.length - 1] + ".json");
            }
        }
        return new File(typeDir, File.separator + id + ".json");
    }
}
