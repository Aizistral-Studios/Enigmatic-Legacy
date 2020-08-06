package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ForbiddenAxe extends SwordItem implements IPerhaps {

	public ForbiddenAxe() {
		super(EnigmaticMaterials.FORBIDDENAXE, 6, -2.4F, ItemBaseTool.getDefaultProperties().defaultMaxDamage(2000).rarity(Rarity.EPIC).func_234689_a_());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "forbidden_axe"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.FORBIDDEN_AXE_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe2", TextFormatting.GOLD, ConfigHandler.FORBIDDEN_AXE_BEHEADING_BONUS.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);

		try {
			if (SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.monsterCharm))
				if (ConfigHandler.MONSTER_CHARM_BONUS_LOOTING.getValue())
					looting++;
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxeBeheadingChance", TextFormatting.GOLD, (ConfigHandler.FORBIDDEN_AXE_BEHEADING_BASE.getValue().asPercentage() + (ConfigHandler.FORBIDDEN_AXE_BEHEADING_BONUS.getValue().asPercentage() * looting)) + "%");
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
		return false;
	}

}
