package com.integral.enigmaticlegacy.handlers;

import java.util.function.Function;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class RealSmoothTeleporter implements ITeleporter {

	public RealSmoothTeleporter() {

	}

	public RealSmoothTeleporter(ServerPlayerEntity serverPlayer, double x, double y, double z) {
		serverPlayer.moveForced(x, y, z);
	}

	@Override
	public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		if (entity instanceof ServerPlayerEntity) {
			entity.setWorld(destWorld);
			destWorld.addDuringPortalTeleport((ServerPlayerEntity) entity);
			this.fireTriggers(currentWorld, (ServerPlayerEntity) entity);
			return entity;
		} else
			return repositionEntity.apply(false);
	}

	private void fireTriggers(ServerWorld p_213846_1_, ServerPlayerEntity player) {
		RegistryKey<World> registrykey = p_213846_1_.getDimensionKey();
		RegistryKey<World> registrykey1 = player.world.getDimensionKey();
		CriteriaTriggers.CHANGED_DIMENSION.testForAll(player, registrykey, registrykey1);
	}

}
