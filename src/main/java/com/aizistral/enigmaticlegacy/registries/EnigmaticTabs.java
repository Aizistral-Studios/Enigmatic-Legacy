package com.aizistral.enigmaticlegacy.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.blocks.BlockAstralDust;
import com.aizistral.enigmaticlegacy.blocks.BlockBigLamp;
import com.aizistral.enigmaticlegacy.blocks.BlockCosmicCake;
import com.aizistral.enigmaticlegacy.blocks.BlockEndAnchor;
import com.aizistral.enigmaticlegacy.blocks.BlockMassiveLamp;
import com.aizistral.enigmaticlegacy.items.CosmicCake;
import com.aizistral.enigmaticlegacy.items.EndAnchor;
import com.aizistral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.aizistral.etherium.blocks.BlockEtherium;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticTabs extends AbstractRegistry<CreativeModeTab> {
	private static final EnigmaticTabs INSTANCE = new EnigmaticTabs();

	@ObjectHolder(value = MODID + ":tab_main", registryName = "creative_mode_tab")
	public static final CreativeModeTab MAIN = null;

	@ObjectHolder(value = MODID + ":tab_potions", registryName = "creative_mode_tab")
	public static final CreativeModeTab POTIONS = null;

	private EnigmaticTabs() {
		super(Registries.CREATIVE_MODE_TAB);

		this.register("tab_potions", () -> CreativeModeTab.builder().title(Component.translatable(
				"itemGroup.enigmaticPotionCreativeTab")).icon(() -> new ItemStack(EnigmaticItems.RECALL_POTION)).build());

		this.register("tab_main", () -> CreativeModeTab.builder().title(Component.translatable(
				"itemGroup.enigmaticCreativeTab")).icon(() -> new ItemStack(EnigmaticItems.ENIGMATIC_ITEM)).build());
	}

}
