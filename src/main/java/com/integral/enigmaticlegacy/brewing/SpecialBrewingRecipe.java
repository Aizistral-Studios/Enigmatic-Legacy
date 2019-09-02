package com.integral.enigmaticlegacy.brewing;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class SpecialBrewingRecipe implements IBrewingRecipe {
	
	    @Nonnull private final Ingredient input;
	    @Nonnull private final Ingredient ingredient;
	    @Nonnull private final ItemStack output;

	    public SpecialBrewingRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
	        this.input = input;
	        this.ingredient = ingredient;
	        this.output = output;
	    }

	    @Override
	    public boolean isInput(@Nonnull ItemStack stack) {
			
			if (stack != null)
			for (ItemStack testStack : this.getInput().getMatchingStacks()) {
				//System.out.println(testStack);
				if (testStack.getItem() == stack.getItem() && PotionUtils.getPotionFromItem(testStack) == PotionUtils.getPotionFromItem(stack))
					return true;
			}

			return false;
	    }

	    @Override
	    public ItemStack getOutput(ItemStack input, ItemStack ingredient)
	    {
	        return isInput(input) && isIngredient(ingredient) ? getOutput().copy() : ItemStack.EMPTY;
	    }

	    public Ingredient getInput()
	    {
	        return input;
	    }

	    public Ingredient getIngredient()
	    {
	        return ingredient;
	    }

	    public ItemStack getOutput()
	    {
	        return output;
	    }

	    @Override
	    public boolean isIngredient(ItemStack ingredient) {
	    	if (ingredient != null)
	    		return this.ingredient.test(ingredient);
	    	else
	    		return false;
	    }
	

}
