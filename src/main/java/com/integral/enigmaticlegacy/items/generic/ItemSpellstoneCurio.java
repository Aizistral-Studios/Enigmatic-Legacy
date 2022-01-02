package com.integral.enigmaticlegacy.items.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.World;

import net.minecraft.world.item.Item.Properties;

public abstract class ItemSpellstoneCurio extends ItemBaseCurio implements ISpellstone {
	public static Omniconfig.BooleanParameter multiequip;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("Spellstones");

		multiequip = builder.comment("Whether or not it should be allowed to equip multiple spellstones if they are different items," + " granted player somehow gets more than one spellstone slot.").getBoolean("Multiequip", false);

		builder.popPrefix();
	}

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
	public boolean canEquip(String identifier, LivingEntity living, ItemStack stack) {
		if (multiequip.getValue())
			return super.canEquip(identifier, living, stack);
		else
			return super.canEquip(identifier, living, stack) && SuperpositionHandler.getSpellstone(living) == null;
	}

}
