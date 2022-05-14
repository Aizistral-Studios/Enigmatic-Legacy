package com.integral.enigmaticlegacy.client;

import java.util.Map;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Quote {
	public static final Quote NO_PERIL = new Quote("no_peril", new Subtitles(8.0,
			"Where there is no peril in the task - there can be no glory in its accomplishment."));
	public static final Quote I_WANDERED = new Quote("i_wandered", new Subtitles(
			11.5, "Yes, I wandered those lands many times...")
			.add(4.75, "...a desolate reminder of everything that was, and all that could have been."));

	private final SoundEvent sound;
	private final Subtitles subtitles;

	public Quote(String name, Subtitles subtitles) {
		this.sound = SuperpositionHandler.registerSound("quote." + name);
		this.subtitles = subtitles;
	}

	public void play() {
		QuoteHandler.INSTANCE.playQuote(this);
	}

	public SoundEvent getSound() {
		return this.sound;
	}

	public Subtitles getSubtitles() {
		return this.subtitles;
	}

}
