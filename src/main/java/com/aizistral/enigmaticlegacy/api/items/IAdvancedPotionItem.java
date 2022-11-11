package com.aizistral.enigmaticlegacy.api.items;

public interface IAdvancedPotionItem {

	public enum PotionType {
		COMMON, ULTIMATE;
	}

	public PotionType getPotionType();

}
