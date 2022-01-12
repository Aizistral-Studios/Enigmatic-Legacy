package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.JsonConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Villager.class)
public class MixinVillagerEntity {

	@Inject(at = @At("RETURN"), method = "updateSpecialPrices")
	private void onSpecialPrices(Player player, CallbackInfo info) {
		Object forgottenObject = this;

		if (forgottenObject instanceof Villager) {
			Villager villager = (Villager) forgottenObject;

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.avariceScroll)) {
				for (MerchantOffer trade : villager.getOffers()) {
					double discountValue = 0.35;
					int discount = (int) Math.floor(discountValue * trade.getBaseCostA().getCount());
					trade.addToSpecialPriceDiff(-Math.max(discount, 1));
				}
			}
		}
	}

}
