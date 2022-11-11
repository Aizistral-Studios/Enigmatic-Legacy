package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for setting player's motion on client side.
 * Required since motion of the player can only be affected there.
 * @author Integral
 */

public class PacketPlayerMotion {
	
	private double x;
	private double y;
	private double z;

	  public PacketPlayerMotion(double x, double y, double z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	  }

	  public static void encode(PacketPlayerMotion msg, FriendlyByteBuf buf) {
	     buf.writeDouble(msg.x);
	     buf.writeDouble(msg.y);
	     buf.writeDouble(msg.z);
	  }

	  public static PacketPlayerMotion decode(FriendlyByteBuf buf) {
	     return new PacketPlayerMotion(buf.readDouble(), buf.readDouble(), buf.readDouble());
	  }


	  public static void handle(PacketPlayerMotion msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	LocalPlayer player = Minecraft.getInstance().player;
		    	
		    	player.setDeltaMovement(msg.x, msg.y, msg.z);
		      	
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
