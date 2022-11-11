package com.aizistral.enigmaticlegacy.items;

import java.util.UUID;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class IronRing extends ItemBaseCurio {

	public IronRing() {
		super(ItemBaseCurio.getDefaultProperties());
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atts = HashMultimap.create();
		atts.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("51faf191-bf72-4654-b349-cc1f4f1143bf"), "Armor bonus", 1.0, AttributeModifier.Operation.ADDITION));

		return atts;
	}

}
