package com.aizistral.enigmaticlegacy.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "isFoil", at = @At("HEAD"), cancellable = true)
    protected void isFoil(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {}
}
