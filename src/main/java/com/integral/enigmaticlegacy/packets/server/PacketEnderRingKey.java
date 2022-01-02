package com.integral.enigmaticlegacy.packets.server;

import java.util.function.Supplier;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.world.inventory.container.ChestContainer;
import net.minecraft.world.inventory.container.MenuType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SOpenWindowPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet for opening Ender Chest inventory to a player.
 * @author Integral
 */

public class PacketEnderRingKey {

	private boolean pressed;

	public PacketEnderRingKey(boolean pressed) {
		this.pressed = pressed;
	}

	public static void encode(PacketEnderRingKey msg, PacketBuffer buf) {
		buf.writeBoolean(msg.pressed);
	}

	public static PacketEnderRingKey decode(PacketBuffer buf) {
		return new PacketEnderRingKey(buf.readBoolean());
	}

	public static void handle(PacketEnderRingKey msg, Supplier<NetworkEvent.Context> ctx) {

		ctx.get().enqueueWork(() -> {
			ServerPlayer playerServ = ctx.get().getSender();

			//if (playerServ.openContainer.windowId == 0)
			if (SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.enderRing) || SuperpositionHandler.hasCurio(playerServ, EnigmaticLegacy.cursedRing)) {
				//ChestContainer container = ChestContainer.createGeneric9X3(playerServ.currentWindowId+1, playerServ.inventory, playerServ.getInventoryEnderChest());
				ChestContainer container = new ChestContainer(MenuType.GENERIC_9x3, playerServ.containerCounter + 1, playerServ.inventory, playerServ.getEnderChestInventory(), 3) {
					@Override
					public void removed(Player playerIn) {
						super.removed(playerIn);

						if (!playerIn.level.isClientSide) {
							playerIn.level.playSound(null, playerServ.blockPosition(), SoundEvents.ENDER_CHEST_CLOSE, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						}
					}
				};

				playerServ.containerCounter = container.containerId;
				playerServ.connection.send(new SOpenWindowPacket(container.containerId, container.getType(), new TranslationTextComponent("container.enderchest")));
				container.addSlotListener(playerServ);
				playerServ.containerMenu = container;
				net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerServ, playerServ.containerMenu));

				playerServ.level.playSound(null, playerServ.blockPosition(), SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

			}
		});
		ctx.get().setPacketHandled(true);
	}

}
