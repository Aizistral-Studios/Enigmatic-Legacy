package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.entities.EnigmaticPotionEntity;
import com.aizistral.enigmaticlegacy.entities.PermanentItemEntity;
import com.aizistral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EnigmaticEntities {
	private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
			DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EnigmaticLegacy.MODID);

	public static final RegistryObject<EntityType<PermanentItemEntity>> PERMANENT_ITEM_ENTITY = register(
			"permanent_item_entity",
			() -> EntityType.Builder.<PermanentItemEntity>of(PermanentItemEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(64)
					.updateInterval(2)
	);

	public static final RegistryObject<EntityType<EnigmaticPotionEntity>> ENIGMATIC_POTION = register(
			"enigmatic_potion_entity",
			() -> EntityType.Builder.<EnigmaticPotionEntity>of(EnigmaticPotionEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(64)
					.updateInterval(10)
	);

	public static final RegistryObject<EntityType<UltimateWitherSkullEntity>> ULTIMATE_WITHER_SKULL = register(
			"ultimate_wither_skull_entity",
			() -> EntityType.Builder.<UltimateWitherSkullEntity>of(UltimateWitherSkullEntity::new, MobCategory.MISC)
					.sized(0.25F, 0.25F)
					.clientTrackingRange(64)
	);

	public static void registerEntities(IEventBus modEventBus) {
		ENTITY_TYPES.register(modEventBus);
	}

	private static <T extends Entity> RegistryObject<EntityType<T>> register(
			String name, Supplier<EntityType.Builder<T>> builder
	) {
		final String key = EnigmaticLegacy.MODID + ":" + name;
		return ENTITY_TYPES.register(name, () -> builder.get().build(key));
	}
}
