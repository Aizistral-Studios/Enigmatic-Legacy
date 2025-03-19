package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnholyGrail extends ItemBase implements Vanishable {

	public UnholyGrail() {
		super(ItemBase.getDefaultProperties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.unholyGrail1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		if (!(entityLiving instanceof Player))
			return stack;

		Player player = (Player) entityLiving;

		if (!worldIn.isClientSide) {
			boolean isTheWorthyOne = SuperpositionHandler.isTheCursedOne(player) && EnigmaticItems.FORBIDDEN_FRUIT.haveConsumedFruit(player);

			if (!isTheWorthyOne) {
				player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.POISON, 160, 1, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 240, 0, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 160, 2, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 240, 0, false, true));

				UseUnholyGrailTrigger.INSTANCE.trigger((ServerPlayer) player, false);
			} else {
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1000 / 2, 2, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1600 / 2, 1, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2400 / 2, 1, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2000 / 2, 1, false, true));
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400 / 2, 0, false, true));
				//player.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0, false, true));

				UseUnholyGrailTrigger.INSTANCE.trigger((ServerPlayer) player, true);
			}
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		return stack;
	}

	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	public SoundEvent getEatingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		playerIn.startUsingItem(handIn);
		return new InteractionResultHolder<>(InteractionResult.CONSUME, playerIn.getItemInHand(handIn));
	}

}
