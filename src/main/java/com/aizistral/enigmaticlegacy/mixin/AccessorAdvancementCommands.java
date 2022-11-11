package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.AdvancementCommands;

@Mixin(AdvancementCommands.class)
public interface AccessorAdvancementCommands {

	@Accessor("SUGGEST_ADVANCEMENTS")
	public static SuggestionProvider<CommandSourceStack> getAdvancementSuggestions() {
		throw new IllegalStateException("Gone wrong, gone sexual");
	}

}
