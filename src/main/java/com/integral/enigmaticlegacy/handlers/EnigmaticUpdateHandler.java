package com.integral.enigmaticlegacy.handlers;

import java.net.URL;
import java.util.Scanner;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateNotification;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

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
	public static TranslationTextComponent updateStatus = null;
	public static boolean show = false;
	static boolean worked = false;

	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

		if (event.getPlayer() instanceof ServerPlayerEntity) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity)event.getPlayer())), new PacketUpdateNotification());
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void handleShowup(ClientPlayerEntity player) {
		if (!EnigmaticUpdateHandler.show)
			return;

		if (notificationsEnabled.getValue()) {
			player.sendMessage(EnigmaticUpdateHandler.updateStatus, player.getUniqueID());
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

				StringTextComponent newVerArg = new StringTextComponent(EnigmaticUpdateHandler.newestVersion);
				newVerArg.mergeStyle(TextFormatting.GOLD);

				EnigmaticUpdateHandler.updateStatus = new TranslationTextComponent("status.enigmaticlegacy.outdated", newVerArg);
				EnigmaticUpdateHandler.updateStatus.mergeStyle(TextFormatting.DARK_PURPLE);
			}
		}
		else
		{
			EnigmaticUpdateHandler.show = true;
			EnigmaticUpdateHandler.updateStatus = new TranslationTextComponent("status.enigmaticlegacy.noconnection");
			EnigmaticUpdateHandler.updateStatus.mergeStyle(TextFormatting.RED);
		}
	}

	private static void getNewestVersion() {
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/Extegral/Enigmatic-Legacy/1.16.X/version.txt");
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