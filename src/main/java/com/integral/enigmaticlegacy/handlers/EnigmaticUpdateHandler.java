package com.integral.enigmaticlegacy.handlers;

import java.net.URL;
import java.util.Scanner;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateNotification;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * Some code is still borrowed from Tainted Magic update handler.
 * Hope that's not illegal.
 * @author Integral
 */

public class EnigmaticUpdateHandler {
	private static String currentVersion = EnigmaticLegacy.VERSION + " " + EnigmaticLegacy.RELEASE_TYPE;
	private static String newestVersion;
	public static TranslationTextComponent updateStatus = null;
	public static boolean show = false;
	static boolean worked = false;

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

		if (event.getPlayer() instanceof ServerPlayerEntity)
		EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity)event.getPlayer())), new PacketUpdateNotification());
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleShowup(ClientPlayerEntity player) {
		if (!EnigmaticUpdateHandler.show)
			return;

		player.sendMessage(EnigmaticUpdateHandler.updateStatus);
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

				StringTextComponent newVerArg = new StringTextComponent(EnigmaticUpdateHandler.newestVersion);
				newVerArg.applyTextStyle(TextFormatting.GOLD);

				EnigmaticUpdateHandler.updateStatus = new TranslationTextComponent("status.enigmaticlegacy.outdated", newVerArg.getFormattedText());
				EnigmaticUpdateHandler.updateStatus.applyTextStyle(TextFormatting.DARK_PURPLE);
			}
		}
		else
		{
			EnigmaticUpdateHandler.show = true;
			EnigmaticUpdateHandler.updateStatus = new TranslationTextComponent("status.enigmaticlegacy.noconnection");
			EnigmaticUpdateHandler.updateStatus.applyTextStyle(TextFormatting.RED);
		}
	}

	private static void getNewestVersion() {
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/Extegral/Enigmatic-Legacy/1.15.X/version.txt");
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