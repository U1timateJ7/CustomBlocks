package com.ulto.customblocks.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    private NbtCompound tag;

    @Shadow
    private NbtCompound getOrCreateTag() {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "addEnchantment", cancellable = true)
    private void unlimitedEnchants(Enchantment enchantment, int level, CallbackInfo ci) {
        this.getOrCreateTag();
        if (!this.tag.contains("Enchantments", 9)) {
            this.tag.put("Enchantments", new NbtList());
        }

        NbtList nbtList = this.tag.getList("Enchantments", 10);
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("id", String.valueOf(Registry.ENCHANTMENT.getId(enchantment)));
        nbtCompound.putInt("lvl", level);
        nbtList.add(nbtCompound);
    }
}
