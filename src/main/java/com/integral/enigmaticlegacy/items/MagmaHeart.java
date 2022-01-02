package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MagmaHeart extends ItemSpellstoneCurio implements ISpellstone {
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
					.comment("Controls how obscured your vision is in lava when Blazing Core is equipped. Lower value equals more visibility.")
					.max(1024)
					.clientOnly()
					.getDouble("LavaDensity", 0.3);

			traitorBar = builder
					.comment("Flips the parabolic function bearing responsibility for heat bar rendering when temporary fire resistance from Blazing Core is active. Instead of default behavior, it will start decreasing slowly, but will expotentially speed up the closer to the end it is. This is a purely visual effect - raw fire immunity time provided stays unchanged.")
					.clientOnly()
					.getBoolean("TraitorBarEnabled", false);
		}

		builder.popPrefix();
	}

	public List<String> nemesisList = new ArrayList<String>();

	public MagmaHeart() {
		super(ItemSpellstoneCurio.getDefaultProperties().rarity(Rarity.UNCOMMON).fireResistant());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "magma_heart"));

		//this.immunityList.add(DamageSource.LAVA.damageType);
		this.immunityList.add(DamageSource.IN_FIRE.msgId);
		this.immunityList.add(DamageSource.ON_FIRE.msgId);
		this.immunityList.add(DamageSource.HOT_FLOOR.msgId);
		//immunityList.add("fireball");

		this.nemesisList.add("mob");
		this.nemesisList.add(DamageSource.GENERIC.msgId);
		this.nemesisList.add("player");
		//nemesisList.add("arrow");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeartCooldown", TextFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", TextFormatting.LIGHT_PURPLE, KeyBinding.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (living.isOnFire()) {
			living.clearFire();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

}
