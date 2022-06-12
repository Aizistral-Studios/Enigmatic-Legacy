package com.integral.enigmaticlegacy.blocks;

import java.util.ArrayList;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.FluidState;

public class BlockMassiveLamp extends Block {

	public BlockMassiveLamp(Properties properties, String registryName) {
		super(properties.sound(SoundType.GLASS).noOcclusion());

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, registryName));
		EnigmaticLegacy.cutoutBlockRegistry.add(this);
	}

	@Override
	public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
		return super.shouldDisplayFluidOverlay(state, world, pos, fluidState);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_60550_) {
		return RenderShape.MODEL;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return List.of(new ItemStack(this));
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
