package com.integral.enigmaticlegacy.items;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.PatchouliHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class RevelationTome extends ItemBase {

	public static final String revelationPointsTag = "revelationPoints";
	public static final String xpPointsTag = "xpPoints";
	public static final String formerReadersTag = "formerReaders";

	public static enum TomeType {
		OVERWORLD("overworld"),
		NETHER("nether"),
		END("end");

		public final String registryName;
		private TomeType(String name) {
			this.registryName = name;
		}
	}

	public final TomeType theType;
	public final String persistantPointsTag;

	public RevelationTome(Rarity rarity, TomeType type) {
		super(ItemBase.getDefaultProperties().rarity(rarity).maxStackSize(1));
		this.theType = type;

		this.persistantPointsTag = "enigmaticlegacy.revelation_points_"+ this.theType.registryName;
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "revelation_tome_" + this.theType.registryName));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);

		//if (playerIn instanceof ServerPlayerEntity)
		//	System.out.println("Current " + this.theType.registryName + " Points: " + SuperpositionHandler.getPersistentInteger(playerIn, this.persistantPointsTag, 0));

		if (!RevelationTome.havePlayerRead(playerIn, stack)) {
			RevelationTome.markRead(playerIn, stack);
			int xp = ItemNBTHelper.getInt(stack, RevelationTome.xpPointsTag, Item.random.nextInt(1000));
			int revelation = ItemNBTHelper.getInt(stack, RevelationTome.revelationPointsTag, 1);
			int currentPoints = SuperpositionHandler.getPersistentInteger(playerIn, this.persistantPointsTag, 0);

			ExperienceHelper.addPlayerXP(playerIn, xp);
			SuperpositionHandler.setPersistentInteger(playerIn, this.persistantPointsTag, currentPoints+revelation);

			playerIn.swingArm(handIn);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}


		return new ActionResult<>(ActionResultType.PASS, stack);

	}

	public ItemStack createTome(int revelationPoints, int experiencePoints) {
		ItemStack theTome = new ItemStack(this);

		ItemNBTHelper.setInt(theTome, RevelationTome.revelationPointsTag, revelationPoints);
		ItemNBTHelper.setInt(theTome, RevelationTome.xpPointsTag, experiencePoints);

		return theTome;
	}

	public static boolean havePlayerRead(PlayerEntity player, ItemStack tome) {
		boolean haveReadBefore = false;

		CompoundNBT nbt = ItemNBTHelper.getNBT(tome);

		INBT uncheckedList = nbt.contains(RevelationTome.formerReadersTag) ? nbt.get(RevelationTome.formerReadersTag) : new ListNBT();
		if (uncheckedList instanceof ListNBT) {
			ListNBT list = (ListNBT) uncheckedList;

			for (INBT entry : list) {
				if (entry.getString().equals(player.getGameProfile().getName())) {
					haveReadBefore = true;
					break;
				}
			}
		}

		return haveReadBefore;
	}

	public static void markRead(PlayerEntity player, ItemStack tome) {
		CompoundNBT nbt = ItemNBTHelper.getNBT(tome);
		ListNBT list = (nbt.get(RevelationTome.formerReadersTag) instanceof ListNBT) ? (ListNBT) nbt.get(RevelationTome.formerReadersTag) : new ListNBT();

		list.add(StringNBT.valueOf(player.getGameProfile().getName()));
		nbt.put(RevelationTome.formerReadersTag, list);
		tome.setTag(nbt);
	}

}