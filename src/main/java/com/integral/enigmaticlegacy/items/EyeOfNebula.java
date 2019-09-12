package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Perhaps;
import com.integral.enigmaticlegacy.helpers.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.capability.ICurio;

public class EyeOfNebula extends Item implements ICurio, IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static List<String> immunityList = new ArrayList<String>();
 public static HashMap<String, Float> resistanceList = new HashMap<String, Float>();
 
 public static int abilityCooldown = 0;
 public static Perhaps magicDamageResistance = new Perhaps(0);
 public static Perhaps dodgeChance = new Perhaps(0);
 public static double dodgeRange = 0D;
 public static double phaseRange = 0D;

 public EyeOfNebula(Properties properties) {
		super(properties);
 }
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.EPIC);
	 
	 return integratedProperties;
 }
 
 public static void initConfigValues() {
	 abilityCooldown = EnigmaticLegacy.configHandler.EYE_OF_NEBULA_COOLDOWN.get();
	 magicDamageResistance = new Perhaps(EnigmaticLegacy.configHandler.EYE_OF_NEBULA_MAGIC_RESISTANCE.get());
	 dodgeChance = new Perhaps(EnigmaticLegacy.configHandler.EYE_OF_NEBULA_DODGE_PROBABILITY.get());
	 dodgeRange = EnigmaticLegacy.configHandler.EYE_OF_NEBULA_DODGE_RANGE.get();
	 phaseRange = EnigmaticLegacy.configHandler.EYE_OF_NEBULA_PHASE_RANGE.get();
	 
	 resistanceList.put(DamageSource.MAGIC.getDamageType(), magicDamageResistance.asModifierInverted());
	 resistanceList.put(DamageSource.DRAGON_BREATH.getDamageType(), magicDamageResistance.asModifierInverted());
 }
 
 @Override
 public boolean isForMortals() {
 	return EnigmaticLegacy.configLoaded ? EnigmaticLegacy.configHandler.EYE_OF_NEBULA_ENABLED.get() : false;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

	 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebulaCooldown", ((float)(abilityCooldown))/20.0F);
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula4", magicDamageResistance.asPercentage()+"%");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula5", dodgeChance.asPercentage()+"%");
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
 
 public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
	 if (SuperpositionHandler.hasSpellstoneCooldown(player))
		 return;
	 
	 LivingEntity target = SuperpositionHandler.getObservedEntity(player, world, 3.0F, (int) phaseRange);
	 
	 if (target != null) {
		 Vector3 targetPos = Vector3.fromEntityCenter(target);
		 Vector3 chaserPos = Vector3.fromEntityCenter(player);
		 //Vector3 targetSight = new Vector3(target.getLookVec());
		 
		 Vector3 dir = targetPos.subtract(chaserPos);
		 dir = dir.normalize();
		 dir = dir.multiply(1.5D);
		 
		 dir = targetPos.add(dir);
		 
		 //player.
		 world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2D)));
	    EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 128, player.dimension)), new PacketPortalParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 72, 1.0F));

		 
		 player.setPositionAndUpdate(dir.x, target.posY+0.25D, dir.z);
		 EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new PacketPlayerSetlook(target.posX, target.posY-1.0D+(target.getHeight()/2), target.posZ));
		 
		 world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2D)));
	     EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 128, player.dimension)), new PacketRecallParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 24));

		 SuperpositionHandler.setSpellstoneCooldown(player, abilityCooldown);
	 }
	 
 }
 
 @Override
 public boolean canRightClickEquip() {
     return true;
 }
 
 @Override
 public void onCurioTick(String identifier, LivingEntity living) {
	 // Insert existential void here
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

