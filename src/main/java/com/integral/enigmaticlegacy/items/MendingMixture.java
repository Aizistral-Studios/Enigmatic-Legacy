package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MendingMixture extends Item implements IPerhaps {

	public static Properties integratedProperties = new Item.Properties();

	public MendingMixture(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		MendingMixture.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		MendingMixture.integratedProperties.maxStackSize(1);
		MendingMixture.integratedProperties.rarity(Rarity.EPIC);
		MendingMixture.integratedProperties.containerItem(Items.GLASS_BOTTLE);

		return MendingMixture.integratedProperties;

	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MENDING_MIXTURE_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.mendingMixture1");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.mendingMixture2");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

}
