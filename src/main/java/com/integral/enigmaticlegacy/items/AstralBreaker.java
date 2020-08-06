package com.integral.enigmaticlegacy.items;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.integral.enigmaticlegacy.packets.clients.PacketFlameParticles;

import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.PacketDistributor;

public class AstralBreaker extends ItemBaseTool {

	public AstralBreaker() {
		super(4F, -2.8F, EnigmaticMaterials.ETHERIUM, new HashSet<>(),
				ItemBaseTool.getDefaultProperties()
				.addToolType(ToolType.PICKAXE, EnigmaticMaterials.ETHERIUM.getHarvestLevel())
				.addToolType(ToolType.AXE, EnigmaticMaterials.ETHERIUM.getHarvestLevel())
				.addToolType(ToolType.SHOVEL, EnigmaticMaterials.ETHERIUM.getHarvestLevel())
				.defaultMaxDamage(4000)
				.rarity(Rarity.EPIC)
				.func_234689_a_());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "astral_breaker"));

		this.effectiveMaterials.addAll(((EtheriumPickaxe) EnigmaticLegacy.etheriumPickaxe).effectiveMaterials);
		this.effectiveMaterials.addAll(((EtheriumAxe) EnigmaticLegacy.etheriumAxe).effectiveMaterials);
		this.effectiveMaterials.addAll(((EtheriumShovel) EnigmaticLegacy.etheriumShovel).effectiveMaterials);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker1", TextFormatting.GOLD, 3, 1);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.astralBreaker3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	public void spawnFlameParticles(World world, BlockPos pos) {
		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 128, world.func_234923_W_())), new PacketFlameParticles(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 18, true));
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (!world.isRemote)
			this.spawnFlameParticles(world, pos);

		if (entityLiving instanceof PlayerEntity && !entityLiving.isCrouching() && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_PICKAXE_RADIUS.getValue() != -1) {

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

}
