package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBasePotion;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IchorBottle extends ItemBasePotion {

	public IchorBottle() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.ichorBottle1");
			//ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.ichorBottle2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return true;
	}

	@Override
	public void onConsumed(Level worldIn, Player player, ItemStack potion) {
		if (player instanceof ServerPlayer playerMP) {
			SuperpositionHandler.setPersistentBoolean(playerMP, "ConsumedIchorBottle", true);
			SuperpositionHandler.unlockSpecialSlot("charm", playerMP);
			playerMP.level().playSound(null, playerMP.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0F, 1F);

			double multiplier = 1;

			player.addEffect(new MobEffectInstance(MobEffects.REGENERATION,            (int) (1200 * multiplier), 2, false, true));
			player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,         (int) (800 * multiplier), 4, false, true));
			player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,          (int) (1600 * multiplier), 2, false, true));
			player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, (int) (3000 * multiplier), 0, false, true));
		}
	}

	@Override
	public boolean canDrink(Level world, Player player, ItemStack potion) {
		return true;
	}

}
