package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.CooldownMap;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;

import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
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

public class EtheriumSword extends SwordItem implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 public static CooldownMap etheriumSwordCooldowns = new CooldownMap();
 
 public EtheriumSword(Properties properties, IItemTier tier, float attackSpeedIn, int attackDamageIn) {
		super(tier, attackDamageIn, attackSpeedIn, properties);
	}
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 return integratedProperties;
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword1");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword2");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword3");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword4", (float)(ConfigHandler.ETHERIUM_SWORD_COOLDOWN.getValue()/20F));
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }

 @Override
 public boolean isForMortals() {
	 return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
 }
 
 @Override
 public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    if (hand == Hand.OFF_HAND)
    	return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
    
	if (!etheriumSwordCooldowns.hasCooldown(player)) {
		if (!player.world.isRemote) {
			Vector3 look = new Vector3(player.getLookVec());
			Vector3 dir = look.multiply(1D);
			
			this.knockBack(player, 1.0F, dir.x, dir.z);
			world.playSound(null, player.getPosition(), SoundEvents.ENTITY_SKELETON_SHOOT, SoundCategory.NEUTRAL, 1.0F, (float) (0.6F + (Math.random()*0.1D)));
			
			etheriumSwordCooldowns.put(player, ConfigHandler.ETHERIUM_SWORD_COOLDOWN.getValue());
		}
	}
	 
	player.setActiveHand(hand);
    return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
 }
 
 
 public void knockBack(PlayerEntity entityIn, float strength, double xRatio, double zRatio) {
	 	entityIn.isAirBorne = true;
        Vec3d vec3d = new Vec3d(0D, 0D, 0D);
        Vec3d vec3d1 = (new Vec3d(xRatio, 0.0D, zRatio)).normalize().scale((double)strength);
        
        EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)entityIn), new PacketPlayerMotion(vec3d.x / 2.0D - vec3d1.x, entityIn.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double)strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z));
        entityIn.setMotion(vec3d.x / 2.0D - vec3d1.x, entityIn.onGround ? Math.min(0.4D, vec3d.y / 2.0D + (double)strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);
 }
  
}



