package com.integral.enigmaticlegacy.helpers;

import net.minecraft.entity.player.PlayerEntity;

public class ExperienceHelper {

	public static int getPlayerXP(PlayerEntity player) {
		return (int)(getExperienceForLevel(player.experienceLevel) + player.experience * player.xpBarCap());
	}

	public static void drainPlayerXP(PlayerEntity player, int amount) {
		addPlayerXP(player, -amount);
	}

	public static void addPlayerXP(PlayerEntity player, int amount) {
		int experience = getPlayerXP(player) + amount;
		player.experienceTotal = experience;
		player.experienceLevel = getLevelForExperience(experience);
		int expForLevel = getExperienceForLevel(player.experienceLevel);
		player.experience = (float)(experience - expForLevel) / (float)player.xpBarCap();
	}

	public static int getExperienceForLevel(int level) {
		if (level == 0)
			return 0;

		if (level > 0 && level < 17)
		    return (int) (level * level + 6 * level);
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
