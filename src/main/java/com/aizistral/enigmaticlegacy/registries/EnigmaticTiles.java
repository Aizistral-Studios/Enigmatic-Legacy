package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.blocks.TileEndAnchor;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EnigmaticTiles {
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
			DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EnigmaticLegacy.MODID);

	public static final RegistryObject<BlockEntityType<TileEndAnchor>> END_ANCHOR = register(
			"tile_end_anchor",
			() -> BlockEntityType.Builder.<TileEndAnchor>of(
					TileEndAnchor::new, EnigmaticBlocks.END_ANCHOR)
	);

	public static void registerTiles(IEventBus modEventBus) {
		BLOCK_ENTITY_TYPES.register(modEventBus);
	}

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(
			String name, Supplier<BlockEntityType.Builder<T>> builder
	) {
		final String key = EnigmaticLegacy.MODID + ":" + name;
		return BLOCK_ENTITY_TYPES.register(name, () -> builder.get().build(Util.fetchChoiceType(References.BLOCK_ENTITY, key)));
	}
}
