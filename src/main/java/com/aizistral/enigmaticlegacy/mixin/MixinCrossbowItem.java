package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEnchantments;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

@Mixin(CrossbowItem.class)
public class MixinCrossbowItem {

	@Inject(method = "getArrow", at = @At("RETURN"), cancellable = true)
	private static void onGetArrow(Level level, LivingEntity shooter, ItemStack crossbowStack,
			ItemStack ammoStack, CallbackInfoReturnable<AbstractArrow> info) {
		AbstractArrow arrow = info.getReturnValue();
		int sharpshooter = crossbowStack.getEnchantmentLevel(EnigmaticEnchantments.SHARPSHOOTER);
		arrow.setBaseDamage(arrow.getBaseDamage() + (0.8 * sharpshooter));

		if (crossbowStack.getEnchantmentLevel(EnigmaticEnchantments.CEASELESS) > 0) {
			arrow.pickup = Pickup.CREATIVE_ONLY;
		}
	}

}
