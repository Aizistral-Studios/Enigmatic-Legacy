package com.integral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketUpdateExperience {
	private int experience;

	public PacketUpdateExperience(int xp) {
		this.experience = xp;
	}

	public static void encode(PacketUpdateExperience msg, PacketBuffer buf) {
		buf.writeInt(msg.experience);
	}

	public static PacketUpdateExperience decode(PacketBuffer buf) {
		return new PacketUpdateExperience(buf.readInt());
	}


	public static void handle(PacketUpdateExperience msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			PlayerEntity player = EnigmaticLegacy.proxy.getClientPlayer();

			int diff = msg.experience - ExperienceHelper.getPlayerXP(player);
			ExperienceHelper.addPlayerXP(player, diff);

		});

		ctx.get().setPacketHandled(true);
	}

}

