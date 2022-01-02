package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.patchouli.api.PatchouliAPI;

import net.minecraft.world.item.Item.Properties;

public class TheAcknowledgment extends ItemBase implements IVanishable {
	private final Multimap<Attribute, AttributeModifier> attributes;
	private static final ResourceLocation bookID = new ResourceLocation(EnigmaticLegacy.MODID, "the_acknowledgment");

	protected TheAcknowledgment(Properties props, String name, double attackDamage, double attackSpeed) {
		super(props);
		this.setRegistryName(EnigmaticLegacy.MODID, name);

		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
		this.attributes = builder.build();
	}

	public TheAcknowledgment() {
		this(getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1), bookID.getPath(), 3.5, -2.1);
	}

	public static boolean isOpen() {
		return bookID.equals(PatchouliAPI.instance.getOpenBookGui());
	}

	public static ITextComponent getEdition() {
		return PatchouliAPI.instance.getSubtitle(bookID);
	}

	public static ITextComponent getTitle(ItemStack stack) {
		ITextComponent title = stack.getHoverName();

		String akashicTomeNBT = "akashictome:displayName";
		if (stack.hasTag() && stack.getTag().contains(akashicTomeNBT)) {
			title = new StringTextComponent(stack.getTag().getString(akashicTomeNBT));
		}

		return title;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		target.setSecondsOnFire(4);

		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 24;
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
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) playerIn;
			PatchouliAPI.instance.openBookGUI((ServerPlayerEntity) playerIn, bookID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
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
		return enchantment == Enchantments.BANE_OF_ARTHROPODS || super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theAcknowledgment1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theAcknowledgment2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			//list.add(new StringTextComponent("").append(TheAcknowledgment.getEdition()).mergeStyle(TextFormatting.DARK_PURPLE));
		} catch (Exception ex) {
			// Just don't do it lol
		}
	}

}
