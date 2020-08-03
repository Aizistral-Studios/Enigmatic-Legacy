package com.integral.enigmaticlegacy.blocks;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockMassiveLamp extends Block {

	public BlockMassiveLamp(Properties properties) {
		super(properties.notSolid());
		
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "massive_lamp"));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	/*
	@Override
	public Item asItem() {
		return Item.BLOCK_TO_ITEM.get(this);
	}
	*/
	
	/*
	@Override
	public boolean isVariableOpacity() {
		return true;
	}
	
	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}
	*/
}
