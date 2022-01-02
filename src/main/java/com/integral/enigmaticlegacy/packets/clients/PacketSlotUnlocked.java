package com.integral.enigmaticlegacy.packets.clients;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.objects.SlotUnlockedToast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for displaying Curio slot unlock notification.
 * @author Integral
 */

public class PacketSlotUnlocked {
	
	private String type;

	  public PacketSlotUnlocked(String type) {
		  this.type = type;
	  }

	  public static void encode(PacketSlotUnlocked msg, PacketBuffer buf) {
		  buf.writeUtf(msg.type);
	  }

	  public static PacketSlotUnlocked decode(PacketBuffer buf) {
	    return new PacketSlotUnlocked(buf.readUtf());
	 }

	  public static void handle(PacketSlotUnlocked msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ClientPlayerEntity player = Minecraft.getInstance().player;
		      ItemStack stack;
		      
		      if (msg.type.equals("ring"))
		    	  stack = new ItemStack(EnigmaticLegacy.ironRing);
		      else if (msg.type.equals("scroll"))
		    	  stack = new ItemStack(EnigmaticLegacy.thiccScroll);
		      else if (msg.type.equals("spellstone"))
		    	  stack = new ItemStack(EnigmaticLegacy.voidPearl);
		      else
		    	  stack = new ItemStack(EnigmaticLegacy.ironRing);
		      
		      IToast toast = new SlotUnlockedToast(stack, msg.type);
		      EnigmaticEventHandler.scheduledToasts.add(toast);
		      EnigmaticEventHandler.deferredToast.put(player, 5);
		      
		    });
		    ctx.get().setPacketHandled(true);
	  }

}

