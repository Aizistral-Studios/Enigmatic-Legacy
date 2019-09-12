package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.handlers.EnigmaticUpdateHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for handling update notification.
 * @author Integral
 */

public class PacketUpdateNotification {

	  public PacketUpdateNotification() {
	  }

	  public static void encode(PacketUpdateNotification msg, PacketBuffer buf) {
	  }

	  public static PacketUpdateNotification decode(PacketBuffer buf) {
	    return new PacketUpdateNotification();
	 }

	  public static void handle(PacketUpdateNotification msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ClientPlayerEntity player = Minecraft.getInstance().player;
		      
		      EnigmaticUpdateHandler.handleShowup(player);
		      
		    });
		    ctx.get().setPacketHandled(true);
	  }

}


