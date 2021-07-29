package com.ulto.customblocks.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    private NbtCompound nbt;

    @Shadow
    private NbtCompound getOrCreateNbt() {
        return this.nbt;
    }

    @Inject(at = @At("HEAD"), method = "addEnchantment", cancellable = true)
    private void unlimitedEnchants(Enchantment enchantment, int level, CallbackInfo ci) {
        this.getOrCreateNbt();
        if (!this.nbt.contains("Enchantments", 9)) {
            this.nbt.put("Enchantments", new NbtList());
        }

        NbtList nbtList = this.nbt.getList("Enchantments", 10);
        nbtList.add(EnchantmentHelper.createNbt(EnchantmentHelper.getEnchantmentId(enchantment), level));
        ci.cancel();
    }
}
