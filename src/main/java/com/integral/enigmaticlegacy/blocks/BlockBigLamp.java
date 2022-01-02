package com.integral.enigmaticlegacy.blocks;

import java.util.ArrayList;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockBigLamp extends LanternBlock {
	
	protected final VoxelShape sittingLantern = VoxelShapes.or(Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D));
	protected final VoxelShape hangingLantern = VoxelShapes.or(Block.box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D));

	public BlockBigLamp(Properties properties, String registryName) {
		super(properties);
		
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, registryName));
		EnigmaticLegacy.cutoutBlockRegistry.add(this);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      return state.getValue(HANGING) ? hangingLantern : sittingLantern;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> stacklist = new ArrayList<ItemStack>();
		stacklist.add(new ItemStack(Item.byBlock(this)));
		return stacklist;
	}
	
	

}
