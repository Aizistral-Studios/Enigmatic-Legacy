package com.aizistral.etherium.items.generic;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.etherium.core.IEtheriumConfig;
import com.aizistral.etherium.core.IEtheriumTool;
import com.google.common.collect.Sets;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public abstract class ItemEtheriumTool extends DiggerItem implements IEtheriumTool, ICreativeTabMember {
	public Set<Material> effectiveMaterials;
	public Set<TagKey<Block>> effectiveTags;

	public ItemEtheriumTool(float attackDamageIn, float attackSpeedIn, TagKey<Block> blocks, Properties builder) {
		super(attackDamageIn, attackSpeedIn, EnigmaticMaterials.ETHERIUM, blocks, builder);

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveTags = Sets.newHashSet();
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return EnigmaticLegacy.MAIN_TAB;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState blockIn) {
		return super.isCorrectToolForDrops(stack, blockIn) || this.hasAnyTag(blockIn, this.effectiveTags) || this.effectiveMaterials.contains(blockIn.getMaterial());
	}

	protected boolean hasAnyTag(BlockState blockstate, Set<TagKey<Block>> tags) {
		return tags.stream().anyMatch(tag -> this.hasTag(blockstate, tag));
	}

	protected boolean hasTag(BlockState blockstate, TagKey<Block> tag) {
		return blockstate.is(tag);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.speed;
	}

}
