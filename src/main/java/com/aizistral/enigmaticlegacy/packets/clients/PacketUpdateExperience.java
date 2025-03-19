package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.helpers.ExperienceHelper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateExperience {
	private int experience;

	public PacketUpdateExperience(int xp) {
		this.experience = xp;
	}

	public static void encode(PacketUpdateExperience msg, FriendlyByteBuf buf) {
		buf.writeInt(msg.experience);
	}

	public static PacketUpdateExperience decode(FriendlyByteBuf buf) {
		return new PacketUpdateExperience(buf.readInt());
	}


	public static void handle(PacketUpdateExperience msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			Player player = EnigmaticLegacy.PROXY.getClientPlayer();

			int diff = msg.experience - ExperienceHelper.getPlayerXP(player);
			ExperienceHelper.addPlayerXP(player, diff);

		});

		ctx.get().setPacketHandled(true);
	}

}

