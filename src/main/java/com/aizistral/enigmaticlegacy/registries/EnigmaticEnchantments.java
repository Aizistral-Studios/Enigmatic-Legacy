package com.aizistral.enigmaticlegacy.registries;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import static net.minecraft.world.entity.EquipmentSlot.*;

import com.aizistral.enigmaticlegacy.api.generic.ConfigurableItem;
import com.aizistral.enigmaticlegacy.enchantments.*;

public class EnigmaticEnchantments extends AbstractRegistry<Enchantment> {
	private static final EnigmaticEnchantments INSTANCE = new EnigmaticEnchantments();

	@ConfigurableItem("Sharpshooter Enchantment")
	@ObjectHolder(value = MODID + ":sharpshooter", registryName = "enchantment")
	public static final SharpshooterEnchantment SHARPSHOOTER = null;

	@ConfigurableItem("Ceaseless Enchantment")
	@ObjectHolder(value = MODID + ":ceaseless", registryName = "enchantment")
	public static final CeaselessEnchantment CEASELESS = null;

	@ConfigurableItem("Torrent Enchantment")
	@ObjectHolder(value = MODID + ":torrent", registryName = "enchantment")
	public static final TorrentEnchantment TORRENT = null;

	@ConfigurableItem("Wrath Enchantment")
	@ObjectHolder(value = MODID + ":wrath", registryName = "enchantment")
	public static final WrathEnchantment WRATH = null;

	@ConfigurableItem("Slayer Enchantment")
	@ObjectHolder(value = MODID + ":slayer", registryName = "enchantment")
	public static final SlayerEnchantment SLAYER = null;

	@ConfigurableItem("Curse of Nemesis")
	@ObjectHolder(value = MODID + ":nemesis", registryName = "enchantment")
	public static final NemesisCurse NEMESIS = null;

	@ConfigurableItem("Curse of Eternal Binding")
	@ObjectHolder(value = MODID + ":eternal_binding", registryName = "enchantment")
	public static final EternalBindingCurse ETERNAL_BINDING = null;

	@ConfigurableItem("Curse of Sorrow")
	@ObjectHolder(value = MODID + ":sorrow", registryName = "enchantment")
	public static final SorrowCurse SORROW = null;

	private EnigmaticEnchantments() {
		super(ForgeRegistries.ENCHANTMENTS);

		this.register("sharpshooter", () -> new SharpshooterEnchantment(MAINHAND, OFFHAND));
		this.register("ceaseless", () -> new CeaselessEnchantment(MAINHAND, OFFHAND));
		this.register("nemesis", () -> new NemesisCurse(MAINHAND));
		this.register("torrent", () -> new TorrentEnchantment(MAINHAND));
		this.register("wrath", () -> new WrathEnchantment(MAINHAND));
		this.register("slayer", () -> new SlayerEnchantment(MAINHAND));
		this.register("eternal_binding", () -> new EternalBindingCurse(HEAD, CHEST, LEGS, FEET));
		this.register("sorrow", () -> new SorrowCurse(HEAD, CHEST, LEGS, FEET));
	}

}
