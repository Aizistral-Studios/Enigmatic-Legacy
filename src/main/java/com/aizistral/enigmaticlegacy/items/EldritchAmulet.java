package com.aizistral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IEldritch;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

public class EldritchAmulet extends AscensionAmulet implements IEldritch {

	public EldritchAmulet() {
		super(getDefaultProperties().rarity(Rarity.EPIC).fireResistant(), "eldritch_amulet");
	}

	@Override
	public List<ItemStack> getCreativeTabStacks() {
		return ImmutableList.of(new ItemStack(this));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		String name = ItemNBTHelper.getString(stack, "Inscription", null);

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eldritchAmulet1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eldritchAmulet2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eldritchAmulet3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eldritchAmulet4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eldritchAmulet5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.indicateWorthyOnesOnly(list);
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");

			if (name != null) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticAmuletInscription", ChatFormatting.DARK_RED, name);
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.indicateCursedOnesOnly(list);
		}


		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		this.addAttributes(list, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void addAttributes(List<Component> list, ItemStack stack) {
		ItemLoreHelper.addLocalizedFormattedString(list, "curios.modifiers.charm", ChatFormatting.GOLD);
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eldritchAmuletStat1");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eldritchAmuletStat2");
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("f5bb82c7-0332-4adf-a414-2e4f03471983"), EnigmaticLegacy.MODID+":attack_bonus", 3.0, AttributeModifier.Operation.ADDITION));
		return multimap;
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof ServerPlayer player && player.tickCount % 5 == 0
				&& SuperpositionHandler.isTheWorthyOne(player)) {
			List<LivingEntity> entities = SuperpositionHandler.getObservedEntities(player, player.level(), 3, 128, false);
			for (LivingEntity entity : entities) {
				if (entity instanceof ServerPlayer otherPlayer && SuperpositionHandler.hasCurio(otherPlayer, this)
						&& SuperpositionHandler.isTheWorthyOne(otherPlayer)) {
					continue;
				}

				entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1));
				entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 10, 1));
				entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 1));
			}
		}
	}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
		tooltips.clear();
		return tooltips;
	}

	@Override
	public boolean canEquip(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player)
			return !SuperpositionHandler.hasCurio(player, EnigmaticItems.ELDRITCH_AMULET)
					&& SuperpositionHandler.isTheWorthyOne(player) && super.canEquip(context, stack);
		else
			return false;
	}

	private Map<String, NonNullList<ItemStack>> inventoryMap(Player player) {
		Map<String, NonNullList<ItemStack>> inventories = new HashMap<>();
		inventories.put("Armor", player.getInventory().armor);
		inventories.put("Main", player.getInventory().items);
		inventories.put("Offhand", player.getInventory().offhand);
		return inventories;
	}

	public void storeInventory(ServerPlayer player) {
		Map<String, NonNullList<ItemStack>> inventories = this.inventoryMap(player);

		CompoundTag tag = new CompoundTag();

		inventories.entrySet().forEach(entry -> {
			ListTag list = new ListTag();

			for (int i = 0; i < entry.getValue().size(); i++) {
				ItemStack stack = entry.getValue().get(i);

				if (EnchantmentHelper.getEnchantments(stack).keySet().contains(Enchantments.VANISHING_CURSE)) {
					stack = ItemStack.EMPTY;
				}

				list.add(stack.serializeNBT());
				entry.getValue().set(i, ItemStack.EMPTY);
			}

			tag.put("Inventory" + entry.getKey(), list);
		});

		SuperpositionHandler.setPersistentTag(player, "ELPersistentInventory", tag);
	}

	public boolean reclaimInventory(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
		Map<String, NonNullList<ItemStack>> inventories = this.inventoryMap(newPlayer);
		Tag maybeTag = SuperpositionHandler.getPersistentTag(oldPlayer, "ELPersistentInventory", null);
		boolean hadTag = false;

		if (maybeTag instanceof CompoundTag tag) {
			SuperpositionHandler.removePersistentTag(newPlayer, "ELPersistentInventory");
			hadTag = true;

			inventories.entrySet().forEach(entry -> {
				Tag maybeList = tag.get("Inventory" + entry.getKey());

				if (maybeList instanceof ListTag list) {
					for (int i = 0; i < entry.getValue().size(); i++) {
						CompoundTag stackTag = list.getCompound(i);
						ItemStack stack = ItemStack.of(stackTag);
						entry.getValue().set(i, stack);
					}
				}
			});
		}

		return hadTag;
	}

}
