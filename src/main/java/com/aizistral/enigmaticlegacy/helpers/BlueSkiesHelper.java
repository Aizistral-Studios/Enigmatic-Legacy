package com.aizistral.enigmaticlegacy.helpers;

import java.lang.reflect.Method;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.entity.player.Player;

public class BlueSkiesHelper {
	private static final Class<?> SKIES_PLAYER;
	private static final Class<?> SKIES_PLAYER_INTERFACE;
	private static final Method SET_HEALTH;
	private static final Method GET_HEALTH;
	private static final Method GET_CAPABILITY;
	private static final boolean MOD_PRESENT;

	static {
		Class<?> player;
		Class<?> iface;
		Method setHealth;
		Method getHealth;
		Method getCapability;

		try {
			player = Class.forName("com.legacy.blue_skies.capability.SkiesPlayer");
			iface = Class.forName("com.legacy.blue_skies.capability.util.ISkiesPlayer");
			setHealth = iface.getMethod("setNatureHealth", float.class);
			getHealth = iface.getMethod("getNatureHealth");
			getCapability = player.getMethod("get", Player.class);
		} catch (Throwable ex) {
			player = null;
			iface = null;
			setHealth = null;
			getHealth = null;
			getCapability = null;
		}

		SKIES_PLAYER = player;
		SKIES_PLAYER_INTERFACE = iface;
		SET_HEALTH = setHealth;
		GET_HEALTH = getHealth;
		GET_CAPABILITY = getCapability;
		MOD_PRESENT = player != null && iface != null ? true : false;

		EnigmaticLegacy.LOGGER.info("Blue Skies detected: " + MOD_PRESENT);
	}

	public static void maybeFixCapability(Player player) {
		if (!MOD_PRESENT)
			return;

		try {
			Object capability = GET_CAPABILITY.invoke(null, player);

			if (capability != null) {
				Object maybeHealth = GET_HEALTH.invoke(capability);

				if (maybeHealth instanceof Float) {
					Float health = (Float) maybeHealth;

					if (health.isNaN()) {
						SET_HEALTH.invoke(capability, 0F);
						player.setHealth(0F);
						EnigmaticLegacy.LOGGER.info("Fixed NaN natural health for player: " + player.getGameProfile().getName());
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}