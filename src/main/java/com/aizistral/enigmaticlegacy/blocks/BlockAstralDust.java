package com.aizistral.enigmaticlegacy.blocks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext.Builder;

import java.util.List;

public class BlockAstralDust extends Block {

	public BlockAstralDust() {
		super(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_LIGHT_BLUE).strength(1F, 10F)
				.lightLevel(arg -> 10).noOcclusion().sound(SoundType.WOOL));
	}

	@Override
	public RenderShape getRenderShape(BlockState p_60550_) {
		return RenderShape.MODEL;
	}

	@Override
	public List<ItemStack> getDrops(BlockState pState, Builder pBuilder) {
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
