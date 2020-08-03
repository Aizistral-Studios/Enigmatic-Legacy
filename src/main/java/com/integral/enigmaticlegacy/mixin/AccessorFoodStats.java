package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.util.FoodStats;

@Mixin(FoodStats.class)
public interface AccessorFoodStats {
	
	@Accessor("foodSaturationLevel")
	void setFoodSaturationLevel(float foodSaturationLevel);
	
}
