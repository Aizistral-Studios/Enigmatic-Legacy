package com.integral.etherium.blocks;

import java.util.ArrayList;
import java.util.List;

import com.integral.etherium.core.IEtheriumConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEtherium extends Block {
	private final IEtheriumConfig config;

	public BlockEtherium(IEtheriumConfig config) {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_BLUE).requiresCorrectToolForDrops()
				.strength(5.0F, 1200.0F).lightLevel(arg -> 10));
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_block"));
		this.config = config;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		List<ItemStack> stacklist = new ArrayList<ItemStack>();
		stacklist.add(new ItemStack(this));
		return stacklist;
	}

	@Override
	public String getDescriptionId() {
		return this.config.isStandalone() ? "block.enigmaticlegacy." + ForgeRegistries.BLOCKS.getKey(this).getPath() : super.getDescriptionId();
	}

}
