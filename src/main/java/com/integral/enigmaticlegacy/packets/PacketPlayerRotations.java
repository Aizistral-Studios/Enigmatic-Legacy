package com.integral.enigmaticlegacy.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketPlayerRotations {
	
	private float rotationYaw;
	private float rotationPitch;
	private float rotationYawHead;

	  public PacketPlayerRotations(float rotationYaw, float rotationPitch, float rotationYawHead) {
	    this.rotationYaw = rotationYaw;
	    this.rotationPitch = rotationPitch;
	    this.rotationYawHead = rotationYawHead;
	  }

	  public static void encode(PacketPlayerRotations msg, PacketBuffer buf) {
	     buf.writeFloat(msg.rotationYaw);
	     buf.writeFloat(msg.rotationPitch);
	     buf.writeFloat(msg.rotationYawHead);
	  }

	  public static PacketPlayerRotations decode(PacketBuffer buf) {
	     return new PacketPlayerRotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
	  }
	  
	  @OnlyIn(Dist.CLIENT)
	  public static void handle(PacketPlayerRotations msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	PlayerEntity player = Minecraft.getInstance().player;
		    	
		    	player.rotateTowards(msg.rotationYaw, msg.rotationPitch);
		    	player.rotationYawHead = msg.rotationYawHead;
		      	
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
