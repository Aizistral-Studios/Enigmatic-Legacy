package com.aizistral.omniconfig.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketSyncOptions {
	private Map<String, String> synchronizedParameters = new HashMap<>();
	private String fileName = "";
	private String configVersion = "";

	private PacketSyncOptions(String fileName, String configVersion, Map<String, String> params) {
		this.fileName = fileName;
		this.configVersion = configVersion;
		this.synchronizedParameters = params;
	}

	public PacketSyncOptions(OmniconfigWrapper wrapper) {
		this.fileName = wrapper.config.getConfigFile().getName();
		this.configVersion = wrapper.config.getLoadedConfigVersion();

		for (Omniconfig.GenericParameter param : wrapper.retrieveInvocationList()) {
			if (param.isSynchronized()) {
				this.synchronizedParameters.put(param.getId(), param.valueToString());
			}
		}
	}

	public static void encode(PacketSyncOptions msg, FriendlyByteBuf buf) {
		buf.writeUtf(msg.fileName, 512);
		buf.writeUtf(String.valueOf(msg.configVersion), 512);

		buf.writeLong(msg.synchronizedParameters.size());

		for (String paramName : msg.synchronizedParameters.keySet()) {
			buf.writeUtf(paramName, 512);
			buf.writeUtf(msg.synchronizedParameters.get(paramName), 32768);
		}
	}

	public static PacketSyncOptions decode(FriendlyByteBuf buf) {
		String fileName = buf.readUtf(512);
		String configVersion = buf.readUtf(512);
		long entryAmount = buf.readLong();

		Map<String, String> params = new HashMap<>();

		for (int counter = 0; counter < entryAmount; counter++) {
			String identifier = buf.readUtf(512);
			String value = buf.readUtf(32768);

			params.put(identifier, value);
		}

		return new PacketSyncOptions(fileName, configVersion, params);
	}

	public static void handle(PacketSyncOptions msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {

			/*
			 * Receiving this packet from server explicitly signifies that we have logged
			 * in to either a dedicated server, or integrated server that we do not host
			 * ourselves, so let's keep track of it.
			 */

			OmniconfigWrapper.onRemoteServer = true;

			OmniconfigWrapper wrapper = OmniconfigWrapper.wrapperRegistry.get(msg.fileName);

			if (wrapper != null) {

				EnigmaticLegacy.LOGGER.info("Synchronizing values of " + msg.fileName + " with ones dispatched by server...");

				for (String id : msg.synchronizedParameters.keySet()) {
					Omniconfig.GenericParameter parameter = wrapper.invokationMap.get(id);

					if (parameter != null) {
						String oldValue = parameter.valueToString();
						parameter.parseFromString(msg.synchronizedParameters.get(id));

						EnigmaticLegacy.LOGGER.info("Value of '" + parameter.getId() + "' was set to '" + parameter.valueToString() + "'; old value: " + oldValue);
					} else {
						EnigmaticLegacy.LOGGER.error("Value '" + id + "' does not exist in " + msg.fileName + "! Skipping.");
					}
				}
			} else {
				EnigmaticLegacy.LOGGER.fatal("Received synchronization packet for non-existent config file: " + msg.fileName);
			}

		});
		ctx.get().setPacketHandled(true);
	}

}
