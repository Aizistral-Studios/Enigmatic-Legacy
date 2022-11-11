package com.aizistral.etherium.blocks;

import java.util.ArrayList;
import java.util.List;

import com.aizistral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEtherium extends Block {

	public BlockEtherium() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops()
				.strength(5.0F, 1200.0F).lightLevel(arg -> 10));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		return List.of(new ItemStack(this));
	}

}
