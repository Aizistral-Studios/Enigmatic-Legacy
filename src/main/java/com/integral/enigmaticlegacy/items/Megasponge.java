package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.objects.CooldownMap;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class Megasponge extends ItemBaseCurio {

	public CooldownMap cooldownMap = new CooldownMap();

	public Megasponge() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mega_sponge"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MEGASPONGE_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.megaSponge1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.megaSponge2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@SuppressWarnings("deprecation")
	public BlockPos getCollidedWater(Tag<Fluid> fluidTag, PlayerEntity player) {
		AxisAlignedBB axisalignedbb = player.getBoundingBox().shrink(0.001D);
		int i = MathHelper.floor(axisalignedbb.minX);
		int j = MathHelper.ceil(axisalignedbb.maxX);
		int k = MathHelper.floor(axisalignedbb.minY);
		int l = MathHelper.ceil(axisalignedbb.maxY);
		int i1 = MathHelper.floor(axisalignedbb.minZ);
		int j1 = MathHelper.ceil(axisalignedbb.maxZ);

		BlockPos pos = null;

		if (!player.world.isAreaLoaded(i, k, i1, j, l, j1)) {
			return null;
		} else {
			try (PooledMutable blockpos$pooledmutableblockpos = BlockPos.PooledMutable.retain()) {
				for (int l1 = i; l1 < j; ++l1) {
					for (int i2 = k; i2 < l; ++i2) {
						for (int j2 = i1; j2 < j1; ++j2) {
							blockpos$pooledmutableblockpos.setPos(l1, i2, j2);
							IFluidState ifluidstate = player.world.getFluidState(blockpos$pooledmutableblockpos);
							if (ifluidstate.isTagged(fluidTag)) {
								ifluidstate.tick(player.world, blockpos$pooledmutableblockpos);
								pos = new BlockPos(l1, i2, j2);
							}
						}
					}
				}
			}

			return pos;
		}
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity & !living.world.isRemote) {
			PlayerEntity player = (PlayerEntity) living;

			this.cooldownMap.tick(living);

			if (!this.cooldownMap.hasCooldown(player)) {
				List<BlockPos> doomedWaterBlocks = new ArrayList<BlockPos>();
				BlockPos initialPos = this.getCollidedWater(FluidTags.WATER, player);
				BlockState initialState = initialPos != null ? player.world.getBlockState(initialPos) : null;

				if (initialPos != null && initialState != null)
					if (initialState.getFluidState() != null && initialState.getFluidState().isTagged(FluidTags.WATER)) {

						doomedWaterBlocks.add(initialPos);
						List<BlockPos> processedBlocks = new ArrayList<BlockPos>();
						processedBlocks.add(initialPos);

						for (int counter = 0; counter <= ConfigHandler.EXTRAPOLATED_MEGASPONGE_RADIUS.getValue(); counter++) {
							List<BlockPos> outputBlocks = new ArrayList<BlockPos>();

							for (BlockPos checkedPos : processedBlocks) {
								outputBlocks.addAll(this.getNearbyWater(player.world, checkedPos));
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

						for (BlockPos exterminatedBlock : doomedWaterBlocks)
							this.absorbWaterBlock(exterminatedBlock, player.world.getBlockState(exterminatedBlock), player.world);

						doomedWaterBlocks.clear();

						player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 64, player.dimension)), new PacketPortalParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 40, 1.0D, false));
						this.cooldownMap.put(player, 20);

					}

			}
		}
	}

	public void absorbWaterBlock(BlockPos pos, BlockState state, World world) {

		if (state.getBlock() instanceof IBucketPickupHandler && ((IBucketPickupHandler) state.getBlock()).pickupFluid(world, pos, state) != Fluids.EMPTY) {
			// Whatever
		} else if (state.getBlock() instanceof FlowingFluidBlock) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		} else if (state.getMaterial() == Material.OCEAN_PLANT || state.getMaterial() == Material.SEA_GRASS) {
			TileEntity tileentity = state.hasTileEntity() ? world.getTileEntity(pos) : null;
			Block.spawnDrops(state, world, pos, tileentity);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
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
			if (world.getBlockState(checkedPos).getFluidState().isTagged(FluidTags.WATER)) {
				waterBlocks.add(checkedPos);
			}
		}

		return waterBlocks;
	}

}
