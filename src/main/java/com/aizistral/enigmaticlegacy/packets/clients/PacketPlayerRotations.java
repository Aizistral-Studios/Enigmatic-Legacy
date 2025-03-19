package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for setting player's rotations on client side.
 * @author Integral
 */

public class PacketPlayerRotations {
	
	private float rotationYaw;
	private float rotationPitch;
	private float rotationYawHead;

	  public PacketPlayerRotations(float rotationYaw, float rotationPitch, float rotationYawHead) {
	    this.rotationYaw = rotationYaw;
	    this.rotationPitch = rotationPitch;
	    this.rotationYawHead = rotationYawHead;
	  }

	  public static void encode(PacketPlayerRotations msg, FriendlyByteBuf buf) {
	     buf.writeFloat(msg.rotationYaw);
	     buf.writeFloat(msg.rotationPitch);
	     buf.writeFloat(msg.rotationYawHead);
	  }

	  public static PacketPlayerRotations decode(FriendlyByteBuf buf) {
	     return new PacketPlayerRotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
	  }


	  public static void handle(PacketPlayerRotations msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		    	LocalPlayer player = Minecraft.getInstance().player;
		    	
		    	player.turn(msg.rotationYaw, msg.rotationPitch);
		    	player.yHeadRot = msg.rotationYawHead;
		      	
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
