package com.aizistral.enigmaticlegacy.packets.clients;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.objects.Vector3;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PacketGenericParticleEffect {

	public enum Effect {
		NONE, GUARDIAN_CURSE;
	}

	private Vector3 pos;
	private int num;
	private boolean check;
	private Effect effect;

	public PacketGenericParticleEffect(double x, double y, double z, int number, boolean checkSettings, Effect effect) {
		this.pos = new Vector3(x, y, z);
		this.num = number;
		this.check = checkSettings;
		this.effect = effect;
	}

	public static void encode(PacketGenericParticleEffect msg, FriendlyByteBuf buf) {
		buf.writeDouble(msg.pos.x);
		buf.writeDouble(msg.pos.y);
		buf.writeDouble(msg.pos.z);
		buf.writeInt(msg.num);
		buf.writeBoolean(msg.check);
		buf.writeUtf(msg.effect.toString(), 512);
	}

	public static PacketGenericParticleEffect decode(FriendlyByteBuf buf) {


		return new PacketGenericParticleEffect(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readBoolean(), evaluateEffect(buf));
	}

	private static Effect evaluateEffect(FriendlyByteBuf buf) {
		Effect effect;

		try {
			effect = Effect.valueOf(buf.readUtf(512));
		} catch (Exception ex) {
			ex.printStackTrace();
			effect = Effect.NONE;
		}

		return effect;
	}


	public static void handle(PacketGenericParticleEffect msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			Player player = EnigmaticLegacy.PROXY.getClientPlayer();
			Vector3 pos = msg.pos;

			int amount = msg.num;

			if (msg.check) {
				amount *= SuperpositionHandler.getParticleMultiplier();
			}

			if (msg.effect == Effect.GUARDIAN_CURSE) {
				double dist = 0.05;
				double distHearts = 0.5;

				for (int counter = 0; counter < 4; counter++) {
					player.level().addParticle(ParticleTypes.ANGRY_VILLAGER, true, pos.x+SuperpositionHandler.getRandomNegative()*distHearts, pos.y+SuperpositionHandler.getRandomNegative()*distHearts, pos.z+SuperpositionHandler.getRandomNegative()*distHearts, SuperpositionHandler.getRandomNegative() * 1.05, SuperpositionHandler.getRandomNegative() * 1.05, SuperpositionHandler.getRandomNegative() * 1.05);
				}

				for (int counter = 0; counter < 12; counter++) {
					player.level().addParticle(ParticleTypes.LARGE_SMOKE, true, pos.x, pos.y, pos.z, SuperpositionHandler.getRandomNegative() * dist, SuperpositionHandler.getRandomNegative() * dist, SuperpositionHandler.getRandomNegative() * dist);
				}

			}

		});
		ctx.get().setPacketHandled(true);
	}

}
