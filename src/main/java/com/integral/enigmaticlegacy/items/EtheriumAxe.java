package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IMultiblockMiningTool;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;

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

public class EtheriumAxe extends AxeItem implements IPerhaps, IMultiblockMiningTool {

	public Set<Material> effectiveMaterials;

	public EtheriumAxe() {
		super(EnigmaticMaterials.ETHERIUM, 10, -3.2F, ItemBaseTool.getDefaultProperties().rarity(Rarity.RARE).func_234689_a_());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_axe"));

		this.effectiveMaterials = Sets.newHashSet();
		this.effectiveMaterials.add(Material.WOOD);
		this.effectiveMaterials.add(Material.LEAVES);
		this.effectiveMaterials.add(Material.CACTUS);
		this.effectiveMaterials.add(Material.BAMBOO);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (ConfigHandler.ETHERIUM_AXE_VOLUME.getValue() == -1)
			return;

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumAxe1", TextFormatting.GOLD, ConfigHandler.ETHERIUM_AXE_VOLUME.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

			if (!ConfigHandler.DISABLE_AOE_SHIFT_SUPPRESSION.getValue())
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumAxe2");
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
	public boolean isForMortals() {
		return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

		if (entityLiving instanceof PlayerEntity && this.areaEffectsEnabled((PlayerEntity) entityLiving, stack) && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_AXE_VOLUME.getValue() != -1) {
			Direction face = Direction.UP;

			AOEMiningHelper.harvestCube(world, (PlayerEntity) entityLiving, face, pos.add(0, (ConfigHandler.ETHERIUM_AXE_VOLUME.getValue() - 1) / 2, 0), this.effectiveMaterials, ConfigHandler.ETHERIUM_AXE_VOLUME.getValue(), ConfigHandler.ETHERIUM_AXE_VOLUME.getValue(), false, pos, stack, (objPos, objState) -> {
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
