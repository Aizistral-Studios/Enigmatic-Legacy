package com.aizistral.enigmaticlegacy.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPlayQuote;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class Quote {
	private static final Random RANDOM = new Random();
	private static final List<Quote> ALL_QUOTES = new ArrayList<>();

	public static final Quote NO_PERIL = new Quote("no_peril").addSubtitles(new Subtitles(8.0));
	public static final Quote END_DOORSTEP = new Quote("end_doorstep").addSubtitles(new Subtitles(6.0));
	public static final Quote ONLY_BECAUSE = new Quote("only_because").addSubtitles(new Subtitles(6.0));
	public static final Quote DEMISE_IS = new Quote("demise_is").addSubtitles(new Subtitles(7.0));
	public static final Quote WE_FALL = new Quote("we_fall").addSubtitles(new Subtitles(6.5));
	public static final Quote YOU_WILL_ENDURE = new Quote("you_will_endure").addSubtitles(new Subtitles(5.5));
	public static final Quote OBLIVION_REJECTS = new Quote("oblivion_rejects").addSubtitles(new Subtitles(9.0));
	public static final Quote SETBACK = new Quote("setback").addSubtitles(new Subtitles(5.0));
	public static final Quote DEATH_MAY = new Quote("death_may").addSubtitles(new Subtitles(8.25));
	public static final Quote ETERNITY_TO_KEEP = new Quote("eternity_to_keep").addSubtitles(new Subtitles(7.0));
	public static final Quote VIOLENCE_CALLS = new Quote("violence_calls").addSubtitles(new Subtitles(7.0));
	public static final Quote IMMORTAL = new Quote("immortal").addSubtitles(new Subtitles(8.5));
	public static final Quote APPALING_PRESENCE = new Quote("appaling_presence").addSubtitles(new Subtitles(9.5));
	public static final Quote ITS_DESTRUCTION = new Quote("its_destruction").addSubtitles(new Subtitles(9.0));

	public static final Quote I_WANDERED = new Quote("i_wandered").addSubtitles(new Subtitles(11.5).add(4.75));
	public static final Quote ANOTHER_DEMIGOD = new Quote("another_demigod").addSubtitles(new Subtitles(14.0).add(5.25));
	public static final Quote ANOTHER_EON = new Quote("another_eon").addSubtitles(new Subtitles(12.0).add(7.5));
	public static final Quote PERHAPS_YOU = new Quote("perhaps_you").addSubtitles(new Subtitles(13.0).add(7.5));
	public static final Quote SULFUR_AIR = new Quote("sulfur_air").addSubtitles(new Subtitles(10.25).add(5.6));
	public static final Quote TORTURED_ROCKS = new Quote("tortured_rocks").addSubtitles(new Subtitles(12.25).add(7.5));
	public static final Quote BREATHES_RELIEVED = new Quote("breathes_relieved").addSubtitles(new Subtitles(11.5).add(7.8));
	public static final Quote WHETHER_IT_IS = new Quote("whether_it_is").addSubtitles(new Subtitles(12.5).add(7.6));
	public static final Quote POOR_CREATURE = new Quote("poor_creature").addSubtitles(new Subtitles(15.0).add(7.9));
	public static final Quote HORRIBLE_EXISTENCE = new Quote("horrible_existence").addSubtitles(new Subtitles(12.0).add(7.6));
	public static final Quote COUNTLESS_DEAD = new Quote("countless_dead").addSubtitles(new Subtitles(16.0).add(8.8));
	public static final Quote WITH_DRAGONS = new Quote("with_dragons").addSubtitles(new Subtitles(32.0).add(9.7).add(14.0).add(21.2));
	public static final Quote TERRIFYING_FORM = new Quote("terrifying_form").addSubtitles(new Subtitles(14.5).add(6.2));
	public static final Quote TOLL_PAID = new Quote("toll_paid").addSubtitles(new Subtitles(11.5).add(7.4));

	public static final List<Quote> DEATH_QUOTES = ImmutableList.of(
			NO_PERIL, ONLY_BECAUSE, DEATH_MAY, DEMISE_IS, WE_FALL, YOU_WILL_ENDURE, OBLIVION_REJECTS, SETBACK);

	public static final List<Quote> DEATH_QUOTES_ENTITY = ImmutableList.of(
			NO_PERIL, ONLY_BECAUSE, DEATH_MAY, DEMISE_IS, WE_FALL, YOU_WILL_ENDURE, OBLIVION_REJECTS, SETBACK,
			ETERNITY_TO_KEEP, IMMORTAL, VIOLENCE_CALLS);

	public static final List<Quote> NARRATOR_INTROS = ImmutableList.of(ANOTHER_DEMIGOD, ANOTHER_EON, PERHAPS_YOU);
	public static final List<Quote> RING_DESTRUCTION = ImmutableList.of(TOLL_PAID, ITS_DESTRUCTION);

	private static Quote lastQuote = null;

	private final SoundEvent sound;
	private final String name;
	private Subtitles subtitles;
	private final int id;

	public Quote(String name) {
		this.name = name;
		this.sound = SuperpositionHandler.registerSound("quote." + name);
		this.id = ALL_QUOTES.size();

		ALL_QUOTES.add(this);
	}

	public Quote addSubtitles(Subtitles subtitles) {
		Preconditions.checkArgument(this.subtitles == null, "Subtitles already added!");
		subtitles.setQuote(this);
		this.subtitles = subtitles;
		return this;
	}

	public void playOnceIfUnlocked(ServerPlayer player, int delayTicks) {
		if (TransientPlayerData.get(player).getUnlockedNarrator()) {
			if (!SuperpositionHandler.hasPersistentTag(player, "ELHeardQuote:" + this.name)) {
				SuperpositionHandler.setPersistentBoolean(player, "ELHeardQuote:" + this.name, true);
				this.play(player, delayTicks);
			}
		}
	}

	public void playOnceIfUnlocked(ServerPlayer player) {
		this.playOnceIfUnlocked(player, 1);
	}

	public void playIfUnlocked(ServerPlayer player) {
		if (TransientPlayerData.get(player).getUnlockedNarrator()) {
			this.play(player);
		}
	}

	public void playIfUnlocked(ServerPlayer player, int delayTicks) {
		if (TransientPlayerData.get(player).getUnlockedNarrator()) {
			this.play(player, delayTicks);
		}
	}

	public void play(ServerPlayer player, int delayTicks) {
		EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPlayQuote(this, delayTicks));
		lastQuote = this;
	}

	public void play(ServerPlayer player) {
		this.play(player, 1);
	}

	@OnlyIn(Dist.CLIENT)
	public void play() {
		this.play(1);
	}

	@OnlyIn(Dist.CLIENT)
	public void play(int delayTicks) {
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

	public String getName() {
		return this.name;
	}

	public static Quote getByID(int id) {
		return ALL_QUOTES.get(id);
	}

	public static List<Quote> getAllQuotes() {
		return Collections.unmodifiableList(ALL_QUOTES);
	}

	public static Quote getRandom(List<Quote> list) {
		Quote quote = null;

		do {
			quote = list.get(RANDOM.nextInt(list.size()));
		} while (quote == lastQuote);

		return quote;
	}

}
