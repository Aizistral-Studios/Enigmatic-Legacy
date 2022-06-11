package com.integral.enigmaticlegacy.blocks;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;

public class TileEndAnchor extends BlockEntity {
	@ObjectHolder(EnigmaticLegacy.MODID + ":tile_end_anchor")
	public static BlockEntityType<TileEndAnchor> TYPE;

	public TileEndAnchor(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public TileEndAnchor(BlockPos pos, BlockState state) {
		this(TYPE, pos, state);
	}

	public boolean shouldRenderFace(Direction face) {
		return face.getAxis() == Direction.Axis.Y;
	}

}
