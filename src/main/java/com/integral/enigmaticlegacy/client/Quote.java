package com.integral.enigmaticlegacy.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableList;
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
	private static final Random RANDOM = new Random();
	private static final List<Quote> ALL_QUOTES = new ArrayList<>();

	public static final Quote NO_PERIL = new Quote("no_peril", new Subtitles(8.0,
			"Where there is no peril in the task - there can be no glory in its accomplishment."));
	public static final Quote END_DOORSTEP = new Quote("end_doorstep", new Subtitles(6.0,
			"The doorstep of The End... single step now separates you from it."));
	public static final Quote ONLY_BECAUSE = new Quote("only_because", new Subtitles(6.0,
			"Only because you have fallen does not yet mean you backed down."));
	public static final Quote DEMISE_IS = new Quote("demise_is", new Subtitles(7.0,
			"Demise is but a temporary inconvenience for a demigod such as you."));
	public static final Quote WE_FALL = new Quote("we_fall", new Subtitles(6.5,
			"We fall, so that we may learn how to pick ourselves back up."));
	public static final Quote YOU_WILL_ENDURE = new Quote("you_will_endure", new Subtitles(5.5,
			"You will endure this loss, and learn from it."));
	public static final Quote OBLIVION_REJECTS = new Quote("oblivion_rejects", new Subtitles(9.0,
			"Oblivion rejects you... make use of such opportunity, while you still can."));
	public static final Quote SETBACK = new Quote("setback", new Subtitles(5.0,
			"A setback, but not a defeat."));
	public static final Quote DEATH_MAY = new Quote("death_may", new Subtitles(8.25,
			"Death may impede your conquest, but only broken will can halt it."));
	public static final Quote ETERNITY_TO_KEEP = new Quote("eternity_to_keep", new Subtitles(7.0,
			"You have eternity to keep trying. Do what it takes."));
	public static final Quote VIOLENCE_CALLS = new Quote("violence_calls", new Subtitles(7.0,
			"Violence calls for vengeance. Return, and dispense it!"));
	public static final Quote IMMORTAL = new Quote("immortal", new Subtitles(8.5,
			"You are immortal! Return and give that pitiful creature a demonstration of this fact."));

	public static final Quote I_WANDERED = new Quote("i_wandered", new Subtitles(
			11.5, "Yes, I wandered those lands many times...")
			.add(4.75, "A desolate reminder of everything that was, and all that could have been.")
			);
	public static final Quote ANOTHER_DEMIGOD = new Quote("another_demigod", new Subtitles(
			14.0, "Another demigod wanders this forsaken land...")
			.add(5.25, "Let me accompany you in your journey. Perhaps, you will manage that which others could not.")
			);
	public static final Quote ANOTHER_EON = new Quote("another_eon", new Subtitles(
			12.0, "Another eon, another wanderer. Another story to be told...")
			.add(7.5, "I will observe yours with great curiosity.")
			);
	public static final Quote PERHAPS_YOU = new Quote("perhaps_you", new Subtitles(
			13.0, "Perhaps, you will be the one to uncover the long-forgotten history of this land...")
			.add(7.5, "If so, I must bear witness to your accomplishments.")
			);
	public static final Quote SULFUR_AIR = new Quote("sulfur_air", new Subtitles(
			10.25, "Sulfur in the air, sharp scent of burnt rock and flesh...")
			.add(5.6, "This place is a Hell indeed.")
			);
	public static final Quote TORTURED_ROCKS = new Quote("tortured_rocks", new Subtitles(
			12.25, "Tortured rocks, pale as bones, hanged up above infinite, all-consuming blackness.")
			.add(7.5, "The End... is almost like a fever dream.")
			);

	public static final List<Quote> DEATH_QUOTES = ImmutableList.of(
			NO_PERIL, ONLY_BECAUSE, DEATH_MAY, DEMISE_IS, WE_FALL, YOU_WILL_ENDURE, OBLIVION_REJECTS, SETBACK);

	public static final List<Quote> DEATH_QUOTES_ENTITY = ImmutableList.of(
			NO_PERIL, ONLY_BECAUSE, DEATH_MAY, DEMISE_IS, WE_FALL, YOU_WILL_ENDURE, OBLIVION_REJECTS, SETBACK,
			ETERNITY_TO_KEEP, IMMORTAL, VIOLENCE_CALLS);

	public static final List<Quote> NARRATOR_INTROS = ImmutableList.of(ANOTHER_DEMIGOD, ANOTHER_EON, PERHAPS_YOU);

	private static Quote lastQuote = null;

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
		lastQuote = this;
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

	public static Quote getRandom(List<Quote> list) {
		Quote quote = null;

		do {
			quote = list.get(RANDOM.nextInt(list.size()));
		} while (quote == lastQuote);

		return quote;
	}

}
