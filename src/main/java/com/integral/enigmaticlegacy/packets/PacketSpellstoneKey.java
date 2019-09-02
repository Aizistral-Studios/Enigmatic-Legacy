package com.integral.enigmaticlegacy.packets;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.OceanStone;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSpellstoneKey {
	
	private boolean pressed;
	private int spellstoneID;

	  public PacketSpellstoneKey(boolean pressed, int spellstoneID) {
	    this.pressed = pressed;
	    this.spellstoneID = spellstoneID;
	    
	  }

	  public static void encode(PacketSpellstoneKey msg, PacketBuffer buf) {
	     buf.writeBoolean(msg.pressed);
	     buf.writeInt(msg.spellstoneID);
	  }

	  public static PacketSpellstoneKey decode(PacketBuffer buf) {
	    return new PacketSpellstoneKey(buf.readBoolean(), buf.readInt());
	 }
	  
	  public static void handle(PacketSpellstoneKey msg, Supplier<NetworkEvent.Context> ctx) {

		    ctx.get().enqueueWork(() -> {
		      ServerPlayerEntity playerServ = ctx.get().getSender();
				
		      	switch (msg.spellstoneID) {
		      		default:
		      			EnigmaticLegacy.enigmaticLogger.error("Invalid spellstone ID received by spellstone active ability handler");
		      			break;
		      		
		      		case 0:
		      			break;
		      			
		      		case 1:
		      			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.angelBlessing)) {
		      				AngelBlessing spellstoneAngelBlessing = (AngelBlessing) EnigmaticLegacy.angelBlessing;
		      				spellstoneAngelBlessing.triggerActiveAbility(playerServ.world, playerServ, SuperpositionHandler.getCurioStack(playerServ, EnigmaticLegacy.angelBlessing));
		      			}
		      			break;
		      		
		      		case 2:
		      			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.oceanStone)) {
		      				OceanStone spellstoneOceanStone = (OceanStone) EnigmaticLegacy.oceanStone;
		      				spellstoneOceanStone.triggerActiveAbility(playerServ.world, playerServ, SuperpositionHandler.getCurioStack(playerServ, EnigmaticLegacy.oceanStone));
		      			}
		      			break;
		      			
		      		case 3:
		      			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.magmaHeart)) {
		      				MagmaHeart spellstoneMagmaHeart = (MagmaHeart) EnigmaticLegacy.magmaHeart;
		      				spellstoneMagmaHeart.triggerActiveAbility(playerServ.world, playerServ, SuperpositionHandler.getCurioStack(playerServ, EnigmaticLegacy.magmaHeart));
		      			}
		      			break;
		      			
		      		case 4:
		      			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.golemHeart)) {
		      				GolemHeart spellstoneGolemHeart = (GolemHeart) EnigmaticLegacy.golemHeart;
		      				spellstoneGolemHeart.triggerActiveAbility(playerServ.world, playerServ, SuperpositionHandler.getCurioStack(playerServ, EnigmaticLegacy.golemHeart));
		      			}
		      			break;
		      			
		      		case 5:
		      			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.eyeOfNebula)) {
		      				EyeOfNebula spellstoneEyeOfNebula = (EyeOfNebula) EnigmaticLegacy.eyeOfNebula;
		      				spellstoneEyeOfNebula.triggerActiveAbility(playerServ.world, playerServ, SuperpositionHandler.getCurioStack(playerServ, EnigmaticLegacy.eyeOfNebula));
		      			}
		      			break;
		      	}
		      	
		      	
		      
		    });
		    ctx.get().setPacketHandled(true);
	  }

}
