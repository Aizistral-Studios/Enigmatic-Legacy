package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class HeavenScroll extends ItemBaseCurio {
	public static Omniconfig.DoubleParameter xpCostModifier;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("HeavenScroll");

		xpCostModifier = builder
				.comment("Multiplier for experience consumption by Gift of the Heaven.")
				.getDouble("XPCostModifier", 1.0);

		builder.popPrefix();
	}

	public Map<Player, Integer> flyMap = new WeakHashMap<Player, Integer>();
	public final double baseXpConsumptionProbability = 0.025D/2D;

	public HeavenScroll() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "heaven_scroll"));
	}

	public HeavenScroll(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome4");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (living.level.isClientSide)
			return;

		if (living instanceof Player) {
			Player player = (Player) living;

			if (Math.random() <= (this.baseXpConsumptionProbability * xpCostModifier.getValue()) && player.getAbilities().flying) {
				ExperienceHelper.drainPlayerXP(player, 1);
			}

			this.handleFlight(player);
		}

	}

	protected void handleFlight(Player player) {
		try {
			if (ExperienceHelper.getPlayerXP(player) > 0 && SuperpositionHandler.isInBeaconRange(player)) {

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
	public void onUnequip(String identifier, int index, LivingEntity entityLivingBase, ItemStack stack) {

		if (entityLivingBase instanceof Player) {
			Player player = (Player) entityLivingBase;

			if (!player.isCreative()) {
				player.getAbilities().mayfly = false;
				player.getAbilities().flying = false;
				player.onUpdateAbilities();
			}

			this.flyMap.put(player, 0);

		}
	}

}
