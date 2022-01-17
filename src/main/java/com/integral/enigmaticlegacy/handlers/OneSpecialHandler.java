package com.integral.enigmaticlegacy.handlers;

import java.util.List;
import java.util.Random;

import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;

/**
 * Did you get the reference?
 * @author Integral
 */

public class OneSpecialHandler {

	Random rand = new Random();

	public Player nextMan() {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

		List<ServerPlayer> players = server.getPlayerList().getPlayers();
		Player man;

		if (players.size() > 0) {
			if (players.size() == 1) {
				man	= players.get(0);
			} else {
				man = players.get(this.rand.nextInt(players.size() - 1));
			}
		} else {
			man = null;
		}

		return man;
	}

}
