package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Cancelable;

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
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ender_ring"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderRing1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enderRing2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", TextFormatting.LIGHT_PURPLE, KeyBinding.getDisplayString("key.enderRing").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

	/*
	 * @Override public ActionResult<ItemStack> onItemRightClick(World worldIn,
	 * PlayerEntity player, Hand handIn) {
	 *
	 * ItemStack itemstack = player.getHeldItem(handIn);
	 * player.setActiveHand(handIn);
	 *
	 * if (!worldIn.isRemote & player instanceof ServerPlayerEntity) {
	 * ServerPlayerEntity playerServ = (ServerPlayerEntity) player;
	 *
	 * ChestContainer container = ChestContainer.createGeneric9X3(8316,
	 * playerServ.inventory, playerServ.getInventoryEnderChest());
	 *
	 * playerServ.currentWindowId = container.windowId;
	 * playerServ.connection.sendPacket(new SOpenWindowPacket(container.windowId,
	 * container.getType(), new TranslationTextComponent("container.enderchest")));
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
	 * return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
	 *
	 * }
	 */

}
