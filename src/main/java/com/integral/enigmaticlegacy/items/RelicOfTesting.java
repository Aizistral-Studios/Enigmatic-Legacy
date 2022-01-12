package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.CreeperEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.stats.Stats;
import net.minecraft.util.InteractionResultHolder;
import net.minecraft.util.InteractionResult;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.level.Level;
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
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.relicOfTesting1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {

		ItemStack itemstack = playerIn.getItemInHand(handIn);
		playerIn.startUsingItem(handIn);

		SuperpositionHandler.setSpellstoneCooldown(playerIn, 0);

		SuperpositionHandler.setPersistentInteger(playerIn, EnigmaticLegacy.overworldRevelationTome.persistantPointsTag, 0);

		ItemStack checkTag = playerIn.inventory.offhand.get(0);

		if (checkTag != null) {
			playerIn.sendMessage(new TextComponent(checkTag.getOrCreateTag().getAsString()), playerIn.getUUID());
		}

		if (!worldIn.isClientSide) {

			//playerIn.sendMessage(new TextComponent("INTEGER: " + UltimaTestConfig.integerTest.getValue()), playerIn.getUniqueID());
			//playerIn.sendMessage(new TextComponent("FLOAT: " + UltimaTestConfig.floatTest.getValue()), playerIn.getUniqueID());
			//playerIn.sendMessage(new TextComponent("BOOLEAN: " + UltimaTestConfig.booleanTest.getValue()), playerIn.getUniqueID());

		}

		/*
		if(!worldIn.isRemote) {
		    Component name = null;

		    if(itemstack.hasDisplayName()) {
		        name = itemstack.getDisplayName();
		    } else {
		        name = new TranslatableComponent(this.getTranslationKey());
		    }

		    playerIn.openContainer(new PortableCrafterContainerProvider(name));
		}
		 */

		playerIn.swing(handIn);

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);

	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
		List<CreeperEntity> list = world.getEntitiesOfClass(CreeperEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(entity, 24D));

		for (CreeperEntity creeper : list) {
			creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(creeper, Player.class, (arg) -> {
				return arg instanceof Player ? SuperpositionHandler.hasCurio(arg, EnigmaticLegacy.enigmaticAmulet) : false;
			}, 6.0F, 1.0D, 1.2D, EntityPredicates.NO_CREATIVE_OR_SPECTATOR::test));

			if (creeper.getTarget() == entity) {
				creeper.setTarget(null);
			}
		}

		if (entity instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) entity;
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
	 * public InteractionResult onItemUse(UseOnContext context) { Player
	 * player = context.getPlayer(); Level world = context.getWorld(); //ItemStack
	 * stack = context.getItem();
	 *
	 * if (world.getBlockState(context.getPos()).hasTileEntity()) { if
	 * (world.getTileEntity(context.getPos()) instanceof ChestTileEntity) {
	 * ChestTileEntity chest = (ChestTileEntity)
	 * world.getTileEntity(context.getPos());
	 *
	 *
	 * if (context.getFace() != Direction.UP) { chest.clear(); } else {
	 * chest.setLootTable(BuiltInLootTables.CHESTS_SIMPLE_DUNGEON,
	 * lootRandomizer.nextLong()); chest.fillWithLoot(player); }
	 *
	 * return InteractionResult.SUCCESS; } }
	 *
	 * return InteractionResult.PASS; }
	 */

}
