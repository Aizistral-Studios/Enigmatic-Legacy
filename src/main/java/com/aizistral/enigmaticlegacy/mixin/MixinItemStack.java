package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.items.Insignia;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public class MixinItemStack {

	@Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true)
	private void onGetHoverName(CallbackInfoReturnable<Component> info) {
		ItemStack stack = (ItemStack) (Object) this;

		if (stack.getItem() instanceof Insignia) {
			info.setReturnValue(stack.getItem().getName(stack));
		}
	}

	@Inject(method = "hasCustomHoverName", at = @At("HEAD"), cancellable = true)
	private void onHasCustomHoverName(CallbackInfoReturnable<Boolean> info) {
		ItemStack stack = (ItemStack) (Object) this;

		if (stack.getItem() instanceof Insignia) {
			info.setReturnValue(false);
		}
	}

}
