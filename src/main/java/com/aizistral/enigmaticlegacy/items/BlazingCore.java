package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ISpellstone;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

public class BlazingCore extends ItemSpellstoneCurio implements ISpellstone {
	public static Omniconfig.IntParameter spellstoneCooldown;
	public static Omniconfig.DoubleParameter damageFeedback;
	public static Omniconfig.IntParameter ignitionFeedback;

	public static Omniconfig.DoubleParameter lavafogDensity;
	public static Omniconfig.BooleanParameter traitorBar;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("BlazingCore");

		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {

			spellstoneCooldown = builder
					.comment("Active ability cooldown for Blazing Core. Measured in ticks. 20 ticks equal to 1 second.")
					.getInt("Cooldown", 0);

			damageFeedback = builder
					.comment("How much fire-based damage instantly receives any creature that attacks bearer of the Blazing Core.")
					.max(512)
					.getDouble("DamageFeedback", 4.0);

			ignitionFeedback = builder
					.comment("How how many seconds any creature that attacks bearer of the Blazing Core will be set on fire.")
					.max(512)
					.getInt("IgnitionFeedback", 4);

		} else {
			lavafogDensity = builder
					.comment("Controls how obscured your vision is in lava when Blazing Core is equipped. Higher value equals more visibility.")
					.max(1024)
					.clientOnly()
					.getDouble("LavaDensity", 4);

			traitorBar = builder
					.comment("Flips the parabolic function bearing responsibility for heat bar rendering when temporary fire resistance from Blazing Core is active. Instead of default behavior, it will start decreasing slowly, but will expotentially speed up the closer to the end it is. This is a purely visual effect - raw fire immunity time provided stays unchanged.")
					.clientOnly()
					.getBoolean("TraitorBarEnabled", false);
		}

		builder.popPrefix();
	}

	public List<ResourceKey<DamageType>> nemesisList = new ArrayList<>();

	public BlazingCore() {
		super(ItemSpellstoneCurio.getDefaultProperties().rarity(Rarity.UNCOMMON).fireResistant());

		//this.immunityList.add(DamageSource.LAVA.damageType);
		this.immunityList.add(DamageTypes.IN_FIRE);
		this.immunityList.add(DamageTypes.ON_FIRE);
		this.immunityList.add(DamageTypes.HOT_FLOOR);
		//immunityList.add("fireball");

		this.nemesisList.add(DamageTypes.MOB_ATTACK);
		this.nemesisList.add(DamageTypes.GENERIC);
		this.nemesisList.add(DamageTypes.PLAYER_ATTACK);
		//nemesisList.add("arrow");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCoreCooldown", ChatFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.blazingCore11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", ChatFormatting.LIGHT_PURPLE, KeyMapping.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity().isOnFire()) {
			context.entity().clearFire();
		}
	}

}
