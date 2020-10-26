package com.integral.enigmaticlegacy.items.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemSpellstoneCurio extends ItemBaseCurio implements ISpellstone {

	public List<String> immunityList = new ArrayList<String>();
	public HashMap<String, Supplier<Float>> resistanceList = new HashMap<String, Supplier<Float>>();

	public ItemSpellstoneCurio() {
		this(ItemSpellstoneCurio.getDefaultProperties());
	}

	public ItemSpellstoneCurio(Properties props) {
		super(props);
	}

	public boolean isResistantTo(String damageType) {
		return this.resistanceList.containsKey(damageType);
	}

	public boolean isImmuneTo(String damageType) {
		return this.immunityList.contains(damageType);
	}

	public Supplier<Float> getResistanceModifier(String damageType) {
		return this.resistanceList.get(damageType);
	}

	public static Properties getDefaultProperties() {
		return ItemBaseCurio.getDefaultProperties();
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		return super.canEquip(identifier, living) && SuperpositionHandler.getSpellstone(living) == null;
	}

}
