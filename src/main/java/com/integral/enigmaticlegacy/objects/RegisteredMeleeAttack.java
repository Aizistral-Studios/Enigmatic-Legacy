package com.integral.enigmaticlegacy.objects;

import static com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack.getRegisteredMeleeAttack;

import java.util.HashMap;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class RegisteredMeleeAttack {
	private static final HashMap<PlayerEntity, RegisteredMeleeAttack> attackRegistry = new HashMap<>();

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
			if (attack.ticksExisted == attacker.ticksExisted) {
				attackStregth = attack.cooledAttackStrength;
			}
		}

		return attackStregth;
	}

	public final PlayerEntity player;
	public final int ticksSinceLastSwing;
	public final int ticksExisted;
	public final float cooledAttackStrength;

	private RegisteredMeleeAttack(PlayerEntity player) {
		this.player = player;

		this.ticksExisted = player.ticksExisted;
		this.ticksSinceLastSwing = player.ticksSinceLastSwing;
		this.cooledAttackStrength = player.getCooledAttackStrength(0.5F);
	}



}
