package com.integral.etherium.items.generic;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.core.IEtheriumTool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.common.ToolType;

public abstract class ItemEtheriumTool extends ToolItem implements IEtheriumTool {
	public Set<Material> effectiveMaterials;
	public ItemStack defaultInstance;
	protected final IEtheriumConfig config;

	public ItemEtheriumTool(float attackDamageIn, float attackSpeedIn, IEtheriumConfig config, Set<Block> effectiveBlocksIn, Properties builder) {
		super(attackDamageIn, attackSpeedIn, config.getToolMaterial(), effectiveBlocksIn, builder);

		this.config = config;
		this.effectiveMaterials = Sets.newHashSet();
		this.defaultInstance = new ItemStack(this);
	}

	@Override
	public IEtheriumConfig getConfig() {
		return this.config;
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
		int i = this.getTier().getHarvestLevel();

		if (this.getToolTypes(this.defaultInstance).contains(blockIn.getHarvestTool())) {
			if (blockIn.getHarvestTool() == ToolType.PICKAXE)
				return i >= blockIn.getHarvestLevel();
				else
					return true;
		}

		Material material = blockIn.getMaterial();
		return this.effectiveMaterials.contains(material);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

}
