package com.aizistral.enigmaticlegacy.objects;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

public class DamageSourceNemesisCurse extends EntityDamageSource {

	public DamageSourceNemesisCurse(Entity attacker) {
		super("thorns", attacker);

		this.setMagic();
	}

}
