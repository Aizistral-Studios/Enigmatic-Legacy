package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.SlotContext;

public class MegaSponge extends ItemBaseCurio implements Vanishable {
	public static Omniconfig.IntParameter radius;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("Megasponge");

		radius = builder
				.comment("Radius in which Exptrapolated Megaspong absorbs water. Default 4 equals to vanilla sponge")
				.max(128)
				.getInt("Radius", 4);

		builder.popPrefix();
	}

	public MegaSponge() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.megaSponge1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.megaSponge2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@SuppressWarnings("deprecation")
	public BlockPos getCollidedWater(TagKey<Fluid> fluidTag, Player player) {
		AABB axisalignedbb = player.getBoundingBox().deflate(0.001D);
		int i = Mth.floor(axisalignedbb.minX);
		int j = Mth.ceil(axisalignedbb.maxX);
		int k = Mth.floor(axisalignedbb.minY);
		int l = Mth.ceil(axisalignedbb.maxY);
		int i1 = Mth.floor(axisalignedbb.minZ);
		int j1 = Mth.ceil(axisalignedbb.maxZ);

		BlockPos pos = null;

		if (!player.level().hasChunksAt(i, k, i1, j, l, j1))
			return null;
		else {
			try {
				net.minecraft.core.BlockPos.MutableBlockPos blockpos$pooledmutableblockpos = new BlockPos.MutableBlockPos();

				for (int l1 = i; l1 < j; ++l1) {
					for (int i2 = k; i2 < l; ++i2) {
						for (int j2 = i1; j2 < j1; ++j2) {
							blockpos$pooledmutableblockpos.set(l1, i2, j2);
							FluidState ifluidstate = player.level().getFluidState(blockpos$pooledmutableblockpos);
							if (ifluidstate.is(fluidTag)) {
								ifluidstate.tick(player.level(), blockpos$pooledmutableblockpos);
								pos = new BlockPos(l1, i2, j2);
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return pos;
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player && !player.level().isClientSide) {
			if (!player.getCooldowns().isOnCooldown(this)) {
				List<BlockPos> doomedWaterBlocks = new ArrayList<BlockPos>();
				BlockPos initialPos = this.getCollidedWater(FluidTags.WATER, player);
				BlockState initialState = initialPos != null ? player.level().getBlockState(initialPos) : null;

				if (initialPos != null && initialState != null)
					if (initialState.getFluidState() != null && initialState.getFluidState().is(FluidTags.WATER)) {

						doomedWaterBlocks.add(initialPos);
						List<BlockPos> processedBlocks = new ArrayList<BlockPos>();
						processedBlocks.add(initialPos);

						for (int counter = 0; counter <= radius.getValue(); counter++) {
							List<BlockPos> outputBlocks = new ArrayList<BlockPos>();

							for (BlockPos checkedPos : processedBlocks) {
								outputBlocks.addAll(this.getNearbyWater(player.level(), checkedPos));
							}

							processedBlocks.clear();

							for (BlockPos thePos : outputBlocks) {
								if (!doomedWaterBlocks.contains(thePos)) {
									processedBlocks.add(thePos);
									doomedWaterBlocks.add(thePos);
								}
							}

							outputBlocks.clear();
						}

						processedBlocks.clear();

						for (BlockPos exterminatedBlock : doomedWaterBlocks) {
							this.absorbWaterBlock(exterminatedBlock, player.level().getBlockState(exterminatedBlock), player.level());
						}

						doomedWaterBlocks.clear();

						player.level().playSound(null, player.blockPosition(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 64, player.level().dimension())), new PacketPortalParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 40, 1.0D, false));
						player.getCooldowns().addCooldown(this, 20);

					}
			}
		}
	}

	public void absorbWaterBlock(BlockPos pos, BlockState state, Level world) {
		if (state.getBlock() instanceof BucketPickup && !((BucketPickup) state.getBlock()).pickupBlock(world, pos, state).isEmpty()) {
			// Whatever
		} else if (state.getBlock() instanceof LiquidBlock) {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		} else if (!state.is(Blocks.KELP) && !state.is(Blocks.KELP_PLANT) && !state.is(Blocks.SEAGRASS) && !state.is(Blocks.TALL_SEAGRASS)) {
			BlockEntity tileentity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
			Block.dropResources(state, world, pos, tileentity);
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}
	}

	public List<BlockPos> getNearbyWater(Level world, BlockPos pos) {
		List<BlockPos> nearBlocks = new ArrayList<BlockPos>();
		List<BlockPos> waterBlocks = new ArrayList<BlockPos>();

		nearBlocks.add(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()));
		nearBlocks.add(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()));
		nearBlocks.add(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()));
		nearBlocks.add(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()));
		nearBlocks.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1));
		nearBlocks.add(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));

		for (BlockPos checkedPos : nearBlocks) {
			if (world.getBlockState(checkedPos).getFluidState().is(FluidTags.WATER)) {
				waterBlocks.add(checkedPos);
			}
		}

		return waterBlocks;
	}

}
