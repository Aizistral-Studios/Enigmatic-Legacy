package com.integral.enigmaticlegacy.objects;

import static com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack.getRegisteredMeleeAttack;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerEntity;

public class RegisteredMeleeAttack {
	private static final Map<PlayerEntity, RegisteredMeleeAttack> attackRegistry = new WeakHashMap<>();

	public static boolean hasRegisteredMeleeAttack(PlayerEntity player) {
		return attackRegistry.containsKey(player);
	}

	public static RegisteredMeleeAttack getRegisteredMeleeAttack(PlayerEntity player) {
		return attackRegistry.get(player);
	}

	public static void registerAttack(PlayerEntity player) {
		attackRegistry.put(player, new RegisteredMeleeAttack(player));
	}

	public static float getRegisteredAttackStregth(LivingEntity attacker) {
		float attackStregth = 1F;
		if (attacker instanceof PlayerEntity && hasRegisteredMeleeAttack((PlayerEntity) attacker)) {
			RegisteredMeleeAttack attack = getRegisteredMeleeAttack((PlayerEntity)attacker);
			if (attack.ticksExisted == attacker.tickCount) {
				attackStregth = attack.cooledAttackStrength;
			}
		}

		return attackStregth;
	}

	public static void clearRegistry() {
		attackRegistry.clear();
	}

	public final PlayerEntity player;
	public final int ticksSinceLastSwing;
	public final int ticksExisted;
	public final float cooledAttackStrength;

	private RegisteredMeleeAttack(PlayerEntity player) {
		this.player = player;

		this.ticksExisted = player.tickCount;
		this.ticksSinceLastSwing = player.attackStrengthTicker;
		this.cooledAttackStrength = player.getAttackStrengthScale(0.5F);
	}



}
