package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPermanentCrystal;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoulCrystal extends ItemBase implements IPermanentCrystal {

	public HashMap<PlayerEntity, Multimap<Attribute, AttributeModifier>> attributeDispatcher = new HashMap<PlayerEntity, Multimap<Attribute, AttributeModifier>>();
	//public Multimap<Attribute, AttributeModifier> playerAttributes = HashMultimap.create();

	public SoulCrystal() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).maxStackSize(1).func_234689_a_().group(null));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "soul_crystal"));

		//this.attributesDefault.put(Attributes.field_233818_a_, new AttributeModifier(UUID.fromString("66a2aa2d-7e3c-4af4-882f-bd2b2ded8e7b"), "Lost Soul Health Modifier", -0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
	}

	public ItemStack createCrystalFrom(PlayerEntity player) {
		int lostFragments = this.getLostCrystals(player);
		this.setLostCrystals(player, lostFragments + 1);

		return new ItemStack(this);
	}

	public boolean retrieveSoulFromCrystal(PlayerEntity player, ItemStack stack) {
		int lostFragments = this.getLostCrystals(player);

		if (lostFragments > 0) {
			this.setLostCrystals(player, lostFragments - 1);
			player.world.playSound(null, new BlockPos(player.getPositionVec()), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1.0f, 1.0f);

			return true;
		} else
			return false;
	}

	public void setLostCrystals(PlayerEntity player, int lost) {
		SuperpositionHandler.setPersistentInteger(player, "enigmaticlegacy.lostsoulfragments", lost);
		this.updatePlayerSoulMap(player);
	}

	public int getLostCrystals(PlayerEntity player) {
		return SuperpositionHandler.getPersistentInteger(player, "enigmaticlegacy.lostsoulfragments", 0);
	}

	public Multimap<Attribute, AttributeModifier> getOrCreateSoulMap(PlayerEntity player) {
		if (this.attributeDispatcher.containsKey(player))
			return this.attributeDispatcher.get(player);
		else {
			Multimap<Attribute, AttributeModifier> playerAttributes = HashMultimap.create();
			this.attributeDispatcher.put(player, playerAttributes);
			return playerAttributes;
		}
	}

	public void applyPlayerSoulMap(PlayerEntity player) {
		Multimap<Attribute, AttributeModifier> soulMap = this.getOrCreateSoulMap(player);
		AttributeModifierManager attributeManager = player.func_233645_dx_();
		attributeManager.func_233793_b_(soulMap);
	}

	public void updatePlayerSoulMap(PlayerEntity player) {
		Multimap<Attribute, AttributeModifier> soulMap = this.getOrCreateSoulMap(player);
		AttributeModifierManager attributeManager = player.func_233645_dx_();

		// Removes former attributes
		attributeManager.func_233785_a_(soulMap);

		soulMap.clear();

		int lostFragments = SuperpositionHandler.getPersistentInteger(player, "enigmaticlegacy.lostsoulfragments", 0);

		if (lostFragments > 0)
			soulMap.put(Attributes.field_233818_a_, new AttributeModifier(UUID.fromString("66a2aa2d-7e3c-4af4-882f-bd2b2ded8e7b"), "Lost Soul Health Modifier", -0.1F * lostFragments, AttributeModifier.Operation.MULTIPLY_TOTAL));

		// Applies new attributes
		attributeManager.func_233793_b_(soulMap);

		this.attributeDispatcher.put(player, soulMap);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);

		if (this.retrieveSoulFromCrystal(player, stack)) {
			player.swingArm(hand);
			stack.setCount(0);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else
			return new ActionResult<>(ActionResultType.PASS, stack);
	}

}
