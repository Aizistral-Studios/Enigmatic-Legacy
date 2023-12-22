package com.aizistral.enigmaticlegacy.items;

import java.awt.TextComponent;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IHidden;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RelicOfTesting extends ItemBase implements IHidden {

	public Random lootRandomizer = new Random();

	public RelicOfTesting() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1));
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return null;
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

		SuperpositionHandler.setPersistentInteger(playerIn, EnigmaticItems.TATTERED_TOME.persistantPointsTag, 0);

		ItemStack checkTag = playerIn.getInventory().offhand.get(0);

		if (checkTag != null) {
			playerIn.sendSystemMessage(Component.literal(checkTag.getOrCreateTag().getAsString()));
		}

		if (!worldIn.isClientSide) {
			//Quote.NO_PERIL.play((ServerPlayer) playerIn);

			TransientPlayerData.get(playerIn).setUnlockedNarrator(false);
			//playerIn.sendMessage(Component.literal("INTEGER: " + UltimaTestConfig.integerTest.getValue()), playerIn.getUniqueID());
			//playerIn.sendMessage(Component.literal("FLOAT: " + UltimaTestConfig.floatTest.getValue()), playerIn.getUniqueID());
			//playerIn.sendMessage(Component.literal("BOOLEAN: " + UltimaTestConfig.booleanTest.getValue()), playerIn.getUniqueID());

			worldIn.broadcastEntityEvent(playerIn, (byte) 60);
		} else {
			playerIn.deathTime = 20;
		}

		/*
		if(!worldIn.isRemote) {
		    Component name = null;

		    if(itemstack.hasDisplayName()) {
		        name = itemstack.getDisplayName();
		    } else {
		        name = Component.translatable(this.getTranslationKey());
		    }

		    playerIn.openContainer(new PortableCrafterContainerProvider(name));
		}
		 */

		playerIn.swing(handIn);

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);

	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
		List<Creeper> list = world.getEntitiesOfClass(Creeper.class, SuperpositionHandler.getBoundingBoxAroundEntity(entity, 24D));

		for (Creeper creeper : list) {
			creeper.goalSelector.addGoal(1, new AvoidEntityGoal<>(creeper, Player.class, (arg) -> {
				return arg instanceof Player ? SuperpositionHandler.hasCurio(arg, EnigmaticItems.ENIGMATIC_AMULET) : false;
			}, 6.0F, 1.0D, 1.2D, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));

			if (creeper.getTarget() == entity) {
				creeper.setTarget(null);
			}
		}

		if (entity instanceof ServerPlayer) {
			ServerPlayer player = (ServerPlayer) entity;
			if (entity.tickCount % 20 == 0) {
				//System.out.println("Time since rest: " + player.getStats().getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)));
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
