package com.aizistral.enigmaticlegacy.api.items;

import java.util.Set;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public interface IBaseTool {
	public float getEfficiency();

	default boolean canHarvestBlock(BlockState blockIn, Player player) {
		return ForgeHooks.isCorrectToolForDrops(blockIn, player);
	}

	/*default float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.getEffectiveMaterials().contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
	}*/

}
