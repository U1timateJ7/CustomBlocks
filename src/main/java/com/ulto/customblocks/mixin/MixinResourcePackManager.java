package com.ulto.customblocks.mixin;

import com.google.common.collect.ImmutableSet;
import com.ulto.customblocks.resource.IMutableResourcePackManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashSet;
import java.util.Set;

@Mixin(ResourcePackManager.class)
public class MixinResourcePackManager implements IMutableResourcePackManager {
    @Shadow(prefix = "customblocksshadow$")
    private Set<ResourcePackProvider> providers;
    
    @Override
    public void customblocks$addProvider (ResourcePackProvider provider) {
        final Set<ResourcePackProvider> mutableProviders = new HashSet<>(this.providers);
        mutableProviders.add(provider);
        this.providers = ImmutableSet.copyOf(mutableProviders);
    }
}