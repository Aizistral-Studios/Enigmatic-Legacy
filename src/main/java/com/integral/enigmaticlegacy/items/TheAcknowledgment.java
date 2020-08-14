package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nonnull;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.base.PersistentData.DataHolder.BookData;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class TheAcknowledgment extends ItemBase {

	public TheAcknowledgment() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).maxStackSize(1));

		this.setRegistryName(EnigmaticLegacy.MODID, "the_acknowledgment");
	}
	/*
	public static boolean isOpen() {
		return Registry.ITEM.getKey(EnigmaticLegacy.theAcknowledgment).equals(PatchouliAPI.instance.getOpenBookGui());
	}

	public static ITextComponent getEdition() {
		return PatchouliAPI.instance.getSubtitle(Registry.ITEM.getKey(EnigmaticLegacy.theAcknowledgment));
	}

	public static ITextComponent getTitle(ItemStack stack) {
		ITextComponent title = stack.getDisplayName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = new StringTextComponent(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) playerIn, Registry.ITEM.getKey(EnigmaticLegacy.theAcknowledgment));

		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		try {
			tooltip.add(new StringTextComponent("").func_230529_a_(TheAcknowledgment.getEdition()).func_240699_a_(TextFormatting.DARK_PURPLE));
		} catch (Exception ex) {
			ex.printStackTrace();
			// Just don't do it lol
		}
	}
	*/
}
