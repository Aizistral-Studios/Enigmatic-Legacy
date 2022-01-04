package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GemOfBinding extends ItemBase implements Vanishable {

	public GemOfBinding() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.UNCOMMON).stacksTo(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "gem_of_binding"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.gemOfBinding1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (ItemNBTHelper.verifyExistance(stack, "BoundPlayer")) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.boundToPlayer", ChatFormatting.DARK_RED, ItemNBTHelper.getString(stack, "BoundPlayer", "Herobrine"));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack stack) {
		return ItemNBTHelper.verifyExistance(stack, "BoundPlayer") || super.isFoil(stack);
	}

	@Override
	public ActionResult<ItemStack> use(Level worldIn, Player player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);

		if (player.isCrouching()) {
			ItemNBTHelper.setString(itemstack, "BoundPlayer", player.getDisplayName().getString());
			ItemNBTHelper.setUUID(itemstack, "BoundUUID", player.getUUID());

			worldIn.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));

			player.swing(hand);
			return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
		}

		return new ActionResult<>(ActionResultType.FAIL, itemstack);
	}

}
