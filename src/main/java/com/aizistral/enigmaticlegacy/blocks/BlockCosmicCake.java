package com.aizistral.enigmaticlegacy.blocks;

import java.util.Random;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCosmicCake extends CakeBlock {
	private static final FoodProperties AS_TASTY_AS = Foods.GOLDEN_CARROT;
	protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[] {
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D),
			Block.box(1.0D, 0.0D, 2.5D, 15.0D, 8.0D, 15.0D),
			Block.box(1.0D, 0.0D, 4.5D, 15.0D, 8.0D, 15.0D),
			Block.box(1.0D, 0.0D, 6.5D, 15.0D, 8.0D, 15.0D),
			Block.box(1.0D, 0.0D, 8.5D, 15.0D, 8.0D, 15.0D),
			Block.box(1.0D, 0.0D, 10.5D, 15.0D, 8.0D, 15.0D),
			Block.box(1.0D, 0.0D, 12.0D, 15.0D, 8.0D, 15.0D)
	};

	public BlockCosmicCake() {
		super(Properties.copy(Blocks.CAKE));
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE_BY_BITE[pState.getValue(BITES)];
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (pState.getValue(BITES) > 5)
			return InteractionResult.PASS;

		ItemStack itemstack = pPlayer.getItemInHand(pHand);

		if (pLevel.isClientSide) {
			if (eat(pLevel, pPos, pState, pPlayer).consumesAction())
				return InteractionResult.SUCCESS;

			if (itemstack.isEmpty())
				return InteractionResult.CONSUME;
		}

		return eat(pLevel, pPos, pState, pPlayer);
	}

	protected static InteractionResult eat(LevelAccessor pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
		if (!pPlayer.canEat(false))
			return InteractionResult.PASS;
		else {
			pPlayer.awardStat(Stats.EAT_CAKE_SLICE);
			pPlayer.getFoodData().eat(AS_TASTY_AS.getNutrition(), AS_TASTY_AS.getSaturationModifier());
			int i = pState.getValue(BITES);
			pLevel.gameEvent(pPlayer, GameEvent.EAT, pPos);

			pLevel.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pPos, Block.getId(pState));

			if (i < 6) {
				pLevel.setBlock(pPos, pState.setValue(BITES, Integer.valueOf(i + 1)), 3);
			} else {
				pLevel.removeBlock(pPos, false);
				pLevel.gameEvent(pPlayer, GameEvent.BLOCK_DESTROY, pPos);
			}

			pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.GENERIC_EAT, SoundSource.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);

			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int bites = 0;
		if ((bites = state.getValue(BITES)) > 0) {
			level.setBlock(pos, state.setValue(BITES, bites - 1), 3);
			level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
			level.playSound(null, pos, EnigmaticSounds.EAT_REVERSE, SoundSource.BLOCKS, 1F, 0.5F + (float) Math.random() * 0.5F);
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return pState.getValue(BITES) > 0;
	}

}
