package com.integral.enigmaticlegacy.handlers;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

/**
 * Did you get the reference?
 * @author Integral
 */

public class OneSpecialHandler {
	
	Random rand = new Random();
	
	public PlayerEntity nextMan() {
		MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		
		List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
		PlayerEntity man;
		
		if (players.size() > 0)
			man = players.get(rand.nextInt(players.size()-1));
		else
			man = null;
		
		return man;
	}

}
