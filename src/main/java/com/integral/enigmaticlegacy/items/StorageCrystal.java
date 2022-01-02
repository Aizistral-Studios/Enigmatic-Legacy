package com.integral.enigmaticlegacy.items;

import java.util.Collection;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPermanentCrystal;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StorageCrystal extends ItemBase implements IPermanentCrystal, IVanishable {

	public StorageCrystal() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).fireResistant().tab(null));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "storage_crystal"));
	}

	public ItemStack storeDropsOnCrystal(Collection<ItemEntity> drops, Player player, @Nullable ItemStack embeddedSoulCrystal) {
		ItemStack crystal = new ItemStack(this);
		CompoundNBT crystalNBT = ItemNBTHelper.getNBT(crystal);
		int counter = 0;

		for (ItemEntity drop : drops) {
			ItemStack dropStack = drop.getItem();
			CompoundNBT nbt = dropStack.serializeNBT();
			crystalNBT.put("storedStack" + counter, nbt);

			counter++;
		}

		if (embeddedSoulCrystal != null) {
			CompoundNBT deserializedCrystal = new CompoundNBT();
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
		CompoundNBT crystalNBT = ItemNBTHelper.getNBT(crystal);
		int counter = crystalNBT.getInt("storedStacks")-1;
		int exp = crystalNBT.getInt("storedXP");

		for (int c = counter; c >= 0; c--) {
			CompoundNBT nbt = crystalNBT.getCompound("storedStack" + c);
			ItemStack stack = ItemStack.of(nbt);
			if (!player.inventory.add(stack)) {
				ItemEntity drop = new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), stack);
				player.level.addFreshEntity(drop);
			}
			crystalNBT.remove("storedStack" + c);
		}

		ExperienceHelper.addPlayerXP(player, exp);

		if (retrieveSoul != null) {
			EnigmaticLegacy.soulCrystal.retrieveSoulFromCrystal(player, retrieveSoul);
		} else {
			player.level.playSound(null, new BlockPos(player.position()), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.0f, 1.0f);
		}

		ItemNBTHelper.setBoolean(crystal, "isStored", false);
		ItemNBTHelper.setInt(crystal, "storedStacks", 0);
		ItemNBTHelper.setInt(crystal, "storedXP", 0);

		return crystal;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, Player playerIn, Hand handIn) {
		playerIn.startUsingItem(handIn);

		if (!worldIn.isClientSide) {}

		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getItemInHand(handIn));
	}

}
