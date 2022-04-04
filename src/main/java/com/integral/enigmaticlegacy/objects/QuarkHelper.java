package com.integral.enigmaticlegacy.objects;

import javax.annotation.Nullable;

public class QuarkHelper {
	private static final Class<?> MINI_BUTTON_CLASS;

	static {
		Class<?> button = null;

		try {
			button = Class.forName("vazkii.quark.content.management.client.screen.widgets.MiniInventoryButton");
		} catch (Exception ex) {
			// NO-OP
		}

		MINI_BUTTON_CLASS = button;
	}

	@Nullable
	public static Class<?> getMiniButtonClass() {
		return MINI_BUTTON_CLASS;
	}

}
