package com.aizistral.enigmaticlegacy.brewing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.IBrewingRecipe;

/**
 * Stub brewing recipe used to validate some potions as valid inputs
 * for brewing stand, even though they cannot actually be processed
 * or upgraded any further.
 * @author Integral
 */

public class ValidationBrewingRecipe implements IBrewingRecipe {

	    private final Ingredient input;
	    private final Ingredient ingredient;

	    public ValidationBrewingRecipe(Ingredient input, Ingredient ingredient) {
	        this.input = input;
	        this.ingredient = ingredient;
	    }

	    @Override
	    public boolean isInput(ItemStack stack) {

			if (stack != null && this.input != null)
				if (this.input.test(stack))
					return true;


			return false;
	    }

	    @Override
	    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
	        return ItemStack.EMPTY;
	    }

	    public Ingredient getInput() {
	        return this.input;
	    }

	    public Ingredient getIngredient() {
	        return this.ingredient;
	    }

	    public ItemStack getOutput() {
	        return ItemStack.EMPTY;
	    }

	    @Override
	    public boolean isIngredient(ItemStack ingredient) {
	    	if (ingredient != null && this.ingredient != null)
	    		return this.ingredient.test(ingredient);
	    	else
	    		return false;
	    }


}

