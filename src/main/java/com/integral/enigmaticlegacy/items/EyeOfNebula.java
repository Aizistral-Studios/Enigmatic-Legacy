package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

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

public class EyeOfNebula extends ItemSpellstoneCurio implements ISpellstone {
	public static Omniconfig.IntParameter spellstoneCooldown;
	public static Omniconfig.PerhapsParameter dodgeProbability;
	public static Omniconfig.DoubleParameter dodgeRange;
	public static Omniconfig.DoubleParameter phaseRange;
	public static Omniconfig.PerhapsParameter magicResistance;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EyeOfNebula");

		spellstoneCooldown = builder
				.comment("Active ability cooldown for Eye of the Nebula. Measured in ticks. 20 ticks equal to 1 second.")
				.getInt("Cooldown", 60);

		dodgeProbability = builder
				.comment("Probability for Eye of the Nebula to teleport it's bearer from any attack without receiving any damage. Defined as percentage.")
				.max(100)
				.getPerhaps("DodgeChance", 15);

		dodgeRange = builder
				.comment("Range in which Eye of the Nebula searches for a position to teleport it's bearer to when dodging the attack.")
				.min(1)
				.max(128)
				.getDouble("DodgeRange", 16);

		phaseRange = builder
				.comment("Range in which Eye of the Nebula can reach an entity when using it's active ability.")
				.min(1)
				.max(128)
				.getDouble("PhaseRange", 32);

		magicResistance = builder
				.comment("Magic Damage resistance provided by Eye of the Nebula. Defined as percentage.")
				.max(100)
				.getPerhaps("MagicResistance", 65);

		builder.popPrefix();
	}

	public EyeOfNebula() {
		super(ItemSpellstoneCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "eye_of_nebula"));

		Supplier<Float> magicResistanceSupplier = () -> magicResistance.getValue().asModifierInverted();
		this.resistanceList.put(DamageSource.MAGIC.getMsgId(), magicResistanceSupplier);
		this.resistanceList.put(DamageSource.DRAGON_BREATH.getMsgId(), magicResistanceSupplier);
		this.resistanceList.put(DamageSource.WITHER.getMsgId(), magicResistanceSupplier);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebulaCooldown", TextFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula4", TextFormatting.GOLD, magicResistance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula5", TextFormatting.GOLD, dodgeProbability.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eyeOfNebula6");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", TextFormatting.LIGHT_PURPLE, KeyBinding.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

	@Override
	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		LivingEntity target = SuperpositionHandler.getObservedEntity(player, world, 3.0F, (int) phaseRange.getValue());

		if (target != null) {
			Vector3 targetPos = Vector3.fromEntityCenter(target);
			Vector3 chaserPos = Vector3.fromEntityCenter(player);

			Vector3 dir = targetPos.subtract(chaserPos);
			dir = dir.normalize();
			dir = dir.multiply(1.5D);

			dir = targetPos.add(dir);

			world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level.dimension())), new PacketPortalParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 72, 1.0F, false));

			player.teleportTo(dir.x, target.getY() + 0.25D, dir.z);
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPlayerSetlook(target.getX(), target.getY() - 1.0D + (target.getBbHeight() / 2), target.getZ()));

			world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 128, player.level.dimension())), new PacketRecallParticles(player.getX(), player.getY() + (player.getBbHeight() / 2), player.getZ(), 24, false));

			SuperpositionHandler.setSpellstoneCooldown(player, spellstoneCooldown.getValue());
		}

	}
}
