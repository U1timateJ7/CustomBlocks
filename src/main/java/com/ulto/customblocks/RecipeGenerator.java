package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ulto.customblocks.util.JsonUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeGenerator {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static List<JsonObject> recipes = new ArrayList<>();

    public static void setup() {
        List<File> namespaces = new ArrayList<>();
        listFiles(CustomResourceCreator.data, namespaces);
        for (File namespace : namespaces) {
            if (new File(namespace, File.separator + "recipes").exists()) {
                try {
                    FileUtils.cleanDirectory(new File(namespace, File.separator + "recipes"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void listFiles(final File folder, List<File> list) {
        if (folder.exists()) {
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (fileEntry.isDirectory()) {
                    list.add(fileEntry);
                }
            }
        }
    }

    public static boolean add(JsonObject recipe) {
        if (recipe.has("custom") && recipe.has("type")) {
            JsonObject custom = recipe.getAsJsonObject("custom");
            File recipes = new File(CustomResourceCreator.data.toPath().resolve(custom.get("namespace").getAsString()).toFile(), File.separator + "recipes");
            recipes.mkdirs();
            File recipeFile = new File(recipes, File.separator + custom.get("id").getAsString() + ".json");
            JsonObject recipeToWrite = JsonUtils.copy(recipe);
            recipeToWrite.remove("custom");
            try {
                FileWriter recipefw = new FileWriter(recipeFile);
                recipefw.write(gson.toJson(recipeToWrite));
                recipefw.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
