package com.integral.enigmaticlegacy.items;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.packets.clients.PacketFlameParticles;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.PacketDistributor;

public class AstralBreaker extends ToolItem implements IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public Set<Material> effectiveMaterials;

	public AstralBreaker(IItemTier tier, Function<Properties, Properties> properties) {
		super(4F, -2.8F, tier, new HashSet<>(), properties.apply(new Properties().defaultMaxDamage((int) (tier.getMaxUses() * 1.5)).addToolType(ToolType.PICKAXE, tier.getHarvestLevel()).addToolType(ToolType.AXE, tier.getHarvestLevel()).addToolType(ToolType.SHOVEL, tier.getHarvestLevel())));

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveMaterials.addAll(((EtheriumPickaxe) EnigmaticLegacy.etheriumPickaxe).effectiveMaterials);
		this.effectiveMaterials.addAll(((EtheriumAxe) EnigmaticLegacy.etheriumAxe).effectiveMaterials);
		this.effectiveMaterials.addAll(((EtheriumShovel) EnigmaticLegacy.etheriumShovel).effectiveMaterials);
	}

	public static Properties setupIntegratedProperties() {
		AstralBreaker.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		AstralBreaker.integratedProperties.maxStackSize(1);
		AstralBreaker.integratedProperties.rarity(Rarity.EPIC);
		AstralBreaker.integratedProperties.defaultMaxDamage(4000);

		return AstralBreaker.integratedProperties;

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker1", 3, 1);
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker3");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public boolean isForMortals() {
		return true;
	}

	public void spawnFlameParticles(World world, BlockPos pos) {
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 128, world.dimension.getType())), new PacketFlameParticles(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 18, true));
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (!world.isRemote)
			this.spawnFlameParticles(world, pos);

		if (entityLiving instanceof PlayerEntity && !entityLiving.isShiftKeyDown() && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_PICKAXE_RADIUS.getValue() != -1) {

			RayTraceResult trace = AOEMiningHelper.calcRayTrace(world, (PlayerEntity) entityLiving, RayTraceContext.FluidMode.ANY);

			if (trace.getType() == RayTraceResult.Type.BLOCK) {
				BlockRayTraceResult blockTrace = (BlockRayTraceResult) trace;
				Direction face = blockTrace.getFace();

				AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos, this.effectiveMaterials, 3, 1, true, pos, stack, (objPos, objState) -> {

					stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
					this.spawnFlameParticles(world, objPos);

				});
			}
		}

		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
	}

	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
		Block block = blockIn.getBlock();
		int i = this.getTier().getHarvestLevel();

		if (blockIn.getHarvestTool() == net.minecraftforge.common.ToolType.PICKAXE) {
			return i >= blockIn.getHarvestLevel();
		} else if (blockIn.getHarvestTool() == net.minecraftforge.common.ToolType.AXE || blockIn.getHarvestTool() == net.minecraftforge.common.ToolType.SHOVEL)
			return true;

		Material material = blockIn.getMaterial();
		return this.effectiveMaterials.contains(material);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

}
