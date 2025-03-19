package com.aizistral.enigmaticlegacy.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Subtitles {
	private static final String PLACEHOLDER_LINE = "PLACEHOLDER_LINE";
	private final Map<Double, String> map = new HashMap<>();
	private final double duration;
	private Quote quote = null;
	private int placeholderCounter = 1;

	public Subtitles(double duration) {
		this(duration, PLACEHOLDER_LINE + 1);
		this.placeholderCounter++;
	}

	public Subtitles(double duration, String firstLine) {
		this.map.put(0.0, firstLine);
		this.duration = duration;
	}

	public Subtitles add(double time) {
		return this.add(time, PLACEHOLDER_LINE + (this.placeholderCounter++));
	}

	public Subtitles add(double time, String line) {
		this.map.put(time, line);
		return this;
	}

	public double getDuration() {
		return this.duration;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	@OnlyIn(Dist.CLIENT)
	public String getLine(double time) {
		String line = "ERROR";
		double bestTime = -1;

		for (Entry<Double, String> entry : this.map.entrySet()) {
			if (entry.getKey() <= time && entry.getKey() > bestTime) {
				line = entry.getValue();
				bestTime = entry.getKey();
			}
		}

		if (line.startsWith(PLACEHOLDER_LINE)) {
			String key = "quote." + this.quote.getName() + "_" + line.replace(PLACEHOLDER_LINE, "");
			return I18n.get(key);
		}

		return line;
	}

}
