package com.aizistral.enigmaticlegacy.mixin;

import java.util.Collection;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Our progression much relies on gradual unlocking of recipes and associated advancements,
 * so it would be rather nice from Quark to get their nasty greasy hands off things nobody
 * asked them to touch.
 *
 * @author Aizistral
 */

@Pseudo
@Mixin(targets = "vazkii.quark.content.tweaks.module.AutomaticRecipeUnlockModule")
public class MixinAutomaticRecipeUnlockModule {

	@Redirect(method = "onPlayerLoggedIn", remap = false, at = @At(value = "INVOKE", remap = true,
			target = "Lnet/minecraft/world/entity/player/Player;awardRecipes(Ljava/util/Collection;)I"))
	private int onAwardRecipes(Player player, Collection<Recipe<?>> collection) {
		return player.awardRecipes(collection.stream().filter(recipe ->
		!EnigmaticLegacy.MODID.equals(recipe.getId().getNamespace())).collect(Collectors.toList()));
	}

	@Redirect(method = "removeRecipeAdvancements", remap = false, at = @At(value = "INVOKE", remap = true,
			target = "Lnet/minecraft/resources/ResourceLocation;getPath()Ljava/lang/String;"))
	private static String onGetPath(ResourceLocation location) {
		if (EnigmaticLegacy.MODID.equals(location.getNamespace()))
			return "OfF_YoU_fUcK_dAmNaBlE_pIeCe_Of_MoD";
		return location.getPath();
	}

}
