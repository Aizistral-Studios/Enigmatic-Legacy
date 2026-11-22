package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import javax.annotation.Nonnull;

public class EnigmaticBrewing {

    public static void registerBrewingRecipes() {
        EnigmaticLegacy.LOGGER.info("Registering brewing recipes...");

        if (OmniconfigHandler.isItemEnabled(EnigmaticItems.RECALL_POTION)) {
            BrewingRecipeRegistry.addRecipe(
                    getIngredientPotion(Items.POTION, Potions.AWKWARD),
                    Ingredient.of(Items.ENDER_EYE),
                    new ItemStack(EnigmaticItems.RECALL_POTION.get())
            );
        }

        if (OmniconfigHandler.isItemEnabled(EnigmaticItems.TWISTED_POTION)) {
            BrewingRecipeRegistry.addRecipe(
                    Ingredient.of(EnigmaticItems.RECALL_POTION.get()),
                    Ingredient.of(EnigmaticItems.TWISTED_HEART.get()),
                    new ItemStack(EnigmaticItems.TWISTED_POTION.get())
            );
        }

        registerFull(
                Items.QUARTZ,
                EnigmaticPotions.HASTE.get(),
                EnigmaticPotions.LONG_HASTE.get(),
                EnigmaticPotions.STRONG_HASTE.get(),
                EnigmaticPotions.ULTIMATE_HASTE.get()
        );
        registerFull(
                Potions.MUNDANE,
                Items.BLAZE_ROD,
                EnigmaticPotions.MOLTEN_HEART.get(),
                EnigmaticPotions.LONG_MOLTEN_HEART.get(),
                null,
                EnigmaticPotions.ULTIMATE_MOLTEN_HEART.get()
        );

        registerUltimate(Potions.LONG_NIGHT_VISION, null, EnigmaticPotions.ULTIMATE_NIGHT_VISION.get());
        registerUltimate(Potions.LONG_INVISIBILITY, null, EnigmaticPotions.ULTIMATE_INVISIBILITY.get());
        registerUltimate(Potions.LONG_FIRE_RESISTANCE, null, EnigmaticPotions.ULTIMATE_FIRE_RESISTANCE.get());
        registerUltimate(Potions.LONG_LEAPING, Potions.STRONG_LEAPING, EnigmaticPotions.ULTIMATE_LEAPING.get());
        registerUltimate(Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS, EnigmaticPotions.ULTIMATE_SWIFTNESS.get());
        registerUltimate(Potions.LONG_SLOWNESS, Potions.STRONG_SLOWNESS, EnigmaticPotions.ULTIMATE_SLOWNESS.get());
        registerUltimate(Potions.LONG_TURTLE_MASTER, Potions.STRONG_TURTLE_MASTER, EnigmaticPotions.ULTIMATE_TURTLE_MASTER.get());
        registerUltimate(Potions.LONG_WATER_BREATHING, null, EnigmaticPotions.ULTIMATE_WATER_BREATHING.get());
        registerUltimate(null, Potions.STRONG_HEALING, EnigmaticPotions.ULTIMATE_HEALING.get());
        registerUltimate(null, Potions.STRONG_HARMING, EnigmaticPotions.ULTIMATE_HARMING.get());
        registerUltimate(Potions.LONG_POISON, Potions.STRONG_POISON, EnigmaticPotions.ULTIMATE_POISON.get());
        registerUltimate(Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION, EnigmaticPotions.ULTIMATE_REGENERATION.get());
        registerUltimate(Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH, EnigmaticPotions.ULTIMATE_STRENGTH.get());
        registerUltimate(Potions.LONG_WEAKNESS, null, EnigmaticPotions.ULTIMATE_WEAKNESS.get());
        registerUltimate(Potions.LONG_SLOW_FALLING, null, EnigmaticPotions.ULTIMATE_SLOW_FALLING.get());
    }

    private static void register(Potion input, Item ingredient, Potion output) {
        BrewingRecipeRegistry.addRecipe(new EPBrewingRecipe(input, ingredient, output));
    }

    private static void registerFull(Item ingredient, Potion result, Potion resultLong, Potion resultStrong, Potion resultUltimate) {
        registerFull(Potions.AWKWARD, ingredient, result, resultLong, resultStrong, resultUltimate);
    }

    private static void registerFull(Potion base, Item ingredient, Potion result, Potion resultLong, Potion resultStrong, Potion resultUltimate) {
        register(base, ingredient, result);

        if (resultLong != null) register(result, Items.REDSTONE, resultLong);
        if (resultStrong != null) register(result, Items.GLOWSTONE_DUST, resultStrong);

        registerUltimate(resultLong, resultStrong, resultUltimate);
    }

    private static void registerUltimate(Potion longPotion, Potion strongPotion, Potion ultimatePotion) {
        if (longPotion != null) register(longPotion, EnigmaticItems.ASTRAL_DUST.get(), ultimatePotion);
        if (strongPotion != null) register(strongPotion, EnigmaticItems.ASTRAL_DUST.get(), ultimatePotion);
    }

    private static @Nonnull ItemStack getPotion(Item base, Potion potion) {
        return PotionUtils.setPotion(new ItemStack(base), potion);
    }

    private static @Nonnull StrictNBTIngredient getIngredientPotion(Item base, Potion potion) {
        return StrictNBTIngredient.of(getPotion(base, potion));
    }

    private record EPBrewingRecipe(Potion input, Item ingredient, Potion output) implements IBrewingRecipe {

        @Override
        public boolean isInput(@Nonnull ItemStack input) {
            return PotionUtils.getPotion(input) == this.input;
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return ingredient.getItem() == this.ingredient;
        }

        @Override
        @Nonnull
        public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
            if (!this.isInput(input) || !this.isIngredient(ingredient)) {
                return ItemStack.EMPTY;
            }

            ItemStack itemStack = new ItemStack(input.getItem());
            PotionUtils.setPotion(itemStack, this.output);
            return itemStack;
        }
    }
}
