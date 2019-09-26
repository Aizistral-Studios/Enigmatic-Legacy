package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumScythe extends SwordItem implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public Set<Material> effectiveMaterials;
 private final float speed;
 private final float attackDamage;
 
 public EtheriumScythe(Properties properties, IItemTier tier, float attackSpeedIn, int attackDamageIn) {
		super(tier, attackDamageIn, attackSpeedIn, properties);
		
		this.speed = attackSpeedIn;
		this.attackDamage = attackDamageIn;
		
		effectiveMaterials = Sets.newHashSet();
		effectiveMaterials.add(Material.LEAVES);
		effectiveMaterials.add(Material.BAMBOO);
		effectiveMaterials.add(Material.BAMBOO_SAPLING);
		effectiveMaterials.add(Material.SEA_GRASS);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.OCEAN_PLANT);
		effectiveMaterials.add(Material.TALL_PLANTS);
		effectiveMaterials.add(Material.CACTUS);
	}
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 return integratedProperties;
 
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() == -1)
		 return;
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe1", ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue());
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe2");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }
 
 @Override
 public boolean isForMortals() {
	 return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
 }
 
 public ActionResultType onItemUse(ItemUseContext context) {
	 return Items.DIAMOND_HOE.onItemUse(context);
 }
 
 @Override
 public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

     if (entityLiving instanceof PlayerEntity && !entityLiving.isSneaking() && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() != -1) {
    	 Direction face = Direction.UP;
    	 
         AOEMiningHelper.harvestCube(world, (PlayerEntity)entityLiving, face, pos.add(0, (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue()-1)/2, 0), effectiveMaterials, ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue(), ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue(), false, pos, stack, true);
     }
     
     return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
 }
 
 @Override
 public float getDestroySpeed(ItemStack stack, BlockState state) {
     Material material = state.getMaterial();
     return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.getTier().getEfficiency();
  }
  
}



