package com.aizistral.enigmaticlegacy.effects;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.ImmutableList;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MoltenHeartEffect extends MobEffect {
	private static final List<ResourceKey<DamageType>> IMMUNITIES = ImmutableList.of(DamageTypes.LAVA,
			DamageTypes.IN_FIRE, DamageTypes.ON_FIRE, DamageTypes.HOT_FLOOR);

	public static Omniconfig.DoubleParameter lavafogDensity;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("MoltenHeart");

		if (builder.config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
			lavafogDensity = builder
					.comment("Controls how obscured your vision is in lava when Molten Heart effect is active. Higher value equals more visibility.")
					.max(1024)
					.clientOnly()
					.getDouble("LavaDensity", 6);
		}

		builder.popPrefix();
	}

	public MoltenHeartEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xF28E0C);
	}

	@Override
	public void applyEffectTick(LivingEntity living, int amplifier) {
		if (living.isOnFire()) {
			living.clearFire();
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	public boolean providesImmunity(DamageSource source) {
		return IMMUNITIES.stream().anyMatch(source::is);
	}

}

