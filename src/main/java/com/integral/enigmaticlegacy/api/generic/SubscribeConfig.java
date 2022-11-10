package com.integral.enigmaticlegacy.api.generic;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

/**
 * This is used to annotate methods that should receive OmniconfigWrapper object
 * when {@link SuperpositionHandler#dispatchWrapperToHolders(String, OmniconfigWrapper)}
 * is called in {@link OmniconfigHandler#loadCommon(OmniconfigWrapper)} &
 * {@link OmniconfigHandler#loadClient(OmniconfigWrapper)}.
 * @author Integral
 */

@Retention(RUNTIME)
@Target(value = ElementType.METHOD)
public @interface SubscribeConfig {
	public static final boolean defaultReceiveClient = false;

	/**
	 * @return By default only wrapper for common file is dispatched, by some handlers define themselves
	 * to receive client wrapper through overriding return value of this method.
	 */

	boolean receiveClient() default defaultReceiveClient;
}
