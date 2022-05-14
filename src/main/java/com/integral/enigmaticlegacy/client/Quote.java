package com.integral.enigmaticlegacy.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayQuote;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;

public class Quote {
	private static final List<Quote> ALL_QUOTES = new ArrayList<>();

	public static final Quote NO_PERIL = new Quote("no_peril", new Subtitles(8.0,
			"Where there is no peril in the task - there can be no glory in its accomplishment."));
	public static final Quote I_WANDERED = new Quote("i_wandered", new Subtitles(
			11.5, "Yes, I wandered those lands many times...")
			.add(4.75, "...a desolate reminder of everything that was, and all that could have been."));

	private final SoundEvent sound;
	private final Subtitles subtitles;
	private final int id;

	public Quote(String name, Subtitles subtitles) {
		this.sound = SuperpositionHandler.registerSound("quote." + name);
		this.subtitles = subtitles;
		this.id = ALL_QUOTES.size();

		ALL_QUOTES.add(this);
	}

	public void playWithDelay(ServerPlayer player, int delayTicks) {
		EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPlayQuote(this, delayTicks));
	}

	public void play(ServerPlayer player) {
		this.playWithDelay(player, 1);
	}

	@OnlyIn(Dist.CLIENT)
	public void play() {
		this.playWithDelay(1);
	}

	@OnlyIn(Dist.CLIENT)
	public void playWithDelay(int delayTicks) {
		if (delayTicks < 1)
			throw new IllegalArgumentException("Delay cannot be less than 1 tick!");

		QuoteHandler.INSTANCE.playQuote(this, delayTicks);
	}

	public SoundEvent getSound() {
		return this.sound;
	}

	public Subtitles getSubtitles() {
		return this.subtitles;
	}

	public int getID() {
		return this.id;
	}

	public static Quote getByID(int id) {
		return ALL_QUOTES.get(id);
	}

}
