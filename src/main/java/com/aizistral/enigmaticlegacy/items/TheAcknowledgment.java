package com.aizistral.enigmaticlegacy.items;

import java.awt.TextComponent;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.patchouli.api.PatchouliAPI;

public class TheAcknowledgment extends ItemBase implements Vanishable {
	private static final ItemStack UNSUSPECTING_DIAMOND_SWORD = new ItemStack(Items.DIAMOND_SWORD);
	private static final ResourceLocation BOOK_ID = new ResourceLocation(EnigmaticLegacy.MODID, "the_acknowledgment");
	private final Multimap<Attribute, AttributeModifier> attributes;
	private boolean allowAllEnchantments = false;

	protected TheAcknowledgment(Properties props, String name, double attackDamage, double attackSpeed) {
		super(props);

		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
		this.attributes = builder.build();
	}

	public TheAcknowledgment() {
		this(getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1), BOOK_ID.getPath(), 3.5, -2.1);
	}

	public static boolean isOpen() {
		return BOOK_ID.equals(PatchouliAPI.get().getOpenBookGui());
	}

	public static Component getEdition() {
		return PatchouliAPI.get().getSubtitle(BOOK_ID);
	}

	public static Component getTitle(ItemStack stack) {
		Component title = stack.getHoverName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = Component.literal(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
	}

	protected void setAllowAllEnchantments(boolean allowAllEnchantments) {
		this.allowAllEnchantments = allowAllEnchantments;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.setSecondsOnFire(4);

		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return 24;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		if (slot.equals(EquipmentSlot.MAINHAND))
			return this.attributes;
		else
			return super.getAttributeModifiers(slot, stack);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (player instanceof ServerPlayer playerMP) {
			PatchouliAPI.get().openBookGUI(playerMP, BOOK_ID);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		if (this.allowAllEnchantments)
			return !EnchantmentHelper.getEnchantments(book).keySet().contains(Enchantments.UNBREAKING) &&
					Items.DIAMOND_SWORD.isBookEnchantable(UNSUSPECTING_DIAMOND_SWORD, book);

		Map<Enchantment, Integer> list = EnchantmentHelper.getEnchantments(book);

		if (list.size() == 1 && list.containsKey(Enchantments.BANE_OF_ARTHROPODS))
			return true;
		else
			return super.isBookEnchantable(stack, book);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (this.allowAllEnchantments)
			return enchantment != Enchantments.UNBREAKING && Items.DIAMOND_SWORD.canApplyAtEnchantingTable(UNSUSPECTING_DIAMOND_SWORD, enchantment);

		return enchantment == Enchantments.BANE_OF_ARTHROPODS || super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theAcknowledgment1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theAcknowledgment2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			//list.add(Component.literal("").append(TheAcknowledgment.getEdition()).mergeStyle(ChatFormatting.DARK_PURPLE));
		} catch (Exception ex) {
			// Just don't do it lol
		}
	}

}
