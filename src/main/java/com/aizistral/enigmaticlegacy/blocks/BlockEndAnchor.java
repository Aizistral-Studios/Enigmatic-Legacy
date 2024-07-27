package com.aizistral.enigmaticlegacy.blocks;

import java.util.List;
import java.util.Optional;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.items.EndAnchor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockEndAnchor extends BaseEntityBlock {
	public static final int MIN_CHARGES = 0;
	public static final int MAX_CHARGES = 4;
	public static final IntegerProperty CHARGE = BlockStateProperties.RESPAWN_ANCHOR_CHARGES;
	private static final ImmutableList<Vec3i> RESPAWN_HORIZONTAL_OFFSETS = ImmutableList.of(new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(1, 0, 0), new Vec3i(-1, 0, -1), new Vec3i(1, 0, -1), new Vec3i(-1, 0, 1), new Vec3i(1, 0, 1));
	private static final ImmutableList<Vec3i> RESPAWN_OFFSETS = (new Builder<Vec3i>()).addAll(RESPAWN_HORIZONTAL_OFFSETS).addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::below).iterator()).addAll(RESPAWN_HORIZONTAL_OFFSETS.stream().map(Vec3i::above).iterator()).add(new Vec3i(0, 1, 0)).build();

	public BlockEndAnchor() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops()
				.strength(50F, 1200F).lightLevel(state -> BlockEndAnchor.getScaledChargeLevel(state, 15)));
		this.registerDefaultState(this.stateDefinition.any().setValue(CHARGE, Integer.valueOf(0)));

		DispenserBlock.registerBehavior(Items.ENDER_PEARL, new OptionalDispenseItemBehavior() {
			@Override
			public ItemStack execute(BlockSource source, ItemStack stack) {
				Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
				BlockPos facedPos = source.getPos().relative(direction);
				Level level = source.getLevel();
				BlockState facedState = level.getBlockState(facedPos);

				if (facedState.getBlock() == BlockEndAnchor.this) {
					if (canBeCharged(facedState)) {
						charge(level, facedPos, facedState);
						stack.shrink(1);
						this.setSuccess(true);
						return stack;
					} else {
						this.setSuccess(false);
						return stack;
					}
				}

				return super.execute(source, stack);
			}
		});
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getItemInHand(hand);
		if (hand == InteractionHand.MAIN_HAND && !isRespawnFuel(stack) && isRespawnFuel(player.getItemInHand(InteractionHand.OFF_HAND)))
			return InteractionResult.PASS;
		else if (isRespawnFuel(stack) && canBeCharged(state)) {
			charge(level, pos, state);
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		} else if (state.getValue(CHARGE) == 0)
			return InteractionResult.PASS;
		else if (!canSetSpawn(level)) {
			if (!level.isClientSide) {
				this.explode(state, level, pos);
			}

			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			if (!level.isClientSide) {
				ServerPlayer splayer = (ServerPlayer)player;
				if (splayer.getRespawnDimension() != level.dimension() || !pos.equals(splayer.getRespawnPosition())) {
					splayer.setRespawnPosition(level.dimension(), pos, 0.0F, false, true);
					level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
					return InteractionResult.SUCCESS;
				}
			}

			return InteractionResult.CONSUME;
		}
	}

	private static boolean isRespawnFuel(ItemStack stack) {
		return stack.is(Items.ENDER_PEARL);
	}

	private static boolean canBeCharged(BlockState state) {
		return state.getValue(CHARGE) < 4;
	}

	private static boolean isWaterThatWouldFlow(BlockPos pos, Level level) {
		FluidState state = level.getFluidState(pos);
		if (!state.is(FluidTags.WATER))
			return false;
		else if (state.isSource())
			return true;
		else {
			if (state.getAmount() < 2.0F)
				return false;
			else
				return !level.getFluidState(pos.below()).is(FluidTags.WATER);
		}
	}

	private void explode(BlockState state, Level level, BlockPos pos) {
		level.removeBlock(pos, false);
		boolean flag = Direction.Plane.HORIZONTAL.stream().map(pos::relative).anyMatch((p_55854_) -> {
			return isWaterThatWouldFlow(p_55854_, level);
		});
		final boolean flag1 = flag || level.getFluidState(pos.above()).is(FluidTags.WATER);
		level.explode(null, level.damageSources().badRespawnPointExplosion(pos.getCenter()), null, pos.getCenter(), 5.0F, true, Level.ExplosionInteraction.BLOCK);
	}

	public static boolean canSetSpawn(Level level) {
		return EndAnchor.endExclusiveMode.getValue() ? level.dimension() == Level.END
				: !EndAnchor.DIMENSION_BLACKLIST.contains(level.dimension().location());
	}

	public static void charge(Level level, BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(CHARGE, Integer.valueOf(state.getValue(CHARGE) + 1)), 3);
		level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(CHARGE) != 0) {
			if (random.nextInt(100) == 0) {
				level.playSound((Player)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
			}

			double d0 = pos.getX() + 0.1 + (random.nextDouble() - 0.1);
			double d1 = pos.getY() + 0.8D;
			double d2 = pos.getZ() + 0.1 + (random.nextDouble() - 0.1);
			level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CHARGE);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	public static int getScaledChargeLevel(BlockState state, int scale) {
		return Mth.floor((state.getValue(CHARGE) - 0) / 4.0F * scale);
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return getScaledChargeLevel(state, 15);
	}

	public static Optional<Vec3> findStandUpPosition(EntityType<?> pEntityType, CollisionGetter pLevel, BlockPos pPos) {
		Optional<Vec3> optional = findStandUpPosition(pEntityType, pLevel, pPos, true);
		return optional.isPresent() ? optional : findStandUpPosition(pEntityType, pLevel, pPos, false);
	}

	private static Optional<Vec3> findStandUpPosition(EntityType<?> p_55844_, CollisionGetter p_55845_, BlockPos p_55846_, boolean p_55847_) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for(Vec3i vec3i : RESPAWN_OFFSETS) {
			blockpos$mutableblockpos.set(p_55846_).move(vec3i);
			Vec3 vec3 = DismountHelper.findSafeDismountLocation(p_55844_, p_55845_, blockpos$mutableblockpos, p_55847_);
			if (vec3 != null)
				return Optional.of(vec3);
		}

		return Optional.empty();
	}

	@Override
	public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
		return false;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new TileEndAnchor(pPos, pState);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		return List.of(new ItemStack(this));
	}

}
