package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class TheCube extends ItemSpellstoneCurio implements ISpellstone {

	public TheCube() {
		super(getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "the_cube"));
	}

	@Override
	public int getCooldown(Player player) {
		if (player != null && SuperpositionHandler.hasArchitectsFavor(player))
			return 600;
		else
			return 3200;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube3", ChatFormatting.GOLD, 120);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube12");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube13");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube14");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube15");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube16");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube17");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube18");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube19");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube20");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theCube21");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", ChatFormatting.LIGHT_PURPLE, KeyMapping.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public void triggerActiveAbility(Level world, ServerPlayer player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		// NO-OP
	}

}
