package com.integral.enigmaticlegacy.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("unchecked")
public class JsonConfigHandler {

	public static float getFloat(String key) {
		try {
            final InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(new FileInputStream("C://Users/Vessel of Darkness/Desktop/config.json")), StandardCharsets.UTF_8);
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

}
