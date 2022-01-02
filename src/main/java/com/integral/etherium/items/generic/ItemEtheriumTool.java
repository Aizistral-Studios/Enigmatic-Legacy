package com.integral.etherium.items.generic;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.core.IEtheriumTool;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.IItemTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolItem;
import net.minecraft.world.item.Item.Properties;
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
	public boolean isCorrectToolForDrops(BlockState blockIn) {
		int i = this.getTier().getLevel();

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
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.speed;
	}

}
