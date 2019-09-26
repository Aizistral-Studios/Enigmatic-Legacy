package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ForbiddenAxe extends SwordItem implements IPerhaps {
	
	public static Properties integratedProperties = new Item.Properties();
	
	public ForbiddenAxe(IItemTier tier, int attackDamageIn, float attackSpeedIn, Item.Properties properties) {
		super(tier, attackDamageIn, attackSpeedIn, properties);
	}
	
	public static Properties setupIntegratedProperties() {
		 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		 integratedProperties.maxStackSize(1);
		 integratedProperties.rarity(Rarity.EPIC);
		 integratedProperties.maxDamage(2000);
		 
		 return integratedProperties;
	 
	 }
	
	 @Override
	 public boolean isForMortals() {
	 	return ConfigHandler.FORBIDDEN_AXE_ENABLED.getValue();
	 }
	
	 @OnlyIn(Dist.CLIENT)
	 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		 if(ControlsScreen.hasShiftDown()) {
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe1");
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe2", ConfigHandler.FORBIDDEN_AXE_BEHEADING_BONUS.getValue().asPercentage()+"%");
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxe3");
		 } else {
			 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		 }
		 
		 int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
		 
		 try {
		 if (SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.monsterCharm))
			 if (ConfigHandler.MONSTER_CHARM_BONUS_LOOTING.getValue())
			 looting++;
		 } catch (NullPointerException ex) {
			 // Just don't do it lol
		 }
		 
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.forbiddenAxeBeheadingChance",  (ConfigHandler.FORBIDDEN_AXE_BEHEADING_BASE.getValue().asPercentage() + (ConfigHandler.FORBIDDEN_AXE_BEHEADING_BONUS.getValue().asPercentage()*looting)) + "%");
	 }
	
	@Override
	public boolean canHarvestBlock(BlockState blockIn) {
	      return false;
	}

}
