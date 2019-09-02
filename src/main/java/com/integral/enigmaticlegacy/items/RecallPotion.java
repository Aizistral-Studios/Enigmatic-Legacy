package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.packets.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.PacketRecallParticles;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class RecallPotion extends Item implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public RecallPotion(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 return integratedProperties;
 
 }
 
 public static void initConfigValues() {}
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.recallPotion1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.recallPotion2");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.RECALL_POTION_ENABLED.get() : false;
 }

 @Override
 public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
     PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity)entityLiving : null;
     if (player == null || !player.abilities.isCreativeMode) {
        stack.shrink(1);
     }

     if (player instanceof ServerPlayerEntity) {
        CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
     }

     if (!worldIn.isRemote) {
    	Vec3d vec = SuperpositionHandler.getValidSpawn(worldIn, player);

    	worldIn.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
    	
    	EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 128, player.dimension)), new PacketPortalParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 100, 1.25F));
    	
    	player.setPositionAndUpdate(vec.x, vec.y, vec.z);
    	worldIn.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
    	
    	EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 128, player.dimension)), new PacketRecallParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 48));
     }

     if (player == null || !player.abilities.isCreativeMode) {
        if (stack.isEmpty()) {
           return new ItemStack(Items.GLASS_BOTTLE);
        }

        if (player != null) {
           player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
        }
     }

     return stack;
  }

  /**
   * How long it takes to use or consume an item
   */
 @Override
  public int getUseDuration(ItemStack stack) {
     return 32;
  }

  /**
   * returns the action that specifies what animation to play when the items is being used
   */
 @Override
  public UseAction getUseAction(ItemStack stack) {
     return UseAction.DRINK;
  }

  /**
   * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
   * {@link #onItemUse}.
   */
 @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
     playerIn.setActiveHand(handIn);
     return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
  }

  @OnlyIn(Dist.CLIENT)
  public boolean hasEffect(ItemStack stack) {
     return true;
  }
  
}
