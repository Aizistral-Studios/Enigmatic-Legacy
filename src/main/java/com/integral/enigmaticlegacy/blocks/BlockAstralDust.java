package com.integral.enigmaticlegacy.blocks;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext.Builder;

public class BlockAstralDust extends Block {

	public BlockAstralDust() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops()
				.strength(3.0F, 10.0F).lightLevel(arg -> 10));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return List.of(new ItemStack(this));
	}

}
