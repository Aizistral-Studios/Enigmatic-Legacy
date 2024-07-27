package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.api.generic.ModRegistry;
import com.aizistral.enigmaticlegacy.effects.*;
import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberContainer;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticEffects extends AbstractRegistry<MobEffect> {
	private static final EnigmaticEffects INSTANCE = new EnigmaticEffects();

	@ObjectHolder(value = MODID + ":blazing_strength", registryName = "mob_effect")
	public static final BlazingStrengthEffect BLAZING_STRENGTH = null;

	@ObjectHolder(value = MODID + ":molten_heart", registryName = "mob_effect")
	public static final MoltenHeartEffect MOLTEN_HEART = null;

	@ObjectHolder(value = MODID + ":growing_hunger", registryName = "mob_effect")
	public static final GrowingHungerEffect GROWING_HUNGER = null;

	@ObjectHolder(value = MODID + ":growing_bloodlust", registryName = "mob_effect")
	public static final GrowingBloodlustEffect GROWING_BLOODLUST = null;

	private EnigmaticEffects() {
		super(ForgeRegistries.MOB_EFFECTS);
		this.register("blazing_strength", BlazingStrengthEffect::new);
		this.register("molten_heart", MoltenHeartEffect::new);
		this.register("growing_hunger", GrowingHungerEffect::new);
		this.register("growing_bloodlust", GrowingBloodlustEffect::new);
	}

}
