package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.WitherSkullEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

public class AngelBlessing extends ItemSpellstoneCurio  {
	public static Omniconfig.IntParameter spellstoneCooldown;
	public static Omniconfig.DoubleParameter accelerationModifier;
	public static Omniconfig.DoubleParameter accelerationModifierElytra;
	public static Omniconfig.PerhapsParameter deflectChance;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("AngelBlessing");

		spellstoneCooldown = builder
				.comment("Active ability cooldown for Angel's Blessing. Measured in ticks. 20 ticks equal to 1 second.")
				.getInt("Cooldown", 40);

		accelerationModifier = builder
				.comment("Acceleration modifier for active ability of Angel's Blessing. The greater it is, the more momentum you will gain.")
				.max(256)
				.getDouble("AccelerationModifier", 1.0);

		accelerationModifierElytra = builder
				.comment("Separate acceleration modifier for active ability of Angel's Blessing when player is flying with Elytra.")
				.max(256)
				.getDouble("AccelerationModifierElytra", 0.6);

		deflectChance = builder
				.comment("Chance to deflect projectile when having Angel's Blessing equipped. Measured in percents.")
				.max(100)
				.getPerhaps("DeflectChance", 50);

		builder.popPrefix();
	}

	protected double range = 4.0D;

	public AngelBlessing() {
		super(ItemSpellstoneCurio.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "angel_blessing"));

		this.immunityList.add(DamageSource.FALL.msgId);
		this.immunityList.add(DamageSource.FLY_INTO_WALL.msgId);

		this.resistanceList.put(DamageSource.WITHER.msgId, () -> 2F);
		this.resistanceList.put(DamageSource.OUT_OF_WORLD.msgId, () -> 2F);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessingCooldown", ChatFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing5", ChatFormatting.GOLD, deflectChance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing7");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", ChatFormatting.LIGHT_PURPLE, KeyBinding.createNameSupplier("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public void triggerActiveAbility(Level world, ServerPlayer player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		Vector3 accelerationVec = new Vector3(player.getLookAngle());
		Vector3 motionVec = new Vector3(player.getDeltaMovement());

		if (player.isFallFlying()) {
			accelerationVec = accelerationVec.multiply(accelerationModifierElytra.getValue());
			accelerationVec = accelerationVec.multiply(1 / (Math.max(0.15D, motionVec.mag()) * 2.25D));
		} else {
			accelerationVec = accelerationVec.multiply(accelerationModifier.getValue());
		}

		Vector3 finalMotion = new Vector3(motionVec.x + accelerationVec.x, motionVec.y + accelerationVec.y, motionVec.z + accelerationVec.z);

		EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPlayerMotion(finalMotion.x, finalMotion.y, finalMotion.z));
		player.setDeltaMovement(finalMotion.x, finalMotion.y, finalMotion.z);

		world.playSound(null, player.blockPosition(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.PLAYERS, 1.0F, (float) (0.6F + (Math.random() * 0.1D)));

		SuperpositionHandler.setSpellstoneCooldown(player, spellstoneCooldown.getValue());
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		/*
		List<AbstractHurtingProjectile> projectileEntities = living.world.getEntitiesWithinAABB(AbstractHurtingProjectile.class, new AABB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));
		List<AbstractArrow> arrowEntities = living.world.getEntitiesWithinAABB(AbstractArrow.class, new AABB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));
		List<PotionEntity> potionEntities = living.world.getEntitiesWithinAABB(PotionEntity.class, new AABB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));

		for (AbstractHurtingProjectile entity : projectileEntities)
			this.redirect(living, entity);

		for (AbstractArrow entity : arrowEntities)
			this.redirect(living, entity);

		for (PotionEntity entity : potionEntities)
			this.redirect(living, entity);
		 */
	}

	public void redirect(LivingEntity bearer, Entity redirected) {
		if (redirected instanceof UltimateWitherSkullEntity || redirected instanceof WitherSkullEntity)
			return;

		/*
		 * if (redirected instanceof ThrownTrident) if
		 * (((ThrownTrident)redirected).getShooter() == bearer) return;
		 */

		Vector3 entityPos = Vector3.fromEntityCenter(redirected);
		Vector3 bearerPos = Vector3.fromEntityCenter(bearer);

		Vector3 redirection = entityPos.subtract(bearerPos);
		redirection = redirection.normalize();

		if (redirected instanceof AbstractArrow && ((AbstractArrow) redirected).getOwner() == bearer) {

			if (redirected instanceof ThrownTrident) {
				ThrownTrident trident = (ThrownTrident) redirected;

				if (trident.clientSideReturnTridentTickCount > 0)
					return;
			}

			redirected.setDeltaMovement(redirected.getDeltaMovement().x * 1.75D, redirected.getDeltaMovement().y * 1.75D, redirected.getDeltaMovement().z * 1.75D);
		} else {
			redirected.setDeltaMovement(redirection.x, redirection.y, redirection.z);
		}

		if (redirected instanceof AbstractHurtingProjectile) {
			AbstractHurtingProjectile redirectedProjectile = (AbstractHurtingProjectile) redirected;
			redirectedProjectile.xPower = (redirection.x / 4.0);
			redirectedProjectile.yPower = (redirection.y / 4.0);
			redirectedProjectile.zPower = (redirection.z / 4.0);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

}
