package com.aizistral.enigmaticlegacy.objects;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RegisteredMeleeAttack {
	private static final Map<Player, RegisteredMeleeAttack> attackRegistry = new WeakHashMap<>();

	public static boolean hasRegisteredMeleeAttack(Player player) {
		return attackRegistry.containsKey(player);
	}

	public static RegisteredMeleeAttack getRegisteredMeleeAttack(Player player) {
		return attackRegistry.get(player);
	}

	public static void registerAttack(Player player) {
		attackRegistry.put(player, new RegisteredMeleeAttack(player));
	}

	public static float getRegisteredAttackStregth(LivingEntity attacker) {
		float attackStregth = 1F;
		if (attacker instanceof Player && hasRegisteredMeleeAttack((Player) attacker)) {
			RegisteredMeleeAttack attack = getRegisteredMeleeAttack((Player)attacker);
			if (attack.ticksExisted == attacker.tickCount) {
				attackStregth = attack.cooledAttackStrength;
			}
		}

		return attackStregth;
	}

	public static void clearRegistry() {
		attackRegistry.clear();
	}

	public final Player player;
	public final int ticksSinceLastSwing;
	public final int ticksExisted;
	public final float cooledAttackStrength;

	private RegisteredMeleeAttack(Player player) {
		this.player = player;

		this.ticksExisted = player.tickCount;
		this.ticksSinceLastSwing = player.attackStrengthTicker;
		this.cooledAttackStrength = player.getAttackStrengthScale(0.5F);
	}



}
