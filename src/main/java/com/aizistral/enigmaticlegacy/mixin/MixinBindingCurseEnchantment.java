package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.api.items.IBindable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.BindingCurseEnchantment;

@Mixin(BindingCurseEnchantment.class)
public class MixinBindingCurseEnchantment {

	@Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
	public void onCanEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		if (stack != null && stack.getItem() instanceof IBindable) {
			info.setReturnValue(true);
		}
	}

}
