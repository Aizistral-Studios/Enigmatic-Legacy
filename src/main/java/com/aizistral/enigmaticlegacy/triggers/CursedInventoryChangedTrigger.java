package com.aizistral.enigmaticlegacy.triggers;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IEldritch;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;

public class CursedInventoryChangedTrigger extends SimpleCriterionTrigger<CursedInventoryChangedTrigger.TriggerInstance> {
	public static final ResourceLocation ID = new ResourceLocation(EnigmaticLegacy.MODID, "cursed_inventory_changed");
	public static final CursedInventoryChangedTrigger INSTANCE = new CursedInventoryChangedTrigger();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public CursedInventoryChangedTrigger.TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pEntityPredicate, DeserializationContext pConditionsParser) {
		JsonObject jsonobject = GsonHelper.getAsJsonObject(pJson, "slots", new JsonObject());
		MinMaxBounds.Ints minmaxbounds$ints = MinMaxBounds.Ints.fromJson(jsonobject.get("occupied"));
		MinMaxBounds.Ints minmaxbounds$ints1 = MinMaxBounds.Ints.fromJson(jsonobject.get("full"));
		MinMaxBounds.Ints minmaxbounds$ints2 = MinMaxBounds.Ints.fromJson(jsonobject.get("empty"));
		ItemPredicate[] aitempredicate = ItemPredicate.fromJsonArray(pJson.get("items"));
		return new CursedInventoryChangedTrigger.TriggerInstance(pEntityPredicate, minmaxbounds$ints, minmaxbounds$ints1, minmaxbounds$ints2, aitempredicate);
	}

	public void trigger(ServerPlayer pPlayer, Inventory pInventory, ItemStack pStack) {
		int i = 0;
		int j = 0;
		int k = 0;

		for(int l = 0; l < pInventory.getContainerSize(); ++l) {
			ItemStack itemstack = pInventory.getItem(l);
			if (itemstack.isEmpty()) {
				++j;
			} else {
				++k;
				if (itemstack.getCount() >= itemstack.getMaxStackSize()) {
					++i;
				}
			}
		}

		this.trigger(pPlayer, pInventory, pStack, i, j, k);
	}

	private void trigger(ServerPlayer pPlayer, Inventory pInventory, ItemStack pStack, int pFull, int pEmpty, int pOccupied) {
		this.trigger(pPlayer, (p_43166_) -> {
			return p_43166_.matches(pInventory, pStack, pFull, pEmpty, pOccupied);
		});
	}

	public static class TriggerInstance extends AbstractCriterionTriggerInstance {
		private final MinMaxBounds.Ints slotsOccupied;
		private final MinMaxBounds.Ints slotsFull;
		private final MinMaxBounds.Ints slotsEmpty;
		private final ItemPredicate[] predicates;

		public TriggerInstance(ContextAwarePredicate pPlayer, MinMaxBounds.Ints pSlotsOccupied, MinMaxBounds.Ints pSlotsFull, MinMaxBounds.Ints pSlotsEmpty, ItemPredicate[] pPredicates) {
			super(CursedInventoryChangedTrigger.ID, pPlayer);
			this.slotsOccupied = pSlotsOccupied;
			this.slotsFull = pSlotsFull;
			this.slotsEmpty = pSlotsEmpty;
			this.predicates = pPredicates;
		}

		public static CursedInventoryChangedTrigger.TriggerInstance hasItems(ItemPredicate... pItems) {
			return new CursedInventoryChangedTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pItems);
		}

		public static CursedInventoryChangedTrigger.TriggerInstance hasItems(ItemLike... pItems) {
			ItemPredicate[] aitempredicate = new ItemPredicate[pItems.length];

			for(int i = 0; i < pItems.length; ++i) {
				aitempredicate[i] = new ItemPredicate((TagKey<Item>)null, ImmutableSet.of(pItems[i].asItem()), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, (Potion)null, NbtPredicate.ANY);
			}

			return hasItems(aitempredicate);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext pConditions) {
			JsonObject jsonobject = super.serializeToJson(pConditions);
			if (!this.slotsOccupied.isAny() || !this.slotsFull.isAny() || !this.slotsEmpty.isAny()) {
				JsonObject jsonobject1 = new JsonObject();
				jsonobject1.add("occupied", this.slotsOccupied.serializeToJson());
				jsonobject1.add("full", this.slotsFull.serializeToJson());
				jsonobject1.add("empty", this.slotsEmpty.serializeToJson());
				jsonobject.add("slots", jsonobject1);
			}

			if (this.predicates.length > 0) {
				JsonArray jsonarray = new JsonArray();

				for(ItemPredicate itempredicate : this.predicates) {
					jsonarray.add(itempredicate.serializeToJson());
				}

				jsonobject.add("items", jsonarray);
			}

			return jsonobject;
		}

		public boolean matches(Inventory pInventory, ItemStack pStack, int pFull, int pEmpty, int pOccupied) {
			if (!this.slotsFull.matches(pFull))
				return false;
			else if (!this.slotsEmpty.matches(pEmpty))
				return false;
			else if (!this.slotsOccupied.matches(pOccupied))
				return false;
			else {
				int i = this.predicates.length;
				if (i == 0)
					return true;
				else if (i != 1) {
					List<ItemPredicate> list = new ObjectArrayList<>(this.predicates);
					int j = pInventory.getContainerSize();

					for(int k = 0; k < j; ++k) {
						if (list.isEmpty())
							return true;

						ItemStack itemstack = pInventory.getItem(k);
						if (!itemstack.isEmpty()) {
							list.removeIf((predicate) -> {
								if (itemstack.getItem() instanceof IEldritch)
									return predicate.matches(itemstack) && SuperpositionHandler.isTheWorthyOne(pInventory.player);
								else
									return predicate.matches(itemstack);
							});
						}
					}

					return list.isEmpty();
				} else {
					if (!pStack.isEmpty()) {
						if (pStack.getItem() instanceof IEldritch)
							return this.predicates[0].matches(pStack) && SuperpositionHandler.isTheWorthyOne(pInventory.player);
						else
							return this.predicates[0].matches(pStack);
					} else
						return false;
				}
			}
		}
	}
}