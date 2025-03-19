package com.aizistral.enigmaticlegacy.api.capabilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerPlaytimeCounter implements IPlaytimeCounter {
	public static final ResourceLocation TIME_WITH_CURSES_STAT = new ResourceLocation(EnigmaticLegacy.MODID, "play_time_with_seven_curses");
	public static final ResourceLocation TIME_WITHOUT_CURSES_STAT = new ResourceLocation(EnigmaticLegacy.MODID, "play_time_without_seven_curses");

	private final Player player;
	private long timeWithCurses, timeWithoutCurses;

	public PlayerPlaytimeCounter(Player player) {
		this.player = player;
	}

	@Override
	public long getTimeWithoutCurses() {
		return this.timeWithoutCurses;
	}

	@Override
	public long getTimeWithCurses() {
		return this.timeWithCurses;
	}

	@Override
	public void setTimeWithoutCurses(long time) {
		if (time != this.timeWithoutCurses) {
			this.updateStat(TIME_WITHOUT_CURSES_STAT, (int) time);
		}

		this.timeWithoutCurses = time;
	}

	@Override
	public void setTimeWithCurses(long time) {
		if (time != this.timeWithCurses) {
			this.updateStat(TIME_WITH_CURSES_STAT, (int) time);
		}

		this.timeWithCurses = time;
	}

	@Override
	public void incrementTimeWithoutCurses() {
		this.updateStat(TIME_WITHOUT_CURSES_STAT, (int) ++this.timeWithoutCurses);
	}

	@Override
	public void incrementTimeWithCurses() {
		this.updateStat(TIME_WITH_CURSES_STAT, (int) ++this.timeWithCurses);
	}

	@Override
	public void matchStats() {
		this.updateStat(TIME_WITH_CURSES_STAT, (int) this.timeWithCurses);
		this.updateStat(TIME_WITHOUT_CURSES_STAT, (int) this.timeWithoutCurses);
	}

	private void updateStat(ResourceLocation stat, int value) {
		if (this.player instanceof ServerPlayer player) {
			player.getStats().setValue(player, Stats.CUSTOM.get(stat), value);
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		var tag = new CompoundTag();
		tag.putLong("timeWithCurses", this.timeWithCurses);
		tag.putLong("timeWithoutCurses", this.timeWithoutCurses);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag tag) {
		this.timeWithCurses = tag.getLong("timeWithCurses");
		this.timeWithoutCurses = tag.getLong("timeWithoutCurses");
	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {
		private final LazyOptional<IPlaytimeCounter> counter;

		public Provider(Player player) {
			this(new PlayerPlaytimeCounter(player));
		}

		public Provider(IPlaytimeCounter counter) {
			this.counter = LazyOptional.of(() -> counter);
		}

		@Override
		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
			return EnigmaticCapabilities.PLAYTIME_COUNTER.orEmpty(capability, this.counter);
		}

		@Override
		public CompoundTag serializeNBT() {
			return this.counter.map(IPlaytimeCounter::serializeNBT).orElseGet(CompoundTag::new);
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			this.counter.ifPresent(counter -> counter.deserializeNBT(nbt));
		}
	}

}
