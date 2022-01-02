package com.integral.enigmaticlegacy.crafting;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.objects.EnabledCondition;

import net.minecraft.world.item.crafting.IRecipeSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnigmaticRecipeSerializers {
	public static final IRecipeSerializer<MendingMixtureRepairRecipe> CRAFTING_MENDING_MIXTURE_REPAIR = new MendingMixtureRepairRecipe.Serializer();
	public static final IRecipeSerializer<ShapelessNoReturnRecipe> SHAPELESS_NO_RETURN = new ShapelessNoReturnRecipe.Serializer();
	public static final IRecipeSerializer<OblivionStoneCombineRecipe> OBLIVION_STONE_COMBINE = new OblivionStoneCombineRecipe.Serializer();
	public static final IRecipeSerializer<EnchantmentTransposingRecipe> ENCHANTMENT_TRANSPOSING = new EnchantmentTransposingRecipe.Serializer();
	public static final IRecipeSerializer<BindToPlayerRecipe> BIND_TO_PLAYER = new BindToPlayerRecipe.Serializer();
	public static final IRecipeSerializer<CursedShapedRecipe> CURSED_SHAPED = new CursedShapedRecipe.Serializer();

	@SubscribeEvent
	public void onRegisterSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		EnigmaticLegacy.logger.info("Initializing recipe serializers registration...");
		CraftingHelper.register(EnabledCondition.Serializer.INSTANCE);

		event.getRegistry().register(CRAFTING_MENDING_MIXTURE_REPAIR.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mending_mixture_repair")));
		event.getRegistry().register(SHAPELESS_NO_RETURN.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "shapeless_no_return_craft")));
		event.getRegistry().register(OBLIVION_STONE_COMBINE.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "oblivion_stone_combine")));
		event.getRegistry().register(ENCHANTMENT_TRANSPOSING.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enchantment_transposing")));
		event.getRegistry().register(BIND_TO_PLAYER.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "bind_to_player")));
		event.getRegistry().register(CURSED_SHAPED.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "crafting_shaped_cursed")));
		EnigmaticLegacy.logger.info("Recipe serializers registered successfully.");
	}
}
