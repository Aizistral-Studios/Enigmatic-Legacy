package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.ConfigurableItem;
import com.aizistral.enigmaticlegacy.enchantments.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.world.entity.EquipmentSlot.*;

public class EnigmaticEnchantments {
	private static final DeferredRegister<Enchantment> ENCHANTMENTS =
			DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, EnigmaticLegacy.MODID);

	@ConfigurableItem("Sharpshooter Enchantment")
	public static final RegistryObject<SharpshooterEnchantment> SHARPSHOOTER =
			register("sharpshooter", () -> new SharpshooterEnchantment(MAINHAND, OFFHAND));

	@ConfigurableItem("Ceaseless Enchantment")
	public static final RegistryObject<CeaselessEnchantment> CEASELESS =
			register("ceaseless", () -> new CeaselessEnchantment(MAINHAND, OFFHAND));

	@ConfigurableItem("Torrent Enchantment")
	public static final RegistryObject<TorrentEnchantment> TORRENT =
			register("torrent", () -> new TorrentEnchantment(MAINHAND));

	@ConfigurableItem("Wrath Enchantment")
	public static final RegistryObject<WrathEnchantment> WRATH =
			register("wrath", () -> new WrathEnchantment(MAINHAND));


	@ConfigurableItem("Slayer Enchantment")
	public static final RegistryObject<SlayerEnchantment> SLAYER =
			register("slayer", () -> new SlayerEnchantment(MAINHAND));

	@ConfigurableItem("Curse of Nemesis")
	public static final RegistryObject<NemesisCurse> NEMESIS =
			register("nemesis", () -> new NemesisCurse(MAINHAND));

	@ConfigurableItem("Curse of Eternal Binding")
	public static final RegistryObject<EternalBindingCurse> ETERNAL_BINDING =
			register("eternal_binding", () -> new EternalBindingCurse(HEAD, CHEST, LEGS, FEET));

	@ConfigurableItem("Curse of Sorrow")
	public static final RegistryObject<SorrowCurse> SORROW =
			register("sorrow", () -> new SorrowCurse(HEAD, CHEST, LEGS, FEET));

	public static void registerEnchantments(IEventBus modEventBus) {
		ENCHANTMENTS.register(modEventBus);
	}

	private static <T extends Enchantment> RegistryObject<T> register(String name, Supplier<T> enchantment) {
		return ENCHANTMENTS.register(name, enchantment);
	}
}
