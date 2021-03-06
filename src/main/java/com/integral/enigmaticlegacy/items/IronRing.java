package com.integral.enigmaticlegacy.items;

import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IronRing extends ItemBaseCurio {

	public IronRing() {
		super(ItemBaseCurio.getDefaultProperties());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "iron_ring"));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();
		atts.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("51faf191-bf72-4654-b349-cc1f4f1143bf"), "Armor bonus", 1.0, AttributeModifier.Operation.ADDITION));

		return atts;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

}
