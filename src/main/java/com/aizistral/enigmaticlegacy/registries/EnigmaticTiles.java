package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.blocks.TileEndAnchor;

import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticTiles extends AbstractRegistry<BlockEntityType<?>> {
	private static final EnigmaticTiles INSTANCE = new EnigmaticTiles();

	@ObjectHolder(value = MODID + ":tile_end_anchor", registryName = "block_entity_type")
	public static final BlockEntityType<TileEndAnchor> END_ANCHOR = null;

	private EnigmaticTiles() {
		super(ForgeRegistries.BLOCK_ENTITY_TYPES);
		this.register("tile_end_anchor", () -> BlockEntityType.Builder.<TileEndAnchor>of(
				TileEndAnchor::new, EnigmaticBlocks.END_ANCHOR).build(Util.fetchChoiceType(
						References.BLOCK_ENTITY, MODID + ":tile_end_anchor")));
	}

}
