package com.integral.enigmaticlegacy.client;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Subtitles {
	private final Map<Double, String> map = new HashMap<>();
	private final double duration;

	public Subtitles(double duration, String firstLine) {
		this.map.put(0.0, firstLine);
		this.duration = duration;
	}

	public Subtitles add(double time, String line) {
		this.map.put(time, line);
		return this;
	}

	public double getDuration() {
		return this.duration;
	}

	public String getLine(double time) {
		String line = "ERROR";

		for (Entry<Double, String> entry : this.map.entrySet()) {
			if (entry.getKey() <= time) {
				line = entry.getValue();
			}
		}

		return line;
	}

}
