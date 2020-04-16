package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.capability.ICurio;

public class EyeOfNebula extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public static List<String> immunityList = new ArrayList<String>();
	public static HashMap<String, Supplier<Float>> resistanceList = new HashMap<String, Supplier<Float>>();

	public EyeOfNebula(Properties properties) {
		super(properties);

		EyeOfNebula.resistanceList.put(DamageSource.MAGIC.getDamageType(), () -> ConfigHandler.EYE_OF_NEBULA_MAGIC_RESISTANCE.getValue().asModifierInverted());
		EyeOfNebula.resistanceList.put(DamageSource.DRAGON_BREATH.getDamageType(), () -> ConfigHandler.EYE_OF_NEBULA_MAGIC_RESISTANCE.getValue().asModifierInverted());
	}

	public static Properties setupIntegratedProperties() {
		EyeOfNebula.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		EyeOfNebula.integratedProperties.maxStackSize(1);
		EyeOfNebula.integratedProperties.rarity(Rarity.EPIC);

		return EyeOfNebula.integratedProperties;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.eyeOfNebula))
			return false;
		else
			return true;
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.EYE_OF_NEBULA_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula1");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebulaCooldown", ((ConfigHandler.EYE_OF_NEBULA_COOLDOWN.getValue())) / 20.0F);
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula3");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula4", ConfigHandler.EYE_OF_NEBULA_MAGIC_RESISTANCE.getValue().asPercentage() + "%");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula5", ConfigHandler.EYE_OF_NEBULA_DODGE_PROBABILITY.getValue().asPercentage() + "%");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.spellstoneAbility").get().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		LivingEntity target = SuperpositionHandler.getObservedEntity(player, world, 3.0F, (int) ConfigHandler.EYE_OF_NEBULA_PHASE_RANGE.getValue());

		if (target != null) {
			Vector3 targetPos = Vector3.fromEntityCenter(target);
			Vector3 chaserPos = Vector3.fromEntityCenter(player);
			//Vector3 targetSight = new Vector3(target.getLookVec());

			Vector3 dir = targetPos.subtract(chaserPos);
			dir = dir.normalize();
			dir = dir.multiply(1.5D);

			dir = targetPos.add(dir);

			//player.
			world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 128, player.dimension)), new PacketPortalParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 72, 1.0F, false));

			player.setPositionAndUpdate(dir.x, target.getPosY() + 0.25D, dir.z);
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPlayerSetlook(target.getPosX(), target.getPosY() - 1.0D + (target.getHeight() / 2), target.getPosZ()));

			world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 128, player.dimension)), new PacketRecallParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 24, false));

			SuperpositionHandler.setSpellstoneCooldown(player, ConfigHandler.EYE_OF_NEBULA_COOLDOWN.getValue());
		}

	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {
		// Insert existential void here
	}

	@Override
	public void onEquipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

}
