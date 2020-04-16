package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class EscapeScroll extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();

	public EscapeScroll(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		EscapeScroll.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		EscapeScroll.integratedProperties.maxStackSize(1);
		EscapeScroll.integratedProperties.rarity(Rarity.RARE);

		return EscapeScroll.integratedProperties;
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.ESCAPE_SCROLL_ENABLED.getValue();
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.escapeScroll))
			return false;
		else
			return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.escapeTome1");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.escapeTome2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.escapeTome3");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public void onEquipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

}
