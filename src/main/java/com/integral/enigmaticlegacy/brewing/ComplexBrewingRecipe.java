package com.integral.enigmaticlegacy.brewing;

import java.util.HashMap;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class ComplexBrewingRecipe implements IBrewingRecipe {
    @Nonnull private final Ingredient input;
    @Nonnull private final HashMap<Ingredient, ItemStack> processingMappings;

    public ComplexBrewingRecipe(Ingredient input, HashMap<Ingredient, ItemStack> ingredientCompliances) {
        this.input = input;
        this.processingMappings = ingredientCompliances;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack)
    {
        return this.input.test(stack);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
    	
    	//System.out.println("Lul: " + input + ", " + ingredient);
    	
    	
    	if (this.isInput(input)) {
    		//System.out.println("Mappings: " +  this.processingMappings.keySet());
    		for (Ingredient checked : this.processingMappings.keySet()) {
    			//System.out.println("Checked: " + checked);
    			/*for (ItemStack testStack : checked.getMatchingStacks()) {
    				//System.out.println(testStack);
    				if (testStack.getItem() == ingredient.getItem()) {
    					ItemStack returnable = testStack.copy();
    					return returnable;
    				}
    			}*/
    			
    			if (checked.test(ingredient))
    				return this.processingMappings.get(checked).copy();
    		}
    	}
    	
    	/*
    	if (input.getItem() == EnigmaticLegacy.hastePotionDefault) {
    		if (ingredient.getItem() == Items.REDSTONE)
    			return new ItemStack(EnigmaticLegacy.hastePotionExtended);
    		else if (ingredient.getItem() == Items.GLOWSTONE_DUSR)
    	}
    	*/
    	
    	return ItemStack.EMPTY;
        //return isInput(input) && isIngredient(ingredient) ? output.copy() : ItemStack.EMPTY;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
    	
    	for (Ingredient checked : this.processingMappings.keySet()) {
    		if (checked.test(ingredient))
    			return true;
    	}
    	
        return false;
    }
}