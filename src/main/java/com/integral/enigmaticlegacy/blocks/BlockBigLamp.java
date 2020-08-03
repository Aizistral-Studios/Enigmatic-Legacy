package com.integral.enigmaticlegacy.blocks;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockBigLamp extends LanternBlock {
	
	protected final VoxelShape sittingLantern = VoxelShapes.or(Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D));
	protected final VoxelShape hangingLantern = VoxelShapes.or(Block.makeCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D));

	public BlockBigLamp(Properties properties) {
		super(properties);

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "big_lamp"));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      return state.get(HANGING) ? hangingLantern : sittingLantern;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

}
