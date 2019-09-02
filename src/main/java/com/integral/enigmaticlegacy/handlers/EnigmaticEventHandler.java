package com.integral.enigmaticlegacy.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.CooldownMap;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.ForbiddenAxe;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.MiningCharm;
import com.integral.enigmaticlegacy.items.MonsterCharm;
import com.integral.enigmaticlegacy.items.OceanStone;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.packets.PacketConfirmTeleportation;
import com.integral.enigmaticlegacy.packets.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
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
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosAPI;

@Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID)
public class EnigmaticEventHandler {
	
	Random randy = new Random();
	public boolean configsLoaded = false;
	private static final String NBT_KEY_FIRSTJOIN = "enigmaticlegacy.firstjoin";
	private static final String NBT_KEY_ENABLESPELLSTONE = "enigmaticlegacy.spellstones_enabled";
	private static final String NBT_KEY_ENABLERING = "enigmaticlegacy.rings_enabled";
	private static final String NBT_KEY_ENABLESCROLL = "enigmaticlegacy.scrolls_enabled";
	@OnlyIn(Dist.CLIENT)
	public static CooldownMap deferredToast = new CooldownMap();
	@OnlyIn(Dist.CLIENT)
	public static List<IToast> scheduledToasts = new ArrayList<IToast>();
	
