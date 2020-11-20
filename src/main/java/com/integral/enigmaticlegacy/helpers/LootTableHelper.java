package com.integral.enigmaticlegacy.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class LootTableHelper {
	public static Field isFrozenTable = null;
	public static Field isFrozenPool = null;
	public static Field lootPoolsTable = null;

	static {
		try {
			isFrozenTable = LootTable.class.getDeclaredField("isFrozen");
			isFrozenPool = LootPool.class.getDeclaredField("isFrozen");
			lootPoolsTable = ObfuscationReflectionHelper.findField(LootTable.class, "field_186466_c");

			isFrozenTable.setAccessible(true);
			isFrozenPool.setAccessible(true);
		} catch (Exception ex) {
			EnigmaticLegacy.logger.fatal("FAILED TO REFLECT LOOTTABLE FIELDS");
			EnigmaticLegacy.logger.catching(ex);
			throw new RuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static void unfreezePlease(LootTable table) {
		try {
			isFrozenTable.set(table, false);
			List<LootPool> poolList = (List<LootPool>)lootPoolsTable.get(table);

			for (LootPool pool : poolList) {
				unfreezePlease(pool);
			}
		} catch (Exception ex) {
			EnigmaticLegacy.logger.fatal("FAILED TO UNFREEZE LOOT TABLE");
			throw new RuntimeException(ex);
		}
	}

	public static void unfreezePlease(LootPool pool) {
		try {
			isFrozenPool.set(pool, false);
		} catch (Exception ex) {
			EnigmaticLegacy.logger.fatal("FAILED TO UNFREEZE LOOT POOL");
			throw new RuntimeException(ex);
		}
	}

}
