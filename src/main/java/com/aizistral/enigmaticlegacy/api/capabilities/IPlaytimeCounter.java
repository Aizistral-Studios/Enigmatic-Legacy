package com.aizistral.enigmaticlegacy.api.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IPlaytimeCounter extends INBTSerializable<CompoundTag> {
	public static final IPlaytimeCounter DEFAULT = new IPlaytimeCounter() {
		@Override
		public void setTimeWithoutCurses(long time) {
			// NO-OP
		}

		@Override
		public void setTimeWithCurses(long time) {
			// NO-OP
		}

		@Override
		public void incrementTimeWithoutCurses() {
			// NO-OP
		}

		@Override
		public void incrementTimeWithCurses() {
			// NO-OP
		}

		@Override
		public long getTimeWithoutCurses() {
			return 0L;
		}

		@Override
		public long getTimeWithCurses() {
			return 1L;
		}

		@Override
		public void matchStats() {
			// NO-OP
		}

		@Override
		public CompoundTag serializeNBT() {
			return new CompoundTag();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			// NO-OP
		}
	};

	public static IPlaytimeCounter get(Player player) {
		return player.getCapability(EnigmaticCapabilities.PLAYTIME_COUNTER).orElse(DEFAULT);
	}

	public long getTimeWithoutCurses();

	public long getTimeWithCurses();

	public void setTimeWithoutCurses(long time);

	public void setTimeWithCurses(long time);

	public void incrementTimeWithoutCurses();

	public void incrementTimeWithCurses();

	public void matchStats();

	@Override
	public CompoundTag serializeNBT();

	@Override
	public void deserializeNBT(CompoundTag nbt);

}
