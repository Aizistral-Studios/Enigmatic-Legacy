package com.integral.enigmaticlegacy.items;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPermanentCrystal;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class SoulCrystal extends ItemBase implements IPermanentCrystal, IVanishable {

	public HashMap<PlayerEntity, Multimap<Attribute, AttributeModifier>> attributeDispatcher = new HashMap<PlayerEntity, Multimap<Attribute, AttributeModifier>>();

	public SoulCrystal() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).maxStackSize(1).isImmuneToFire().group(EnigmaticLegacy.enigmaticTab));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "soul_crystal"));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.soulCrystal1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.soulCrystal2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
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

			if (!player.world.isRemote) {
				player.world.playSound(null, new BlockPos(player.getPositionVec()), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1.0f, 1.0f);
			}

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
		AttributeModifierManager attributeManager = player.getAttributeManager();
		attributeManager.reapplyModifiers(soulMap);
	}

	public void updatePlayerSoulMap(PlayerEntity player) {
		Multimap<Attribute, AttributeModifier> soulMap = this.getOrCreateSoulMap(player);
		AttributeModifierManager attributeManager = player.getAttributeManager();

		// Removes former attributes
		attributeManager.removeModifiers(soulMap);

		soulMap.clear();

		int lostFragments = SuperpositionHandler.getPersistentInteger(player, "enigmaticlegacy.lostsoulfragments", 0);

		if (lostFragments > 0) {
			soulMap.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("66a2aa2d-7e3c-4af4-882f-bd2b2ded8e7b"), "Lost Soul Health Modifier", -0.1F * lostFragments, AttributeModifier.Operation.MULTIPLY_TOTAL));
		}

		// Applies new attributes
		attributeManager.reapplyModifiers(soulMap);

		this.attributeDispatcher.put(player, soulMap);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);

		if (this.retrieveSoulFromCrystal(player, stack)) {
			Vector3 playerCenter = Vector3.fromEntityCenter(player);
			if (!player.world.isRemote) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(playerCenter.x, playerCenter.y, playerCenter.z, 64, player.world.getDimensionKey())), new PacketRecallParticles(playerCenter.x, playerCenter.y, playerCenter.z, 48, false));
			}

			player.swingArm(hand);
			stack.setCount(0);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else
			return new ActionResult<>(ActionResultType.PASS, stack);
	}

}
