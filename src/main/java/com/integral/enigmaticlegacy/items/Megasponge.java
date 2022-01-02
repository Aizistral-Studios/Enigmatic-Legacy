package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowingFluidBlock;
import net.minecraft.world.level.block.IBucketPickupHandler;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class Megasponge extends ItemBaseCurio implements IVanishable {
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

	public Megasponge() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mega_sponge"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.megaSponge1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.megaSponge2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@SuppressWarnings("deprecation")
	public BlockPos getCollidedWater(INamedTag<Fluid> fluidTag, PlayerEntity player) {
		AxisAlignedBB axisalignedbb = player.getBoundingBox().deflate(0.001D);
		int i = MathHelper.floor(axisalignedbb.minX);
		int j = MathHelper.ceil(axisalignedbb.maxX);
		int k = MathHelper.floor(axisalignedbb.minY);
		int l = MathHelper.ceil(axisalignedbb.maxY);
		int i1 = MathHelper.floor(axisalignedbb.minZ);
		int j1 = MathHelper.ceil(axisalignedbb.maxZ);

		BlockPos pos = null;

		if (!player.level.hasChunksAt(i, k, i1, j, l, j1))
			return null;
		else {
			try {
				net.minecraft.util.math.BlockPos.Mutable blockpos$pooledmutableblockpos = new BlockPos.Mutable();

				for (int l1 = i; l1 < j; ++l1) {
					for (int i2 = k; i2 < l; ++i2) {
						for (int j2 = i1; j2 < j1; ++j2) {
							blockpos$pooledmutableblockpos.set(l1, i2, j2);
							FluidState ifluidstate = player.level.getFluidState(blockpos$pooledmutableblockpos);
							if (ifluidstate.is(fluidTag)) {
								ifluidstate.tick(player.level, blockpos$pooledmutableblockpos);
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
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (living instanceof PlayerEntity & !living.level.isClientSide) {
			PlayerEntity player = (PlayerEntity) living;

			if (!player.getCooldowns().isOnCooldown(this)) {
				List<BlockPos> doomedWaterBlocks = new ArrayList<BlockPos>();
				BlockPos initialPos = this.getCollidedWater(FluidTags.WATER, player);
				BlockState initialState = initialPos != null ? player.level.getBlockState(initialPos) : null;

				if (initialPos != null && initialState != null)
					if (initialState.getFluidState() != null && initialState.getFluidState().is(FluidTags.WATER)) {

						doomedWaterBlocks.add(initialPos);
						List<BlockPos> processedBlocks = new ArrayList<BlockPos>();
						processedBlocks.add(initialPos);

						for (int counter = 0; counter <= radius.getValue(); counter++) {
							List<BlockPos> outputBlocks = new ArrayList<BlockPos>();

							for (BlockPos checkedPos : processedBlocks) {
								outputBlocks.addAll(this.getNearbyWater(player.level, checkedPos));
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
							this.absorbWaterBlock(exterminatedBlock, player.level.getBlockState(exterminatedBlock), player.level);
						}

						doomedWaterBlocks.clear();

						player.level.playSound(null, player.blockPosition(), SoundEvents.BUCKET_FILL, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 64, player.level.dimension())), new PacketPortalParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 40, 1.0D, false));
						player.getCooldowns().addCooldown(this, 20);

					}

			}
		}
	}

	public void absorbWaterBlock(BlockPos pos, BlockState state, World world) {

		if (state.getBlock() instanceof IBucketPickupHandler && ((IBucketPickupHandler) state.getBlock()).takeLiquid(world, pos, state) != Fluids.EMPTY) {
			// Whatever
		} else if (state.getBlock() instanceof FlowingFluidBlock) {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		} else if (state.getMaterial() == Material.WATER_PLANT || state.getMaterial() == Material.REPLACEABLE_WATER_PLANT) {
			TileEntity tileentity = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
			Block.dropResources(state, world, pos, tileentity);
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}
	}

	public List<BlockPos> getNearbyWater(World world, BlockPos pos) {
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

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

}
