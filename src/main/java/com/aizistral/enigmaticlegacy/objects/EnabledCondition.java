package com.aizistral.enigmaticlegacy.objects;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Condition that checks whether item is instance of IPerhaps, and if it is,
 * whether or not it is enabled. Used to disable recipes for items disabled
 * in config.
 * @author Integral
 */

public class EnabledCondition implements ICondition {
	private static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "is_enabled");
	private final ResourceLocation item;

	public EnabledCondition(ResourceLocation item) {
		this.item = item;
	}

	@Override
	public ResourceLocation getID() {
		return ID;
	}

	@Override
	public boolean test(IContext context) {
		Item item = ForgeRegistries.ITEMS.getValue(this.item);

		if (this.item.toString().equals(EnigmaticLegacy.MODID + ":bonuswoolrecipes"))
			return OmniconfigHandler.bonusWoolRecipesEnabled.getValue();
		else
			return OmniconfigHandler.isItemEnabled(item);
	}

	public static class Serializer implements IConditionSerializer<EnabledCondition> {
		public static final Serializer INSTANCE = new Serializer();

		@Override
		public void write(JsonObject json, EnabledCondition value) {
			json.addProperty("item", value.item.toString());
		}

		@Override
		public EnabledCondition read(JsonObject json) {
			return new EnabledCondition(new ResourceLocation(GsonHelper.getAsString(json, "item")));
		}

		@Override
		public ResourceLocation getID() {
			return EnabledCondition.ID;
		}
	}
}

