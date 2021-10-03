package com.ulto.customblocks.mixin;

import com.ulto.customblocks.event.global.callbacks.EntityTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        EntityTickCallback.PLAYER.invoker().onPlayerTick((PlayerEntity) (Object) this, ((PlayerEntity) (Object) this).world);
    }
}
