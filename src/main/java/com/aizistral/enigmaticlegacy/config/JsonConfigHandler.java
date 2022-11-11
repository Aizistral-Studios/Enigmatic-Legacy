package com.aizistral.enigmaticlegacy.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraftforge.fml.loading.FMLPaths;

@SuppressWarnings("unchecked")
public class JsonConfigHandler {

	@Nullable
	public static File getConfigFile() {
		try {
			return new File(FMLPaths.CONFIGDIR.get().toFile().getCanonicalFile(), "config.json");
		} catch (Exception ex) {
			return null;
		}
	}

	public static int getInt(String key) {
		return (int)getFloat(key);
	}

	public static float getFloat(String key) {
		try {
			final InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(new FileInputStream(getConfigFile())), StandardCharsets.UTF_8);
			final GsonBuilder builder = new GsonBuilder();
			final Gson gson = builder.create();
			final HashMap<String, Double> panelsParameters = (HashMap<String, Double>)gson.fromJson((Reader)reader, (Class)HashMap.class);
			if (panelsParameters.containsKey(key))
				return (float)((double)panelsParameters.get(key));
			else
				return 0;
		}
		catch (Exception ex) {
			//ex.printStackTrace();
			return 0;
		}
	}

	public static String getString(String key) {
		try {
			final InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(new FileInputStream(getConfigFile())), StandardCharsets.UTF_8);
			final GsonBuilder builder = new GsonBuilder();
			final Gson gson = builder.create();
			final HashMap<String, String> panelsParameters = (HashMap<String, String>)gson.fromJson((Reader)reader, (Class)HashMap.class);
			if (panelsParameters.containsKey(key))
				return panelsParameters.get(key);
			else
				return "";
		}
		catch (Exception ex) {
			return "";
		}
	}

}
