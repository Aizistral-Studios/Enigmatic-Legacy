package com.integral.enigmaticlegacy.blocks;

import java.util.ArrayList;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.util.ResourceLocation;

public class BlockMassiveLamp extends Block {

	public BlockMassiveLamp(Properties properties, String registryName) {
		super(properties.sound(SoundType.GLASS).notSolid());

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, registryName));
		EnigmaticLegacy.cutoutBlockRegistry.add(this);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> stacklist = new ArrayList<ItemStack>();
		stacklist.add(new ItemStack(Item.getItemFromBlock(this)));
		return stacklist;
	}


	@Override
	public boolean isVariableOpacity() {
		return true;
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}

}
