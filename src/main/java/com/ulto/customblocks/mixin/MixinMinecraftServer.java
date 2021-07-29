package com.ulto.customblocks.mixin;

import com.ulto.customblocks.resource.CustomResourcePackProvider;
import com.ulto.customblocks.resource.IMutableResourcePackManager;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftServer.class, priority = 1001)
public class MixinMinecraftServer {
    @Inject(method = "loadDataPacks", at = @At("HEAD"))
    private static void loadDataPacks (ResourcePackManager manager, DataPackSettings settings, boolean safe, CallbackInfoReturnable<DataPackSettings> info) {
        CustomResourcePackProvider.Type.DATA.getLogger().info("Injecting datapack provider for server.");
        final IMutableResourcePackManager mutable = (IMutableResourcePackManager) manager;
        mutable.customblocks$addProvider(new CustomResourcePackProvider(CustomResourcePackProvider.Type.DATA));
    }
}