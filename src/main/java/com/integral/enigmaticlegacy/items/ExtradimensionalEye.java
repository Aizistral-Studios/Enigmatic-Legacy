package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ExtradimensionalEye extends ItemBase implements IVanishable {

	public float range = 3.0F;

	public ExtradimensionalEye() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.UNCOMMON).stacksTo(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "extradimensional_eye"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye7");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
		if (ItemNBTHelper.verifyExistance(stack, "BoundDimension")) {

			String boundDimensionName = null;
			String dimensionID = ItemNBTHelper.getString(stack, "BoundDimension", "minecraft:overworld");

			if (dimensionID.equals("minecraft:overworld")) {
				boundDimensionName = "tooltip.enigmaticlegacy.overworld";
			} else if (dimensionID.equals("minecraft:the_nether")) {
				boundDimensionName = "tooltip.enigmaticlegacy.nether";
			} else if (dimensionID.equals("minecraft:the_end")) {
				boundDimensionName = "tooltip.enigmaticlegacy.end";
			}

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeLocation");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeX", TextFormatting.GOLD, ItemNBTHelper.getInt(stack, "BoundX", 0));
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeY", TextFormatting.GOLD, ItemNBTHelper.getInt(stack, "BoundY", 0));
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeZ", TextFormatting.GOLD, ItemNBTHelper.getInt(stack, "BoundZ", 0));
			if (boundDimensionName != null) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeDimension", null, new TranslationTextComponent(boundDimensionName));
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeDimension", TextFormatting.GOLD, dimensionID);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, Player playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		if (playerIn.isCrouching() && ItemNBTHelper.getString(itemstack, "BoundDimension", null) == null) {
			ItemNBTHelper.setDouble(itemstack, "BoundX", playerIn.getX());
			ItemNBTHelper.setDouble(itemstack, "BoundY", playerIn.getY());
			ItemNBTHelper.setDouble(itemstack, "BoundZ", playerIn.getZ());

			ItemNBTHelper.setString(itemstack, "BoundDimension", playerIn.level.dimension().location().toString());
			playerIn.swing(handIn);
			return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
		}

		return new ActionResult<>(ActionResultType.FAIL, itemstack);
	}

}
