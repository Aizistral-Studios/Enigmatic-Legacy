package com.integral.enigmaticlegacy;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

	@Override
	public void connect() {
		Mixins.addConfigurations("assets/enigmaticlegacy/enigmaticlegacy.mixins.json");

		try {
			Class.forName("com.aizistral.enigmaticlockbox.eXtErMINaTioN");
			Mixins.addConfigurations("assets/enigmaticlockbox/enigmaticlockbox.mixins.json");
		} catch (Exception ex) {
			System.out.println("No Enigmatic Lockbox found, proceeding without it...");
		}
	}

}
