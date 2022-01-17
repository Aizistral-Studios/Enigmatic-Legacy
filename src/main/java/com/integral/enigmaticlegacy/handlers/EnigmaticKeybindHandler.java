package com.integral.enigmaticlegacy.handlers;

import org.lwjgl.glfw.GLFW;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.integral.enigmaticlegacy.packets.server.PacketSpellstoneKey;
import com.integral.enigmaticlegacy.packets.server.PacketXPScrollKey;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
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

	@OnlyIn(Dist.CLIENT)
	public void registerKeybinds() {
		this.enderRingKey = new KeyMapping("key.enderRing", GLFW.GLFW_KEY_I, "key.categories.enigmaticLegacy");
		this.spellstoneAbilityKey = new KeyMapping("key.spellstoneAbility", GLFW.GLFW_KEY_K, "key.categories.enigmaticLegacy");
		this.xpScrollKey = new KeyMapping("key.xpScroll", GLFW.GLFW_KEY_J, "key.categories.enigmaticLegacy");

		ClientRegistry.registerKeyBinding(this.enderRingKey);
		ClientRegistry.registerKeyBinding(this.spellstoneAbilityKey);
		ClientRegistry.registerKeyBinding(this.xpScrollKey);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onKeyInput(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END || !Minecraft.getInstance().isWindowActive())
			return;

		if (this.enderRingKey.consumeClick()) {
			if (Minecraft.getInstance().isWindowActive()) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketEnderRingKey(true));
			}
		}

		if (this.xpScrollKey.consumeClick()) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketXPScrollKey(true));
		}

		if (this.spellstoneAbilityKey.isDown() && SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.enigmaticItem)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketSpellstoneKey(true));
		} else if (this.spellstoneAbilityKey.consumeClick() && SuperpositionHandler.hasSpellstone(Minecraft.getInstance().player)) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketSpellstoneKey(true));
		}
	}

}
