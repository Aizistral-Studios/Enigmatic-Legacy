package com.integral.enigmaticlegacy.api.items;

import java.util.Set;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeHooks;

public interface IBaseTool {
	public Set<Material> getEffectiveMaterials();
	public float getEfficiency();

	default boolean canHarvestBlock(BlockState blockIn, Player player) {
		if (ForgeHooks.isCorrectToolForDrops(blockIn, player))
			return true;

		Material material = blockIn.getMaterial();
		return this.getEffectiveMaterials().contains(material);
	}

	/*default float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.getEffectiveMaterials().contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
	}*/

}
