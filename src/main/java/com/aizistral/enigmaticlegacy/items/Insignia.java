package com.aizistral.enigmaticlegacy.items;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class Insignia extends ItemBaseCurio {

	public Insignia() {
		super(getDefaultProperties().rarity(Rarity.UNCOMMON).stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.insignia1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.insignia2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.insignia3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		this.getCustomName(stack).ifPresent(component -> {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.insigniaName", null, component);
		});

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.insigniaTagDisplay" +
				(ItemNBTHelper.getBoolean(stack, "tagDisplayEnabled", true) ? "Enabled" : "Disabled"));
	}

	public Optional<Component> getCustomName(ItemStack stack) {
		CompoundTag tag = stack.getTagElement("display");

		if (tag != null && tag.contains("Name", 8)) {
			try {
				Component component = Component.Serializer.fromJson(tag.getString("Name"));
				if (component != null)
					return Optional.of(component);
				else {
					tag.remove("Name");
				}
			} catch (Exception exception) {
				tag.remove("Name");
			}
		}

		return Optional.empty();
	}

	public boolean canSeeTrueName(Player player) {
		return player.isCreative() || SuperpositionHandler.hasCurio(player, EnigmaticItems.ENIGMATIC_EYE);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
		CuriosApi.getCuriosHelper().addSlotModifier(attributes, "charm",
				UUID.fromString("4b2533eb-26ca-4470-8805-2faa90735fde"), 1, Operation.ADDITION);
		return attributes;
	}

	@Override
	public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
		if (player.isCrouching()) {
			ItemStack stack = player.getItemInHand(handIn);

			if (ItemNBTHelper.getBoolean(stack, "tagDisplayEnabled", true)) {
				ItemNBTHelper.setBoolean(stack, "tagDisplayEnabled", false);
				world.playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_OFF, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			} else {
				ItemNBTHelper.setBoolean(stack, "tagDisplayEnabled", true);
				world.playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_ON, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			}

			player.swing(handIn);

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		} else
			return super.use(world, player, handIn);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
	}

}

