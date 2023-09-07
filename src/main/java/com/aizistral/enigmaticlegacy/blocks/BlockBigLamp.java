package com.aizistral.enigmaticlegacy.blocks;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockBigLamp extends LanternBlock {
	protected final VoxelShape sittingLantern = Shapes.or(Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D));
	protected final VoxelShape hangingLantern = Shapes.or(Block.box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D));

	public BlockBigLamp() {
		super(BlockBehaviour.Properties.copy(Blocks.LANTERN));
	}

	@Override
	public RenderShape getRenderShape(BlockState p_60550_) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(HANGING) ? this.hangingLantern : this.sittingLantern;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		return List.of(new ItemStack(this));
	}

}
