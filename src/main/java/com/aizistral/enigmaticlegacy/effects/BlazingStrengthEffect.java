package com.aizistral.enigmaticlegacy.effects;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BlazingStrengthEffect extends AttackDamageMobEffect {

	public BlazingStrengthEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xFF5000, 3.0);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "9D86B288-C0E2-45DD-95BF-AE43ED7E2116", 0.0D, AttributeModifier.Operation.ADDITION);
	}

}
