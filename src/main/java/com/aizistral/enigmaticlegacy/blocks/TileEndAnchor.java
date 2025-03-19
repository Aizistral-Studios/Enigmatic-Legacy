package com.aizistral.enigmaticlegacy.blocks;

import com.aizistral.enigmaticlegacy.registries.EnigmaticTiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEndAnchor extends BlockEntity {

	public TileEndAnchor(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public TileEndAnchor(BlockPos pos, BlockState state) {
		this(EnigmaticTiles.END_ANCHOR, pos, state);
	}

	public boolean shouldRenderFace(Direction face) {
		return face.getAxis() == Direction.Axis.Y;
	}

}
