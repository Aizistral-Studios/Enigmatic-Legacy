package com.integral.enigmaticlegacy.packets.clients;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for bursting out a bunch of dragon breath particles.
 * @author Integral
 */

public class PacketFlameParticles {
	
	public static final Random random = new Random();
	
	private double x;
	private double y;
	private double z;
	private int num;

	  public PacketFlameParticles(double x, double y, double z, int number) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	    this.num = number;
	  }

	  public static void encode(PacketFlameParticles msg, PacketBuffer buf) {
	    buf.writeDouble(msg.x);
	    buf.writeDouble(msg.y);
	    buf.writeDouble(msg.z);
	    buf.writeInt(msg.num);
	  }

	  public static PacketFlameParticles decode(PacketBuffer buf) {
		  return new PacketFlameParticles(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt());
	  }


	  public static void handle(PacketFlameParticles msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {    	
		      ClientPlayerEntity player = Minecraft.getInstance().player;
		      
		      for (int counter = 0; counter <= msg.num; counter++) {
		    		player.world.addParticle(ParticleTypes.FLAME, true, msg.x+(Math.random()-0.5), msg.y+(Math.random()-0.5), msg.z+(Math.random()-0.5), (Math.random()-0.5D)*0.1D, (Math.random()-0.5D)*0.1D, (Math.random()-0.5D)*0.1D);
		      }
		      
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
