package com.integral.enigmaticlegacy.registry;

import com.integral.enigmaticlegacy.api.generic.ModRegistry;
import com.integral.enigmaticlegacy.effects.*;
import com.integral.enigmaticlegacy.gui.containers.LoreInscriberContainer;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticEffects extends AbstractRegistry<MobEffect> {
	private static final EnigmaticEffects INSTANCE = new EnigmaticEffects();

	@ObjectHolder(value = MODID + ":blazing_strength", registryName = "mob_effect")
	public static final BlazingStrengthEffect blazingStrengthEffect = null;

	@ObjectHolder(value = MODID + ":molten_heart", registryName = "mob_effect")
	public static final MoltenHeartEffect moltenHeartEffect = null;

	private EnigmaticEffects() {
		super(ForgeRegistries.MOB_EFFECTS);
		this.register("blazing_strength", BlazingStrengthEffect::new);
		this.register("molten_heart", MoltenHeartEffect::new);
	}

}
