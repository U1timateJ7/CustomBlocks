package com.ulto.customblocks.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ulto.customblocks.RecipeGenerator;
import com.ulto.customblocks.util.JsonUtils;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        for (JsonObject recipe : RecipeGenerator.recipes) {
            if (recipe.has("custom")) {
                JsonObject custom = recipe.getAsJsonObject("custom");
                if (custom.has("namespace") || custom.has("id")) {
                    JsonObject newRecipe = JsonUtils.copy(recipe);
                    newRecipe.remove("custom");
                    map.put(new Identifier(custom.get("namespace").getAsString(), custom.get("id").getAsString()), newRecipe);
                }
            }
        }
    }
}
