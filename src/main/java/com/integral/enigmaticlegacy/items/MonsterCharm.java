package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MonsterCharm extends ItemBaseCurio {

	public float bonusXPModifier = 2.0F;

	public MonsterCharm() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "monster_charm"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MONSTER_CHARM_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm1", TextFormatting.GOLD, ConfigHandler.MONSTER_CHARM_UNDEAD_DAMAGE.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm2", TextFormatting.GOLD, ConfigHandler.MONSTER_CHARM_AGGRESSIVE_DAMAGE.getValue().asPercentage() + "%");
			if (ConfigHandler.MONSTER_CHARM_BONUS_LOOTING.getValue())
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm3");
			if (ConfigHandler.MONSTER_CHARM_DOUBLE_XP.getValue()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm4");
			}
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

}
