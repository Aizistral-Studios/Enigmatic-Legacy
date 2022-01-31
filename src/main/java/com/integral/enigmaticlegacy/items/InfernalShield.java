package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ITaintable;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class InfernalShield extends ItemBase implements Vanishable {

	public InfernalShield() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "infernal_shield"));
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.tainted1");
	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_43105_) {
		return UseAnim.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack p_43107_) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_43099_, Player p_43100_, InteractionHand p_43101_) {
		ItemStack itemstack = p_43100_.getItemInHand(p_43101_);
		p_43100_.startUsingItem(p_43101_);
		return InteractionResultHolder.consume(itemstack);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
		return net.minecraftforge.common.ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
	}

	@Override
	public boolean isValidRepairItem(ItemStack p_43091_, ItemStack p_43092_) {
		return p_43092_.is(ItemTags.PLANKS) || super.isValidRepairItem(p_43091_, p_43092_);
	}



}
