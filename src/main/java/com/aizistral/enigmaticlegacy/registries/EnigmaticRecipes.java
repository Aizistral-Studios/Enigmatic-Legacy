package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.crafting.BindToPlayerRecipe;
import com.aizistral.enigmaticlegacy.crafting.BlessedShapedRecipe;
import com.aizistral.enigmaticlegacy.crafting.CursedShapedRecipe;
import com.aizistral.enigmaticlegacy.crafting.EnchantmentTransposingRecipe;
import com.aizistral.enigmaticlegacy.crafting.HiddenRecipe;
import com.aizistral.enigmaticlegacy.crafting.MendingMixtureRepairRecipe;
import com.aizistral.enigmaticlegacy.crafting.OblivionStoneCombineRecipe;
import com.aizistral.enigmaticlegacy.crafting.ShapelessNoReturnRecipe;
import com.aizistral.enigmaticlegacy.objects.EnabledCondition;
import com.aizistral.enigmaticlegacy.objects.SpecialLootModifier;
import com.mojang.serialization.Codec;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe.Serializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

public class EnigmaticRecipes extends AbstractRegistry<RecipeSerializer<?>> {
	private static final EnigmaticRecipes INSTANCE = new EnigmaticRecipes();

	@ObjectHolder(value = MODID + ":mending_mixture_repair", registryName = "recipe_serializer")
	public static final RecipeSerializer<MendingMixtureRepairRecipe> MENDING_MIXTURE_REPAIR = null;

	@ObjectHolder(value = MODID + ":enchantment_transposing", registryName = "recipe_serializer")
	public static final RecipeSerializer<EnchantmentTransposingRecipe> ENCHANTMENT_TRANSPOSING = null;

	@ObjectHolder(value = MODID + ":void_stone_combine", registryName = "recipe_serializer")
	public static final RecipeSerializer<OblivionStoneCombineRecipe> OBLIVION_STONE_COMBINE = null;

	@ObjectHolder(value = MODID + ":bind_to_player", registryName = "recipe_serializer")
	public static final RecipeSerializer<BindToPlayerRecipe> BIND_TO_PLAYER = null;

	@ObjectHolder(value = MODID + ":crafting_shaped_hidden", registryName = "recipe_serializer")
	public static final RecipeSerializer<HiddenRecipe> HIDDEN_SHAPED = null;

	@ObjectHolder(value = MODID + ":shapeless_no_return_craft", registryName = "recipe_serializer")
	public static final RecipeSerializer<ShapelessRecipe> SHAPELESS_NO_RETURN = null;

	@ObjectHolder(value = MODID + ":crafting_shaped_cursed", registryName = "recipe_serializer")
	public static final RecipeSerializer<CursedShapedRecipe> CURSED_SHAPED = null;

	@ObjectHolder(value = MODID + ":crafting_shaped_blessed", registryName = "recipe_serializer")
	public static final RecipeSerializer<BlessedShapedRecipe> BLESSED_SHAPED = null;

	private EnigmaticRecipes() {
		super(ForgeRegistries.RECIPE_SERIALIZERS);
		this.register("mending_mixture_repair", () -> MendingMixtureRepairRecipe.SERIALIZER);
		this.register("enchantment_transposing", () -> EnchantmentTransposingRecipe.SERIALIZER);
		this.register("void_stone_combine", () -> OblivionStoneCombineRecipe.SERIALIZER);
		this.register("bind_to_player", () -> BindToPlayerRecipe.SERIALIZER);
		this.register("crafting_shaped_hidden", () -> HiddenRecipe.SERIALIZER);
		this.register("shapeless_no_return_craft", ShapelessNoReturnRecipe.Serializer::new);
		this.register("crafting_shaped_cursed", CursedShapedRecipe.Serializer::new);
		this.register("crafting_shaped_blessed", BlessedShapedRecipe.Serializer::new);
	}

	@Override
	protected void onRegister(RegisterEvent event) {
		CraftingHelper.register(EnabledCondition.Serializer.INSTANCE);
	}

}
