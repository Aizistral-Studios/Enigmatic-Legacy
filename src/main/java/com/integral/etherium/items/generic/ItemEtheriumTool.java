package com.integral.etherium.items.generic;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.core.IEtheriumTool;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Tier;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item.Properties;

public abstract class ItemEtheriumTool extends DiggerItem implements IEtheriumTool {
	public Set<Material> effectiveMaterials;
	public Set<Tag<Block>> effectiveTags;
	public ItemStack defaultInstance;
	protected final IEtheriumConfig config;

	public ItemEtheriumTool(float attackDamageIn, float attackSpeedIn, IEtheriumConfig config, Tag<Block> blocks, Properties builder) {
		super(attackDamageIn, attackSpeedIn, config.getToolMaterial(), blocks, builder);

		this.config = config;
		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveTags = Sets.newHashSet();
		this.defaultInstance = new ItemStack(this);
	}

	@Override
	public IEtheriumConfig getConfig() {
		return this.config;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState blockIn) {
		return super.isCorrectToolForDrops(stack, blockIn) || this.hasAnyTag(blockIn, this.effectiveTags) || this.effectiveMaterials.contains(blockIn.getMaterial());
	}

	protected boolean hasAnyTag(BlockState blockstate, Set<Tag<Block>> tags) {
		return tags.stream().anyMatch(tag -> this.hasTag(blockstate, tag));
	}

	protected boolean hasTag(BlockState blockstate, Tag<Block> tag) {
		return tag.contains(blockstate.getBlock());
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.speed;
	}

}
