package com.aizistral.enigmaticlegacy.registries;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.client.Quote;
import com.mojang.serialization.Codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

public abstract class AbstractRegistry<T> {
	protected static final String MODID = EnigmaticLegacy.MODID;
	private final DeferredRegister<T> register;

	protected AbstractRegistry(ResourceKey<Registry<T>> registry) {
		this.register = DeferredRegister.create(registry, MODID);
		this.register.register(FMLJavaModLoadingContext.get().getModEventBus());
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterEvent);
	}

	protected AbstractRegistry(IForgeRegistry<T> registry) {
		this(registry.getRegistryKey());
	}

	protected void register(String name, Supplier<T> supplier) {
		this.register.register(name, supplier);
	}

	private void onRegisterEvent(RegisterEvent event) {
		if (event.getRegistryKey() == this.register.getRegistryKey()) {
			this.onRegister(event);
		}
	}

	/**
	 * Called when {@link RegisterEvent} for appropriate registry is dispatched.
	 * @param event
	 */

	protected void onRegister(RegisterEvent event) {
		// NO-OP
	}

}
