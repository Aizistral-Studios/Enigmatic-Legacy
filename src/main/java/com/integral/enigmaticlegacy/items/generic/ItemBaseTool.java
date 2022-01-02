package com.integral.enigmaticlegacy.items.generic;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.IItemTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolItem;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.item.Item.Properties;

public abstract class ItemBaseTool extends ToolItem {
	public Set<Material> effectiveMaterials;
	public ItemStack defaultInstance;

	public ItemBaseTool(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builder) {
		super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builder);

		this.effectiveMaterials = Sets.newHashSet();
		this.defaultInstance = new ItemStack(this);
	}

	public ItemBaseTool(IItemTier tier) {
		this(4F, -2.8F, tier, new HashSet<>(), ItemBaseTool.getDefaultProperties().addToolType(ToolType.PICKAXE, tier.getLevel()));
	}

	public ItemBaseTool() {
		this(4F, -2.8F, EnigmaticMaterials.ETHERIUM, new HashSet<>(), ItemBaseTool.getDefaultProperties().addToolType(ToolType.PICKAXE, EnigmaticMaterials.ETHERIUM.getLevel()));
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


	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.tab(EnigmaticLegacy.enigmaticTab);
		props.stacksTo(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
