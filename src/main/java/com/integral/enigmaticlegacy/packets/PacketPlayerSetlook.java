package com.integral.enigmaticlegacy.packets;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

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
	  
	  @OnlyIn(Dist.CLIENT)
	  public static void handle(PacketPlayerSetlook msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	PlayerEntity player = Minecraft.getInstance().player;
		    	
		    	SuperpositionHandler.lookAt(msg.x, msg.y, msg.z, player);
		      	
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
