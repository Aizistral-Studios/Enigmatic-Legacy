package com.integral.enigmaticlegacy.api.items;

import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public interface IBaseTool {

	public Set<Material> getEffectiveMaterials();
	public int getHarvestLevel();
	public Set<ToolType> getToolTypes();
	public float getEfficiency();

	default boolean canHarvestBlock(BlockState blockIn) {
		int i = this.getHarvestLevel();

		if (this.getToolTypes().contains(blockIn.getHarvestTool())) {
			if (blockIn.getHarvestTool() == ToolType.PICKAXE) {
				return i >= blockIn.getHarvestLevel();
			} else {
				return true;
			}
		}

		Material material = blockIn.getMaterial();
		return this.getEffectiveMaterials().contains(material);
	}

	/*default float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.getEffectiveMaterials().contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
	}*/

}
