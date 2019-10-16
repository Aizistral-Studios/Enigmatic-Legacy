package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.capability.ICurio;

public class AngelBlessing extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();

 public static List<String> immunityList = new ArrayList<String>();
 public static HashMap<String, Float> resistanceList = new HashMap<String, Float>();
 public static double range = 4.0D;

 public AngelBlessing(Properties properties) {
		super(properties);
		
		immunityList.add(DamageSource.FALL.damageType);
		immunityList.add(DamageSource.FLY_INTO_WALL.damageType);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 return integratedProperties;
 }
 
 @Override
 public boolean isForMortals() {
 	return ConfigHandler.ANGEL_BLESSING_ENABLED.getValue();
 }
 
 @Override
 public boolean canEquip(String identifier, LivingEntity living) {
	  if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.angelBlessing))
		  return false;
	  else
		  return true;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessingCooldown", ((float)(ConfigHandler.ANGEL_BLESSING_COOLDOWN.getValue()))/20.0F);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing4");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing5");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing6");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
	 
	 try {
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 	LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.spellstoneAbility").get().toUpperCase());
		 } catch (NullPointerException ex) {
			// Just don't do it lol 
	 }
 }
 
 public void triggerActiveAbility(World world, PlayerEntity player, ItemStack stack) {
	 if (SuperpositionHandler.hasSpellstoneCooldown(player))
		 return;
	 
	 Vector3 accelerationVec = new Vector3(player.getLookVec());
	 Vector3 motionVec = new Vector3(player.getMotion());
	 
	 if (player.isElytraFlying()) {
		 accelerationVec = accelerationVec.multiply(ConfigHandler.ANGEL_BLESSING_ACCELERATION_MODIFIER_ELYTRA.getValue());
			 accelerationVec = accelerationVec.multiply(1/(motionVec.mag()*2.25D));
	 } else
		 accelerationVec = accelerationVec.multiply(ConfigHandler.ANGEL_BLESSING_ACCELERATION_MODIFIER.getValue());
	 
	 
	 
	 Vector3 finalMotion = new Vector3(motionVec.x + accelerationVec.x, motionVec.y + accelerationVec.y, motionVec.z + accelerationVec.z);
	 
	 EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new PacketPlayerMotion(finalMotion.x, finalMotion.y, finalMotion.z));
	 player.setMotion(finalMotion.x, finalMotion.y, finalMotion.z);
	 
	 world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.PLAYERS, 1.0F, (float) (0.6F + (Math.random()*0.1D)));
	 
	 SuperpositionHandler.setSpellstoneCooldown(player, ConfigHandler.ANGEL_BLESSING_COOLDOWN.getValue());
 }
 
 @Override
 public void onCurioTick(String identifier, LivingEntity living) {
	 
	 List<DamagingProjectileEntity>projectileEntities = living.world.getEntitiesWithinAABB(DamagingProjectileEntity.class, new AxisAlignedBB(living.posX - range, living.posY - range, living.posZ - range, living.posX + range, living.posY + range, living.posZ + range));
	 List<AbstractArrowEntity>arrowEntities = living.world.getEntitiesWithinAABB(AbstractArrowEntity.class, new AxisAlignedBB(living.posX - range, living.posY - range, living.posZ - range, living.posX + range, living.posY + range, living.posZ + range));
	 List<PotionEntity>potionEntities = living.world.getEntitiesWithinAABB(PotionEntity.class, new AxisAlignedBB(living.posX - range, living.posY - range, living.posZ - range, living.posX + range, living.posY + range, living.posZ + range));
	 
	
	 
	 for (DamagingProjectileEntity entity : projectileEntities)
		 this.redirect(living, entity);
		 
	 for (AbstractArrowEntity entity : arrowEntities)
		 this.redirect(living, entity);
	 
	 for (PotionEntity entity : potionEntities)
		 this.redirect(living, entity);
	 
 }
 
 public void redirect(LivingEntity bearer, Entity redirected) {
	 if (redirected instanceof UltimateWitherSkullEntity || redirected instanceof WitherSkullEntity)
		 return;
	 
	 /*if (redirected instanceof TridentEntity)
	 	 if (((TridentEntity)redirected).getShooter() == bearer)
	 		 return;*/
	 
	 Vector3 entityPos = Vector3.fromEntityCenter(redirected);
	 Vector3 bearerPos = Vector3.fromEntityCenter(bearer);
	 
	 Vector3 redirection = entityPos.subtract(bearerPos);
	 redirection = redirection.normalize();
	 
	 if (redirected instanceof AbstractArrowEntity && ((AbstractArrowEntity)redirected).getShooter() == bearer) {
		 
		 if (redirected instanceof TridentEntity) {
			 TridentEntity trident = (TridentEntity) redirected;
			 
			 if (trident.returningTicks > 0)
				 return;
		 }
		 
		 redirected.setMotion(redirected.getMotion().x*1.75D, redirected.getMotion().y*1.75D, redirected.getMotion().z*1.75D);
	 } else
		 redirected.setMotion(redirection.x, redirection.y, redirection.z);
	 
	 if (redirected instanceof DamagingProjectileEntity) {
		 DamagingProjectileEntity redirectedProjectile = (DamagingProjectileEntity) redirected;
		 redirectedProjectile.accelerationX = (redirection.x/4.0);
		 redirectedProjectile.accelerationY = (redirection.y/4.0);
		 redirectedProjectile.accelerationZ = (redirection.z/4.0);
	 }
 }
 
  @Override
  public boolean canRightClickEquip() {
    return true;
  }
  
  @Override
  public void onEquipped(String identifier, LivingEntity entityLivingBase) {
	  // Insert existential void here
  }
  
  @Override
  public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
	  // Insert existential void here
  }
  
}

