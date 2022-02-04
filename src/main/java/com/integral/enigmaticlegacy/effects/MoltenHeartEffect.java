package com.integral.enigmaticlegacy.effects;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MoltenHeartEffect extends MobEffect {
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
		super(MobEffectCategory.BENEFICIAL, 0xF43900);
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "molten_heart"));
	}

	@Override
	public void applyEffectTick(LivingEntity living, int amplifier) {
		if (living.isOnFire()) {
			living.clearFire();
		}
	}

}

