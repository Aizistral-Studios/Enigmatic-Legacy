package com.aizistral.enigmaticlegacy.handlers;

import java.util.function.Function;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;

public class RealSmoothTeleporter implements ITeleporter {

	public RealSmoothTeleporter() {

	}

	public RealSmoothTeleporter(ServerPlayer serverPlayer, double x, double y, double z) {
		serverPlayer.moveTo(x, y, z);
	}

	@Override
	public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
		if (entity instanceof ServerPlayer)
			return repositionEntity.apply(false);
		//entity.level = destWorld;
		//destWorld.addDuringPortalTeleport((ServerPlayer) entity);
		//this.fireTriggers(currentWorld, (ServerPlayer) entity);
		//return entity;
		else
			return repositionEntity.apply(false);
	}

	private void fireTriggers(ServerLevel p_213846_1_, ServerPlayer player) {
		ResourceKey<Level> registrykey = p_213846_1_.dimension();
		ResourceKey<Level> registrykey1 = player.level().dimension();
		CriteriaTriggers.CHANGED_DIMENSION.trigger(player, registrykey, registrykey1);
	}

}
