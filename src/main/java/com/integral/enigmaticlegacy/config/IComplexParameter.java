package com.integral.enigmaticlegacy.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public interface IComplexParameter<ParameterType> {

	public void reset();

	@SuppressWarnings("unchecked")
	default ParameterType setClientOnly() {
		DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> {
			return () -> {
				System.out.println("executed!!!!");
				ConfigHandler.allValues.remove(this);
			};
		});
		return (ParameterType) this;
	}

}
