package com.integral.enigmaticlegacy.helpers;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.packets.clients.PacketUpdateExperience;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * A couple of methods for messing around with player's experience.
 * Originated from Botania's code.
 * @author Integral
 */

public class ExperienceHelper {

	public static int getPlayerXP(PlayerEntity player) {
		return (int) (getExperienceForLevel(player.experienceLevel) + player.experienceProgress * player.getXpNeededForNextLevel());
	}

	public static int getPlayerXPLevel(PlayerEntity player) {
		return getLevelForExperience(getPlayerXP(player));
	}

	public static void drainPlayerXP(PlayerEntity player, int amount) {
		addPlayerXP(player, -amount);
	}

	public static void addPlayerXP(PlayerEntity player, int amount) {
		// TODO Post events again when we are ready to fully acknowledge their alterable nature

		PlayerXpEvent.XpChange eventXP = new PlayerXpEvent.XpChange(player, amount);
		//if (MinecraftForge.EVENT_BUS.post(eventXP))
		//	return;

		//System.out.println("Former: " + amount + ", new: " + eventXP.getAmount());
		amount = eventXP.getAmount();

		int oldLevel = getPlayerXPLevel(player);
		int newLevel = getLevelForExperience(getPlayerXP(player) + amount);
		int experience = getPlayerXP(player) + amount;

		if (oldLevel != newLevel) {
			PlayerXpEvent.LevelChange eventLvl = new PlayerXpEvent.LevelChange(player, newLevel - oldLevel);
			int remainder = experience - getExperienceForLevel(newLevel);

			//if (MinecraftForge.EVENT_BUS.post(eventLvl))
			//	return;

			newLevel = oldLevel + eventLvl.getLevels();
			amount = getExperienceForLevel(newLevel) - getExperienceForLevel(oldLevel) + remainder;
		}

		player.totalExperience = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experienceProgress = (float) (experience - expForLevel) / (float) player.getXpNeededForNextLevel();
	}

	public static int getExperienceForLevel(int level) {
		if (level == 0)
			return 0;

		if (level > 0 && level < 17)
			return (level * level + 6 * level);
		else if (level > 16 && level < 32)
			return (int) (2.5 * level * level - 40.5 * level + 360);
		else
			return (int) (4.5 * level * level - 162.5 * level + 2220);
	}

	public static int getLevelForExperience(int experience) {
		int i = 0;
		while (getExperienceForLevel(i) <= experience) {
			i++;
		}
		return i - 1;
	}

}
