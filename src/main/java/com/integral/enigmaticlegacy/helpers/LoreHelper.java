package com.integral.enigmaticlegacy.helpers;

import java.util.List;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * A couple of methods for more convenient work with addInformation() method.
 * @author Integral
 */

public class LoreHelper {
	
	public static void addLocalizedString(List<ITextComponent> list, String str) {
		list.add(new TranslationTextComponent(str));
	}
	
	public static void addLocalizedString(List<ITextComponent> list, String str, Object... values) {
		list.add(new TranslationTextComponent(str, values));
	}

}
