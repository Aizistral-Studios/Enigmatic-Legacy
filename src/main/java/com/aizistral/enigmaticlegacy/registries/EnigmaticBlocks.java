package com.aizistral.enigmaticlegacy.registries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.api.generic.ModRegistry;
import com.aizistral.enigmaticlegacy.blocks.*;
import com.aizistral.enigmaticlegacy.items.CosmicCake;
import com.aizistral.enigmaticlegacy.items.EndAnchor;
import com.aizistral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.aizistral.etherium.blocks.BlockEtherium;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticBlocks extends AbstractRegistry<Block> {
	private static final Map<ResourceLocation, BlockItemSupplier> BLOCK_ITEM_MAP = new HashMap<>();
	private static final EnigmaticBlocks INSTANCE = new EnigmaticBlocks();

	@ObjectHolder(value = MODID + ":massive_lamp", registryName = "block")
	public static final BlockMassiveLamp MASSIVE_LAMP = null;

	@ObjectHolder(value = MODID + ":massive_shroomlamp", registryName = "block")
	public static final BlockMassiveLamp MASSIVE_SHROOMLAMP = null;

	@ObjectHolder(value = MODID + ":massive_redstonelamp", registryName = "block")
	public static final BlockMassiveLamp MASSIVE_REDSTONELAMP = null;

	@ObjectHolder(value = MODID + ":big_lamp", registryName = "block")
	public static final BlockBigLamp BIG_LAMP = null;

	@ObjectHolder(value = MODID + ":big_shroomlamp", registryName = "block")
	public static final BlockBigLamp BIG_SHROOMLAMP = null;

	@ObjectHolder(value = MODID + ":big_redstonelamp", registryName = "block")
	public static final BlockBigLamp BIG_REDSTONELAMP = null;

	@ObjectHolder(value = MODID + ":etherium_block", registryName = "block")
	public static final BlockEtherium ETHERIUM_BLOCK = null;

	@ObjectHolder(value = MODID + ":cosmic_cake", registryName = "block")
	public static final BlockCosmicCake COSMIC_CAKE = null;

	@ObjectHolder(value = MODID + ":astral_block", registryName = "block")
	public static final BlockAstralDust ASTRAL_BLOCK = null;

	@ObjectHolder(value = MODID + ":end_anchor", registryName = "block")
	public static final BlockEndAnchor END_ANCHOR = null;

	@ObjectHolder(value = MODID + ":star_fabric", registryName = "block")
	public static final BlockStarFabric STAR_FABRIC = null;

	private EnigmaticBlocks() {
		super(ForgeRegistries.BLOCKS);
		this.register("massive_lamp", BlockMassiveLamp::new, GenericBlockItem::new);
		this.register("massive_shroomlamp", BlockMassiveLamp::new, GenericBlockItem::new);
		this.register("massive_redstonelamp", BlockMassiveLamp::new, GenericBlockItem::new);
		this.register("big_lamp", BlockBigLamp::new, GenericBlockItem::new);
		this.register("big_shroomlamp", BlockBigLamp::new, GenericBlockItem::new);
		this.register("big_redstonelamp", BlockBigLamp::new, GenericBlockItem::new);
		this.register("cosmic_cake", BlockCosmicCake::new, block -> new CosmicCake());
		this.register("end_anchor", BlockEndAnchor::new, block -> new EndAnchor());

		this.register("etherium_block", BlockEtherium::new, block ->
			new GenericBlockItem(block, GenericBlockItem.getDefaultProperties().rarity(Rarity.RARE)));

		this.register("astral_block", BlockAstralDust::new, block ->
			new GenericBlockItem(block, GenericBlockItem.getDefaultProperties().rarity(Rarity.EPIC)));

		this.register("star_fabric", BlockStarFabric::new, block ->
				new GenericBlockItem(block, GenericBlockItem.getDefaultProperties().rarity(Rarity.EPIC)));
	}

	protected void register(String name, Supplier<Block> block, BlockItemSupplier item) {
		super.register(name, block);
		BLOCK_ITEM_MAP.put(new ResourceLocation(MODID, name), item);
	}

	protected static Map<ResourceLocation, BlockItemSupplier> getBlockItemMap() {
		return Collections.unmodifiableMap(BLOCK_ITEM_MAP);
	}

	@FunctionalInterface
	protected static interface BlockItemSupplier extends Function<Block, BlockItem> {
		@Override
		public BlockItem apply(Block block);
	}

}
