package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

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
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "fabulous_scroll"));
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
			boolean inRange = SuperpositionHandler.isInBeaconRange(player);

			if (!SuperpositionHandler.isInBeaconRange(player))
				if (Math.random() <= (this.baseXpConsumptionProbability*8) * xpCostModifier.getValue() && player.getAbilities().flying) {
					ExperienceHelper.drainPlayerXP(player, 1);
				}

			this.handleFabulousFlight(player, inRange);
		}
	}

	protected void handleFabulousFlight(Player player, boolean inRange) {
		try {
			if (ExperienceHelper.getPlayerXP(player) > 0 || inRange) {

				if (!player.getAbilities().mayfly) {
					player.getAbilities().mayfly = true;
				}

				player.onUpdateAbilities();
				this.flyMap.put(player, 100);

			} else if (this.flyMap.get(player) > 1) {
				this.flyMap.put(player, this.flyMap.get(player)-1);
			} else if (this.flyMap.get(player) == 1) {
				if (!player.isCreative()) {
					player.getAbilities().mayfly = false;
					player.getAbilities().flying = false;
					player.onUpdateAbilities();
					player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0, true, false));
				}

				this.flyMap.put(player, 0);
			}

		} catch (NullPointerException ex) {
			this.flyMap.put(player, 0);
		}
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
