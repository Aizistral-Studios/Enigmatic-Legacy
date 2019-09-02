package com.integral.enigmaticlegacy.helpers;

import java.util.HashMap;

import net.minecraft.entity.LivingEntity;

public class CooldownMap extends HashMap<LivingEntity, Integer> {

	private static final long serialVersionUID = 1159860520734947286L;
	
	public void tick(LivingEntity entity) {
		if (this.containsKey(entity)) {
			if (this.get(entity) > 0)
				this.put(entity, this.get(entity)-1);
		} else
			this.put(entity, 0);
	}
	
	public boolean hasCooldown(LivingEntity entity) {
		if (this.containsKey(entity))
			if (this.get(entity) > 0)
				return true;
		
		return false;
	}
	
	public int getCooldown(LivingEntity entity) {
		if (this.containsKey(entity))
			return this.get(entity);
		else {
			this.put(entity, 0);
			return 0;
		}
	}
}
