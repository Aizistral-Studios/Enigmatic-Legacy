package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.objects.Vector3;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.curios.api.SlotContext;

public class EnigmaticElytra extends ItemBaseCurio {
	private static boolean wasBoosting = false;

	public EnigmaticElytra() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_elytra"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			if (Minecraft.getInstance().player != null && SuperpositionHandler.isTheCursedOne(Minecraft.getInstance().player)) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.gemRing1Cursed");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.gemRing1");
			}
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player && player.level.isClientSide) {
			if (player.isFallFlying() && Minecraft.getInstance().options.keyJump.isDown()) {
				Vec3 vec31 = player.getLookAngle();
				Vec3 vec32 = player.getDeltaMovement();
				player.setDeltaMovement(vec32.add(vec31.x * 0.1D + (vec31.x * 1.5D - vec32.x) * 0.5D, vec31.y * 0.1D + (vec31.y * 1.5D - vec32.y) * 0.5D, vec31.z * 0.1D + (vec31.z * 1.5D - vec32.z) * 0.5D));

				int amount = 3;
				double rangeModifier = 0.1;

				if (!wasBoosting) {
					wasBoosting = true;
					player.level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.AMBIENT, 3.0F, 1.0F);
				}

				for (int counter = 0; counter <= amount; counter++) {
					Vector3 vec = Vector3.fromEntityCenter(player);
					vec = vec.add(Math.random() - 0.5, -1.0 + Math.random() - 0.5, Math.random() - 0.5);
					player.level.addParticle(ParticleTypes.DRAGON_BREATH, true, vec.x, vec.y, vec.z, ((Math.random()-0.5D)*2.0D)*rangeModifier, ((Math.random()-0.5D)*2.0D)*rangeModifier, ((Math.random()-0.5D)*2.0D)*rangeModifier);
				}
			} else {
				wasBoosting = false;
			}
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(CaelusApi.getInstance().getFlightAttribute(), new AttributeModifier(UUID.fromString("44dfce5a-2f09-4f19-bc29-9b0324cd2a40"), EnigmaticLegacy.MODID+":elytra_modifier", 1.0, AttributeModifier.Operation.ADDITION));
		return attributes;
	}

}
