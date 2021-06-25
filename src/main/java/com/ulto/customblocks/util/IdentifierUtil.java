package com.ulto.customblocks.util;

import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class IdentifierUtil {
    public static boolean isIdentifierPotion(Identifier id) {
        return Registry.ITEM.get(id) == Items.POTION || Registry.ITEM.get(id) == Items.SPLASH_POTION || Registry.ITEM.get(id) == Items.LINGERING_POTION || Registry.ITEM.get(id) == Items.TIPPED_ARROW;
    }
}
