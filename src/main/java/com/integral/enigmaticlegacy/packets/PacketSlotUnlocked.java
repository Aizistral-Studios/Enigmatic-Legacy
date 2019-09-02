package com.integral.enigmaticlegacy.packets;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.helpers.SlotUnlockedToast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSlotUnlocked {
	
	private String type;

	  public PacketSlotUnlocked(String type) {
		  this.type = type;
	  }

	  public static void encode(PacketSlotUnlocked msg, PacketBuffer buf) {
		  buf.writeString(msg.type);
	  }

	  public static PacketSlotUnlocked decode(PacketBuffer buf) {
	    return new PacketSlotUnlocked(buf.readString());
	 }
	  
	  @OnlyIn(Dist.CLIENT)
	  public static void handle(PacketSlotUnlocked msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      PlayerEntity player = Minecraft.getInstance().player;
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

