package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTabs;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class ForbiddenAxe extends SwordItem implements ICreativeTabMember {
	public static Omniconfig.PerhapsParameter beheadingBase;
	public static Omniconfig.PerhapsParameter beheadingBonus;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("ForbiddenAxe");

		beheadingBase = builder
				.comment("Default chance to behead an enemy with Axe of Executioner. Defined as percentage.")
				.max(100)
				.getPerhaps("BeheadingBase", 10);

		beheadingBonus = builder
				.comment("Bonus percantage to beheading chance from each Looting level applied to Axe of Executioner.")
				.max(100)
				.getPerhaps("BeheadingBonus", 5);

		builder.popPrefix();
	}

	public ForbiddenAxe() {
		super(EnigmaticMaterials.FORBIDDEN_AXE, 6, -2.4F, ItemBaseTool.getDefaultProperties().defaultDurability(2000).rarity(Rarity.EPIC).fireResistant());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe2", ChatFormatting.GOLD, beheadingBonus.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		int looting = 0;

		if (worldIn != null) {
			looting = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, stack);
		}

		if (Minecraft.getInstance().player != null) {
			ICuriosItemHandler handler = CuriosApi.getCuriosHelper().getCuriosHandler(Minecraft.getInstance().player).orElse(null);

			if (handler != null && worldIn != null) {
				looting += handler.getLootingLevel(worldIn.damageSources().generic(), Minecraft.getInstance().player, looting);
			}
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxeBeheadingChance", ChatFormatting.GOLD, (beheadingBase.getValue().asPercentage() + (beheadingBonus.getValue().asPercentage() * looting)) + "%");
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState blockIn) {
		return false;
	}

	@Override
	public @Nullable CreativeModeTab getCreativeTab() {
		return EnigmaticTabs.MAIN;
	}

}
