package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.world.item.enchantment.BindingCurseEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.container.EnchantmentContainer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mixin(Enchantment.class)
public class MixinEnchantment {

	@Inject(at = @At("HEAD"), method = "canEnchant", cancellable = true)
	public void allowEnchantment(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
		Object forgottenObject = this;

		if (Enchantments.BINDING_CURSE == forgottenObject) {
			if (stack != null && stack.getItem() instanceof ItemBaseCurio) {
				info.setReturnValue(true);
				return;
			}
		}
	}

}
