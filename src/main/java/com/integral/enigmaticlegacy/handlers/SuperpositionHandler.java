package com.integral.enigmaticlegacy.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;

import net.minecraft.block.BlockState;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootPool.Builder;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.EnchantWithLevels;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetDamage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;

/**
 * The vessel and library for most most of the handling methods in the Enigmatic Legacy.
 * @author Integral
 */

public class SuperpositionHandler {
	
	public static HashMap<PlayerEntity, Integer> spellstoneCooldowns = new HashMap<PlayerEntity, Integer>();
    
	/**
	 * Checks whether LivingEntity has given Item equipped in it's Curios slots.
	 * @return True if has, false otherwise.
	 */
	
    public static boolean hasCurio(final LivingEntity entity, final Item curio) {
        final Optional<ImmutableTriple<String, Integer, ItemStack>> data = (Optional<ImmutableTriple<String, Integer, ItemStack>>)CuriosAPI.getCurioEquipped(curio, entity);
        return data.isPresent();
    }
    
    /**
     * Gets the ItemStack of provided Item within entity's Curio slots.
     * @return First sufficient ItemStack found, null if such item is not equipped.
     */
    @Nullable
    public static ItemStack getCurioStack(final LivingEntity entity, final Item curio) {
        final Optional<ImmutableTriple<String, Integer, ItemStack>> data = (Optional<ImmutableTriple<String, Integer, ItemStack>>)CuriosAPI.getCurioEquipped(curio, entity);
        if (data.isPresent()) {
            return (ItemStack)data.get().getRight();
        }
        return null;
    }
    
    /**
     * Sends message to Curios API in order to register specified Curio type.
     * Should be used within InterModEnqueueEvent.
     * 
     * @param identifier Unique identifier of the type.
     * @param slots How many slots of this curio will be available by default.
     * @param isEnabled Whether or not this curio type should be initially acessible by player.
     * @param isHidden Whether or not this curio type should be hidden from default Curio GUI.
     * @param icon Optional resource location for custom icon.
     */
    public static void registerCurioType(final String identifier, final int slots, final boolean isEnabled, final boolean isHidden, @Nullable final ResourceLocation icon) {
        final CurioIMCMessage message = new CurioIMCMessage(identifier);
        message.setSize(slots);
        message.setEnabled(isEnabled);
        message.setHidden(isHidden);
        InterModComms.sendTo("curios", "register_type", () -> message);
        if (icon != null) {
            InterModComms.sendTo("curios", "register_icon", () -> new Tuple<String, ResourceLocation>(identifier, icon));
        }
    }
    
    /**
     * Registers a sound in Enigmatic Legacy namespace, for it to be lately defined
     * in respective .json file.
     * 
     * @return SoundEvent usable by playSound() methods.
     */
    
