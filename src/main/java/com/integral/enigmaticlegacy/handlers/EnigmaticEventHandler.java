package com.integral.enigmaticlegacy.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.helpers.CooldownMap;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper.AnvilParser;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.helpers.Vector3;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.EnigmaticItem;
import com.integral.enigmaticlegacy.items.EtheriumArmor;
import com.integral.enigmaticlegacy.items.EtheriumSword;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.LootGenerator;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.MonsterCharm;
import com.integral.enigmaticlegacy.items.OceanStone;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.integral.enigmaticlegacy.packets.server.PacketConfirmTeleportation;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

/**
 * Generic event handler of the whole mod.
 * @author Integral
 */

@Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID)
public class EnigmaticEventHandler {

	private static final String NBT_KEY_FIRSTJOIN = "enigmaticlegacy.firstjoin";
	private static final String NBT_KEY_ENABLESPELLSTONE = "enigmaticlegacy.spellstones_enabled";
	private static final String NBT_KEY_ENABLERING = "enigmaticlegacy.rings_enabled";
	private static final String NBT_KEY_ENABLESCROLL = "enigmaticlegacy.scrolls_enabled";
	
	public static CooldownMap deferredToast = new CooldownMap();
	public static List<IToast> scheduledToasts = new ArrayList<IToast>();
	public static Random theySeeMeRollin = new Random();
	public static HashMap<PlayerEntity, String> anvilFields = new HashMap<PlayerEntity, String>();
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onEntityTick(TickEvent.ClientTickEvent event) {
		try {
		PlayerEntity player = Minecraft.getInstance().player;
		
		/*
		 * Handler for displaying queued Toasts on client.
		 */
		
		deferredToast.tick(player);
		
		if (deferredToast.getCooldown(player) == 1) {
			Minecraft.getInstance().getToastGui().add(scheduledToasts.get(0));
			scheduledToasts.remove(0);
			
			if (scheduledToasts.size() > 0)
				deferredToast.put(player, 5);
		}
		
		} catch (Exception ex) {
			//DO NOTHING
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLooting(LootingLevelEvent event) {
		
		/*
		 * Handler for adding additional Looting level,
		 * if player is bearing Emblem of Monster Slayer.
		 */
		
		if (event.getDamageSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getDamageSource().getTrueSource();
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm))
				if (ConfigHandler.MONSTER_CHARM_BONUS_LOOTING.getValue())
					event.setLootingLevel(event.getLootingLevel() + 1);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onExperienceDrop(LivingExperienceDropEvent event) {
		PlayerEntity player = event.getAttackingPlayer();
		
		if (event.getEntityLiving() instanceof MonsterEntity)
		if (player != null && SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm))
			event.setDroppedExperience((int) (event.getDroppedExperience()*MonsterCharm.bonusXPModifier));
	}
	
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void miningStuff(PlayerEvent.BreakSpeed event) {
		
		/*
		 * Handler for calculating mining speed boost
		 * from wearing Charm of Treasure Hunter.
		 */
		
		float miningBoost = 1.0F;
		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.miningCharm))
			miningBoost += ConfigHandler.MINING_CHARM_BREAK_BOOST.getValue().asModifier(false);
		
