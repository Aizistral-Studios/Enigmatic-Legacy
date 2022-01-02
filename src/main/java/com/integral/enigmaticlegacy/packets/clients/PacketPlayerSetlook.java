package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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

	  public static void encode(PacketPlayerSetlook msg, PacketBuffer buf) {
	     buf.writeDouble(msg.x);
	     buf.writeDouble(msg.y);
	     buf.writeDouble(msg.z);
	  }

	  public static PacketPlayerSetlook decode(PacketBuffer buf) {
	     return new PacketPlayerSetlook(buf.readDouble(), buf.readDouble(), buf.readDouble());
	  }


	  public static void handle(PacketPlayerSetlook msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	ClientPlayer player = Minecraft.getInstance().player;
		    	
		    	SuperpositionHandler.lookAt(msg.x, msg.y, msg.z, player);
		      	
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
