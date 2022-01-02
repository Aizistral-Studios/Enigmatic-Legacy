package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.triggers.RevelationGainTrigger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RevelationTome extends ItemBase implements IVanishable {

	public static final String revelationPointsTag = "revelationPoints";
	public static final String xpPointsTag = "xpPoints";
	public static final String formerReadersTag = "formerReaders";
	public static final String lastHolderTag = "lastHolder";

	public static enum TomeType {
		OVERWORLD("overworld"), NETHER("nether"), END("end"), GENERIC("generic");

		public final String typeName;

		private TomeType(String name) {
			this.typeName = name;
		}

		public static TomeType resolveType(@Nonnull String type) {
			if (type.equals("overworld"))
				return OVERWORLD;
			else if (type.equals("nether"))
				return NETHER;
			else if (type.equals("end"))
				return END;
			else
				return GENERIC;
		}
	}

	public final TomeType theType;
	public final String persistantPointsTag;

	public RevelationTome(Rarity rarity, TomeType type, String registryName) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(1));
		this.theType = type;
		this.persistantPointsTag = "enigmaticlegacy.revelation_points_" + this.theType.typeName;

		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, registryName));
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof ServerPlayerEntity) {
			ItemNBTHelper.setUUID(stack, lastHolderTag, entityIn.getUUID());
		}

	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);

		if (!RevelationTome.havePlayerRead(player, stack)) {
			RevelationTome.markRead(player, stack);

			int xp = ItemNBTHelper.getInt(stack, RevelationTome.xpPointsTag, Item.random.nextInt(1000));
			int revelation = ItemNBTHelper.getInt(stack, RevelationTome.revelationPointsTag, 1);

			if (player instanceof ServerPlayerEntity) {
				int currentPoints = SuperpositionHandler.getPersistentInteger(player, this.persistantPointsTag, 0);

				ExperienceHelper.addPlayerXP(player, xp);
				SuperpositionHandler.setPersistentInteger(player, this.persistantPointsTag, currentPoints + revelation);

				world.playSound(null, new BlockPos(player.position()), EnigmaticLegacy.LEARN, SoundCategory.PLAYERS, 0.75f, 1.0f);
				RevelationGainTrigger.INSTANCE.trigger((ServerPlayerEntity) player, this.theType, currentPoints + revelation);
				RevelationGainTrigger.INSTANCE.trigger((ServerPlayerEntity) player, TomeType.GENERIC, RevelationTome.getGenericPoints(player));
			} else {
				EnigmaticLegacy.proxy.pushRevelationToast(stack, xp, revelation);
			}

			player.swing(hand);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult<>(ActionResultType.PASS, stack);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.revelationTome1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.revelationTome2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.revelationTome3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.revelationTome4");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (!RevelationTome.havePlayerRead(Minecraft.getInstance().player, stack)) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.revelationTomeClick");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.revelationTomeMarkRead");
		}
	}

	public static int getGenericPoints(PlayerEntity player) {
		int overworldPoints = SuperpositionHandler.getPersistentInteger(player, "enigmaticlegacy.revelation_points_" + TomeType.OVERWORLD.typeName, 0);
		int netherPoints = SuperpositionHandler.getPersistentInteger(player, "enigmaticlegacy.revelation_points_" + TomeType.NETHER.typeName, 0);
		int endPoints = SuperpositionHandler.getPersistentInteger(player, "enigmaticlegacy.revelation_points_" + TomeType.END.typeName, 0);

		//System.out.println("Overworld points: " + overworldPoints + ", Nether points: " + netherPoints + ", End points: " + endPoints);
		//System.out.println("Generic points: " + (overworldPoints + netherPoints + endPoints));

		return overworldPoints + netherPoints + endPoints;
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
				if (entry.getAsString().equals(player.getGameProfile().getName())) {
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

	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 400;
	}

}