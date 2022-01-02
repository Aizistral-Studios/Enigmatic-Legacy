package com.integral.enigmaticlegacy.blocks;

import java.util.ArrayList;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockRenderType;
import net.minecraft.world.level.block.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.level.block.AbstractBlock.Properties;
import net.minecraft.fluid.FluidState;

public class BlockMassiveLamp extends Block {

	public BlockMassiveLamp(Properties properties, String registryName) {
		super(properties.sound(SoundType.GLASS).noOcclusion());

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, registryName));
		EnigmaticLegacy.cutoutBlockRegistry.add(this);
	}

	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
		return true;
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


	@Override
	public boolean hasDynamicShape() {
		return true;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

}
