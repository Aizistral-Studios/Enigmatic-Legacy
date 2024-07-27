package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
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
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayerFactory;

public class Infinimeal extends ItemBase implements Vanishable {
	private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new OptionalDispenseItemBehavior() {

		@Override
		protected ItemStack execute(BlockSource source, ItemStack stack) {
			this.setSuccess(true);
			Level level = source.getLevel();
			BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

			if (!tryApply(stack, level, pos, Optional.empty(), Optional.empty())) {
				this.setSuccess(false);
			}

			return stack;
		};

	};


	public Infinimeal() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON));
		DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
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
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		boolean success = tryApply(stack, level, pos, Optional.of(context.getPlayer()), Optional.of(context.getClickedFace()));

		if (success)
			return InteractionResult.sidedSuccess(level.isClientSide);
		else
			return InteractionResult.PASS;
	}

	private static boolean tryApply(ItemStack stack, Level world, BlockPos pos, Optional<Player> optionalPlayer,
			Optional<Direction> clickedFace) {
		ItemStack stackCopy = new ItemStack(stack.getItem());

		if (applyVanillaBonemeal(stackCopy, world, pos, optionalPlayer, clickedFace)) {
			if (!world.isClientSide) {
				world.levelEvent(1505, pos, 0);
			}

			return true;
		}

		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (block instanceof CactusBlock || block instanceof SugarCaneBlock) {
			BlockPos topMostPos = findTopmostGrowable(world, pos, block, true);
			BlockState topMostState = world.getBlockState(topMostPos);

			if (topMostState.hasProperty(BlockStateProperties.AGE_15) && world.isEmptyBlock(topMostPos.above())) {
				int age = topMostState.getValue(BlockStateProperties.AGE_15);

				int plantHeight;
				for(plantHeight = 1; world.getBlockState(topMostPos.below(plantHeight)).is(block); ++plantHeight) {}

				if (plantHeight >= 3)
					return false;

				if (!world.isClientSide) {
					world.levelEvent(2005, pos, 0);
				}

				age += world.random.nextInt(20);
				world.setBlock(topMostPos, topMostState.setValue(BlockStateProperties.AGE_15, Integer.valueOf(Math.min(age, 15))), 4);

				if (world instanceof ServerLevel) {
					world.getBlockState(topMostPos).randomTick((ServerLevel)world, topMostPos, world.random);
				}

				return true;
			}
		} else if (block instanceof VineBlock) {
			if (!block.isRandomlyTicking(state))
				return false;

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

			return true;
		} else if (block instanceof NetherWartBlock) {
			if (!block.isRandomlyTicking(state))
				return false;

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

			return true;
		} else if (block instanceof ChorusPlantBlock || block instanceof ChorusFlowerBlock) {
			if (!world.isClientSide) {
				world.levelEvent(2005, pos, 0);
			}

			if (world instanceof ServerLevel serverWorld) {
				List<BlockPos> flowers = findChorusFlowers(world, pos);

				flowers.forEach(flowerPos -> {
					int cycles = 1 + world.random.nextInt(2);
					cycles *= 11;

					for (int i = 0; i <= cycles; i++) {
						BlockState flowerState = world.getBlockState(flowerPos);
						flowerState.randomTick(serverWorld, flowerPos, world.random);
					}
				});
			}

			return true;
		}

		return false;
	}

	public static boolean applyVanillaBonemeal(ItemStack stack, Level level, BlockPos pos, Optional<Player> optionalPlayer,
			Optional<Direction> clickedFace) {
		if (!growCrop(stack, level, pos, optionalPlayer))
			return BoneMealItem.growWaterPlant(stack, level, pos, clickedFace.orElse(null));
		else
			return true;
	}

	public static boolean growCrop(ItemStack stack, Level level, BlockPos pos, Optional<Player> optionalPlayer) {
		if (!optionalPlayer.isPresent()) {
			if (level instanceof ServerLevel)
				return BoneMealItem.applyBonemeal(stack, level, pos, FakePlayerFactory.getMinecraft((ServerLevel)level));
			return false;
		} else
			return BoneMealItem.applyBonemeal(stack, level, pos, optionalPlayer.get());
	}

	private static List<BlockPos> findChorusFlowers(Level level, BlockPos pos) {
		List<BlockPos> chorusTree = new ArrayList<>();
		chorusTree.add(pos);

		while (true) {
			int formerSize = chorusTree.size();
			for (BlockPos treePos : new ArrayList<>(chorusTree)) {
				chorusTree.addAll(getNeighboringBlocks(level, treePos, chorusTree, ChorusFlowerBlock.class,
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
	private static List<BlockPos> getNeighboringBlocks(Level level, BlockPos pos, List<BlockPos> exclude, Class<? extends Block>... classes) {
		BlockPos[] neighbors = new BlockPos[] { pos.above(), pos.below(), pos.east(), pos.north(), pos.south(), pos.west() };

		return Arrays.stream(neighbors).filter(neighbor -> !exclude.contains(neighbor) && Arrays.stream(classes)
				.anyMatch(theClass -> theClass.isInstance(level.getBlockState(neighbor).getBlock()))).collect(Collectors.toList());
	}

	private static BlockPos findTopmostGrowable(Level world, BlockPos pos, Block block, boolean goUp) {
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
