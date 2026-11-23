package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EnigmaticTabs {

	private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
			DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EnigmaticLegacy.MODID);

	public static final RegistryObject<CreativeModeTab> MAIN =
			register("tab_main", () -> CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.enigmaticCreativeTab"))
					.icon(() -> new ItemStack(EnigmaticItems.ENIGMATIC_ITEM.get()))
			);

	private static RegistryObject<CreativeModeTab> register(String name, Supplier<CreativeModeTab.Builder> builder) {
		return CREATIVE_MODE_TABS.register(name, () -> builder.get().build());
	}

	public static void registerTabs(IEventBus modEventBus) {
		CREATIVE_MODE_TABS.register(modEventBus);
	}
}
