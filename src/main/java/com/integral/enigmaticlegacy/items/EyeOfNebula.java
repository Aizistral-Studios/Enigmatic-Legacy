package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemAdvancedCurio;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class EyeOfNebula extends ItemAdvancedCurio implements ISpellstone {

	public EyeOfNebula() {
		super(ItemAdvancedCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "eye_of_nebula"));

		this.resistanceList.put(DamageSource.MAGIC.getDamageType(), () -> ConfigHandler.EYE_OF_NEBULA_MAGIC_RESISTANCE.getValue().asModifierInverted());
		this.resistanceList.put(DamageSource.DRAGON_BREATH.getDamageType(), () -> ConfigHandler.EYE_OF_NEBULA_MAGIC_RESISTANCE.getValue().asModifierInverted());
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.EYE_OF_NEBULA_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebulaCooldown", TextFormatting.GOLD, ((ConfigHandler.EYE_OF_NEBULA_COOLDOWN.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula4", TextFormatting.GOLD, ConfigHandler.EYE_OF_NEBULA_MAGIC_RESISTANCE.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula5", TextFormatting.GOLD, ConfigHandler.EYE_OF_NEBULA_DODGE_PROBABILITY.getValue().asPercentage() + "%");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", TextFormatting.LIGHT_PURPLE, KeyBinding.getDisplayString("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

	@Override
	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		LivingEntity target = SuperpositionHandler.getObservedEntity(player, world, 3.0F, (int) ConfigHandler.EYE_OF_NEBULA_PHASE_RANGE.getValue());

		if (target != null) {
			Vector3 targetPos = Vector3.fromEntityCenter(target);
			Vector3 chaserPos = Vector3.fromEntityCenter(player);

			Vector3 dir = targetPos.subtract(chaserPos);
			dir = dir.normalize();
			dir = dir.multiply(1.5D);

			dir = targetPos.add(dir);

			world.playSound(null, player.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 128, player.world.func_234923_W_())), new PacketPortalParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 72, 1.0F, false));

			player.setPositionAndUpdate(dir.x, target.getPosY() + 0.25D, dir.z);
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPlayerSetlook(target.getPosX(), target.getPosY() - 1.0D + (target.getHeight() / 2), target.getPosZ()));

			world.playSound(null, player.func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 128, player.world.func_234923_W_())), new PacketRecallParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 24, false));

			SuperpositionHandler.setSpellstoneCooldown(player, ConfigHandler.EYE_OF_NEBULA_COOLDOWN.getValue());
		}

	}
}
