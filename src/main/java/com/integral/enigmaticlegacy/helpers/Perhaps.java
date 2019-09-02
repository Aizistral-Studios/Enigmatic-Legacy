package com.integral.enigmaticlegacy.helpers;

public class Perhaps {
	
	private int probability;
	
	public Perhaps(int probability) {
		this.probability = probability;
	}
	
	public int asPercentage() {
		return probability;
	}
	
	public double asMultiplier(boolean baseOne) {
		if (baseOne)
			return 1.0D + (this.probability/100D);
		else
			return this.probability/100D;
		 
	}
	
	public double asMultiplierInverted() {
		return 1.0D - (this.probability/100D);
	}
	
	public float asModifier(boolean baseOne) {
		if (baseOne)
			return 1.0F + (this.probability/100F);
		else
			return this.probability/100F;
		 
	}
	
	public float asModifierInverted() {
			return 1.0F - (this.probability/100F);
	}

}
