package com.integral.enigmaticlegacy.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.network.play.server.SAdvancementInfoPacket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentContainer.class)
public class MixinEnchantmentContainer {

	@Inject(at = @At("RETURN"), method = "enchantItem")
	public void onEnchantedItem(PlayerEntity playerIn, int id, CallbackInfoReturnable<Boolean> info) {
		/*
		System.out.println("I AM THE CONTAINER!!! " + this.getClass());

		if (EnchantmentContainer.class.isInstance(this)) {
			System.out.println("I am the instance, checks out...");

			// Evaluating expression promts error assuming incompatible types,
			// so we need to forget our own class to avoid alerting the compiler
			Object forgottenObject = this;

			EnchantmentContainer container = (EnchantmentContainer)forgottenObject;
			System.out.println("TableInventory: " + container.tableInventory);
		}
		 */
	}

}
