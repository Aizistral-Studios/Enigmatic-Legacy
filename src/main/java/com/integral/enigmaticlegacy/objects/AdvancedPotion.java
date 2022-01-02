package com.integral.enigmaticlegacy.objects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.alchemy.EffectInstance;

/**
 * Mimics vanilla Potion type, but is more minimalistic and manageable.
 * @author Integral
 */

public class AdvancedPotion {

	private List<EffectInstance> effects;
	private String id;

	public AdvancedPotion(String identifier, EffectInstance... effects) {

		this.effects = new ArrayList<EffectInstance>();

		for (EffectInstance effect : effects)
			this.effects.add(effect);

		this.id = identifier;
	}

	public String getId() {
		return this.id;
	}

	public List<EffectInstance> getEffects() {
		List<EffectInstance> returnList = new ArrayList<EffectInstance>();

		for (EffectInstance effect : this.effects)
			returnList.add(new EffectInstance(effect));

		return returnList;
	}

}
