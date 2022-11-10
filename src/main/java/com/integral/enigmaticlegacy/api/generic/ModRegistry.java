package com.integral.enigmaticlegacy.api.generic;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

@Retention(RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ModRegistry {
	// NO-OP
}
