package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for setting the player to look at specific point in space,
 * defined by absolute x/y/z coordinates.
 * @author Integral
 */

public class PacketPlayerSetlook {
	
	private double x;
	private double y;
	private double z;

	  public PacketPlayerSetlook(double x, double y, double z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	  }

	  public static void encode(PacketPlayerSetlook msg, FriendlyByteBuf buf) {
	     buf.writeDouble(msg.x);
	     buf.writeDouble(msg.y);
	     buf.writeDouble(msg.z);
	  }

	  public static PacketPlayerSetlook decode(FriendlyByteBuf buf) {
	     return new PacketPlayerSetlook(buf.readDouble(), buf.readDouble(), buf.readDouble());
	  }


	  public static void handle(PacketPlayerSetlook msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	LocalPlayer player = Minecraft.getInstance().player;
		    	
		    	SuperpositionHandler.lookAt(msg.x, msg.y, msg.z, player);
		      	
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
