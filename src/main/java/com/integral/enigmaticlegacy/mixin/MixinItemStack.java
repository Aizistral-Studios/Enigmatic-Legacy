package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public class MixinItemStack {

	@Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
	private void onGetHoverName(CallbackInfoReturnable<Component> info) {
		ItemStack stack = (ItemStack) (Object) this;

		if (stack.is(EnigmaticLegacy.insignia)) {
			info.setReturnValue(EnigmaticLegacy.insignia.getName(stack));
		}
	}

	@Inject(method = "hasCustomHoverName", at = @At("HEAD"), cancellable = true)
	private void onHasCustomHoverName(CallbackInfoReturnable<Boolean> info) {
		ItemStack stack = (ItemStack) (Object) this;

		if (stack.is(EnigmaticLegacy.insignia)) {
			info.setReturnValue(false);
		}
	}

}
