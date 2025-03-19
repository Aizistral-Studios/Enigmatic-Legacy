package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ExperienceHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;
import com.aizistral.enigmaticlegacy.triggers.RevelationGainTrigger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RevelationTome extends ItemBase implements Vanishable {

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

	public RevelationTome(Rarity rarity, TomeType type) {
		super(ItemBase.getDefaultProperties().rarity(rarity).stacksTo(1));
		this.theType = type;
		this.persistantPointsTag = "enigmaticlegacy.revelation_points_" + this.theType.typeName;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (entityIn instanceof ServerPlayer) {
			ItemNBTHelper.setUUID(stack, lastHolderTag, entityIn.getUUID());
		}

	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);

		if (!RevelationTome.havePlayerRead(player, stack)) {
			RevelationTome.markRead(player, stack);

			int xp = ItemNBTHelper.getInt(stack, RevelationTome.xpPointsTag, random.nextInt(1000));
			int revelation = ItemNBTHelper.getInt(stack, RevelationTome.revelationPointsTag, 1);

			if (player instanceof ServerPlayer) {
				int currentPoints = SuperpositionHandler.getPersistentInteger(player, this.persistantPointsTag, 0);

				ExperienceHelper.addPlayerXP(player, xp);
				SuperpositionHandler.setPersistentInteger(player, this.persistantPointsTag, currentPoints + revelation);

				world.playSound(null, BlockPos.containing(player.position()), EnigmaticSounds.LEARN, SoundSource.PLAYERS, 0.75f, 1.0f);
				RevelationGainTrigger.INSTANCE.trigger((ServerPlayer) player, this.theType, currentPoints + revelation);
				RevelationGainTrigger.INSTANCE.trigger((ServerPlayer) player, TomeType.GENERIC, RevelationTome.getGenericPoints(player));
			} else {
				EnigmaticLegacy.PROXY.pushRevelationToast(stack, xp, revelation);
			}

			player.swing(hand);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		}

		return new InteractionResultHolder<>(InteractionResult.PASS, stack);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
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

	public static int getGenericPoints(Player player) {
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

	public static boolean havePlayerRead(Player player, ItemStack tome) {
		boolean haveReadBefore = false;

		CompoundTag nbt = ItemNBTHelper.getNBT(tome);

		Tag uncheckedList = nbt.contains(RevelationTome.formerReadersTag) ? nbt.get(RevelationTome.formerReadersTag) : new ListTag();
		if (uncheckedList instanceof ListTag) {
			ListTag list = (ListTag) uncheckedList;

			for (Tag entry : list) {
				if (entry.getAsString().equals(player.getGameProfile().getName())) {
					haveReadBefore = true;
					break;
				}
			}
		}

		return haveReadBefore;
	}

	public static void markRead(Player player, ItemStack tome) {
		CompoundTag nbt = ItemNBTHelper.getNBT(tome);
		ListTag list = (nbt.get(RevelationTome.formerReadersTag) instanceof ListTag) ? (ListTag) nbt.get(RevelationTome.formerReadersTag) : new ListTag();

		list.add(StringTag.valueOf(player.getGameProfile().getName()));
		nbt.put(RevelationTome.formerReadersTag, list);
		tome.setTag(nbt);
	}

	@Override
	public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
		return 400;
	}

}