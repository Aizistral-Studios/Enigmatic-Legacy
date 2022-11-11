package com.aizistral.enigmaticlegacy.api.items;
/*
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;

public interface IColored {
	int getColor(int index);

	class BlockColors implements IBlockColor {
		@Override
		public int getColor(BlockState state, IEnviromentBlockReader world, BlockPos post, int index) {
			return ((IColored) state.getBlock()).getColor(index);
		}
	}

	class ItemColors implements IItemColor {
		@Override
		public int getColor(ItemStack stack, int index) {
			return ((IColored) stack.getItem()).getColor(index);
		}
	}

	class ItemBlockColors implements IItemColor {
		@Override
		public int getColor(ItemStack stack, int index) {
			return ((IColored) Block.getBlockFromItem(stack.getItem())).getColor(index);
		}
	}
}
*/