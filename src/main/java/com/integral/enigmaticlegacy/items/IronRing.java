package com.integral.enigmaticlegacy.items;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.capability.ICurio;

public class IronRing extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();

	public IronRing(Properties properties) {
		super(properties);
	}

	public static Properties setupIntegratedProperties() {
		IronRing.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		IronRing.integratedProperties.maxStackSize(1);
		IronRing.integratedProperties.rarity(Rarity.COMMON);

		return IronRing.integratedProperties;

	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.IRON_RING_ENABLED.getValue();
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.ironRing))
			return false;
		else
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

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		// Insert existential void here
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {

		Multimap<String, AttributeModifier> atts = HashMultimap.create();

		atts.put(SharedMonsterAttributes.ARMOR.getName(), new AttributeModifier(UUID.fromString("51faf191-bf72-4654-b349-cc1f4f1143bf"), "Armor bonus", 1.0, AttributeModifier.Operation.ADDITION));

		return atts;
	}

}