    public static SoundEvent registerSound(final String soundName) {
        final ResourceLocation location = new ResourceLocation("enigmaticlegacy", soundName);
        final SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
    
    /**
     * Creates and returns simple bounding box of given radius around the entity.
     */
    
    public static AxisAlignedBB getBoundingBoxAroundEntity(final Entity entity, final double radius) {
        return new AxisAlignedBB(entity.posX - radius, entity.posY - radius, entity.posZ - radius, entity.posX + radius, entity.posY + radius, entity.posZ + radius);
    }
    
    /**
     * Accelerates the entity towards specific point.
     * 
     * @param originalPosVector Absolute coordinates of the point entity should move towards.
     * @param modifier Applied velocity modifier.
     */
    
    public static void setEntityMotionFromVector(final Entity entity, final Vector3 originalPosVector, final float modifier) {
        final Vector3 entityVector = Vector3.fromEntityCenter(entity);
        Vector3 finalVector = originalPosVector.subtract(entityVector);
        if (finalVector.mag() > 1.0) {
            finalVector = finalVector.normalize();
        }
        entity.setMotion(finalVector.x * modifier, finalVector.y * modifier, finalVector.z * modifier);
    }
    
    /**
     * Gets the entity player is looking at.
     * 
     * @param range Defines the size of bounding box checked for entities with each iteration.
     * Smaller box may provide more precise results, but is ineffective over large distances.
     * @param maxDist Maximum amount of iteration the method should trace for.
     * @return First entity found on the path, or null if none exist.
     */
    
    @Nullable
    public static LivingEntity getObservedEntity(final PlayerEntity player, final World world, final float range, final int maxDist) {
        LivingEntity newTarget = null;
        Vector3 target = Vector3.fromEntityCenter((Entity)player);
        List<LivingEntity> entities = new ArrayList<LivingEntity>();
        for (int distance = 1; entities.size() == 0 && distance < maxDist; ++distance) {
            target = target.add(new Vector3(player.getLookVec()).multiply((double)distance)).add(0.0, 0.5, 0.0);
            entities = (List<LivingEntity>)player.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
            if (entities.contains(player)) {
                entities.remove(player);
            }
            for (final LivingEntity checked : entities) {
                if (!checked.isNonBoss()) {
                    entities.remove(checked);
                }
            }
        }
        if (entities.size() > 0) {
            newTarget = entities.get(0);
        }
        return newTarget;
    }
    
    /**
     * Retrieves the player respawn location.
     * @return Bed position if there is a valid one, coordinates of obsidian
     * platform in the End, or coordinates of Center of the World otherwise.
     */
    
    public static Vec3d getValidSpawn(final World world, final PlayerEntity player) {
        BlockPos pos = player.getBedLocation(world.getDimension().getType());
        if (pos != null && PlayerEntity.func_213822_a((IWorldReader)world, pos, false).isPresent()) {
            final Vec3d vec = PlayerEntity.func_213822_a((IWorldReader)world, pos, true).get();
            return vec;
        }
        pos = world.getSpawnPoint();
        
        if (world instanceof ServerWorld)
        if (((ServerWorld)player.world).getSpawnCoordinate() != null) {
			pos = ((ServerWorld)player.world).getSpawnCoordinate();
			pos = new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()+1);
        }
        
        return new Vec3d(pos.getX() - 0.5, (double)pos.getY(), pos.getZ() - 0.5);
    }
    
    /**
     * Returns the given player's spellstone cooldown.
     */
    
    public static int getSpellstoneCooldown(PlayerEntity player) {
    	if (!player.world.isRemote) {
    		if (SuperpositionHandler.spellstoneCooldowns.containsKey(player))
    			return SuperpositionHandler.spellstoneCooldowns.get(player);
    		else {
    			SuperpositionHandler.spellstoneCooldowns.put(player, 0);
    			return 0;
    		}
    	}
    	
    	return 0;
    }
    
    /**
     * Sets the given player's spellstone cooldown to specified value.
     */
    
    public static void setSpellstoneCooldown(PlayerEntity player, int cooldown) {
    	if (!player.world.isRemote) {
    		SuperpositionHandler.spellstoneCooldowns.put(player, cooldown);
    	}
    	
    	return;
    }
    
    /**
     * Checks whether player's spellstone cooldown is greater than zero.
     */
    
    public static boolean hasSpellstoneCooldown(PlayerEntity player) {
    	if (!player.world.isRemote) {
    		
    		if (SuperpositionHandler.getSpellstoneCooldown(player) > 0)
    			return true;
    		else
    			return false;
    		
    	}
    	
    	return false;
    }
    
    /**
     * Sets the player's rotations so that they will looking at specified point in space.
     */
    
    @OnlyIn(Dist.CLIENT)
    public static void lookAt(double px, double py, double pz, ClientPlayerEntity me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;

        double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        //to degree
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;

        yaw += 90f;
        me.rotationPitch = (float)pitch;
        me.rotationYaw = (float)yaw;
        //me.rotationYawHead = (float)yaw;
    }
    
    
    /**
	 * Attempts to teleport entity at given coordinates, or nearest valid location on Y axis.
	 * @return True if successfull, false otherwise.
	 */
	
