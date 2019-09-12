package com.integral.enigmaticlegacy.crafting;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.EnabledCondition;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnigmaticRecipeSerializers {
	public static final IRecipeSerializer<MendingMixtureRepairRecipe> CRAFTING_MENDING_MIXTURE_REPAIR = new MendingMixtureRepairRecipe.Serializer();
	public static final IRecipeSerializer<ShapelessNoReturnRecipe> SHAPELESS_NO_RETURN = new ShapelessNoReturnRecipe.Serializer();

	@SubscribeEvent
	public void onRegisterSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		EnigmaticLegacy.enigmaticLogger.info("Initializing recipe serializers registration...");
		CraftingHelper.register(EnabledCondition.Serializer.INSTANCE);
		
		event.getRegistry().register(CRAFTING_MENDING_MIXTURE_REPAIR.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mending_mixture_repair")));
		event.getRegistry().register(SHAPELESS_NO_RETURN.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "shapeless_no_return_craft")));
		EnigmaticLegacy.enigmaticLogger.info("Recipe serializers registered successfully.");
	}
}