	@SubscribeEvent
	public void onEntityTick(TickEvent.ClientTickEvent event) {
		try {
		PlayerEntity player = Minecraft.getInstance().player;
		
		deferredToast.tick(player);
		
		if (deferredToast.getCooldown(player) == 1) {
			Minecraft.getInstance().getToastGui().add(scheduledToasts.get(0));
			scheduledToasts.remove(0);
			
			if (scheduledToasts.size() > 0)
				deferredToast.put(player, 5);
		}
			
		
		if (Minecraft.getInstance().isGamePaused())
			return;
		
		int range = 24;
		List<Entity> list = Minecraft.getInstance().player.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(player.posX - range, player.posY - range, player.posZ - range, player.posX + range, player.posY + range, player.posZ + range));
		for (Entity entity : list) {
			if (entity instanceof ItemEntity) {
				ItemEntity item = (ItemEntity) entity;
				if (item.getItem() != null)
					if (item.hasNoGravity())
						 if (item.getItem().getItem() == EnigmaticLegacy.escapeScroll)
							item.world.addParticle(ParticleTypes.PORTAL, item.posX, item.posY+(item.getHeight()/2), item.posZ, ((Math.random()-0.5)*2.0), ((Math.random()-0.5)*2.0), ((Math.random()-0.5)*2.0));
						
			}
		}
		} catch (Exception ex) {
			//DO NOTHING
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLooting(LootingLevelEvent event) {
		if (event.getDamageSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getDamageSource().getTrueSource();
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm))
				if (MonsterCharm.lootingLevelEnabled)
					event.setLootingLevel(event.getLootingLevel() + 1);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onExperienceDrop(LivingExperienceDropEvent event) {
		PlayerEntity player = event.getAttackingPlayer();
		if (event.getEntityLiving() instanceof MonsterEntity)
		if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm))
			event.setDroppedExperience((int) (event.getDroppedExperience()*MonsterCharm.bonusXPModifier));
	}
	
	
	@SubscribeEvent
	public void miningStuff(PlayerEvent.BreakSpeed event) {
		
		/*
		 * Handler for calculating mining speed boost from wearing Mining Charm.
		 */
		
		float miningBoost = 1.0F;
		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.miningCharm))
			miningBoost += MiningCharm.miningBoost.asModifier(false);
		
		event.setNewSpeed(event.getNewSpeed()*miningBoost);
		
		
		//System.out.println("New Speed: " + event.getNewSpeed());
		//System.out.println("Original Speed: " + event.getOriginalSpeed());
	}
	
	@SubscribeEvent
	public void onBlockDropsHarvest(HarvestDropsEvent event) {
		
		// This never happens
		System.out.println("Event fired!");
		
		
		/*
		try {
			
			Field target = Class.forName("net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent").getDeclaredField("fortuneLevel");
			
			target.setAccessible(true);
            target.setInt(event, 10);
			
            System.out.println("Level set tp 10!");
            System.out.println("Level now: " + event.getFortuneLevel());
            
		} catch (Exception ex) {
			System.out.println("Exception!");
		}
		*/
	}
	
	@SubscribeEvent
	public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
		
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart))
				if (!player.getActivePotionEffects().isEmpty()) {
					Collection<EffectInstance> effects = player.getActivePotionEffects();
					
					for (EffectInstance effect : effects)
						effect.tick(player);
					/*if (player.ticksExisted % 20 == 0)
					for (EffectInstance effect : effects) {
						try {
							Field target = Class.forName("net.minecraft.potion.EffectInstance").getDeclaredField("duration");
							
							target.setAccessible(true);
				            target.setInt(effect, effect.getDuration()+10);
							
				            //System.out.println("Sucess!");
				            
						} catch (Exception ex) {
							//ex.printStackTrace();
						}
						effect.tick(player);
					}
					*/
				}
			
			
			if (event.getEntityLiving().world.isRemote)		
				return;
			
			if (SuperpositionHandler.hasSpellstoneCooldown(player))
				SuperpositionHandler.setSpellstoneCooldown(player, SuperpositionHandler.getSpellstoneCooldown(player)-1);
			
			SuperpositionHandler.handleEnigmaticFlight(player);
			
		}
		
	}
	
	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {

		CapabilitiesRegistrationHandler.registerCapabilities(evt);
	  
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	 public void onConfirmedDeath(LivingDeathEvent event) {

		 if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();

				if(SuperpositionHandler.hasCurio(player, EnigmaticLegacy.escapeScroll) & !player.world.isRemote) {
					ItemStack tomeStack = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.escapeScroll);
					ItemEntity droppedTomeStack = new ItemEntity(player.world, player.posX, player.posY+(player.getHeight()/2), player.posZ, tomeStack.copy());
					droppedTomeStack.setPickupDelay(10);
					droppedTomeStack.setNoGravity(true);
					droppedTomeStack.setNoDespawn();
					//droppedTomeStack.setInvulnerable(true);
					player.world.addEntity(droppedTomeStack);
					droppedTomeStack.setMotion(0, 0, 0);
					//droppedTomeStack.setGlowing(fa);
					
					tomeStack.shrink(1);
					
					Vec3d vec = SuperpositionHandler.getValidSpawn(player.world, player);
					
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
			    	EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 1024, player.dimension)), new PacketPortalParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 72, 1.0F));
					
					player.setPositionAndUpdate(vec.x, vec.y, vec.z);
					
					player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
			    	EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.posX, player.posY, player.posZ, 1024, player.dimension)), new PacketRecallParticles(player.posX, player.posY+(player.getHeight()/2), player.posZ, 48));
				}
		 }
		 
	 }
	
	
	 @SubscribeEvent(priority = EventPriority.HIGHEST)
	 public void onLivingDeath(LivingDeathEvent event) {

		 if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();

				if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem) || player.inventory.hasItemStack(new ItemStack(EnigmaticLegacy.enigmaticItem))) {
					event.setCanceled(true);
					player.setHealth(1);
				} else if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl) && Math.random() <= VoidPearl.savingChance.asMultiplier(false)) {
					event.setCanceled(true);
					player.setHealth(1);
				}
		 }
		 
	 }
	 
	 @SubscribeEvent
	 public void onLivingHeal(LivingHealEvent event) {
		 
		 if(event.getEntityLiving() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getEntityLiving();

				if(SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
					if (event.getAmount() <= 1.0F) {
						event.setAmount((float) (event.getAmount()/(1.5F*VoidPearl.regenerationModifier)));
					}
				}
		 }
		 
	 }
	 
	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event) {
		
		if (event.getEntityLiving().world.isRemote)
			return;
		
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem))
				if (EnigmaticLegacy.damageTypesFire.contains(event.getSource().damageType))
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
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.eyeOfNebula)) {
				//System.out.println(player.hurtResistantTime);
				if (EyeOfNebula.immunityList.contains(event.getSource().damageType)) {
					event.setCanceled(true);
			    } else if (Math.random() <= EyeOfNebula.dodgeChance.asMultiplier(false) && player.hurtResistantTime <= 10 && event.getSource().getTrueSource() instanceof LivingEntity) {
			    	EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketConfirmTeleportation(true));
			    	player.hurtResistantTime = 20;
					event.setCanceled(true);
				}
			}
		} else if (event.getSource().getImmediateSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();
			
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == EnigmaticLegacy.extradimensionalEye)
				if (ItemNBTHelper.verifyExistance(player.getHeldItemMainhand(), "BoundDimension"))
					if (ItemNBTHelper.getInt(player.getHeldItemMainhand(), "BoundDimension", 0) == event.getEntityLiving().dimension.getId()) {
					  event.setCanceled(true);
					  ItemStack stack = player.getHeldItemMainhand();
					  
					  EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, 128, event.getEntityLiving().dimension)), new PacketPortalParticles(event.getEntityLiving().posX, event.getEntityLiving().posY+(event.getEntityLiving().getHeight()/2), event.getEntityLiving().posZ, 96, 1.5D));
					  
					  event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
					  event.getEntityLiving().setPositionAndUpdate(ItemNBTHelper.getDouble(stack, "BoundX", 0D), ItemNBTHelper.getDouble(stack, "BoundY", 0D), ItemNBTHelper.getDouble(stack, "BoundZ", 0D));
					  event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2)));
					  
					  EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, 128, event.getEntityLiving().dimension)), new PacketRecallParticles(event.getEntityLiving().posX, event.getEntityLiving().posY+(event.getEntityLiving().getHeight()/2), event.getEntityLiving().posZ, 48));
					  
					  if (!player.abilities.isCreativeMode)
					  stack.shrink(1);
				}
		}
		
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.angelBlessing))
				if (AngelBlessing.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * AngelBlessing.resistanceList.get(event.getSource().damageType));
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.oceanStone)) {
				Entity attacker = event.getSource().getTrueSource();
				
				if (attacker instanceof DrownedEntity || attacker instanceof GuardianEntity || attacker instanceof ElderGuardianEntity)
					event.setAmount(event.getAmount()*OceanStone.underwaterCreaturesResistance.asModifierInverted());
			}
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart)) {
				//System.out.println("Damage type: " + event.getSource().damageType);
				if (event.getSource().getTrueSource() instanceof LivingEntity && MagmaHeart.nemesisList.contains(event.getSource().damageType)) {
					LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
					if (!attacker.isImmuneToFire()) {
						attacker.attackEntityFrom(new EntityDamageSource(DamageSource.ON_FIRE.damageType, player), (float) MagmaHeart.damageBack);
						attacker.setFire((int) MagmaHeart.fireBack);
					}
				}
				
			}
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.eyeOfNebula))
				if (EyeOfNebula.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * EyeOfNebula.resistanceList.get(event.getSource().damageType));
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.golemHeart))
				if (GolemHeart.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * GolemHeart.resistanceList.get(event.getSource().damageType));
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl))
				if (VoidPearl.resistanceList.containsKey(event.getSource().damageType))
					event.setAmount(event.getAmount() * VoidPearl.resistanceList.get(event.getSource().damageType));
			
		} else if (event.getEntityLiving() instanceof MonsterEntity) {
			MonsterEntity monster = (MonsterEntity) event.getEntityLiving();
			
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm)) {
				if (monster.isEntityUndead()) {
					event.setAmount(event.getAmount()*MonsterCharm.undeadDamageModifier.asModifier(true));	
				} else if (monster.isAggressive() ||  monster instanceof CreeperEntity) {
					
					if (monster instanceof EndermanEntity || monster instanceof ZombiePigmanEntity || monster instanceof BlazeEntity || monster instanceof GuardianEntity || monster instanceof ElderGuardianEntity || !monster.isNonBoss()) {
					} else {
						event.setAmount(event.getAmount()*MonsterCharm.aggressiveDamageModifier.asModifier(true));	
					}
					
				}
			}	
			
			}
		}
		
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.WITHER, VoidPearl.witheringTime, VoidPearl.witheringLevel, false, true));
			}
		}
		
	}
	
	@SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
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
            if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !this.containsDrop(event.getDrops(), Items.WITHER_SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel())) {
                this.addDrop(event, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
            }
            
            if (event.getSource().getTrueSource() instanceof ServerPlayerEntity && this.containsDrop(event.getDrops(), Items.WITHER_SKELETON_SKULL))
                BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
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
		
		/*
		if (event.getName().equals(LootTables.CHESTS_SIMPLE_DUNGEON)) {
		
			LootPool theOne = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.HEART_OF_THE_SEA)).name("pool34").build();
	        LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -10F, 1F, ItemLootEntry.builder(EnigmaticLegacy.golemHeart));
			
	        LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
			
			//Builder lootEntry = LootPool.builder().rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(Items.NETHER_BRICK).weight(20)).addEntry(ItemLootEntry.builder(Items.NETHER_STAR)).addEntry(ItemLootEntry.builder(Items.ACACIA_BUTTON).weight(30)).addEntry(ItemLootEntry.builder(Items.BOOK).weight(10).acceptFunction(EnchantRandomly.func_215900_c())).addEntry(ItemLootEntry.builder(Items.YELLOW_WOOL).weight(5)).addEntry(EmptyLootEntry.func_216167_a().weight(5));
	        
	    }*/
		
		List<ResourceLocation> underwaterRuins = new ArrayList<ResourceLocation>();
		underwaterRuins.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		underwaterRuins.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		
		
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
		
		
		if (SuperpositionHandler.getOverworldDungeons().contains(event.getName())) {
			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_PICKAXE, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_AXE, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_SWORD, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_SHOVEL, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.BOW, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.ironRing, 20),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.hastePotionDefault, 20),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.magnetRing, 7),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.unholyGrail, 4),
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
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.recallPotion, 15),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.mendingMixture, 35),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.extradimensionalEye, 10)
				);
			
			LootTable modified = event.getTable();
			modified.addPool(epic);
			event.setTable(modified);
		}

		
		
		if (event.getName().equals(LootTables.CHESTS_STRONGHOLD_LIBRARY)) {
			LootPool special = SuperpositionHandler.constructLootPool("el_special", 1F, 1F,
					ItemLootEntry.builder(EnigmaticLegacy.thiccScroll).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(4.0F, 13F))));
			
			LootTable modified = event.getTable();
			modified.addPool(special);
			event.setTable(modified);
		}
		
		
		 if (event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_BIG) || event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_SMALL)) {
				LootPool special = SuperpositionHandler.constructLootPool("el_special", 1F, 1F,
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
	    	
	    	ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

	        if (!SuperpositionHandler.hasPersistentTag(player, NBT_KEY_FIRSTJOIN)) {
	        	
	            ItemStack stack = new ItemStack(EnigmaticLegacy.enigmaticAmulet);
	            ItemNBTHelper.setString(stack, "Inscription", player.getDisplayName().getString());
	            
	            player.inventory.setInventorySlotContents(8, stack);
	            
	            SuperpositionHandler.setPersistentBoolean(player, NBT_KEY_FIRSTJOIN, true);
	        }
	        
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
	    
	    }
	    
	    
	    
	@SubscribeEvent
	public void onAdvancement(AdvancementEvent event) {
		
		String id = event.getAdvancement().getId().toString();
		PlayerEntity player = event.getPlayer();
		
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
		
		/*
		LazyOptional<ICurioItemHandler> curioHandler = CuriosAPI.getCuriosHandler(event.getPlayer());
		curioHandler.ifPresent(handler -> {});
		*/
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerEntity player = event.getPlayer();
		
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
	
	
	public void addDrop(LivingDropsEvent event, ItemStack drop) {
        ItemEntity entityitem = new ItemEntity(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
        entityitem.setPickupDelay(10);
        event.getDrops().add(entityitem);
    }
	
	public boolean containsDrop(Collection <ItemEntity> drops, Item item) {
		
		for (ItemEntity drop : drops) {
			if (drop.getItem() != null)
				if (drop.getItem().getItem() == item)
					return true;
		}
		
		return false;
	}
	
	public boolean theySeeMeRollin(int lootingLevel) {
		double chance = ForbiddenAxe.beheadingChanceBase.asMultiplier(false) + (ForbiddenAxe.beheadingChanceBonus.asMultiplier(false)*lootingLevel);
		
		if (Math.random() <= chance)
			return true;
		else
			return false;
	}
	
}
