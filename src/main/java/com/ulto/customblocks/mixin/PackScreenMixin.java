package com.ulto.customblocks.mixin;

import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackScreen.class)
public class PackScreenMixin {
    @Inject(at = @At("RETURN"), method = "loadPackIcon", cancellable = true)
    private void customPackIcon(TextureManager textureManager, ResourcePackProfile resourcePackProfile, CallbackInfoReturnable<Identifier> cir) {
        if (resourcePackProfile.getName().equals("Custom Block Resources")) cir.setReturnValue(new Identifier("custom_blocks", "icon.png"));
    }
}
