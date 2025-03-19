package com.aizistral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
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

public class PacketEnderRingKey {

	private boolean pressed;

	public PacketEnderRingKey(boolean pressed) {
		this.pressed = pressed;
	}

	public static void encode(PacketEnderRingKey msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.pressed);
	}

	public static PacketEnderRingKey decode(FriendlyByteBuf buf) {
		return new PacketEnderRingKey(buf.readBoolean());
	}

	public static void handle(PacketEnderRingKey msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			ServerPlayer playerServ = ctx.get().getSender();

			//if (playerServ.openContainer.windowId == 0)
			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticItems.ENDER_RING) || SuperpositionHandler.hasCurio(playerServ, EnigmaticItems.CURSED_RING)) {
				//ChestContainer container = ChestContainer.createGeneric9X3(playerServ.currentWindowId+1, playerServ.inventory, playerServ.getInventoryEnderChest());
				ChestMenu container = new ChestMenu(MenuType.GENERIC_9x3, playerServ.containerCounter + 1, playerServ.getInventory(), playerServ.getEnderChestInventory(), 3) {
					@Override
					public void removed(Player playerIn) {
						super.removed(playerIn);

						if (!playerIn.level().isClientSide) {
							playerIn.level().playSound(null, playerServ.blockPosition(), SoundEvents.ENDER_CHEST_CLOSE, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						}
					}
				};

				playerServ.containerCounter = container.containerId;
				playerServ.connection.send(new ClientboundOpenScreenPacket(container.containerId, container.getType(), Component.translatable("container.enderchest")));
				playerServ.initMenu(container);
				playerServ.containerMenu = container;
				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerServ, playerServ.containerMenu));

				playerServ.level().playSound(null, playerServ.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

			}
		});
		ctx.get().setPacketHandled(true);
	}

}
