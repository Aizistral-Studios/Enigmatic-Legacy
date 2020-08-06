package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumScythe extends SwordItem implements IPerhaps {

	protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.getDefaultState(), Blocks.GRASS_PATH, Blocks.FARMLAND.getDefaultState(), Blocks.DIRT, Blocks.FARMLAND.getDefaultState(), Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState()));
	public Set<Material> effectiveMaterials;

	public EtheriumScythe() {
		super(EnigmaticMaterials.ETHERIUM, 3, -2.0F, ItemBaseTool.getDefaultProperties().rarity(Rarity.RARE).func_234689_a_());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_scythe"));

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveMaterials.add(Material.LEAVES);
		this.effectiveMaterials.add(Material.BAMBOO);
		this.effectiveMaterials.add(Material.BAMBOO_SAPLING);
		this.effectiveMaterials.add(Material.SEA_GRASS);
		this.effectiveMaterials.add(Material.PLANTS);
		this.effectiveMaterials.add(Material.OCEAN_PLANT);
		this.effectiveMaterials.add(Material.TALL_PLANTS);
		this.effectiveMaterials.add(Material.CACTUS);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() == -1)
			return;

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe1", TextFormatting.GOLD, ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe2", TextFormatting.GOLD, ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
	}

	public static boolean attemptTransformLand(World world, PlayerEntity player, BlockPos blockpos, ItemStack item, Hand hand) {

		if (world.isAirBlock(blockpos.up())) {
			BlockState blockstate = EtheriumScythe.HOE_LOOKUP.get(world.getBlockState(blockpos).getBlock());
			if (blockstate != null) {
				world.playSound(player, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				if (!world.isRemote) {
					world.setBlockState(blockpos, blockstate, 11);
					if (player != null) {
						item.damageItem(1, player, (p_220043_1_) -> {
							p_220043_1_.sendBreakAnimation(hand);
						});
					}
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		ActionResultType type = Items.DIAMOND_HOE.onItemUse(context);

		if (context.getPlayer().isCrouching())
			return type;

		int supRad = (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() - 1) / 2;

		if (type == ActionResultType.SUCCESS)
			for (int x = -supRad; x <= supRad; x++) {
				for (int z = -supRad; z <= supRad; z++) {
					if (x == 0 & z == 0)
						continue;

					EtheriumScythe.attemptTransformLand(context.getWorld(), context.getPlayer(), context.getPos().add(x, 0, z), context.getItem(), context.getHand());
				}
			}

		return type;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof PlayerEntity && !entityLiving.isCrouching() && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() != -1) {
			Direction face = Direction.UP;

			AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos.add(0, (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() - 1) / 2, 0), this.effectiveMaterials, ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue(), ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue(), false, pos, stack, (objPos, objState) -> {
				stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
			});
		}

		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.getTier().getEfficiency();
	}

}
