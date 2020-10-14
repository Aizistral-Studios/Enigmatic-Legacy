package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.IMultiblockMiningTool;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

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
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumScythe extends SwordItem implements IMultiblockMiningTool {
	public static Omniconfig.IntParameter miningVolume;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EtheriumScythe");

		miningVolume = builder
				.comment("The volume Etherium Scythe AOE mining. Set to -1 to disable the feature.")
				.min(-1)
				.max(128-1)
				.getInt("MiningVolume", 3);

		builder.popPrefix();
	}

	protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.getDefaultState(), Blocks.GRASS_PATH, Blocks.FARMLAND.getDefaultState(), Blocks.DIRT, Blocks.FARMLAND.getDefaultState(), Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState()));
	public Set<Material> effectiveMaterials;

	public EtheriumScythe() {
		super(EnigmaticMaterials.ETHERIUM, 3, -2.0F, ItemBaseTool.getDefaultProperties().rarity(Rarity.RARE).isBurnable());
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
		if (miningVolume.getValue() == -1)
			return;

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe1", TextFormatting.GOLD, miningVolume.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe2", TextFormatting.GOLD, miningVolume.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			if (!OmniconfigHandler.disableAOEShiftSuppression.getValue()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe3");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe4");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (!this.areaEffectsAllowed(stack)) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.aoeDisabled");
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else
			return super.onItemRightClick(world, player, hand);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (context.getPlayer().isCrouching())
			return this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType();

		ActionResultType type = Items.DIAMOND_HOE.onItemUse(context);

		if (!this.areaEffectsEnabled(context.getPlayer(), context.getItem()))
			return type;

		int supRad = (miningVolume.getValue() - 1) / 2;

		if (type == ActionResultType.CONSUME) {
			for (int x = -supRad; x <= supRad; x++) {
				for (int z = -supRad; z <= supRad; z++) {
					if (x == 0 & z == 0) {
						continue;
					}

					Items.DIAMOND_HOE.onItemUse(new ItemUseContext(context.getPlayer(), context.getHand(), new BlockRayTraceResult(context.getHitVec().add(x, 0, z), Direction.UP, context.getPos().add(x, 0, z), context.isInside())));
				}
			}
		}

		return type;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof PlayerEntity && this.areaEffectsEnabled((PlayerEntity) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && miningVolume.getValue() != -1) {
			Direction face = Direction.UP;

			AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos.add(0, (miningVolume.getValue() - 1) / 2, 0), this.effectiveMaterials, miningVolume.getValue(), miningVolume.getValue(), false, pos, stack, (objPos, objState) -> {
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
