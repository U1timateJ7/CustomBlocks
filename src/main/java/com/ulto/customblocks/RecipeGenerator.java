package com.ulto.customblocks;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeGenerator {
    public static List<JsonObject> recipes = new ArrayList<>();

    public static boolean add(JsonObject recipe) {
        if (recipe.has("custom") && recipe.has("type")) {
            recipes.add(recipe);
            return true;
        }
        return false;
    }
}
