package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet for opening Ender Chest inventory to a player.
 * @author Integral
 */

public class PacketEnchantingGUI {

	public PacketEnchantingGUI() {
		// NO-OP
	}

	public static void encode(PacketEnchantingGUI msg, FriendlyByteBuf buf) {
		// NO-OP
	}

	public static PacketEnchantingGUI decode(FriendlyByteBuf buf) {
		return new PacketEnchantingGUI();
	}

	public static void handle(PacketEnchantingGUI msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			SuperpositionHandler.grantAdvancement(ctx.get().getSender(), new ResourceLocation(EnigmaticLegacy.MODID, "book/enchantments/generic"));
		});
		ctx.get().setPacketHandled(true);
	}

}
