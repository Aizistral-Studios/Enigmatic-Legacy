package com.integral.enigmaticlegacy.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public interface IComplexParameter<WhatIAm> {

	public void reset();

	@SuppressWarnings("unchecked")
	default WhatIAm setClientOnly() {
		DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> {
			return () -> {
				ConfigHandler.allValues.remove(this);
			};
		});
		return (WhatIAm) this;
	}

}
