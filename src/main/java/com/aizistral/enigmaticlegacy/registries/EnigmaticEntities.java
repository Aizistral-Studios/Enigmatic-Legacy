package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.aizistral.enigmaticlegacy.entities.PermanentItemEntity;
import com.aizistral.enigmaticlegacy.entities.UltimateWitherSkullEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticEntities extends AbstractRegistry<EntityType<?>> {
	private static final EnigmaticEntities INSTANCE = new EnigmaticEntities();

	@ObjectHolder(value = MODID + ":permanent_item_entity", registryName = "entity_type")
	public static final EntityType<PermanentItemEntity> PERMANENT_ITEM_ENTITY = null;

	@ObjectHolder(value = MODID + ":enigmatic_potion_entity", registryName = "entity_type")
	public static final EntityType<EnigmaticPotionEntity> ENIGMATIC_POTION = null;

	@ObjectHolder(value = MODID + ":ultimate_wither_skull_entity", registryName = "entity_type")
	public static final EntityType<UltimateWitherSkullEntity> ULTIMATE_WITHER_SKULL = null;

	private EnigmaticEntities() {
		super(ForgeRegistries.ENTITY_TYPES);
		this.register("permanent_item_entity", () -> EntityType.Builder.<PermanentItemEntity>of(
				PermanentItemEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(64)
				.setCustomClientFactory((spawnEntity, world) ->
				new PermanentItemEntity(PERMANENT_ITEM_ENTITY, world))
				.setUpdateInterval(2).setShouldReceiveVelocityUpdates(true)
				.build(MODID + ":permanent_item_entity"));

		this.register("enigmatic_potion_entity", () -> EntityType.Builder.<EnigmaticPotionEntity>of(
				EnigmaticPotionEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(64)
				.setCustomClientFactory((spawnEntity, world) ->
				new EnigmaticPotionEntity(ENIGMATIC_POTION, world))
				.setUpdateInterval(10).setShouldReceiveVelocityUpdates(true)
				.build(MODID + ":enigmatic_potion_entity"));

		this.register("ultimate_wither_skull_entity", () -> EntityType.Builder.<UltimateWitherSkullEntity>of(
				UltimateWitherSkullEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(64)
				.setCustomClientFactory((spawnEntity, world) ->
				new UltimateWitherSkullEntity(ULTIMATE_WITHER_SKULL, world))
				//.setUpdateInterval(1).setShouldReceiveVelocityUpdates(true)
				.build(MODID + ":ultimate_wither_skull_entity"));
	}

}
