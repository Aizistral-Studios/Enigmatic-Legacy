package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for spawning a bunch of portal particles around specific point.
 * @author Integral
 */

public class PacketPortalParticles {
	
	private double x;
	private double y;
	private double z;
	private int num;
	private double rangeModifier;
	private boolean check;

	  public PacketPortalParticles(double x, double y, double z, int number, double rangeModifier, boolean checkSettings) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	    this.num = number;
	    this.rangeModifier = rangeModifier;
	    this.check = checkSettings;
	  }

	  public static void encode(PacketPortalParticles msg, PacketBuffer buf) {
	    buf.writeDouble(msg.x);
	    buf.writeDouble(msg.y);
	    buf.writeDouble(msg.z);
	    buf.writeInt(msg.num);
	    buf.writeDouble(msg.rangeModifier);
	    buf.writeBoolean(msg.check);
	  }

	  public static PacketPortalParticles decode(PacketBuffer buf) {
	    return new PacketPortalParticles(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readDouble(), buf.readBoolean());
	 }


	  public static void handle(PacketPortalParticles msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	ClientPlayer player = Minecraft.getInstance().player;
		      
		    	int amount = msg.num;
			      
			      if (msg.check)
			    	  amount *= SuperpositionHandler.getParticleMultiplier();
			      
			      for (int counter = 0; counter <= amount; counter++)
		    		player.level.addParticle(ParticleTypes.PORTAL, true, msg.x, msg.y, msg.z, ((Math.random()-0.5D)*2.0D)*msg.rangeModifier, ((Math.random()-0.5D)*2.0D)*msg.rangeModifier, ((Math.random()-0.5D)*2.0D)*msg.rangeModifier);
		      
		      
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
