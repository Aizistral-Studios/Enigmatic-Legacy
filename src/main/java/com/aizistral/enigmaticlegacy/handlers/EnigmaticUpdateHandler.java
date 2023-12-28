package com.aizistral.enigmaticlegacy.handlers;

import java.net.URL;
import java.util.Scanner;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.packets.clients.PacketUpdateNotification;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

/**
 * Some code is still borrowed from Tainted Magic update handler.
 * @author Integral
 */

public class EnigmaticUpdateHandler {
	public static Omniconfig.BooleanParameter notificationsEnabled;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {

		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			// NO-OP
		} else {

			notificationsEnabled = builder
					.comment("Whether or not Enigmatic Legacy should show notification in chat when new mod update is available.")
					.getBoolean("UpdateHandlerEnabled", true);

		}
	}

	private static String currentVersion = EnigmaticLegacy.VERSION + " " + EnigmaticLegacy.RELEASE_TYPE;
	private static String newestVersion;
	public static MutableComponent updateStatus = null;
	public static boolean show = false;
	static boolean worked = false;

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketUpdateNotification());
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleShowup(LocalPlayer player) {
		if (!EnigmaticUpdateHandler.show)
			return;

		if (notificationsEnabled.getValue()) {
			player.sendSystemMessage(EnigmaticUpdateHandler.updateStatus);
		}

		EnigmaticUpdateHandler.show = false;

	}

	public static void init() {

		EnigmaticUpdateHandler.getNewestVersion();

		if (EnigmaticUpdateHandler.newestVersion != null)
		{
			if (EnigmaticUpdateHandler.newestVersion.equalsIgnoreCase(EnigmaticUpdateHandler.currentVersion))
			{
				EnigmaticUpdateHandler.show = false;
			}
			else if (!EnigmaticUpdateHandler.newestVersion.equalsIgnoreCase(EnigmaticUpdateHandler.currentVersion))
			{
				EnigmaticUpdateHandler.show = true;

				MutableComponent newVerArg = Component.literal(EnigmaticUpdateHandler.newestVersion);
				newVerArg.withStyle(ChatFormatting.GOLD);

				EnigmaticUpdateHandler.updateStatus = Component.translatable("status.enigmaticlegacy.outdated", newVerArg);
				EnigmaticUpdateHandler.updateStatus.withStyle(ChatFormatting.DARK_PURPLE);
			}
		}
		else
		{
			EnigmaticUpdateHandler.show = true;
			EnigmaticUpdateHandler.updateStatus = Component.translatable("status.enigmaticlegacy.noconnection");
			EnigmaticUpdateHandler.updateStatus.withStyle(ChatFormatting.RED);
		}
	}

	private static void getNewestVersion() {
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/Extegral/Enigmatic-Legacy/1.20.X/version.txt");
			Scanner s = new Scanner(url.openStream());
			EnigmaticUpdateHandler.newestVersion = s.nextLine();
			s.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}