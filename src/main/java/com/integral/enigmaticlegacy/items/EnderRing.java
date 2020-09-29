package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;

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
import top.theillusivec4.curios.api.CuriosApi;

public class EnderRing extends ItemBaseCurio {

	public EnderRing() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ender_ring"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.ENDER_RING_ENABLED.getValue();
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
