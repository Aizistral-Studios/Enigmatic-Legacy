package com.aizistral.enigmaticlegacy.enchantments;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.enigmaticlegacy.api.items.IBindable;
import org.apache.commons.lang3.StringUtils;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

public class EternalBindingCurse extends Enchantment {
	private final List<String> incompatibleKeywords = new ArrayList<>();

	public EternalBindingCurse(EquipmentSlot... slots) {
		super(Enchantment.Rarity.RARE, EnchantmentCategory.WEARABLE, slots);

		this.incompatibleKeywords.add("soulbound");
		this.incompatibleKeywords.add("soulbinding");
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 25;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return 50;
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (!OmniconfigHandler.isItemEnabled(this))
			return false;

		if (stack.is(EnigmaticItems.CURSED_RING) || stack.is(EnigmaticItems.ESCAPE_SCROLL) ||
				stack.is(EnigmaticItems.ENIGMATIC_AMULET) || stack.is(EnigmaticItems.DESOLATION_RING))
			return false;

		return stack.getItem() instanceof IBindable || super.canEnchant(stack);
	}

	@Override
	public boolean isTreasureOnly() {
		return true;
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return OmniconfigHandler.isItemEnabled(this);
	}

	@Override
	public boolean isDiscoverable() {
		return false;
	}

	@Override
	protected boolean checkCompatibility(Enchantment ench) {
		if (this.incompatibleKeywords.stream().anyMatch(keyword ->
		StringUtils.containsIgnoreCase(keyword, ForgeRegistries.ENCHANTMENTS.getKey(ench).getPath())))
			return false;

		return ench != Enchantments.BINDING_CURSE && ench != Enchantments.VANISHING_CURSE ? super.checkCompatibility(ench) : false;
	}

}
