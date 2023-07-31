package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketCosmicRevive {
	private int entityID;
	private int reviveType;

	public PacketCosmicRevive(int entityID, int reviveType) {
		this.entityID = entityID;
		this.reviveType = reviveType;
	}

	public static void encode(PacketCosmicRevive msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.entityID);
		buf.writeInt(msg.reviveType);
	}

	public static PacketCosmicRevive decode(FriendlyByteBuf buf) {
		return new PacketCosmicRevive(buf.readInt(), buf.readInt());
	}

	public static void handle(PacketCosmicRevive msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (msg.reviveType == 0) {
				EnigmaticLegacy.PROXY.displayReviveAnimation(msg.entityID, msg.reviveType);
			} else {
				Player player = EnigmaticLegacy.PROXY.getClientPlayer();
				Entity entity = player.level().getEntity(msg.entityID);

				if (entity == player) {
					EnigmaticEventHandler.scheduledCubeRevive = 5;
				} else {
					EnigmaticLegacy.PROXY.displayReviveAnimation(msg.entityID, msg.reviveType);
				}
			}
		});

		ctx.get().setPacketHandled(true);
	}

}
