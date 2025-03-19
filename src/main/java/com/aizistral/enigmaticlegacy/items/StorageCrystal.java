package com.aizistral.enigmaticlegacy.items;

import java.util.Collection;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IPermanentCrystal;
import com.aizistral.enigmaticlegacy.helpers.ExperienceHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

public class StorageCrystal extends ItemBase implements IPermanentCrystal, Vanishable {

	public StorageCrystal() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).fireResistant());
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return null;
	}

	public ItemStack storeDropsOnCrystal(Collection<ItemEntity> drops, Player player, @Nullable ItemStack embeddedSoulCrystal) {
		ItemStack crystal = new ItemStack(this);
		CompoundTag crystalNBT = ItemNBTHelper.getNBT(crystal);
		int counter = 0;

		for (ItemEntity drop : drops) {
			ItemStack dropStack = drop.getItem();
			CompoundTag nbt = dropStack.serializeNBT();
			crystalNBT.put("storedStack" + counter, nbt);

			counter++;
		}

		if (embeddedSoulCrystal != null) {
			CompoundTag deserializedCrystal = new CompoundTag();
			embeddedSoulCrystal.deserializeNBT(deserializedCrystal);

			crystalNBT.put("embeddedSoul", deserializedCrystal);
		}

		ItemNBTHelper.setInt(crystal, "storedStacks", counter);

		int exp = ExperienceHelper.getPlayerXP(player);
		ExperienceHelper.drainPlayerXP(player, exp);

		ItemNBTHelper.setInt(crystal, "storedXP", (int) (exp * EnigmaticAmulet.savedXPFraction.getValue()));
		ItemNBTHelper.setBoolean(crystal, "isStored", true);

		return crystal;
	}

	public ItemStack retrieveDropsFromCrystal(ItemStack crystal, Player player, ItemStack retrieveSoul) {
		CompoundTag crystalNBT = ItemNBTHelper.getNBT(crystal);
		int counter = crystalNBT.getInt("storedStacks")-1;
		int exp = crystalNBT.getInt("storedXP");

		for (int c = counter; c >= 0; c--) {
			CompoundTag nbt = crystalNBT.getCompound("storedStack" + c);
			ItemStack stack = ItemStack.of(nbt);
			if (!player.getInventory().add(stack)) {
				ItemEntity drop = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack);
				player.level().addFreshEntity(drop);
			}
			crystalNBT.remove("storedStack" + c);
		}

		ExperienceHelper.addPlayerXP(player, exp);

		if (retrieveSoul != null) {
			EnigmaticItems.SOUL_CRYSTAL.retrieveSoulFromCrystal(player, retrieveSoul);
		} else {
			player.level().playSound(null, BlockPos.containing(player.position()), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);
		}

		ItemNBTHelper.setBoolean(crystal, "isStored", false);
		ItemNBTHelper.setInt(crystal, "storedStacks", 0);
		ItemNBTHelper.setInt(crystal, "storedXP", 0);

		return crystal;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		playerIn.startUsingItem(handIn);

		if (!worldIn.isClientSide) {}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
	}

}
