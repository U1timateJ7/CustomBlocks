package com.ulto.customblocks.mixin;

import com.ulto.customblocks.event.global.callbacks.EntityTickCallback;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        EntityTickCallback.EVENT.invoker().onEntityTick((Entity) (Object) this, ((Entity) (Object) this).world);
    }
}
