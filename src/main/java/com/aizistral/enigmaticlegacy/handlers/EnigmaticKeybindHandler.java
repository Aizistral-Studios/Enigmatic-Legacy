package com.aizistral.enigmaticlegacy.handlers;

import org.lwjgl.glfw.GLFW;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.aizistral.enigmaticlegacy.packets.server.PacketSpellstoneKey;
import com.aizistral.enigmaticlegacy.packets.server.PacketXPScrollKey;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

/**
 * Class for creating and handling keybinds on the client.
 * @author Integral
 */

public class EnigmaticKeybindHandler {
	@OnlyIn(Dist.CLIENT)
	public static boolean checkVariable;

	public KeyMapping enderRingKey;
	public KeyMapping spellstoneAbilityKey;
	public KeyMapping xpScrollKey;

	private boolean spaceDown = false;

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onKeyInput(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.START || !Minecraft.getInstance().isWindowActive() || Minecraft.getInstance().player == null)
			return;

		boolean spaceDown = Minecraft.getInstance().options.keyJump.isDown();
		boolean jumpClicked = false;

		if (this.spaceDown != spaceDown) {
			this.spaceDown = spaceDown;
			if (spaceDown) {
				jumpClicked = true;
			}
		}

		if (Minecraft.getInstance().player.isFallFlying()) {
			jumpClicked = Minecraft.getInstance().options.keyJump.isDown();
		}

		if (!OmniconfigHandler.angelBlessingDoubleJump.getValue()) {
			jumpClicked = false;
		}

		if (this.enderRingKey.consumeClick()) {
			if (Minecraft.getInstance().isWindowActive()) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketEnderRingKey(true));
			}
		}

		if (this.xpScrollKey.consumeClick()) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketXPScrollKey(true));
		}

		if (this.spellstoneAbilityKey.isDown() && SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticItems.ENIGMATIC_ITEM)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketSpellstoneKey(true));
		} else if (this.spellstoneAbilityKey.consumeClick() && SuperpositionHandler.hasSpellstone(Minecraft.getInstance().player)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketSpellstoneKey(true));
		} else if (jumpClicked) {
			LocalPlayer player = Minecraft.getInstance().player;

			if (!player.isInWater() && !player.onGround() && !player.isCreative() && !player.isSpectator()
					&& SuperpositionHandler.hasCurio(player, EnigmaticItems.ANGEL_BLESSING)) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketSpellstoneKey(true));
			}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onLivingJump(LivingJumpEvent event) {
		if (event.getEntity() instanceof LocalPlayer player) {
			//this.suppressNextJumpClick = true;
		}
	}

}
