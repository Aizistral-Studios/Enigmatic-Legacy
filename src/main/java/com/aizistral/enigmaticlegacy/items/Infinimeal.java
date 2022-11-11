package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Infinimeal extends ItemBase implements Vanishable {

	public Infinimeal() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON));
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

					age += world.random.nextInt(20);
					world.setBlock(topMostPos, topMostState.setValue(BlockStateProperties.AGE_15, Integer.valueOf(Math.min(age, 15))), 4);

					if (world instanceof ServerLevel) {
						world.getBlockState(topMostPos).randomTick((ServerLevel)world, topMostPos, world.random);
					}

					return InteractionResult.sidedSuccess(world.isClientSide);
				}
			} else if (block instanceof VineBlock) {
				if (!block.isRandomlyTicking(state))
					return result;

				if (world.isClientSide) {
					EnigmaticLegacy.PROXY.spawnBonemealParticles(world, pos, 0);
				}

				int cycles = 7+world.random.nextInt(7);

				if (world instanceof ServerLevel) {
					for (int i = 0; i <= cycles; i++) {
						state.randomTick((ServerLevel)world, pos, world.random);
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

				int cycles = 1+world.random.nextInt(1);
				cycles*=11;

				if (world instanceof ServerLevel) {
					for (int i = 0; i <= cycles; i++) {
						state.randomTick((ServerLevel)world, pos, world.random);
					}
				}

				return InteractionResult.sidedSuccess(world.isClientSide);
			} else if (block instanceof ChorusPlantBlock || block instanceof ChorusFlowerBlock) {
				if (!world.isClientSide) {
					world.levelEvent(2005, pos, 0);
				}

				if (world instanceof ServerLevel serverWorld) {
					List<BlockPos> flowers = this.findChorusFlowers(world, pos);

					flowers.forEach(flowerPos -> {
						int cycles = 1 + world.random.nextInt(2);
						cycles *= 11;

						for (int i = 0; i <= cycles; i++) {
							BlockState flowerState = world.getBlockState(flowerPos);
							flowerState.randomTick(serverWorld, flowerPos, world.random);
						}
					});
				}

				return InteractionResult.sidedSuccess(world.isClientSide);
			}
		}

		return result;
	}

	private List<BlockPos> findChorusFlowers(Level level, BlockPos pos) {
		List<BlockPos> chorusTree = new ArrayList<>();
		chorusTree.add(pos);

		while (true) {
			int formerSize = chorusTree.size();
			for (BlockPos treePos : new ArrayList<>(chorusTree)) {
				chorusTree.addAll(this.getNeighboringBlocks(level, treePos, chorusTree, ChorusFlowerBlock.class,
						ChorusPlantBlock.class));
			}

			if (formerSize == chorusTree.size()) {
				break;
			}
		}

		return chorusTree.stream().filter(p -> level.getBlockState(p).getBlock() instanceof ChorusFlowerBlock)
				.collect(Collectors.toList());
	}

	@SafeVarargs
	private List<BlockPos> getNeighboringBlocks(Level level, BlockPos pos, List<BlockPos> exclude, Class<? extends Block>... classes) {
		BlockPos[] neighbors = new BlockPos[] { pos.above(), pos.below(), pos.east(), pos.north(), pos.south(), pos.west() };

		return Arrays.stream(neighbors).filter(neighbor -> !exclude.contains(neighbor) && Arrays.stream(classes)
				.anyMatch(theClass -> theClass.isInstance(level.getBlockState(neighbor).getBlock()))).collect(Collectors.toList());
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
