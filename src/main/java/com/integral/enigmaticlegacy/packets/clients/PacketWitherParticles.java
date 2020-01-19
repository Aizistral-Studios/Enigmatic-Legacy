package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for bursting out particles for UltimateWitherSkull.
 * @author Integral
 */

public class PacketWitherParticles {
	
	private double x;
	private double y;
	private double z;
	private int num;
	private boolean check;

	  public PacketWitherParticles(double x, double y, double z, int number, boolean checkSettings) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	    this.num = number;
	    this.check = checkSettings;
	  }

	  public static void encode(PacketWitherParticles msg, PacketBuffer buf) {
	    buf.writeDouble(msg.x);
	    buf.writeDouble(msg.y);
	    buf.writeDouble(msg.z);
	    buf.writeInt(msg.num);
	    buf.writeBoolean(msg.check);
	  }

	  public static PacketWitherParticles decode(PacketBuffer buf) {
		  return new PacketWitherParticles(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readBoolean());
	  }


	  public static void handle(PacketWitherParticles msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {    	

			      ClientPlayerEntity player = Minecraft.getInstance().player;
			      
			    	int amount = msg.num;
				      
				      if (msg.check)
				    	  amount *= SuperpositionHandler.getParticleMultiplier();
				      
				      for (int counter = 0; counter <= amount; counter++)
		    		player.world.addParticle(ParticleTypes.LARGE_SMOKE, true, msg.x, msg.y, msg.z, (Math.random()-0.5D)*0.2D, (Math.random()-0.5D)*0.2D, (Math.random()-0.5D)*0.2D);
		      
		      
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
