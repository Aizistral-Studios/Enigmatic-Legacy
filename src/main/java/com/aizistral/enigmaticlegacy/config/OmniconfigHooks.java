package com.aizistral.enigmaticlegacy.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OmniconfigHooks {
	static final List<Consumer<Object>> HOOKS = new ArrayList<>();

	public static void addHook(Consumer<Object> hook) {
		HOOKS.add(hook);
	}

}
