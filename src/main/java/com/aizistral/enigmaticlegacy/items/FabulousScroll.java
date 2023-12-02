package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ExperienceHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

public class FabulousScroll extends HeavenScroll {

	public FabulousScroll() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.fabulousScroll1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.fabulousScroll2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.fabulousScroll3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.fabulousScroll4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.fabulousScroll5");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity().level.isClientSide)
			return;

		if (context.entity() instanceof Player player) {
			//don't check in range every tick as it is expensive. Particularly for multiple people.
			//instead, check once per second, based on the player's tick count
			if (player.tickCount % 20 != 0)
				return;

			boolean inRange = SuperpositionHandler.isInBeaconRange(player);

			final int fabConsumptionProbMod = 8;
			//only check xp drain if flying.
			//because we're only checking once per 20 ticks, increase the probability by 20
			if (shouldCheckXpDrain(player) && !inRange && Math.random() <= ((this.baseXpConsumptionProbability * fabConsumptionProbMod) * 20)) {
				//cost modifier hooked up to drain xp cost
				ExperienceHelper.drainPlayerXP(player, (int) (xpCostModifier.getValue()));
			}

			this.handleFlight(player, inRange);
		}
	}

	@Override
	protected boolean canFly(Player player, boolean inRangeCheckedAndSucceeded)
	{
		return inRangeCheckedAndSucceeded || ExperienceHelper.getPlayerXP(player) > 0 || (SuperpositionHandler.isInBeaconRange(player));
	}

	@Override
	public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
		if (context.entity() instanceof Player player) {
			if (!player.isCreative()) {
				player.getAbilities().mayfly = false;
				player.getAbilities().flying = false;
				player.onUpdateAbilities();
			}

			this.flyMap.put(player, 0);
		}
	}

}
