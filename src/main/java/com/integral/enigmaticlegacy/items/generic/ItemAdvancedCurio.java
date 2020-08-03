package com.integral.enigmaticlegacy.items.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public abstract class ItemAdvancedCurio extends ItemBaseCurio {

	public List<String> immunityList = new ArrayList<String>();
	public HashMap<String, Supplier<Float>> resistanceList = new HashMap<String, Supplier<Float>>();

	public ItemAdvancedCurio() {
		this(ItemAdvancedCurio.getDefaultProperties());
	}

	public ItemAdvancedCurio(Properties props) {
		super(props);
	}

	public boolean isResistantTo(String damageType) {
		return this.resistanceList.containsKey(damageType);
	}

	public boolean isImmuneTo(String damageType) {
		return this.immunityList.contains(damageType);
	}

	public Supplier<Float> getResistanceModifier(String damageType) {
		return this.resistanceList.get(damageType);
	}

	public static Properties getDefaultProperties() {
		return ItemBaseCurio.getDefaultProperties();
	}

}
