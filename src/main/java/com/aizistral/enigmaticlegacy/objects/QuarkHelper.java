package com.aizistral.enigmaticlegacy.objects;

import javax.annotation.Nullable;

public class QuarkHelper {
	private static final Class<?> MINI_BUTTON_CLASS;
	private static final Class<?> MATRIX_ENCHANTER_CLASS;

	static {
		Class<?> button = null;
		Class<?> matrix = null;

		try {
			button = Class.forName("vazkii.quark.content.management.client.screen.widgets.MiniInventoryButton");
			matrix = Class.forName("vazkii.quark.addons.oddities.client.screen.MatrixEnchantingScreen");
		} catch (Exception ex) {
			// NO-OP
		}

		MINI_BUTTON_CLASS = button;
		MATRIX_ENCHANTER_CLASS = matrix;
	}

	@Nullable
	public static Class<?> getMiniButtonClass() {
		return MINI_BUTTON_CLASS;
	}

	public static Class<?> getMatrixEnchanterClass() {
		return MATRIX_ENCHANTER_CLASS;
	}

}
