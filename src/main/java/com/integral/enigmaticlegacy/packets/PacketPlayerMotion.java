package com.integral.enigmaticlegacy.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketPlayerMotion {
	
	private double x;
	private double y;
	private double z;

	  public PacketPlayerMotion(double x, double y, double z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	  }

	  public static void encode(PacketPlayerMotion msg, PacketBuffer buf) {
	     buf.writeDouble(msg.x);
	     buf.writeDouble(msg.y);
	     buf.writeDouble(msg.z);
	  }

	  public static PacketPlayerMotion decode(PacketBuffer buf) {
	     return new PacketPlayerMotion(buf.readDouble(), buf.readDouble(), buf.readDouble());
	  }
	  
	  @OnlyIn(Dist.CLIENT)
	  public static void handle(PacketPlayerMotion msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	PlayerEntity player = Minecraft.getInstance().player;
		    	
		    	player.setMotion(msg.x, msg.y, msg.z);
		      	
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
