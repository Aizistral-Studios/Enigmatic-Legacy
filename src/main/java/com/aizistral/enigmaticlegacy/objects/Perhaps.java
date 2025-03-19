package com.aizistral.enigmaticlegacy.objects;

import java.util.Random;

/**
 * A percentage-based value that can be converted into factor,
 * or 1.0 + factor in case it's required.
 * @author Integral
 */

public class Perhaps {

	private static final Random theySeeMeRollin = new Random();
	private int probability;

	public Perhaps(int probability) {
		this.probability = probability;
	}

	public boolean roll() {
		return Math.random() <= this.asMultiplier(false);
	}

	public int asPercentage() {
		return this.probability;
	}

	/**
	 * @param baseOne if true, adds 1.0D to return
	 * @return integer value / 100D, thus 25 for instance will return 0.25D
	 */

	public double asMultiplier(boolean baseOne) {
		if (baseOne)
			return 1.0D + (this.probability / 100D);
		else
			return this.probability / 100D;

	}

	/**
	 * @return 1.0D - (integer value/100D)
	 */

	public double asMultiplierInverted() {
		return 1.0D - (this.probability / 100D);
	}

	/**
	 * @param baseOne if true, adds 1.0F to return
	 * @return integer value / 100F, thus 25 for instance will return 0.25F
	 */

	public float asModifier(boolean baseOne) {
		if (baseOne)
			return 1.0F + (this.probability / 100F);
		else
			return this.probability / 100F;

	}

	/**
	 * @return 1.0f - (integer value/100F)
	 */

	public float asModifierInverted() {
		return 1.0F - (this.probability / 100F);
	}

	public float asModifier() {
		return this.asModifier(false);
	}

	public double asMultiplier() {
		return this.asMultiplier(false);
	}

	@Override
	public String toString() {
		return ""+this.probability;
	}

}
