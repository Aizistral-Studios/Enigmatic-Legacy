package com.integral.enigmaticlegacy.brewing;

import java.util.HashMap;

import javax.annotation.Nonnull;

import com.integral.enigmaticlegacy.helpers.PotionHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

/**
 * A recipe with one output and any amount of input-ingredient pairs.
 * @author Integral
 */

public class ComplexBrewingRecipe implements IBrewingRecipe {
    @Nonnull private final HashMap<Ingredient, Ingredient> processingMappings;
    @Nonnull private final ItemStack output;

    public ComplexBrewingRecipe(HashMap<Ingredient, Ingredient> ingredientCompliances, ItemStack output) {
        this.processingMappings = ingredientCompliances;
        this.output = output;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        for (Ingredient ing : this.processingMappings.keySet()) {
        	if (this.isInput(stack, ing))
        		return true;
        }
        
        return false;
    }
    
    public boolean isInput(@Nonnull ItemStack stack, Ingredient ingredient) {
    	for (ItemStack testStack : ingredient.getMatchingStacks()) {
    		if (testStack.getItem().equals(stack.getItem()) && PotionUtils.getPotionFromItem(testStack).equals(PotionUtils.getPotionFromItem(stack)) && PotionHelper.getAdvancedPotion(testStack).equals(PotionHelper.getAdvancedPotion(stack)))
    			return true;
    	}
    	
    	return false;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
    	for (Ingredient ing : this.processingMappings.keySet()) {
    		
    		if (this.isInput(input, ing)) {
    			if (this.processingMappings.get(ing).test(ingredient))
    				return this.output.copy();
    		}
    		
    	}
    	
    	return ItemStack.EMPTY;
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
    	if (stack != null)
    	for (Ingredient ing : this.processingMappings.values()) {
    		if (ing.test(stack))
    			return true;
    	}
    	
        return false;
    }
}