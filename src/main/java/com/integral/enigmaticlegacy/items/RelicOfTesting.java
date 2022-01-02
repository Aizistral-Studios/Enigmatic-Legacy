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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.stats.Stats;
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
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).tab(null));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "relic_of_testing"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.relicOfTesting1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {

		ItemStack itemstack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);

		SuperpositionHandler.setSpellstoneCooldown(playerIn, 0);

		SuperpositionHandler.setPersistentInteger(playerIn, EnigmaticLegacy.overworldRevelationTome.persistantPointsTag, 0);

		ItemStack checkTag = playerIn.inventory.offhand.get(0);

		if (checkTag != null) {
			playerIn.sendMessage(new StringTextComponent(checkTag.getOrCreateTag().getAsString()), playerIn.getUUID());
		}

		if (!worldIn.isClientSide) {

			//playerIn.sendMessage(new StringTextComponent("INTEGER: " + UltimaTestConfig.integerTest.getValue()), playerIn.getUniqueID());
			//playerIn.sendMessage(new StringTextComponent("FLOAT: " + UltimaTestConfig.floatTest.getValue()), playerIn.getUniqueID());
			//playerIn.sendMessage(new StringTextComponent("BOOLEAN: " + UltimaTestConfig.booleanTest.getValue()), playerIn.getUniqueID());

		}

		/*
		if(!worldIn.isRemote) {
		    ITextComponent name = null;

		    if(itemstack.hasDisplayName()) {
		        name = itemstack.getDisplayName();
		    } else {
		        name = new TranslationTextComponent(this.getTranslationKey());
		    }

		    playerIn.openContainer(new PortableCrafterContainerProvider(name));
		}
		 */

		playerIn.swing(handIn);

		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);

	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		List<CreeperEntity> list = world.getEntitiesOfClass(CreeperEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(entity, 24D));

		for (CreeperEntity creeper : list) {
			creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(creeper, PlayerEntity.class, (arg) -> {
				return arg instanceof PlayerEntity ? SuperpositionHandler.hasCurio(arg, EnigmaticLegacy.enigmaticAmulet) : false;
			}, 6.0F, 1.0D, 1.2D, EntityPredicates.NO_CREATIVE_OR_SPECTATOR::test));

			if (creeper.getTarget() == entity) {
				creeper.setTarget(null);
			}
		}

		if (entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) entity;
			if (entity.tickCount % 20 == 0) {
				System.out.println("Time since rest: " + player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)));
			}
		}

		/*

		if (world.isRemote) {
			if (UltimaTestConfig.fovOverride.getValue() != Minecraft.getInstance().gameSettings.fov) {
				Minecraft.getInstance().gameSettings.fov = UltimaTestConfig.fovOverride.getValue();
			}
		}

		 */
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
