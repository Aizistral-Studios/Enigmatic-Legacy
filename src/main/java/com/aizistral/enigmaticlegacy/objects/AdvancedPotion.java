package com.aizistral.enigmaticlegacy.objects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.effect.MobEffectInstance;

/**
 * Mimics vanilla Potion type, but is more minimalistic and manageable.
 * @author Integral
 */

public class AdvancedPotion {

	private List<MobEffectInstance> effects;
	private String id;

	public AdvancedPotion(String identifier, MobEffectInstance... effects) {

		this.effects = new ArrayList<MobEffectInstance>();

		for (MobEffectInstance effect : effects) {
			this.effects.add(effect);
		}

		this.id = identifier;
	}

	public String getId() {
		return this.id;
	}

	public List<MobEffectInstance> getEffects() {
		List<MobEffectInstance> returnList = new ArrayList<MobEffectInstance>();

		for (MobEffectInstance effect : this.effects) {
			returnList.add(new MobEffectInstance(effect));
		}

		return returnList;
	}

}
