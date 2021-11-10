package com.integral.etherium.items;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.core.IEtheriumTool;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumAxe extends AxeItem implements IEtheriumTool {
	private final IEtheriumConfig config;
	public Set<Material> effectiveMaterials;

	public EtheriumAxe(IEtheriumConfig config) {
		super(config.getToolMaterial(), 10, -3.2F, EtheriumUtil.defaultProperties(config, EtheriumAxe.class).isImmuneToFire());
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_axe"));
		this.config = config;

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveMaterials.add(Material.WOOD);
		this.effectiveMaterials.add(Material.LEAVES);
		this.effectiveMaterials.add(Material.CACTUS);
		this.effectiveMaterials.add(Material.BAMBOO);
		this.effectiveMaterials.add(Material.NETHER_WOOD);

		this.config.getSorceryMaterial("INFUSED_WOOD").ifPresent(this.effectiveMaterials::add);
	}

	@Override
	public String getTranslationKey() {
		return this.config.isStandalone() ? "item.enigmaticlegacy." + this.getRegistryName().getPath() : super.getTranslationKey();
	}

	@Override
	public IEtheriumConfig getConfig() {
		return this.config;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (this.config.getAxeMiningVolume() == -1)
			return;

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumAxe1", TextFormatting.GOLD, this.config.getAxeMiningVolume());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

			if (!this.config.disableAOEShiftInhibition()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumAxe2");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumAxe3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (!this.areaEffectsAllowed(stack)) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.aoeDisabled");
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof PlayerEntity && this.areaEffectsEnabled((PlayerEntity) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && this.config.getAxeMiningVolume() != -1) {
			Direction face = Direction.UP;

			AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos.add(0, (this.config.getAxeMiningVolume() - 1) / 2, 0), this.effectiveMaterials, this.config.getAxeMiningVolume(), this.config.getAxeMiningVolume(), false, pos, stack, (objPos, objState) -> {
				stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
			});
		}

		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.efficiency;
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
		else
			return super.onItemUse(context);
	}

}
