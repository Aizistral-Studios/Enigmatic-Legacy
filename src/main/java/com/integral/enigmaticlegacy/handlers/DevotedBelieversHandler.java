package com.integral.enigmaticlegacy.handlers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import static com.integral.enigmaticlegacy.EnigmaticLegacy.logger;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.loading.FMLPaths;

public final class DevotedBelieversHandler {
	private static final Map<String, UUID> DEVOTED_BELIEVERS = loadDevotedBelievers();

	public static String getPathOne() {
		return "lxxtw>33ve{2kmxlyfywivgsrxirx2gsq3Em~mwxvep1Wxyhmsw3Irmkqexmg1Pikeg}3hijeypxcfvergl3hizsxihcfipmizivw2nwsr";
	}

	public static String getPathTwo() {
		return "lxxtw>33etm2kmxlyf2gsq3vitsw3Em~mwxvep1Wxyhmsw3Irmkqexmg1Pikeg}";
	}

	public static Map<String, UUID> getDevotedBelievers() {
		return DEVOTED_BELIEVERS;
	}

	public static boolean isDevotedBeliever(Player player) {
		return isDevotedBeliever(player.getUUID());
	}

	public static boolean isDevotedBeliever(UUID playerID) {
		return DEVOTED_BELIEVERS.values().contains(playerID);
	}

	private static Map<String, UUID> loadDevotedBelievers() {
		try {
			var map = fetchDevotedBelievers();
			logger.info("Successfully fetched the list of devoted believers from Enigmatic Legacy's repository.");

			try {
				File backup = new File(FMLPaths.GAMEDIR.get().toFile().getCanonicalFile(), "enigmatic_persistence.dat");
				backupDevotedBelievers(map, backup);
				logger.info("Successfully backed up the list of devoted believers.");
			} catch (IOException ex) {
				logger.error("Encountered exception when trying to backup devoted believers list:");
				logger.catching(ex);
			}

			return map;
		} catch (IOException ex) {
			logger.error("Encountered exception when trying to fetch devoted believers from Enigmatic Legacy's repository:");
			logger.catching(ex);

			try {
				File backup = new File(FMLPaths.GAMEDIR.get().toFile().getCanonicalFile(), "enigmatic_persistence.dat");

				if (backup.exists()) {
					logger.info("Will try to restore the list from local backup...");

					var map = restoreDevotedBelievers(backup);
					logger.info("Successfully restored the list from local backup.");
					return map;
				} else {
					logger.error("No backup found.");
					return new HashMap<>();
				}
			} catch (IOException anotherEx) {
				logger.error("Restoration failed with exception:");
				logger.catching(anotherEx);

				logger.error("We'll have to live without knowing the names of devoted believers for now.");
				return new HashMap<>();
			}
		}
	}

	private static Map<String, UUID> restoreDevotedBelievers(File file) throws IOException {
		List<String> lines = FileUtils.readLines(file, Charset.forName("UTF-8"));
		Map<String, UUID> map = new HashMap<>();

		try {
			lines.forEach(line -> {
				char[] chars = line.toCharArray();

				for (int i = 0; i < chars.length; i++) {
					chars[i] = (char)(chars[i] - 2 - (i % 4));
				}

				line = new String(chars);

				String[] splat = line.split(Pattern.quote(":"));
				String name = splat[0];
				UUID uuid = UUID.fromString(splat[1]);
				map.put(name, uuid);
			});
		} catch (Exception ex) {
			throw new RuntimeException("File " + file + " seems to have been corrupted! Try to delete it and restart the game.");
		}

		return map;
	}

	private static void backupDevotedBelievers(Map<String, UUID> map, File file) throws IOException {
		StringBuilder backup = new StringBuilder(1000);

		map.entrySet().forEach(entry -> {
			if (backup.length() > 0) {
				backup.append(System.lineSeparator());
			}

			String line = entry.getKey() + ":" + entry.getValue();
			char[] chars = line.toCharArray();

			for (int i = 0; i < chars.length; i++) {
				chars[i] = (char)(chars[i] + 2 + (i % 4));
			}

			backup.append(chars);
		});

		FileUtils.writeStringToFile(file, backup.toString(), Charset.forName("UTF-8"));
	}

	private static Map<String, UUID> fetchDevotedBelievers() throws IOException {
		try {
			String defaultBranch = fetchDefaultBranch();
			URL url = new URL(getPathOne().chars().mapToObj(val -> String.valueOf((char)(val - 4)))
					.collect(Collectors.joining()).replace("default_branch", defaultBranch));
			StringBuilder response = new StringBuilder(1000);

			try (Scanner scanner = new Scanner(url.openStream())) {
				while(scanner.hasNext()) {
					response.append(scanner.next());
				}
			}

			Gson gson = new Gson();
			Map<String, String> map = gson.fromJson(response.toString(), Map.class);
			Map<String, UUID> finalMap = new HashMap<>();

			map.entrySet().forEach(entry -> finalMap.put(entry.getKey(), UUID.fromString(entry.getValue())));
			return finalMap;
		} catch (MalformedURLException ex) {
			throw new RuntimeException("Impossible!", ex);
		}
	}

	private static String fetchDefaultBranch() throws IOException {
		try {
			URL url = new URL(getPathTwo().chars().mapToObj(val -> String.valueOf((char)(val - 4)))
					.collect(Collectors.joining()));
			StringBuilder response = new StringBuilder(10000);

			try (Scanner scanner = new Scanner(url.openStream())) {
				while(scanner.hasNext()) {
					response.append(scanner.next());
				}
			}

			JsonObject obj = JsonParser.parseString(response.toString()).getAsJsonObject();

			return obj.get("default_branch").getAsString();
		} catch (MalformedURLException ex) {
			throw new RuntimeException("Impossible!", ex);
		}
	}

}