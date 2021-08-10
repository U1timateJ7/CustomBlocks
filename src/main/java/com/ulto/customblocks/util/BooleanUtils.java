package com.ulto.customblocks.util;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class BooleanUtils {
    public static boolean isResourceLocationPotion(ResourceLocation id) {
        return Registry.ITEM.get(id) == Items.POTION || Registry.ITEM.get(id) == Items.SPLASH_POTION || Registry.ITEM.get(id) == Items.LINGERING_POTION || Registry.ITEM.get(id) == Items.TIPPED_ARROW;
    }

    public static boolean isValidBlock(JsonObject block) {
        if (!block.has("textures")) return false;
        JsonObject textures = block.getAsJsonObject("textures");
        return block.has("namespace") && block.has("id") && block.has("display_name") && ((textures.has("top_texture") && textures.has("bottom_texture") && textures.has("front_texture") && textures.has("back_texture") && textures.has("right_texture") && textures.has("left_texture")) || textures.has("all"));
    }

    /**
     * @param block The <code>JsonObject</code> of the block.
     * @return <code>TriState.TRUE</code> if it uses the top, bottom, front, back, right and left textures, <code>TriState.FALSE</code> if uses the <code>all</code> texture and <code>TriState.DEFAULT</code> if otherwise.
     */
    public static TriState hasDifferentTextures(JsonObject block) {
        if (!block.has("textures")) return TriState.DEFAULT;
        JsonObject textures = block.getAsJsonObject("textures");
        if (textures.has("top_texture") && textures.has("bottom_texture") && textures.has("front_texture") && textures.has("back_texture") && textures.has("right_texture") && textures.has("left_texture")) return TriState.TRUE;
        else if (textures.has("all")) return TriState.FALSE;
        return TriState.DEFAULT;
    }
}
