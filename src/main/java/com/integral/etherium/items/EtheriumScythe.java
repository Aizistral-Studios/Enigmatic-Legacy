package com.integral.etherium.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.core.IEtheriumTool;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.material.Material;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
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

public class EtheriumScythe extends SwordItem implements IEtheriumTool {
	protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(), Blocks.GRASS_PATH, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()));
	public Set<Material> effectiveMaterials;
	private final IEtheriumConfig config;

	public EtheriumScythe(IEtheriumConfig config) {
		super(config.getToolMaterial(), 3, -2.0F, EtheriumUtil.defaultProperties(config, EtheriumScythe.class).fireResistant());
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_scythe"));
		this.config = config;

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveMaterials.add(Material.LEAVES);
		this.effectiveMaterials.add(Material.BAMBOO);
		this.effectiveMaterials.add(Material.BAMBOO_SAPLING);
		this.effectiveMaterials.add(Material.REPLACEABLE_WATER_PLANT);
		this.effectiveMaterials.add(Material.PLANT);
		this.effectiveMaterials.add(Material.WATER_PLANT);
		this.effectiveMaterials.add(Material.REPLACEABLE_PLANT);
		this.effectiveMaterials.add(Material.CACTUS);
	}

	@Override
	public String getDescriptionId() {
		return this.config.isStandalone() ? "item.enigmaticlegacy." + this.getRegistryName().getPath() : super.getDescriptionId();
	}

	@Override
	public IEtheriumConfig getConfig() {
		return this.config;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (this.config.getScytheMiningVolume() == -1)
			return;

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe1", TextFormatting.GOLD, this.config.getScytheMiningVolume());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe2", TextFormatting.GOLD, this.config.getScytheMiningVolume());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			if (!this.config.disableAOEShiftInhibition()) {
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
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else
			return super.use(world, player, hand);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if (context.getPlayer().isCrouching())
			return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();

		ActionResultType type = Items.DIAMOND_HOE.useOn(context);

		if (!this.areaEffectsEnabled(context.getPlayer(), context.getItemInHand()))
			return type;

		int supRad = (this.config.getScytheMiningVolume() - 1) / 2;

		if (type == ActionResultType.CONSUME) {
			for (int x = -supRad; x <= supRad; x++) {
				for (int z = -supRad; z <= supRad; z++) {
					if (x == 0 & z == 0) {
						continue;
					}

					Items.DIAMOND_HOE.useOn(new ItemUseContext(context.getPlayer(), context.getHand(), new BlockRayTraceResult(context.getClickLocation().add(x, 0, z), Direction.UP, context.getClickedPos().offset(x, 0, z), context.isInside())));
				}
			}
		}

		return type;
	}

	@Override
	public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity && this.areaEffectsEnabled((PlayerEntity) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isClientSide && this.config.getScytheMiningVolume() != -1) {
			Direction face = Direction.UP;

			AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos.offset(0, (this.config.getScytheMiningVolume() - 1) / 2, 0), this.effectiveMaterials, this.config.getScytheMiningVolume(), this.config.getScytheMiningVolume(), false, pos, stack, (objPos, objState) -> {
				stack.hurtAndBreak(1, entityLiving, p -> p.broadcastBreakEvent(MobEntity.getEquipmentSlotForItem(stack)));
			});
		}

		return super.mineBlock(stack, world, state, pos, entityLiving);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.getTier().getSpeed();
	}

}
