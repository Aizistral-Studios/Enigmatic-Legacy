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

public class MonsterCharm extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public static float bonusXPModifier = 2.0F;

	public MonsterCharm(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		MonsterCharm.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		MonsterCharm.integratedProperties.maxStackSize(1);
		MonsterCharm.integratedProperties.rarity(Rarity.EPIC);

		return MonsterCharm.integratedProperties;
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MONSTER_CHARM_ENABLED.getValue();
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.monsterCharm))
			return false;
		else
			return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm1", ConfigHandler.MONSTER_CHARM_UNDEAD_DAMAGE.getValue().asPercentage() + "%");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm2", ConfigHandler.MONSTER_CHARM_AGGRESSIVE_DAMAGE.getValue().asPercentage() + "%");
			if (ConfigHandler.MONSTER_CHARM_BONUS_LOOTING.getValue())
				LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm3");
			if (ConfigHandler.MONSTER_CHARM_DOUBLE_XP.getValue()) {
				LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.monsterCharm4");
			}
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {
		// Insert existential void here
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
