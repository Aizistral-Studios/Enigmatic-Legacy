package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.item.ItemStack;

@Pseudo
@Mixin(targets = "squeek.appleskin.client.TooltipOverlayHandler", remap = false)
public class MixinTooltipOverlayHandler {

	@Inject(method = "shouldShowTooltip", at = @At("HEAD"), cancellable = true)
	private void onItemCheck(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		if (stack != null && stack.getItem().getRegistryName().getNamespace().equals(EnigmaticLegacy.MODID)) {
			info.setReturnValue(false);
		}
	}

}
