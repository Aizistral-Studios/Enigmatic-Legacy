package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.base.PersistentData.DataHolder.BookData;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class TheAcknowledgment extends ItemBase implements IVanishable {

	private final Multimap<Attribute, AttributeModifier> attributes;

	public TheAcknowledgment() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).maxStackSize(1));

		this.setRegistryName(EnigmaticLegacy.MODID, "the_acknowledgment");

		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 3.5, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.1F, AttributeModifier.Operation.ADDITION));
		this.attributes = builder.build();
	}

	public static boolean isOpen() {
		return EnigmaticLegacy.theAcknowledgment.getRegistryName().equals(PatchouliAPI.instance.getOpenBookGui());
	}

	public static ITextComponent getEdition() {
		return PatchouliAPI.instance.getSubtitle(EnigmaticLegacy.theAcknowledgment.getRegistryName());
	}

	public static ITextComponent getTitle(ItemStack stack) {
		ITextComponent title = stack.getDisplayName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = new StringTextComponent(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.setFire(4);

		return super.hitEntity(stack, target, attacker);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {

		if (slot.equals(EquipmentSlotType.MAINHAND))
			return this.attributes;
		else
			return super.getAttributeModifiers(slot, stack);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);

		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) playerIn, this.getRegistryName());

		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theAknowledgment1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theAknowledgment2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			//list.add(new StringTextComponent("").append(TheAcknowledgment.getEdition()).mergeStyle(TextFormatting.DARK_PURPLE));
		} catch (Exception ex) {
			EnigmaticLegacy.enigmaticLogger.info("For whatever reason Minecraft really calls .addInformation in Item classes in the middle of THE FUCKING STARTUP.");
			EnigmaticLegacy.enigmaticLogger.info("You can safely ignore following stacktrace, mostly there for the sake of there being.");
			EnigmaticLegacy.enigmaticLogger.catching(ex);
			// Just don't do it lol
		}
	}

}
