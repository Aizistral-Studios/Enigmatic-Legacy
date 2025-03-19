package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderRing extends ItemBaseCurio {
	public static Omniconfig.BooleanParameter inventoryButtonEnabled;
	public static Omniconfig.IntParameter buttonOffsetX;
	public static Omniconfig.IntParameter buttonOffsetY;
	public static Omniconfig.IntParameter buttonOffsetXCreative;
	public static Omniconfig.IntParameter buttonOffsetYCreative;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EnderChest");

		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			// NO-OP
		} else {
			inventoryButtonEnabled = builder
					.comment("Whether or not button for accessing Ender Chest should be added to inventory GUI when player has Ring of Ender equipped.")
					.getBoolean("ButtonEnabled", true);

			buttonOffsetX = builder
					.comment("Allows to set offset for Ender Chest button on X axis.")
					.minMax(32768)
					.getInt("ButtonOffsetX", 0);

			buttonOffsetY = builder
					.comment("Allows to set offset for Ender Chest button on Y axis.")
					.minMax(32768)
					.getInt("ButtonOffsetY", 0);

			buttonOffsetXCreative = builder
					.comment("Allows to set offset for Ender Chest button on X axis, for creative inventory specifically.")
					.minMax(32768)
					.getInt("ButtonOffsetXCreative", 0);

			buttonOffsetYCreative = builder
					.comment("Allows to set offset for Ender Chest button on Y axis, for creative inventory specifically.")
					.minMax(32768)
					.getInt("ButtonOffsetYCreative", 0);
		}

		builder.popPrefix();
	}

	public EnderRing() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderRing1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderRing2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", ChatFormatting.LIGHT_PURPLE, KeyMapping.createNameSupplier("key.enderRing").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}

	}

	/*
	 * @Override public InteractionResultHolder<ItemStack> onItemRightClick(Level worldIn,
	 * Player player, InteractionHand handIn) {
	 *
	 * ItemStack itemstack = player.getHeldItem(handIn);
	 * player.setActiveHand(handIn);
	 *
	 * if (!worldIn.isRemote & player instanceof ServerPlayer) {
	 * ServerPlayer playerServ = (ServerPlayer) player;
	 *
	 * ChestContainer container = ChestContainer.createGeneric9X3(8316,
	 * playerServ.inventory, playerServ.getInventoryEnderChest());
	 *
	 * playerServ.currentWindowId = container.windowId;
	 * playerServ.connection.sendPacket(new SOpenWindowPacket(container.windowId,
	 * container.getType(), Component.translatable("container.enderchest")));
	 * container.addListener(playerServ); playerServ.openContainer = container;
	 * net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new
	 * net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerServ,
	 * playerServ.openContainer));
	 *
	 * }
	 *
	 * EnigmaticLegacy.enigmaticLogger.info("Item used: " +
	 * CuriosAPI.getCurioTags(itemstack.getItem()));
	 *
	 * return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
	 *
	 * }
	 */

}
