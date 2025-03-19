package com.aizistral.enigmaticlegacy.api.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Fired when player summons entity, like Wither or Iron Golem.
 * Not cancelable, no result, fired on {@link MinecraftForge#EVENT_BUS}.
 * @author Aizistral
 */

public class SummonedEntityEvent extends PlayerEvent {
	private final Entity summonedEntity;

	public SummonedEntityEvent(Player player, Entity entity) {
		super(player);
		this.summonedEntity = entity;
	}

	public Entity getSummonedEntity() {
		return this.summonedEntity;
	}
}
