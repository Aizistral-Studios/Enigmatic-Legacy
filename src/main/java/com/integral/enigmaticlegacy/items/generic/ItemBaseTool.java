package com.integral.enigmaticlegacy.items.generic;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;

import net.minecraft.item.Item.Properties;

public abstract class ItemBaseTool extends ToolItem {

	public Set<Material> effectiveMaterials;
	public ItemStack defaultInstance;

	public ItemBaseTool(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builder) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);

		this.effectiveMaterials = Sets.newHashSet();
		this.defaultInstance = new ItemStack(this);
	}

	public ItemBaseTool(IItemTier tier) {
		this(4F, -2.8F, tier, new HashSet<>(), ItemBaseTool.getDefaultProperties().addToolType(ToolType.PICKAXE, tier.getHarvestLevel()));
	}

	public ItemBaseTool() {
		this(4F, -2.8F, EnigmaticMaterials.ETHERIUM, new HashSet<>(), ItemBaseTool.getDefaultProperties().addToolType(ToolType.PICKAXE, EnigmaticMaterials.ETHERIUM.getHarvestLevel()));
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


	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.group(EnigmaticLegacy.enigmaticTab);
		props.maxStackSize(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
