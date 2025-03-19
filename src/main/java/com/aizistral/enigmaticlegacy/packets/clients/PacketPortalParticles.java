package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for spawning a bunch of portal particles around specific point.
 * @author Integral
 */

public class PacketPortalParticles {
	private double x;
	private double y;
	private double z;
	private int num;
	private double rangeModifier;
	private boolean check;

	public PacketPortalParticles(double x, double y, double z, int number, double rangeModifier, boolean checkSettings) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.num = number;
		this.rangeModifier = rangeModifier;
		this.check = checkSettings;
	}

	public static void encode(PacketPortalParticles msg, FriendlyByteBuf buf) {
		buf.writeDouble(msg.x);
		buf.writeDouble(msg.y);
		buf.writeDouble(msg.z);
		buf.writeInt(msg.num);
		buf.writeDouble(msg.rangeModifier);
		buf.writeBoolean(msg.check);
	}

	public static PacketPortalParticles decode(FriendlyByteBuf buf) {
		return new PacketPortalParticles(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readDouble(), buf.readBoolean());
	}


	public static void handle(PacketPortalParticles msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			LocalPlayer player = Minecraft.getInstance().player;

			int amount = msg.num;

			if (msg.check) {
				amount *= SuperpositionHandler.getParticleMultiplier();
			}

			for (int counter = 0; counter <= amount; counter++) {
				player.level().addParticle(ParticleTypes.PORTAL, true, msg.x, msg.y, msg.z, ((Math.random()-0.5D)*2.0D)*msg.rangeModifier, ((Math.random()-0.5D)*2.0D)*msg.rangeModifier, ((Math.random()-0.5D)*2.0D)*msg.rangeModifier);
			}


		});
		ctx.get().setPacketHandled(true);
	}

}
