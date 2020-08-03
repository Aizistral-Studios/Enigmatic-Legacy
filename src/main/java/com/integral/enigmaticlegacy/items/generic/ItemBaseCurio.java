package com.integral.enigmaticlegacy.items.generic;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.capability.ICurio;

public abstract class ItemBaseCurio extends ItemBase implements ICurio {

	public ItemBaseCurio() {
		this(ItemBaseCurio.getDefaultProperties());
	}

	public ItemBaseCurio(Properties props) {
		super(props);
	}

	@Override
	public void onEquipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		// Insert existential void here
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, this))
			return false;
		else
			return true;

	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity living) {
		return true;
	}

	@Override
	public DropRule getDropRule(LivingEntity livingEntity) {
		return DropRule.DEFAULT;
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.group(EnigmaticLegacy.enigmaticTab);
		props.maxStackSize(1);
		props.rarity(Rarity.COMMON);

		return props;
	}
}
