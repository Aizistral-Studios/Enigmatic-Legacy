package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HeavenScroll extends ItemBaseCurio {

	public HashMap<PlayerEntity, Boolean> flyMap = new HashMap<PlayerEntity, Boolean>();

	public HeavenScroll() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "heaven_scroll"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.HEAVEN_SCROLL_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.heavenTome3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {
		if (living.world.isRemote)
			return;

		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			if (Math.random() <= (0.025D * ConfigHandler.HEAVEN_SCROLL_XP_COST_MODIFIER.getValue()) & player.abilities.isFlying)
				player.giveExperiencePoints(-1);

			try {
				if (player.experienceTotal > 0) {

					if (!player.abilities.allowFlying)
						player.abilities.allowFlying = true;
					player.sendPlayerAbilities();
					this.flyMap.put(player, true);
				} else if (this.flyMap.get(player)) {
					if (!player.isCreative()) {
						player.abilities.allowFlying = false;
						player.abilities.isFlying = false;
						player.sendPlayerAbilities();
					}

					this.flyMap.put(player, false);
				}

			} catch (NullPointerException ex) {
				this.flyMap.put(player, false);
			}
		}

	}
	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {

		if (entityLivingBase instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLivingBase;

			if (!player.isCreative()) {
				player.abilities.allowFlying = false;
				player.abilities.isFlying = false;
				player.sendPlayerAbilities();
			}

			this.flyMap.put(player, false);

		}
	}

}