	public static boolean validTeleport(Entity entity, double x_init, double y_init, double z_init, World world, int checkAxis) {
		
		int x = (int) x_init;
		int y = (int) y_init;
		int z = (int) z_init;
		
		BlockState block = world.getBlockState(new BlockPos(x, y-1, z));
		
		if (world.isAirBlock(new BlockPos(x, y-1, z)) & block.isSolid()) {
			
			for (int counter = 0; counter <= checkAxis; counter++) {
				
				if (!world.isAirBlock(new BlockPos(x, y+counter-1, z)) & world.getBlockState(new BlockPos(x, y+counter-1, z)).isSolid() & world.isAirBlock(new BlockPos(x, y+counter, z)) & world.isAirBlock(new BlockPos(x, y+counter+1, z))) {
					
					world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.posX, entity.posY, entity.posZ, 128, entity.dimension)), new PacketPortalParticles(entity.posX, entity.posY+(entity.getHeight()/2), entity.posZ, 72, 1.0F));
					
					if (entity instanceof ServerPlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) entity;
						player.setPositionAndUpdate(x+0.5, y+counter, z+0.5);
					} else
						((LivingEntity) entity).setPositionAndUpdate(x+0.5, y+counter, z+0.5);
					
					world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.posX, entity.posY, entity.posZ, 128, entity.dimension)), new PacketRecallParticles(entity.posX, entity.posY+(entity.getHeight()/2), entity.posZ, 48));
					
					return true;
				}
				
			}
			
		} else {
			
			for (int counter = 0; counter <= checkAxis; counter++) {
				
				if (!world.isAirBlock(new BlockPos(x, y-counter-1, z)) & world.getBlockState(new BlockPos(x, y-counter-1, z)).isSolid() & world.isAirBlock(new BlockPos(x, y-counter, z)) & world.isAirBlock(new BlockPos(x, y-counter+1, z))) {
					
					world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.posX, entity.posY, entity.posZ, 128, entity.dimension)), new PacketRecallParticles(entity.posX, entity.posY+(entity.getHeight()/2), entity.posZ, 48));
					
					if (entity instanceof ServerPlayerEntity) {
						ServerPlayerEntity player = (ServerPlayerEntity) entity;
						player.setPositionAndUpdate(x+0.5, y-counter, z+0.5);
					} else
						((LivingEntity) entity).setPositionAndUpdate(x+0.5, y-counter, z+0.5);
					
					world.playSound(null, entity.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, (float) (0.8F + (Math.random()*0.2D)));
					EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.posX, entity.posY, entity.posZ, 128, entity.dimension)), new PacketRecallParticles(entity.posX, entity.posY+(entity.getHeight()/2), entity.posZ, 48));
					
					return true;
				}
				
			}
			
		}
		
		return false;
	}
	
	
	/**
	  * Attempts to find valid location within given radius and teleport entity there.
	  * @return True if teleportation were successfull, false otherwise.
	  */
	 public static boolean validTeleportRandomly(Entity entity, World world, int radius) {
		 int d = radius*2;
		 
	     double x = entity.posX + ((Math.random()-0.5D)*d);
	     double y = entity.posY + ((Math.random()-0.5D)*d);
	     double z = entity.posZ + ((Math.random()-0.5D)*d);
	     return SuperpositionHandler.validTeleport(entity, x, y, z, world, radius);
	 }
	 
	 /**
	  * Builds standart loot pool with any amount of ItemLootEntries.
	  */
	 
	 public static LootPool constructLootPool(String poolName, float minRolls, float maxRolls, @Nullable net.minecraft.world.storage.loot.LootEntry.Builder<?>... entries) {
		 
		 Builder poolBuilder = LootPool.builder();
		 poolBuilder.rolls(RandomValueRange.func_215837_a(minRolls, maxRolls));
		 poolBuilder.name(poolName);
		 
		 for (net.minecraft.world.storage.loot.LootEntry.Builder<?> entry : entries) {
			 if (entry != null)
			 poolBuilder.addEntry(entry);
		 }
		 
		 LootPool constructedPool = poolBuilder.build();
		 
		 return constructedPool;
		 
	 }
	 
	 /**
	  * Creates ItemLootEntry builder for an item.
	  * If item is disabled in config, returns null instead.
	  * Count-sensitive version, allows you to specifiy min-max
	  * amounts of items generated per entry.
	  */
	 
	 @Nullable
	 public static net.minecraft.world.storage.loot.StandaloneLootEntry.Builder<?> createOptionalLootEntry(Item item, int weight, float minCount, float maxCount) {
		 
		 if (item instanceof IPerhaps) {
			 IPerhaps perhaps = (IPerhaps) item;
			 
			 if (!perhaps.isForMortals())
				 return null;
		 }
			 return ItemLootEntry.builder(item).weight(weight).acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(minCount, maxCount)));
	 }
	 
	 /**
	  * Creates ItemLootEntry builder for an item.
	  * If item is disabled in config, returns null instead.
	  */
	 
	 @Nullable
	 public static net.minecraft.world.storage.loot.StandaloneLootEntry.Builder<?> createOptionalLootEntry(Item item, int weight) {
		 
		 if (item instanceof IPerhaps) {
			 IPerhaps perhaps = (IPerhaps) item;
			 
			 if (!perhaps.isForMortals())
				 return null;
		 }
			 return ItemLootEntry.builder(item).weight(weight);
	 }
	 
	 /**
	  * Creates ItemLootEntry with a given weight, randomly ranged
	  * damage and level-based enchantments.
	  * Damage should be specified as modifier, where 1.0 is full durability. 
	  * @return
	  */
	 
	 public static ItemLootEntry.Builder<?> itemEntryBuilderED(Item item, int weight, float enchantLevelMin, float enchantLevelMax, float damageMin, float damageMax) {
		 ItemLootEntry.Builder<?> builder = ItemLootEntry.builder(item);
		 
		 builder.weight(weight);
		 builder.acceptFunction(SetDamage.func_215931_a(RandomValueRange.func_215837_a(damageMax, damageMin)));
		 builder.acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.func_215837_a(enchantLevelMin, enchantLevelMax)).func_216059_e());
		 
		 return builder;
	 }
	 
	 public static List<ResourceLocation> getEarthenDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_SIMPLE_DUNGEON);
		 lootChestList.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);
		 
		 return lootChestList;
	 }
	 
	 public static List<ResourceLocation> getWaterDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		 lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		 lootChestList.add(LootTables.CHESTS_SHIPWRECK_TREASURE);
		 lootChestList.add(LootTables.CHESTS_BURIED_TREASURE);
		 
		 return lootChestList;
	 }
	 
	 
	 public static List<ResourceLocation> getFieryDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_NETHER_BRIDGE);
		 
		 return lootChestList;
	 }
	 
	 public static List<ResourceLocation> getAirDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);
		 
		 return lootChestList;
	 }
	 
	 public static List<ResourceLocation> getEnderDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_END_CITY_TREASURE);
		 
		 return lootChestList;
	 }
	 
	 public static List<ResourceLocation> getMergedAir$EarthenDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_DESERT_PYRAMID);
		 lootChestList.add(LootTables.CHESTS_JUNGLE_TEMPLE);
		 
		 return lootChestList;
	 }
	 
	 public static List<ResourceLocation> getMergedEnder$EarthenDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
		 lootChestList.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
		 
		 return lootChestList;
	 }
	 
	 public static List<ResourceLocation> getOverworldDungeons() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_SIMPLE_DUNGEON);
		 lootChestList.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
		 lootChestList.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
		 lootChestList.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
		 lootChestList.add(LootTables.CHESTS_DESERT_PYRAMID);
		 lootChestList.add(LootTables.CHESTS_JUNGLE_TEMPLE);
		 lootChestList.add(LootTables.CHESTS_IGLOO_CHEST);
		 lootChestList.add(LootTables.CHESTS_WOODLAND_MANSION);
		 lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
		 lootChestList.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		 lootChestList.add(LootTables.CHESTS_SHIPWRECK_SUPPLY);
		 lootChestList.add(LootTables.CHESTS_PILLAGER_OUTPOST);
		 
		 return lootChestList;
	 }
	 
	 public static List<ResourceLocation> getVillageChests() {
		 List<ResourceLocation> lootChestList = new ArrayList<ResourceLocation>();
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_MASON);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_BUTCHER);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FLETCHER);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_FISHER);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TANNERY);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE);
		 lootChestList.add(LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE);
		 
		 return lootChestList;
	 }
	 
	 /**
	  * Sets the given boolean tag to the player's persistent NBT.
	  */
	 
	 public static void setPersistentBoolean(PlayerEntity player, String tag, boolean value) {
		 
		 CompoundNBT data = player.getPersistentData();
	     CompoundNBT persistent;
	     
	     if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
	    	 data.put(PlayerEntity.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
	     } else {
	    	 persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
	     }
	     
	     persistent.putBoolean(tag, value);

	 }
	 
	 /**
	  * Retrieves the given boolean tag from the player's persistent NBT.
	  */
	 
	 public static boolean getPersistentBoolean(PlayerEntity player, String tag, boolean expectedValue) {
		 
		 CompoundNBT data = player.getPersistentData();
	     CompoundNBT persistent;
	     
	     if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
	    	 data.put(PlayerEntity.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
	     } else {
	    	 persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
	     }
	     
	     if (persistent.contains(tag)) {
	    	 return persistent.getBoolean(tag);
	     } else {
	    	 persistent.putBoolean(tag, expectedValue);
	    	 return expectedValue;
	     }
		 
	 }
	 
	 /**
	  * Checks whether player has specified tag in their persistent NBT, whatever it's type or value is.	
	  */
	 
	 public static boolean hasPersistentTag(PlayerEntity player, String tag) {
		 	
		 CompoundNBT data = player.getPersistentData();
	     CompoundNBT persistent;
	     
	     if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
	    	 data.put(PlayerEntity.PERSISTED_NBT_TAG, (persistent = new CompoundNBT()));
	     } else {
	    	 persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
	     }
	     
	     if (persistent.contains(tag))
	    	 return true;
	     else
	    	 return false;
		 
		 
	 }
	 
	 
	 public static boolean hasStoredAnvilField(PlayerEntity entity) {
		 if (EnigmaticEventHandler.anvilFields.containsKey(entity))
			 if (EnigmaticEventHandler.anvilFields.get(entity) != null && !EnigmaticEventHandler.anvilFields.get(entity).equals(""))
				 return true;
		 
		 return false;
	 }
	 
	 /**
	  * Checks whether or not player has completed specified advancement.
	  */
	 
	 public static boolean hasAdvancement(@Nonnull ServerPlayerEntity player, @Nonnull ResourceLocation location) {
		 /*
		 if (player != null && location != null && player.server != null) {
			 PlayerAdvancements advancements = player.getAdvancements();
			 AdvancementManager manager = player.server.getAdvancementManager();
			 	 
			 if (advancements != null && manager != null) {
				 if (manager.getAdvancement(location) != null)
				 return advancements.getProgress(manager.getAdvancement(location)).isDone();
			 }
		 }
		 */
		 
		 try {
			 if (player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(location)).isDone())
				 return true;
		 } catch (NullPointerException ex) {
			 // Just don't do it lol		 }
		 }
		 
			 return false;
	 }
	 
	 /**
	  * Applies vanilla-stype potion tooltip to the item.
	  * 
	  * @param list List of effects to be displayed in tooltip.
	  * @param lores Text component list provided in addInformation() method.
	  * @param durationFactor Displayed effects duration will be multiplied by this value.
	  */
	 
	   @OnlyIn(Dist.CLIENT)
	   public static void addPotionTooltip(List<EffectInstance> list, ItemStack itemIn, List<ITextComponent> lores, float durationFactor) {
	      List<Tuple<String, AttributeModifier>> list1 = Lists.newArrayList();
	      if (list.isEmpty()) {
	         lores.add((new TranslationTextComponent("effect.none")).applyTextStyle(TextFormatting.GRAY));
	      } else {
	         for(EffectInstance effectinstance : list) {
	            ITextComponent itextcomponent = new TranslationTextComponent(effectinstance.getEffectName());
	            Effect effect = effectinstance.getPotion();
	            Map<IAttribute, AttributeModifier> map = effect.getAttributeModifierMap();
	            if (!map.isEmpty()) {
	               for(Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
	                  AttributeModifier attributemodifier = entry.getValue();
	                  AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierAmount(effectinstance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
	                  list1.add(new Tuple<>(entry.getKey().getName(), attributemodifier1));
	               }
	            }

	            if (effectinstance.getAmplifier() > 0) {
	               itextcomponent.appendText(" ").appendSibling(new TranslationTextComponent("potion.potency." + effectinstance.getAmplifier()));
	            }

	            if (effectinstance.getDuration() > 20) {
	               itextcomponent.appendText(" (").appendText(EffectUtils.getPotionDurationString(effectinstance, durationFactor)).appendText(")");
	            }

	            lores.add(itextcomponent.applyTextStyle(effect.getEffectType().getColor()));
	         }
	      }

	      if (!list1.isEmpty()) {
	         lores.add(new StringTextComponent(""));
	         lores.add((new TranslationTextComponent("potion.whenDrank")).applyTextStyle(TextFormatting.DARK_PURPLE));

	         for(Tuple<String, AttributeModifier> tuple : list1) {
	            AttributeModifier attributemodifier2 = tuple.getB();
	            double d0 = attributemodifier2.getAmount();
	            double d1;
	            if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
	               d1 = attributemodifier2.getAmount();
	            } else {
	               d1 = attributemodifier2.getAmount() * 100.0D;
	            }

	            if (d0 > 0.0D) {
	               lores.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String)tuple.getA()))).applyTextStyle(TextFormatting.BLUE));
	            } else if (d0 < 0.0D) {
	               d1 = d1 * -1.0D;
	               lores.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier2.getOperation().getId(), ItemStack.DECIMALFORMAT.format(d1), new TranslationTextComponent("attribute.name." + (String)tuple.getA()))).applyTextStyle(TextFormatting.RED));
	            }
	         }
	      }

	   }
	   
	   /**
	    * @return True if ItemStack can be added to player's inventory (fully or partially), false otherwise.
	    */
	   
	   public static boolean canPickStack(PlayerEntity player, ItemStack stack) {
		   
		   if (player.inventory.getFirstEmptyStack() >= 0)
			   return true;
		   else {
			   List<ItemStack> allInventories = new ArrayList<ItemStack>();
			   
			   allInventories.addAll(player.inventory.armorInventory);
			   allInventories.addAll(player.inventory.mainInventory);
			   allInventories.addAll(player.inventory.offHandInventory);
			   
			   for (ItemStack invStack : allInventories) {
				   if (canMergeStacks(invStack, stack, player.inventory.getInventoryStackLimit())) {
					   return true;
				   }
			   }
		   }
		   
		   return false;
	   }
	   
	       public static boolean canMergeStacks(ItemStack stack1, ItemStack stack2, int invStackLimit) {
		      return !stack1.isEmpty() && stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < invStackLimit;
		   }

		   /**
		    * Checks item, NBT, and meta if the item is not damageable
		    */
		   public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		      return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
		   }
	 
}
