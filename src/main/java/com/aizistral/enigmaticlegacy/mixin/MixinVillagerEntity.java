package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;

@Mixin(Villager.class)
public class MixinVillagerEntity {

	@Inject(at = @At("RETURN"), method = "updateSpecialPrices")
	private void onSpecialPrices(Player player, CallbackInfo info) {
		Object forgottenObject = this;

		if (forgottenObject instanceof Villager) {
			Villager villager = (Villager) forgottenObject;

			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.AVARICE_SCROLL)) {
				for (MerchantOffer trade : villager.getOffers()) {
					double discountValue = 0.35;
					int discount = (int) Math.floor(discountValue * trade.getBaseCostA().getCount());
					trade.addToSpecialPriceDiff(-Math.max(discount, 1));
				}
			}
		}
	}

}
