package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.crafting.*;
import com.aizistral.enigmaticlegacy.objects.EnabledCondition;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EnigmaticRecipes {

	private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
			DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, EnigmaticLegacy.MODID);

	public static final RegistryObject<RecipeSerializer<MendingMixtureRepairRecipe>> MENDING_MIXTURE_REPAIR = register("mending_mixture_repair", () -> MendingMixtureRepairRecipe.SERIALIZER);
	public static final RegistryObject<RecipeSerializer<EnchantmentTransposingRecipe>> ENCHANTMENT_TRANSPOSING = register("enchantment_transposing", () -> EnchantmentTransposingRecipe.SERIALIZER);
	public static final RegistryObject<RecipeSerializer<OblivionStoneCombineRecipe>> OBLIVION_STONE_COMBINE = register("void_stone_combine", () -> OblivionStoneCombineRecipe.SERIALIZER);
	public static final RegistryObject<RecipeSerializer<BindToPlayerRecipe>> BIND_TO_PLAYER = register("bind_to_player", () -> BindToPlayerRecipe.SERIALIZER);
	public static final RegistryObject<RecipeSerializer<HiddenRecipe>> HIDDEN_SHAPED = register("crafting_shaped_hidden", () -> HiddenRecipe.SERIALIZER);
	public static final RegistryObject<RecipeSerializer<ShapelessRecipe>> SHAPELESS_NO_RETURN = register("shapeless_no_return_craft", ShapelessNoReturnRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<CursedShapedRecipe>> CURSED_SHAPED = register("crafting_shaped_cursed", CursedShapedRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<BlessedShapedRecipe>> BLESSED_SHAPED = register("crafting_shaped_blessed", BlessedShapedRecipe.Serializer::new);

    public static void registerRecipes(IEventBus modEventBus) {
		RECIPE_SERIALIZERS.register(modEventBus);
		CraftingHelper.register(EnabledCondition.Serializer.INSTANCE);
	}

	private static <T extends CraftingRecipe> RegistryObject<RecipeSerializer<T>> register(
			String name, Supplier<RecipeSerializer<T>> recipe
	) {
		return RECIPE_SERIALIZERS.register(name, recipe);
	}
}
