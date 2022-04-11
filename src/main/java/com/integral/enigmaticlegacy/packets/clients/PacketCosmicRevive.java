package com.integral.enigmaticlegacy.packets.clients;

import java.util.Random;
import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.AstralBreaker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
			Player player = EnigmaticLegacy.proxy.getClientPlayer();
			Entity entity = player.level.getEntity(msg.entityID);

			if (entity != null) {
				Item item = msg.reviveType == 0 ? EnigmaticLegacy.cosmicScroll : EnigmaticLegacy.theCube;
				int i = 40;
				Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);

				int amount = 50;

				for (int counter = 0; counter <= amount; counter++) {
					player.level.addParticle(ParticleTypes.FLAME, true, entity.getX() + (Math.random() - 0.5), entity.getY() + (entity.getBbHeight()/2) + (Math.random() - 0.5), entity.getZ() + (Math.random() - 0.5), (Math.random() - 0.5D) * 0.2, (Math.random() - 0.5D) * 0.2, (Math.random() - 0.5D) * 0.2);
				}

				player.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);

				if (entity == player) {
					ItemStack stack = SuperpositionHandler.getCurioStack(player, item);

					if (stack == null) {
						stack = new ItemStack(item, 1);
					}

					Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
				}
			}
		});

		ctx.get().setPacketHandled(true);
	}

}
