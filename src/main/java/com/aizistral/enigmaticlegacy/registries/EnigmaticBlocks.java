package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.blocks.*;
import com.aizistral.enigmaticlegacy.items.CosmicCake;
import com.aizistral.enigmaticlegacy.items.EndAnchor;
import com.aizistral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.aizistral.etherium.blocks.BlockEtherium;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnigmaticBlocks {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EnigmaticLegacy.MODID);

	private static final Map<ResourceLocation, BlockItemSupplier> BLOCK_ITEM_MAP = new HashMap<>();

	public static final RegistryObject<BlockMassiveLamp> MASSIVE_LAMP = register("massive_lamp", BlockMassiveLamp::new, GenericBlockItem::new);
	public static final RegistryObject<BlockMassiveLamp> MASSIVE_SHROOMLAMP = register("massive_shroomlamp", BlockMassiveLamp::new, GenericBlockItem::new);
	public static final RegistryObject<BlockMassiveLamp> MASSIVE_REDSTONELAMP = register("massive_redstonelamp", BlockMassiveLamp::new, GenericBlockItem::new);
	public static final RegistryObject<BlockBigLamp> BIG_LAMP = register("big_lamp", BlockBigLamp::new, GenericBlockItem::new);
	public static final RegistryObject<BlockBigLamp> BIG_SHROOMLAMP = register("big_shroomlamp", BlockBigLamp::new, GenericBlockItem::new);
	public static final RegistryObject<BlockBigLamp> BIG_REDSTONELAMP = register("big_redstonelamp", BlockBigLamp::new, GenericBlockItem::new);
	public static final RegistryObject<BlockCosmicCake> COSMIC_CAKE = register("cosmic_cake", BlockCosmicCake::new, block -> new CosmicCake());
	public static final RegistryObject<BlockEndAnchor> END_ANCHOR = register("end_anchor", BlockEndAnchor::new, block -> new EndAnchor());
	public static final RegistryObject<BlockEtherium> ETHERIUM_BLOCK = register("etherium_block", BlockEtherium::new, block ->
			new GenericBlockItem(block, GenericBlockItem.getDefaultProperties().rarity(Rarity.RARE)));
	public static final RegistryObject<BlockAstralDust> ASTRAL_BLOCK = register("astral_block", BlockAstralDust::new, block ->
			new GenericBlockItem(block, GenericBlockItem.getDefaultProperties().rarity(Rarity.EPIC)));
	public static final RegistryObject<BlockStarFabric> STAR_FABRIC = register("star_fabric", BlockStarFabric::new, block ->
				new GenericBlockItem(block, GenericBlockItem.getDefaultProperties().rarity(Rarity.EPIC)));

	public static void registerBlocks(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
	}

	private static <T extends Block> RegistryObject<T> register(
			String name, Supplier<T> block, BlockItemSupplier item
	) {
		BLOCK_ITEM_MAP.put(new ResourceLocation(EnigmaticLegacy.MODID, name), item);
		return BLOCKS.register(name, block);
	}

	protected static Map<ResourceLocation, BlockItemSupplier> getBlockItemMap() {
		return Collections.unmodifiableMap(BLOCK_ITEM_MAP);
	}

	@FunctionalInterface
	protected interface BlockItemSupplier extends Function<Block, BlockItem> {
		@Override
        BlockItem apply(Block block);
	}

}