		if (!event.getPlayer().onGround)
		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.heavenScroll) || SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.enigmaticItem)) {			
			event.setNewSpeed(event.getNewSpeed()*5F);
		}
		
		event.setNewSpeed(event.getNewSpeed()*miningBoost);
	}
	
	@SubscribeEvent
	public void onBlockDropsHarvest(HarvestDropsEvent event) {
		
		// This never happens
		System.out.println("Event fired!");
	}
	
	@SubscribeEvent
	public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
		
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				
			
			/*
			 * Handler for faster effect ticking,
			 * if player is bearing Blazing Core.
			 */
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart))
				if (!player.getActivePotionEffects().isEmpty()) {
					Collection<EffectInstance> effects = player.getActivePotionEffects();
					
					for (EffectInstance effect : effects)
						effect.tick(player);
					
				}
			
			
			/*
			 * Handler for removing debuffs from players
			 * protected by Etherium Armor Shield.
			 */
			
			if (EtheriumArmor.hasShield(player))
				if (!player.getActivePotionEffects().isEmpty()) {
					List<EffectInstance> effects = new ArrayList<EffectInstance>(player.getActivePotionEffects());
					
					for (EffectInstance effect : effects) {
						if (!effect.getPotion().isBeneficial())
							player.removePotionEffect(effect.getPotion());
					}
				}
			
			
			if (event.getEntityLiving().world.isRemote)		
				return;
			
			/*
			 * Handler for players' spellstone cooldowns.
			 */
			
			if (SuperpositionHandler.hasSpellstoneCooldown(player))
				SuperpositionHandler.setSpellstoneCooldown(player, SuperpositionHandler.getSpellstoneCooldown(player)-1);
			
			EtheriumSword.etheriumSwordCooldowns.tick(player);
			EnigmaticItem.handleEnigmaticFlight(player);
			
		}
		
	}
	
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
		
		ItemStack stack = evt.getObject();
		
		/*
		 * Handler for registering item's capabilities implemented in ICurio interface, for Enigmatic Legacy's namespace specifically.
		 */
		
		if (stack.getItem() instanceof ICurio && stack.getItem().getRegistryName().getNamespace().equals(EnigmaticLegacy.MODID)) {
			ICurio curioCapabilities = (ICurio) stack.getItem();
			
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> curioCapabilities);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		}
	  
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	 public void onConfirmedDeath(LivingDeathEvent event) {

		 if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				
				/*
				 * Handler for Scroll of Postmortal Recall.				
				 */
				
				if(SuperpositionHandler.hasCurio(player, EnigmaticLegacy.escapeScroll) & !player.world.isRemote) {
					ItemStack tomeStack = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.escapeScroll);
					PermanentItemEntity droppedTomeStack = new PermanentItemEntity(player.world, player.posX, player.posY+(player.getHeight()/2), player.posZ, tomeStack.copy());
					droppedTomeStack.setPickupDelay(10);
					player.world.addEntity(droppedTomeStack);
					
					tomeStack.shrink(1);
					
					Vec3d vec = SuperpositionHandler.getValidSpawn(player.world, player);
					
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random()*0.2)));
			    	EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 1024, player.dimension)), new PacketPortalParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 72, 1.0F));
					
					player.setPositionAndUpdate(vec.x, vec.y, vec.z);
					
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random()*0.2)));
			    	EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 1024, player.dimension)), new PacketRecallParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 48));
				}
		 }
		 
	 }
	
	
	 @SubscribeEvent(priority = EventPriority.HIGHEST)
	 public void onLivingDeath(LivingDeathEvent event) {

		 if(event.getEntityLiving() instanceof PlayerEntity & !event.getEntityLiving().world.isRemote) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				
				/*
				 * Immortality handler for Heart of Creation and Pearl of the Void.
				 */
				
				if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem) || player.inventory.hasItemStack(new ItemStack(EnigmaticLegacy.enigmaticItem))) {
					event.setCanceled(true);
					player.setHealth(1);
				} else if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl) && Math.random() <= ConfigHandler.VOID_PEARL_UNDEAD_PROBABILITY.getValue().asMultiplier(false)) {
					event.setCanceled(true);
					player.setHealth(1);
				}
				
				/*
				 * Immortality handler for Etherium Armor.
				 */
				/*
				if (!event.isCanceled() & EtheriumArmor.hasFullSet(player)) {
					for (ItemStack stack : player.getArmorInventoryList()) {
						int damage = 50 + theySeeMeRollin.nextInt(50);
						stack.damageItem(damage, player, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack)));
					}
					
					event.setCanceled(true);
					player.setHealth(1);
				}
				*/
		 }
		 
	 }
	 
	 @SubscribeEvent
	 public void onLivingHeal(LivingHealEvent event) {
		 
		 if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();
				
				/*
				 * Regeneration slowdown handler for Pearl of the Void.
				 */

				if(SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
					if (event.getAmount() <= 1.0F) {
						event.setAmount((float) (event.getAmount()/(1.5F*ConfigHandler.VOID_PEARL_REGENERATION_MODIFIER.getValue())));
					}
				}
		 }
		 
	 }
	 
	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event) {
		
		if (event.getEntityLiving().world.isRemote)
			return;
		
		/*
		 * Handler for spellstones' immunities.
		 */
		
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			
			if (EtheriumArmor.hasShield(player)) {
				if (event.getSource().getImmediateSource() instanceof DamagingProjectileEntity || event.getSource().getImmediateSource() instanceof AbstractArrowEntity) {
					event.setCanceled(true);
					player.world.playSound(null, player.getPosition(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float)(Math.random() * 0.1D));player.world.playSound(null, player.getPosition(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float)(Math.random() * 0.1D));
				}
			}
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem))
				if (EnigmaticItem.immunityList.contains(event.getSource().damageType))
					event.setCanceled(true);
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.angelBlessing))
				if (AngelBlessing.immunityList.contains(event.getSource().damageType))
					event.setCanceled(true);
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.oceanStone))
				if (OceanStone.immunityList.contains(event.getSource().damageType))
					event.setCanceled(true);
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart))
				if (MagmaHeart.immunityList.contains(event.getSource().damageType))
					event.setCanceled(true);
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.golemHeart))
				if (GolemHeart.immunityList.contains(event.getSource().damageType))
					event.setCanceled(true);
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				if (VoidPearl.immunityList.contains(event.getSource().damageType))
					event.setCanceled(true);
				else if (VoidPearl.healList.contains(event.getSource().damageType)) {
					player.heal(event.getAmount());
					event.setCanceled(true);
				} else {}
			
			}
			
			/*
			 * Handler for Eye of the Nebula dodge effect.
			 */
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.eyeOfNebula)) {
				if (EyeOfNebula.immunityList.contains(event.getSource().damageType)) {
					event.setCanceled(true);
			    } else if (Math.random() <= ConfigHandler.EYE_OF_NEBULA_DODGE_PROBABILITY.getValue().asMultiplier(false) && player.hurtResistantTime <= 10 && event.getSource().getTrueSource() instanceof LivingEntity) {
			    	EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketConfirmTeleportation(true));
			    	player.hurtResistantTime = 20;
					event.setCanceled(true);
				}
			}
		} else if (event.getSource().getImmediateSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();
			
			/*
			 * Handler for triggering Extradimensional Eye's effects when player left-clicks
			 * (or, more technically correct, directly attacks) the entity with the Eye in main hand.
			 */
			
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == EnigmaticLegacy.extradimensionalEye)
				if (ItemNBTHelper.verifyExistance(player.getHeldItemMainhand(), "BoundDimension"))
					if (ItemNBTHelper.getInt(player.getHeldItemMainhand(), "BoundDimension", 0) == event.getEntityLiving().dimension.getId()) {
					  event.setCanceled(true);
					  ItemStack stack = player.getHeldItemMainhand();
					  
					  EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, 128, event.getEntityLiving().dimension)), new PacketPortalParticles(event.getEntityLiving().posX, event.getEntityLiving().posY+(event.getEntityLiving().getHeight()/2), event.getEntityLiving().posZ, 96, 1.5D));
					  
					  event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random()*0.2)));
					  event.getEntityLiving().setPositionAndUpdate(ItemNBTHelper.getDouble(stack, "BoundX", 0D), ItemNBTHelper.getDouble(stack, "BoundY", 0D), ItemNBTHelper.getDouble(stack, "BoundZ", 0D));
					  event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random()*0.2)));
					  
					  EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, 128, event.getEntityLiving().dimension)), new PacketRecallParticles(event.getEntityLiving().posX, event.getEntityLiving().posY+(event.getEntityLiving().getHeight()/2), event.getEntityLiving().posZ, 48));
					  
					  if (!player.abilities.isCreativeMode)
					  stack.shrink(1);
				}
		}
		
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		
		if (event.getEntityLiving() instanceof PlayerEntity & !event.getEntityLiving().world.isRemote) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			
			/*
			 * Handler for spellstone's resistance lists.
			 */
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.angelBlessing))
				if (AngelBlessing.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * AngelBlessing.resistanceList.get(event.getSource().damageType));
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.oceanStone)) {
				Entity attacker = event.getSource().getTrueSource();
				
				if (attacker instanceof DrownedEntity || attacker instanceof GuardianEntity || attacker instanceof ElderGuardianEntity)
					event.setAmount(event.getAmount()*ConfigHandler.OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE.getValue().asModifierInverted());
			}
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.eyeOfNebula))
				if (EyeOfNebula.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * EyeOfNebula.resistanceList.get(event.getSource().damageType).get());
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.golemHeart))
				if (GolemHeart.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * GolemHeart.resistanceList.get(event.getSource().damageType).get());
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl))
				if (VoidPearl.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * VoidPearl.resistanceList.get(event.getSource().damageType).get());
			
			/*
			 * Handler for damaging feedback of Blazing Core.
			 */
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart)) {
				//System.out.println("Damage type: " + event.getSource().damageType);
				if (event.getSource().getTrueSource() instanceof LivingEntity && MagmaHeart.nemesisList.contains(event.getSource().damageType)) {
					LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
					if (!attacker.isImmuneToFire()) {
						attacker.attackEntityFrom(new EntityDamageSource(DamageSource.ON_FIRE.damageType, player), (float) ConfigHandler.BLAZING_CORE_DAMAGE_FEEDBACK.getValue());
						attacker.setFire((int) ConfigHandler.BLAZING_CORE_IGNITION_FEEDBACK.getValue());
					}
				}
				
			}
			
			/*
			 * Handler for knockback feedback and damage reduction of Etherium Armor Shield.
			 */
			
			if (EtheriumArmor.hasShield(player)) {
				if (event.getSource().getImmediateSource() instanceof LivingEntity) {
					LivingEntity attacker = ((LivingEntity)event.getSource().getTrueSource());
					Vector3 vec = Vector3.fromEntityCenter(player).subtract(Vector3.fromEntityCenter(event.getSource().getTrueSource())).normalize();
					attacker.knockBack(player, 0.75F, vec.x, vec.y);
					player.world.playSound(null, player.getPosition(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float)(Math.random() * 0.1D));player.world.playSound(null, player.getPosition(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float)(Math.random() * 0.1D));
				}
				
				event.setAmount(event.getAmount()*ConfigHandler.ETHERIUM_ARMOR_SHIELD_REDUCTION.getValue().asModifierInverted());
			}
			
		} else if (event.getEntityLiving() instanceof MonsterEntity) {
			MonsterEntity monster = (MonsterEntity) event.getEntityLiving();
			
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				
				/*
				 * Handler for damage bonuses of Charm of Monster Slayer.
				 */
				
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm)) {
				if (monster.isEntityUndead()) {
					event.setAmount(event.getAmount()*ConfigHandler.MONSTER_CHARM_UNDEAD_DAMAGE.getValue().asModifier(true));	
				} else if (monster.isAggressive() ||  monster instanceof CreeperEntity) {
					
					if (monster instanceof EndermanEntity || monster instanceof ZombiePigmanEntity || monster instanceof BlazeEntity || monster instanceof GuardianEntity || monster instanceof ElderGuardianEntity || !monster.isNonBoss()) {
					} else {
						event.setAmount(event.getAmount()*ConfigHandler.MONSTER_CHARM_AGGRESSIVE_DAMAGE.getValue().asModifier(true));	
					}
					
				}
			}	
			
			}
		}
		
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			
			/*
			 * Handler for applying Withering to victims of bearer of the Void Pearl.
			 */
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.WITHER, ConfigHandler.VOID_PEARL_WITHERING_EFFECT_TIME.getValue(), ConfigHandler.VOID_PEARL_WITHERING_EFFECT_LEVEL.getValue(), false, true));
			}
		}
		
	}
	
	@SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
		
		/*
		 * Beheading handler for Axe of Executioner.
		 */
		
		if (event.getEntityLiving().getClass() == SkeletonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
            ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !this.containsDrop(event.getDrops(), Items.SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel())) {
                this.addDrop(event, new ItemStack(Items.SKELETON_SKULL, 1));
                
                if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
                	BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
            }
        }
		else if (event.getEntityLiving().getClass() == WitherSkeletonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
            ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe) {
            	
            	if (!this.containsDrop(event.getDrops(), Items.WITHER_SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel()))
            		this.addDrop(event, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));

                if (event.getSource().getTrueSource() instanceof ServerPlayerEntity && this.containsDrop(event.getDrops(), Items.WITHER_SKELETON_SKULL))
                    BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
            }
        }
		else if (event.getEntityLiving().getClass() == ZombieEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
            ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !this.containsDrop(event.getDrops(), Items.ZOMBIE_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
                this.addDrop(event, new ItemStack(Items.ZOMBIE_HEAD, 1));
                
                if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
                    BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
            }
        }
		else if (event.getEntityLiving().getClass() == CreeperEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
            ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !this.containsDrop(event.getDrops(), Items.CREEPER_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
                this.addDrop(event, new ItemStack(Items.CREEPER_HEAD, 1));
                
                if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
                    BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
            }
        }
		else if (event.getEntityLiving().getClass() == EnderDragonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
            ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
            if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !this.containsDrop(event.getDrops(), Items.DRAGON_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
                this.addDrop(event, new ItemStack(Items.DRAGON_HEAD, 1));
                
                if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
                    BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
            }
        }
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		
		List<ResourceLocation> underwaterRuins = new ArrayList<ResourceLocation>();
		underwaterRuins.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		underwaterRuins.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		
		/*
		 * Handlers for adding spellstones to dungeon loot.
		 */
		
		if (SuperpositionHandler.getMergedAir$EarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -8F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 35), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.angelBlessing, 65));
			
			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
		} else if (SuperpositionHandler.getMergedEnder$EarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -4F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.eyeOfNebula, 35), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 65));
			
			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
			
		} else if (SuperpositionHandler.getAirDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -4F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.angelBlessing, 100));
			
			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
			
		} else if (SuperpositionHandler.getEarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -8F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 100));
			
			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
			
		} else if (SuperpositionHandler.getFieryDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -12F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.magmaHeart, 100));
			
			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
			
		} else if (SuperpositionHandler.getWaterDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -7F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.oceanStone, 100));
			
			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
			
		} else if (SuperpositionHandler.getEnderDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -7F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.eyeOfNebula, 90), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.voidPearl, 10));
			
			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
			
		}
		
		/*
		 * Handlers for adding epic dungeon loot to most dungeons.
		 */
		
		if (SuperpositionHandler.getOverworldDungeons().contains(event.getName())) {
			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_PICKAXE, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_AXE, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_SWORD, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_SHOVEL, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.BOW, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.ironRing, 20),
				ItemLootEntry.builder(EnigmaticLegacy.commonPotionBase).weight(20).acceptFunction(SetNBT.func_215952_a(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.HASTE).getTag())),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.magnetRing, 8),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.unholyGrail, 4),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.loreInscriber, 5),
				// TODO Maybe reconsider
				// SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.oblivionStone, 4),
				ItemLootEntry.builder(Items.CLOCK).weight(10),
				ItemLootEntry.builder(Items.COMPASS).weight(10),
				ItemLootEntry.builder(Items.EMERALD).weight(20).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(1.0F, 4F))),
				ItemLootEntry.builder(Items.SLIME_BALL).weight(20).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(2.0F, 10F))),
				ItemLootEntry.builder(Items.LEATHER).weight(35).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(3.0F, 8F))),
				ItemLootEntry.builder(Items.PUMPKIN_PIE).weight(25).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(4.0F, 16F)))
				);
			
			LootTable modified = event.getTable();
			modified.addPool(epic);
			event.setTable(modified);
			
		} else if (event.getName().equals(LootTables.CHESTS_NETHER_BRIDGE)) {
			ItemStack fireResistancePotion = new ItemStack(Items.POTION);
			fireResistancePotion = PotionUtils.addPotionToItemStack(fireResistancePotion, Potions.LONG_FIRE_RESISTANCE);

			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_PICKAXE, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_AXE, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_SWORD, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_SHOVEL, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.oblivionStone, 8),
				ItemLootEntry.builder(Items.EMERALD).weight(30).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(2.0F, 7F))),
				ItemLootEntry.builder(Items.WITHER_ROSE).weight(25).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(1.0F, 4F))),
				ItemLootEntry.builder(Items.GHAST_TEAR).weight(10).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(1.0F, 2F))),
				ItemLootEntry.builder(Items.LAVA_BUCKET).weight(30),
				ItemLootEntry.builder(Items.POTION).weight(15).acceptFunction(SetNBT.func_215952_a(fireResistancePotion.getTag()))
				);
			
			LootTable modified = event.getTable();
			modified.addPool(epic);
			event.setTable(modified);
		} else if (event.getName().equals(LootTables.CHESTS_END_CITY_TREASURE)) {
				LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
				ItemLootEntry.builder(Items.ENDER_PEARL).weight(40).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(2.0F, 5F))),
				ItemLootEntry.builder(Items.ENDER_EYE).weight(20).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(1.0F, 2F))),
				ItemLootEntry.builder(Items.GLISTERING_MELON_SLICE).weight(30).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(1.0F, 4F))),
				ItemLootEntry.builder(Items.GOLDEN_CARROT).weight(30).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(1.0F, 4F))),
				ItemLootEntry.builder(Items.PHANTOM_MEMBRANE).weight(25).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(3.0F, 7F))),
				ItemLootEntry.builder(Items.ENCHANTING_TABLE).weight(10),
				ItemLootEntry.builder(Items.CAKE).weight(15),
				ItemLootEntry.builder(Items.END_CRYSTAL).weight(7),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.loreInscriber, 10),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.recallPotion, 15),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.mendingMixture, 40),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.astralDust, 85, 1F, 4F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.etheriumOre, 60, 1F, 2F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.extradimensionalEye, 20)
				);
			
			LootTable modified = event.getTable();
			modified.addPool(epic);
			event.setTable(modified);
		}

		/*
		 * Handlers for adding special loot to some dungeons.
		 */
		
		if (event.getName().equals(LootTables.CHESTS_STRONGHOLD_LIBRARY)) {
			LootPool special = SuperpositionHandler.constructLootPool("el_special", 2F, 2F,
					ItemLootEntry.builder(EnigmaticLegacy.thiccScroll).weight(20).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(2F, 6F))),
					ItemLootEntry.builder(EnigmaticLegacy.loreFragment).weight(10).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(1F, 2F))));
			
			LootTable modified = event.getTable();
			modified.addPool(special);
			event.setTable(modified);
		}
		
		
		 if (event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_BIG) || event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_SMALL)) {
				LootPool special = SuperpositionHandler.constructLootPool("el_special", -5F, 1F,
						ItemLootEntry.builder(Items.TRIDENT).acceptFunction(SetDamage.func_215931_a(RandomValueRange.func_215837_a(0.5F, 1.0F))).acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.func_215837_a(15F, 40F)).func_216059_e())
						);
				
				LootTable modified = event.getTable();
				modified.addPool(special);
				event.setTable(modified);
		}
		
	}

	    @SubscribeEvent
	    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
	    	if (!(event.getPlayer() instanceof ServerPlayerEntity))
	    		return;
	    	
	    	try {
	    	
	    	ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
	    	
	    	/*
	    	 * Handler for bestowing Enigmatic Amulet to the player,
	    	 * when they first join the world.
	    	 */
	    	
	    	
	        if (!SuperpositionHandler.hasPersistentTag(player, NBT_KEY_FIRSTJOIN)) {
	        	
	            ItemStack stack = new ItemStack(EnigmaticLegacy.enigmaticAmulet);
	            ItemNBTHelper.setString(stack, "Inscription", player.getDisplayName().getString());
	            
	            if (player.inventory.getStackInSlot(8).isEmpty()) {
	            	player.inventory.setInventorySlotContents(8, stack);
	            } else {
	            	if (!player.inventory.addItemStackToInventory(stack)) {
	            		ItemEntity dropIt = new ItemEntity(player.world, player.posX, player.posY, player.posZ, stack);
	            		player.world.addEntity(dropIt);
	            	}
	            }
	            
	            SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_FIRSTJOIN, true);
	        }
	        
	        /*
	         * Handlers for fixing missing Curios slots upong joining the world.
	         */
	        
	        if (!SuperpositionHandler.hasPersistentTag(player, NBT_KEY_ENABLESPELLSTONE))
	        if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_spellstone"))) {
	        	CuriosAPI.enableTypeForEntity("spellstone", event.getPlayer());
	        	SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_ENABLESPELLSTONE, true);
	        }
	        
	        if (!SuperpositionHandler.hasPersistentTag(player, NBT_KEY_ENABLESCROLL))
	        if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_spellstone"))) {
	        	CuriosAPI.enableTypeForEntity("scroll", event.getPlayer());
	        	SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_ENABLESCROLL, true);
	        }
	        
	        if (!SuperpositionHandler.hasPersistentTag(player, NBT_KEY_ENABLERING))
	        if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_spellstone"))) {
	        	CuriosAPI.enableTypeForEntity("ring", event.getPlayer());
	        	SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_ENABLERING, true);
	        }
	        
	    	} catch(Exception ex) {
	    		EnigmaticLegacy.enigmaticLogger.error("Failed to check player's advancements upon joining the world!");
	    		ex.printStackTrace();
	    	}
	    
	    }
	    
	    
	    
	@SubscribeEvent
	public void onAdvancement(AdvancementEvent event) {
		
		String id = event.getAdvancement().getId().toString();
		PlayerEntity player = event.getPlayer();
		
		/*
		 * Handler for permanently unlocking Curio slots to player
		 * once they obtain respective advancement.
		 */

		if (id.equals(EnigmaticLegacy.MODID + ":main/discover_spellstone")) {	
			CuriosAPI.enableTypeForEntity("spellstone", player);
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketSlotUnlocked("spellstone"));
			SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_ENABLESPELLSTONE, true);
		} else if (id.equals(EnigmaticLegacy.MODID + ":main/discover_scroll")) {
			CuriosAPI.enableTypeForEntity("scroll", player);
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketSlotUnlocked("scroll"));
			SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_ENABLESCROLL, true);
		} else if (id.equals(EnigmaticLegacy.MODID + ":main/discover_ring")) {
			CuriosAPI.enableTypeForEntity("ring", player);
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketSlotUnlocked("ring"));
			SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_ENABLERING, true);
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerEntity player = event.getPlayer();
		
		/*
		 * Handler for re-enabling disabled Curio slots that are supposed
		 * to be permanently unlocked, when the player respawns.
		 */
		
		if (!player.world.isRemote)
		if (!event.isEndConquered()) {
			
			if (!player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
			
			if (SuperpositionHandler.hasPersistentTag(player, NBT_KEY_ENABLERING))
				CuriosAPI.enableTypeForEntity("ring", event.getPlayer());
			if (SuperpositionHandler.hasPersistentTag(player, NBT_KEY_ENABLESCROLL))
				CuriosAPI.enableTypeForEntity("scroll", event.getPlayer());
			if (SuperpositionHandler.hasPersistentTag(player, NBT_KEY_ENABLESPELLSTONE))
				CuriosAPI.enableTypeForEntity("spellstone", event.getPlayer());
			
			}
		}
		
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onAnvilOpen(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof AnvilScreen) {
			
			AnvilScreen screen = (AnvilScreen) event.getGui();
			
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketAnvilField(""));
			
			try {
				for (Field f : screen.getClass().getDeclaredFields()) {
					
					f.setAccessible(true);
					
					if (f.get(screen) instanceof TextFieldWidget) {
						TextFieldWidget widget = (TextFieldWidget) f.get(screen);
						widget.setMaxStringLength(64);
					}
					
				}
	            
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		}
	}
	
	@SubscribeEvent
	public void onAnvilRepair(AnvilRepairEvent event) {
		if (!SuperpositionHandler.hasStoredAnvilField(event.getPlayer()) || event.getPlayer().world.isRemote || event.getItemInput().getItem() != EnigmaticLegacy.loreFragment || event.getIngredientInput().getItem() != EnigmaticLegacy.loreInscriber)
			return;
			
		AnvilParser parser = AnvilParser.parseField(anvilFields.get(event.getPlayer()));
		
		if (!parser.getFormattedString().equals("") && !parser.shouldRemoveString()) {
			if (parser.isLoreString()) {
				if (parser.getLoreIndex() != -1)
					ItemLoreHelper.setLoreString(event.getItemResult(), parser.getFormattedString(), parser.getLoreIndex());
				else
					ItemLoreHelper.setLastLoreString(event.getItemResult(), parser.getFormattedString());
			} else {
				ItemLoreHelper.setDisplayName(event.getItemResult(), parser.getFormattedString());
			}
		}
		
		event.setBreakChance(0.01F);
		
		event.getPlayer().addItemStackToInventory(event.getIngredientInput().copy());
	}
	
	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {
		
		if (event.getLeft().getCount() == 1)
		if (event.getLeft().getItem().equals(EnigmaticLegacy.loreFragment) && event.getRight().getItem().equals(EnigmaticLegacy.loreInscriber) && event.getName() != null) {
			ItemStack returned = event.getLeft().copy();
			
			DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
				
				if (!event.getName().equals(returned.getDisplayName().getFormattedText()))
				EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketAnvilField(event.getName()));
				
			});
			
			AnvilParser parser = AnvilParser.parseField(event.getName());
			
			if (!parser.getFormattedString().equals("") || parser.shouldRemoveString()) {
				
				if (parser.isLoreString()) {
					if (parser.getLoreIndex() != -1)
						ItemLoreHelper.setLoreString(returned, parser.getFormattedString(), parser.getLoreIndex());
					else
						ItemLoreHelper.addLoreString(returned, parser.getFormattedString());
				} else if (parser.shouldRemoveString()) {
					ItemLoreHelper.removeLoreString(returned, parser.getLoreIndex());
				} else {
					ItemLoreHelper.setDisplayName(returned, parser.getFormattedString());
				}
				
				event.setCost(1);
				event.setMaterialCost(1);
				event.setOutput(returned);
			}
			
		} else if (event.getRight().getItem().equals(EnigmaticLegacy.loreFragment) && event.getRight().getChildTag("display") != null) {
			event.setCost(4);
			event.setMaterialCost(1);
			event.setOutput(ItemLoreHelper.mergeDisplayData(event.getRight(), event.getLeft().copy()));
		}
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onRenderTick(RenderGameOverlayEvent.Post event) {
		
			/*
			 * Five-minute job that took me almost TEN FREAKIN' HOURS TO GET IT TO WORK!
			 */
			
			if (event.getType() != RenderGameOverlayEvent.ElementType.ALL || !ConfigHandler.CLOCK_HUD_ENABLED.getValue())
				return;
			
			if (ConfigHandler.CLOCK_HUD_HIDE_IN_CHAT.getValue())
				if (Minecraft.getInstance().currentScreen instanceof ChatScreen)
					return;
			
			if (ConfigHandler.CLOCK_HUD_ONLY_IN_FULLSCREEN.getValue())
				if (!Minecraft.getInstance().mainWindow.isFullscreen())
					return;
			
		    String text = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + (Calendar.getInstance().get(Calendar.MINUTE) <= 9 ? ("0"+Calendar.getInstance().get(Calendar.MINUTE)) : (""+Calendar.getInstance().get(Calendar.MINUTE)));
		    
		    Minecraft minecraft = Minecraft.getInstance();
	        FontRenderer textRenderer = minecraft.fontRenderer;
	        
	        int guiPosX = ConfigHandler.CLOCK_HUD_X.getValue();
	        int guiPosY = minecraft.mainWindow.getScaledHeight() - ConfigHandler.CLOCK_HUD_Y.getValue();
	        float scale = (float) ConfigHandler.CLOCK_HUD_SCALE.getValue();
	        
	        //1736173
	        
	         if (ConfigHandler.CLOCK_HUD_BACKGROUND_ENABLED.getValue()) {
	         
	         GlStateManager.pushMatrix();

             GlStateManager.disableLighting();
	         GlStateManager.enableAlphaTest();
	         
	         GlStateManager.alphaFunc(516, 0.1F);
	         GlStateManager.enableBlend();
	         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	         
	         GlStateManager.pushMatrix();
	        
	         GlStateManager.scalef(scale, scale, scale);
	        
	         minecraft.getTextureManager().bindTexture(new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/clock_hud_rect.png"));
             GlStateManager.color3f(1.0F, 1.0F, 1.0F);
             GuiUtils.drawTexturedModalRect(guiPosX-29, guiPosY-10, 0, 0, 66, 28, 0F); 
            
            
             GlStateManager.popMatrix();
	        
             GlStateManager.disableAlphaTest();
             RenderHelper.enableGUIStandardItemLighting();
	         
	         GlStateManager.popMatrix();
	         
	         }
	         
	         GlStateManager.pushMatrix();
	         
	         GlStateManager.scalef(scale, scale, scale);
	         
	         minecraft.getItemRenderer().renderItemAndEffectIntoGUI(EnigmaticLegacy.universalClock, guiPosX-20, guiPosY-4);
	         
	         textRenderer.drawStringWithShadow(text, guiPosX, guiPosY, TextFormatting.GOLD.getColor());
	         
	         GlStateManager.popMatrix();
	        
	}
	
	/**
	 * Adds passed ItemStack to LivingDropsEvent.
	 * @author Integral
	 */
	
	public void addDrop(LivingDropsEvent event, ItemStack drop) {
        ItemEntity entityitem = new ItemEntity(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
        entityitem.setPickupDelay(10);
        event.getDrops().add(entityitem);
    }
	
	/**
	 * Checks whether the collection of ItemEntities contains given Item.
	 * @author Integral
	 */
	
	public boolean containsDrop(Collection <ItemEntity> drops, Item item) {
		
		for (ItemEntity drop : drops) {
			if (drop.getItem() != null)
				if (drop.getItem().getItem() == item)
					return true;
		}
		
		return false;
	}
	
	/**
	 * Calculates the chance for Axe of Executioner to behead an enemy.
	 * @param lootingLevel Amount of looting levels applied to axe or effective otherwise.
	 * @return True if chance works and head should drop, false otherwise.
	 * @author Integral
	 */
	
	public boolean theySeeMeRollin(int lootingLevel) {
		double chance = ConfigHandler.FORBIDDEN_AXE_BEHEADING_BASE.getValue().asMultiplier(false) + (ConfigHandler.FORBIDDEN_AXE_BEHEADING_BONUS.getValue().asMultiplier(false)*lootingLevel);
		
		if (Math.random() <= chance)
			return true;
		else
			return false;
	}
	
}
