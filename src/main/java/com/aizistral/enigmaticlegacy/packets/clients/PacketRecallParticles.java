package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for bursting out a bunch of dragon breath particles.
 * @author Integral
 */

public class PacketRecallParticles {

	private double x;
	private double y;
	private double z;
	private int num;
	private boolean check;

	public PacketRecallParticles(double x, double y, double z, int number, boolean checkSettings) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.num = number;
		this.check = checkSettings;
	}

	public static void encode(PacketRecallParticles msg, FriendlyByteBuf buf) {
		buf.writeDouble(msg.x);
		buf.writeDouble(msg.y);
		buf.writeDouble(msg.z);
		buf.writeInt(msg.num);
		buf.writeBoolean(msg.check);
	}

	public static PacketRecallParticles decode(FriendlyByteBuf buf) {
		return new PacketRecallParticles(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readBoolean());
	}


	public static void handle(PacketRecallParticles msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			LocalPlayer player = Minecraft.getInstance().player;

			int amount = msg.num;

			if (msg.check) {
				amount *= SuperpositionHandler.getParticleMultiplier();
			}

			for (int counter = 0; counter <= amount; counter++) {
				player.level().addParticle(ParticleTypes.DRAGON_BREATH, true, msg.x, msg.y, msg.z, (Math.random()-0.5D)*0.2D, (Math.random()-0.5D)*0.2D, (Math.random()-0.5D)*0.2D);
			}


		});
		ctx.get().setPacketHandled(true);
	}

}
