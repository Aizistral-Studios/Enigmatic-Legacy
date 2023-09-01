package com.aizistral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.client.Quote;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

public class EnigmaticEye extends ItemBaseCurio {
	private static final ResourceLocation EYE_ADVANCEMENT = new ResourceLocation(EnigmaticLegacy.MODID, "book/relics/enigmatic_eye");

	public EnigmaticEye() {
		super(getDefaultProperties().rarity(Rarity.EPIC).fireResistant());
	}

	@OnlyIn(Dist.CLIENT)
	public void registerVariants() {
		ItemProperties.register(this, new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_eye_activated"), (stack, world, entity, numberlol) -> {
			if (!this.isDormant(stack))
				return 1F;

			int animTicks = ItemNBTHelper.getInt(stack, "ActivationAnimation", -1);

			if (animTicks > -1) {
				float result = 0.0F;

				if (animTicks > 2) {
					result = 0.4F;
				} else {
					result = 0.8F;
				}

				return result;
			}

			return 0F;
		});
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			if (this.isDormant(stack)) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEye1");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEye2");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEye3");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEyeAwakened1");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEyeAwakened2");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEyeAwakened3");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEyeAwakened4");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEyeAwakened5");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticEyeAwakened6");
			}
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	public boolean isDormant(ItemStack eye) {
		return ItemNBTHelper.getBoolean(eye, "IsDormant", true);
	}

	public void setDormant(ItemStack eye, boolean dormant) {
		ItemNBTHelper.setBoolean(eye, "IsDormant", dormant);
	}

	public void activateWithAnimation(ItemStack eye) {
		if (this.isDormant(eye)) {
			ItemNBTHelper.setInt(eye, "ActivationAnimation", 4);
		}
	}

	@Override
	public boolean canEquip(SlotContext context, ItemStack stack) {
		return super.canEquip(context, stack) && !this.isDormant(stack);
	}

	@Override
	public Component getName(ItemStack stack) {
		if (this.isDormant(stack))
			return Component.translatable("item.enigmaticlegacy.enigmatic_eye_dormant");
		else
			return Component.translatable("item.enigmaticlegacy.enigmatic_eye_active");
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		int animTicks = ItemNBTHelper.getInt(pStack, "ActivationAnimation", -1);

		if (animTicks > 0) {
			ItemNBTHelper.setInt(pStack, "ActivationAnimation", animTicks - 1);
		} else if (animTicks == 0) {
			ItemNBTHelper.setInt(pStack, "ActivationAnimation", -1);
			this.setDormant(pStack, false);
		}

		if (pEntity instanceof ServerPlayer player && !this.isDormant(pStack)) {
			if (!TransientPlayerData.get(player).getUnlockedNarrator()) {
				TransientPlayerData.get(player).setUnlockedNarrator(true);
				Quote.getRandom(Quote.NARRATOR_INTROS).play(player, 60);
			}

			if (player.tickCount % 100 == 0)
				if (!SuperpositionHandler.hasAdvancement(player, EYE_ADVANCEMENT)) {
					SuperpositionHandler.grantAdvancement(player, EYE_ADVANCEMENT);
				}
		}

		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player pPlayer, InteractionHand pUsedHand) {
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);

		if (this.isDormant(stack) && !ItemNBTHelper.verifyExistance(stack, "ActivationAnimation")) {
			if (pPlayer instanceof ServerPlayer player) {
				this.activateWithAnimation(stack);
				TransientPlayerData data = TransientPlayerData.get(player);
				boolean wasNarratorUnlocked = data.getUnlockedNarrator();

				level.playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_ON, SoundSource.PLAYERS, 1.0F, 1F);

				if (!wasNarratorUnlocked) {
					data.setUnlockedNarrator(true);
					data.needsSync = true;
					Quote.getRandom(Quote.NARRATOR_INTROS).play(player, 80);
				}

				if (!SuperpositionHandler.hasAdvancement(player, EYE_ADVANCEMENT)) {
					SuperpositionHandler.grantAdvancement(player, EYE_ADVANCEMENT);
				}
			}

			return InteractionResultHolder.success(stack);
		}

		return super.use(level, pPlayer, pUsedHand);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();

		if (!this.isDormant(stack)) {
			if (slotContext.entity() instanceof Player player) {
				CuriosApi.getCuriosHelper().addSlotModifier(attributes, "charm",
						UUID.fromString("d020cd5d-c050-49e4-a0ea-ef27adf7e6d0"), 1, Operation.ADDITION);
			}

			attributes.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(UUID.fromString("313fba36-cc58-4106-a42b-66b7fd420c5a"), "Reach Bonus", 3.0F, AttributeModifier.Operation.ADDITION));
		}

		return attributes;
	}

}
