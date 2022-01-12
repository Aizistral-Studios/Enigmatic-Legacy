package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.InteractionResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Infinimeal extends ItemBase implements Vanishable {

	public Infinimeal() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "infinimeal"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.infinimeal1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.infinimeal2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.infinimeal3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public InteractionResult useOn(UseOnContext context) {
		ItemStack stack = context.getItemInHand();
		int savedCount = stack.getCount();

		InteractionResult result = Items.BONE_MEAL.useOn(context);
		stack.setCount(savedCount);

		if (result == InteractionResult.PASS) {
			BlockPos pos = context.getClickedPos();
			Level world = context.getLevel();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof CactusBlock || block instanceof SugarCaneBlock) {
				BlockPos topMostPos = this.findTopmostGrowable(world, pos, block, true);
				BlockState topMostState = world.getBlockState(topMostPos);

				if (topMostState.hasProperty(BlockStateProperties.AGE_15) && world.isEmptyBlock(topMostPos.above())) {
					int age = topMostState.getValue(BlockStateProperties.AGE_15);

					int plantHeight;
					for(plantHeight = 1; world.getBlockState(topMostPos.below(plantHeight)).is(block); ++plantHeight) {}

					if (plantHeight >= 3)
						return result;

					if (!world.isClientSide) {
						world.levelEvent(2005, pos, 0);
					}

					age += random.nextInt(20);
					world.setBlock(topMostPos, topMostState.setValue(BlockStateProperties.AGE_15, Integer.valueOf(Math.min(age, 15))), 4);

					if (world instanceof ServerLevel) {
						block.randomTick(world.getBlockState(topMostPos), (ServerLevel)world, topMostPos, random);
					}

					return InteractionResult.sidedSuccess(world.isClientSide);
				}
			} else if (block instanceof VineBlock) {
				if (!block.isRandomlyTicking(state))
					return result;

				if (world.isClientSide) {
					EnigmaticLegacy.proxy.spawnBonemealParticles(world, pos, 0);
				}

				int cycles = 7+random.nextInt(7);

				if (world instanceof ServerLevel) {
					for (int i = 0; i <= cycles; i++) {
						block.randomTick(state, (ServerLevel)world, pos, random);
					}

					state.updateNeighbourShapes(world, pos, 4);
				}

				return InteractionResult.sidedSuccess(world.isClientSide);
			} else if (block instanceof NetherWartBlock) {
				if (!block.isRandomlyTicking(state))
					return result;

				if (!world.isClientSide) {
					world.levelEvent(2005, pos, 0);
				}

				int cycles = 1+random.nextInt(1);
				cycles*=11;

				if (world instanceof ServerLevel) {
					for (int i = 0; i <= cycles; i++) {
						block.randomTick(state, (ServerLevel)world, pos, random);
					}
				}

				return InteractionResult.sidedSuccess(world.isClientSide);
			}
		}

		return result;
	}

	private BlockPos findTopmostGrowable(Level world, BlockPos pos, Block block, boolean goUp) {
		BlockPos top = pos;

		while (true) {
			if (world.getBlockState(top) != null && world.getBlockState(top).getBlock() == block) {
				BlockPos nextUp = goUp ? top.above() : top.below();

				if (world.getBlockState(nextUp) == null || world.getBlockState(nextUp).getBlock() != block)
					return top;
				else {
					top = nextUp;
					continue;
				}
			} else
				return pos;
		}
	}

}
