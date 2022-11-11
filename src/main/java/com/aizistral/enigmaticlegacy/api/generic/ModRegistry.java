package com.aizistral.enigmaticlegacy.api.generic;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

@Retention(RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ModRegistry {
	// NO-OP
}
