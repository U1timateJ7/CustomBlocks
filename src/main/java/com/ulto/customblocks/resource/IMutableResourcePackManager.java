package com.ulto.customblocks.resource;

import org.spongepowered.asm.mixin.Unique;

import net.minecraft.resource.ResourcePackProvider;

public interface IMutableResourcePackManager {
    @Unique
    void customblocks$addProvider (ResourcePackProvider provider);
}
