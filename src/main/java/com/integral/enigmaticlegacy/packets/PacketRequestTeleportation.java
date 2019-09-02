package com.integral.enigmaticlegacy.packets;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class PacketRequestTeleportation {
	
	private boolean pressed;

	  public PacketRequestTeleportation(boolean pressed) {
	    this.pressed = pressed;
	  }

	  public static void encode(PacketRequestTeleportation msg, PacketBuffer buf) {
	     buf.writeBoolean(msg.pressed);
	  }

	  public static PacketRequestTeleportation decode(PacketBuffer buf) {
	    return new PacketRequestTeleportation(buf.readBoolean());
	 }
	  
	  @OnlyIn(Dist.CLIENT)
	  public static void handle(PacketRequestTeleportation msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      if (msg.pressed)
		    	  EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketConfirmTeleportation(true));
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
