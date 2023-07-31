package com.aizistral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

public class PacketUpdateElytraBoosting {
	private boolean boosting;

	public PacketUpdateElytraBoosting(boolean boosting) {
		this.boosting = boosting;
	}

	public static void encode(PacketUpdateElytraBoosting msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.boosting);
	}

	public static PacketUpdateElytraBoosting decode(FriendlyByteBuf buf) {
		return new PacketUpdateElytraBoosting(buf.readBoolean());
	}

	public static void handle(PacketUpdateElytraBoosting msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();

			if (msg.boosting) {
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
			}

			TransientPlayerData data = TransientPlayerData.get(player);

			boolean wasBoosting = data.isElytraBoosting();
			data.setElytraBoosting(msg.boosting);

			if (wasBoosting != msg.boosting) {
				data.syncToAllClients();
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
