package com.integral.enigmaticlegacy.packets.clients;
import java.util.Random;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for playing respective sound and animation on client side,
 * whenever item is picked up by player.
 * @author Integral
 */

public class PacketHandleItemPickup {
	  
	  static Random random = new Random();
	  private int item_id;
	  private int pickuper_id;

	  public PacketHandleItemPickup(int itemID, int pickuperID) {
		  this.item_id = itemID;
		  this.pickuper_id = pickuperID;
	  }

	  public static void encode(PacketHandleItemPickup msg, PacketBuffer buf) {
		  buf.writeInt(msg.item_id);
		  buf.writeInt(msg.pickuper_id);
	  }

	  public static PacketHandleItemPickup decode(PacketBuffer buf) {
	    return new PacketHandleItemPickup(buf.readInt(), buf.readInt());
	 }

	  public static void handle(PacketHandleItemPickup msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      
		    	Entity pickuper = Minecraft.getInstance().player.world.getEntityByID(msg.pickuper_id);
			      Entity entity = Minecraft.getInstance().player.world.getEntityByID(msg.item_id);
			      
			      Minecraft.getInstance().particles.addEffect(new ItemPickupParticle(Minecraft.getInstance().player.world, pickuper, entity, 0.5F));
			      
				Minecraft.getInstance().player.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (random.nextFloat() - random.nextFloat()) * 1.4F + 2.0F, false);
		    	
		      });
		    ctx.get().setPacketHandled(true);
	  }

}

