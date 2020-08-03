package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemAdvancedCurio;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OceanStone extends ItemAdvancedCurio implements ISpellstone {

	public final int xpCostBase = 150;
	public final int nightVisionDuration = 210;

	public OceanStone() {
		super(ItemAdvancedCurio.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ocean_stone"));

		this.immunityList.add(DamageSource.DROWN.damageType);
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.OCEAN_STONE_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStoneCooldown", ((ConfigHandler.OCEAN_STONE_COOLDOWN.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone5", ConfigHandler.OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.oceanStone8");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.spellstoneAbility").get().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}

	}

	public void triggerActiveAbility(World world, PlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		if (player.world.getDimension().getType().getId() != 1 & player.world.getDimension().getType().getId() != -1)
			if (!world.getWorldInfo().isThundering()) {
				boolean paybackReceived = false;
				/*
				 * ItemStack scroll = SuperpositionHandler.getCurioStack(player,
				 * EnigmaticLegacy.xpScroll);
				 *
				 * if (scroll != null && ItemNBTHelper.getInt(scroll, "XPStored", 0) >=
				 * xpCostBase*2) { ItemNBTHelper.setInt(scroll, "XPStored",
				 * ItemNBTHelper.getInt(scroll, "XPStored", 0) - (int)
				 * ((xpCostBase+(Math.random()*xpCostBase))*ConfigHandler.
				 * OCEAN_STONE_XP_COST_MODIFIER.getValue())); paybackReceived = true; } else
				 */ if (player.experienceTotal >= this.xpCostBase * 2) {
					player.giveExperiencePoints((int) -((this.xpCostBase + (Math.random() * this.xpCostBase)) * ConfigHandler.OCEAN_STONE_XP_COST_MODIFIER.getValue()));
					paybackReceived = true;
				}

				if (paybackReceived) {

					int thunderstormTime = (int) (10000 + (Math.random() * 20000));

					world.getWorldInfo().setClearWeatherTime(0);
					world.getWorldInfo().setRainTime(thunderstormTime);
					world.getWorldInfo().setThunderTime(thunderstormTime);
					world.getWorldInfo().setRaining(true);
					world.getWorldInfo().setThundering(true);

					world.playSound(null, player.getPosition(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.NEUTRAL, 2.0F, (float) (0.7F + (Math.random() * 0.3D)));

					SuperpositionHandler.setSpellstoneCooldown(player, ConfigHandler.OCEAN_STONE_COOLDOWN.getValue());
				}

			}
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity living) {
		if (living instanceof PlayerEntity)
			EnigmaticLegacy.miningCharm.removeNightVisionEffect((PlayerEntity) living, this.nightVisionDuration);
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {

		if (living instanceof PlayerEntity & !living.world.isRemote)
			if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.oceanStone)) {
				PlayerEntity player = (PlayerEntity) living;

				if (player.areEyesInFluid(FluidTags.WATER, true)) {
					player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, this.nightVisionDuration, 0, true, false));
					player.setAir(300);
				} else {
					EnigmaticLegacy.miningCharm.removeNightVisionEffect(player, this.nightVisionDuration);
				}
			}

	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {

		Multimap<String, AttributeModifier> atts = HashMultimap.create();

		atts.put(LivingEntity.SWIM_SPEED.getName(), new AttributeModifier(UUID.fromString("13faf191-bf38-4654-b369-cc1f4f1143bf"), "Swim speed bonus", ConfigHandler.OCEAN_STONE_SWIMMING_SPEED_BOOST.getValue().asMultiplier(false), AttributeModifier.Operation.MULTIPLY_BASE));

		return atts;
	}

}
