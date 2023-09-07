package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.helpers.LootTableHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.LootTableLoadEvent;

/**
 * No dear Forge, if I said my loot is added to tables - my loot WILL BE ADDED TO TABLES.
 * @author Integral
 */

@Mixin(value = ForgeHooks.class, remap = false)
public class MixinForgeHooks {

	@Inject(at = @At("RETURN"), method = "loadLootTable", cancellable = true, remap = false)
	private static void onLoadLootTable(Gson gson, ResourceLocation name, JsonElement data, boolean custom, CallbackInfoReturnable<LootTable> info) {
		LootTable returnedTable = info.getReturnValue();

		if (custom && returnedTable != null) {
			EnigmaticLegacy.LOGGER.debug("Caught custom LootTable loading: " + name);

			try {
				EnigmaticLegacy.LOGGER.debug("Unfreezing " + name + "...");
				LootTableHelper.unfreezePlease(returnedTable);
			} catch (Exception ex) {
				EnigmaticLegacy.LOGGER.fatal("FAILED TO PROCESS LOOT TABLE: " + name);
				throw new RuntimeException(ex);
			}

			EnigmaticLegacy.LOGGER.debug("Force dispatching LootTableLoadEvent for " + name + "...");

			LootTableLoadEvent event = new LootTableLoadEvent(name, returnedTable);
			EnigmaticLegacy.enigmaticHandler.onLootTablesLoaded(event);

			if (event.isCanceled()) {
				returnedTable = LootTable.EMPTY;
			}

			EnigmaticLegacy.LOGGER.debug("Freezing " + name + " back...");
			returnedTable.freeze();

			EnigmaticLegacy.LOGGER.debug("Returning " + name + " to Forge handler...");
			info.setReturnValue(returnedTable);
		}
	}


}
