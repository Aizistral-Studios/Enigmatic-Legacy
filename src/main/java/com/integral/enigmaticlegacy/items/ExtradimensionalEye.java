package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ExtradimensionalEye extends ItemBase {

	public float range = 3.0F;

	public ExtradimensionalEye() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.UNCOMMON).maxStackSize(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "extradimensional_eye"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.EXTRADIMENSIONAL_EYE_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEye2");
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

			if (dimensionID.equals("minecraft:overworld"))
				boundDimensionName = "tooltip.enigmaticlegacy.overworld";
			else if (dimensionID.equals("minecraft:the_nether"))
				boundDimensionName = "tooltip.enigmaticlegacy.nether";
			else if (dimensionID.equals("minecraft:the_end"))
				boundDimensionName = "tooltip.enigmaticlegacy.end";

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeLocation");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeX", TextFormatting.GOLD, ItemNBTHelper.getInt(stack, "BoundX", 0));
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeY", TextFormatting.GOLD, ItemNBTHelper.getInt(stack, "BoundY", 0));
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeZ", TextFormatting.GOLD, ItemNBTHelper.getInt(stack, "BoundZ", 0));
			if (boundDimensionName != null)
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeDimension", null, new TranslationTextComponent(boundDimensionName));
			else
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.extradimensionalEyeDimension", TextFormatting.GOLD, dimensionID);
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (playerIn.isCrouching() && ItemNBTHelper.getString(itemstack, "BoundDimension", null) == null) {
			ItemNBTHelper.setDouble(itemstack, "BoundX", playerIn.getPosX());
			ItemNBTHelper.setDouble(itemstack, "BoundY", playerIn.getPosY());
			ItemNBTHelper.setDouble(itemstack, "BoundZ", playerIn.getPosZ());
			
			ItemNBTHelper.setString(itemstack, "BoundDimension", playerIn.world.func_234923_W_().func_240901_a_().toString());
			playerIn.swingArm(handIn);
			return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
		}

		return new ActionResult<>(ActionResultType.FAIL, itemstack);
	}

}
