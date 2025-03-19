package com.aizistral.enigmaticlegacy.api.generic;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;

/**
 * This annotation is used on public static fields to automatically populate
 * accessibility config with options for all objects that annotated fields hold.
 * {@link OmniconfigHandler#isItemEnabled(Object)} presents a way to use it later;
 * object passed as argument is checked on whether or not it has associated field
 * and respective config option for itself, and if so, returns value of that config
 * option.<br/><br/>
 *
 * Multiple fields marked with this annotation can refer to the same accessibility
 * option.
 * @author Integral
 */


@Retention(RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ConfigurableItem {

	/**
	 * @return Human-readable object name that will appear in config, as part of
	 * comment on generated accessibility option. Specifically, it will look like
	 * "Whether or not {@link #value()} should be enabled."
	 */

	public String value() default "";

}
