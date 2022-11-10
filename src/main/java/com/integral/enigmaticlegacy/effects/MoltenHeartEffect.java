package com.integral.enigmaticlegacy.effects;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MoltenHeartEffect extends MobEffect {
	private static final List<String> IMMUNITIES = ImmutableList.of(DamageSource.LAVA.msgId,
			DamageSource.IN_FIRE.msgId, DamageSource.ON_FIRE.msgId, DamageSource.HOT_FLOOR.msgId);

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
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "molten_heart"));
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

	public boolean providesImmunity(DamageSource to) {
		return IMMUNITIES.contains(to.msgId);
	}

}

