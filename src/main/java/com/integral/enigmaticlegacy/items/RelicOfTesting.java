package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RelicOfTesting extends ItemBase {

	public Random lootRandomizer = new Random();

	public RelicOfTesting() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).maxStackSize(1).group(null));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "relic_of_testing"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.relicOfTesting1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);

		SuperpositionHandler.setSpellstoneCooldown(playerIn, 0);

		ItemStack checkTag = playerIn.inventory.offHandInventory.get(0);

		playerIn.sendMessage(new StringTextComponent(checkTag.getOrCreateTag().getString()));

		playerIn.swingArm(handIn);

		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);

	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		List<CreeperEntity> list = world.getEntitiesWithinAABB(CreeperEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(entity, 24D));

		for (CreeperEntity creeper : list) {
			creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(creeper, PlayerEntity.class, (arg) -> {
				return arg instanceof PlayerEntity ? SuperpositionHandler.hasCurio(arg, EnigmaticLegacy.enigmaticAmulet) : false;
			}, 6.0F, 1.0D, 1.2D, EntityPredicates.CAN_AI_TARGET::test));

			if (creeper.getAttackTarget() == entity)
				creeper.setAttackTarget(null);
		}
	}

	/*
	 * public ActionResultType onItemUse(ItemUseContext context) { PlayerEntity
	 * player = context.getPlayer(); World world = context.getWorld(); //ItemStack
	 * stack = context.getItem();
	 *
	 * if (world.getBlockState(context.getPos()).hasTileEntity()) { if
	 * (world.getTileEntity(context.getPos()) instanceof ChestTileEntity) {
	 * ChestTileEntity chest = (ChestTileEntity)
	 * world.getTileEntity(context.getPos());
	 *
	 *
	 * if (context.getFace() != Direction.UP) { chest.clear(); } else {
	 * chest.setLootTable(LootTables.CHESTS_SIMPLE_DUNGEON,
	 * lootRandomizer.nextLong()); chest.fillWithLoot(player); }
	 *
	 * return ActionResultType.SUCCESS; } }
	 *
	 * return ActionResultType.PASS; }
	 */

}
