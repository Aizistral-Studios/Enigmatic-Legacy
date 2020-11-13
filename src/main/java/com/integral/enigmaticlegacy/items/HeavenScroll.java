package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.item.Item.Properties;

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

	public HashMap<PlayerEntity, Integer> flyMap = new HashMap<PlayerEntity, Integer>();
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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
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
		if (living.world.isRemote)
			return;

		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			if (Math.random() <= (this.baseXpConsumptionProbability * xpCostModifier.getValue()) && player.abilities.isFlying) {
				player.giveExperiencePoints(-1);
			}

			this.handleFlight(player);
		}

	}

	protected void handleFlight(PlayerEntity player) {
		try {
			if (player.experienceTotal > 0 && SuperpositionHandler.isInBeaconRange(player)) {

				if (!player.abilities.allowFlying) {
					player.abilities.allowFlying = true;
				}

				player.sendPlayerAbilities();
				this.flyMap.put(player, 100);

			} else if (this.flyMap.get(player) > 1) {
				this.flyMap.put(player, this.flyMap.get(player)-1);
			} else if (this.flyMap.get(player) == 1) {
				if (!player.isCreative()) {
					player.abilities.allowFlying = false;
					player.abilities.isFlying = false;
					player.sendPlayerAbilities();
					player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 200, 0, true, false));
				}

				this.flyMap.put(player, 0);
			}

		} catch (NullPointerException ex) {
			this.flyMap.put(player, 0);
		}
	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity entityLivingBase, ItemStack stack) {

		if (entityLivingBase instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLivingBase;

			if (!player.isCreative()) {
				player.abilities.allowFlying = false;
				player.abilities.isFlying = false;
				player.sendPlayerAbilities();
			}

			this.flyMap.put(player, 0);

		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

}
