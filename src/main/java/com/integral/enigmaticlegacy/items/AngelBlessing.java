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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.ServerPlayerEntity;
import net.minecraft.world.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.entity.projectile.DamagingProjectileEntity;
import net.minecraft.world.entity.projectile.TridentEntity;
import net.minecraft.world.entity.projectile.WitherSkullEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessingCooldown", TextFormatting.GOLD, ((spellstoneCooldown.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing5", TextFormatting.GOLD, deflectChance.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing7");
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
	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
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

		world.playSound(null, player.blockPosition(), SoundEvents.ENDER_EYE_LAUNCH, SoundCategory.PLAYERS, 1.0F, (float) (0.6F + (Math.random() * 0.1D)));

		SuperpositionHandler.setSpellstoneCooldown(player, spellstoneCooldown.getValue());
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		/*
		List<DamagingProjectileEntity> projectileEntities = living.world.getEntitiesWithinAABB(DamagingProjectileEntity.class, new AxisAlignedBB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));
		List<AbstractArrowEntity> arrowEntities = living.world.getEntitiesWithinAABB(AbstractArrowEntity.class, new AxisAlignedBB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));
		List<PotionEntity> potionEntities = living.world.getEntitiesWithinAABB(PotionEntity.class, new AxisAlignedBB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));

		for (DamagingProjectileEntity entity : projectileEntities)
			this.redirect(living, entity);

		for (AbstractArrowEntity entity : arrowEntities)
			this.redirect(living, entity);

		for (PotionEntity entity : potionEntities)
			this.redirect(living, entity);
		 */
	}

	public void redirect(LivingEntity bearer, Entity redirected) {
		if (redirected instanceof UltimateWitherSkullEntity || redirected instanceof WitherSkullEntity)
			return;

		/*
		 * if (redirected instanceof TridentEntity) if
		 * (((TridentEntity)redirected).getShooter() == bearer) return;
		 */

		Vector3 entityPos = Vector3.fromEntityCenter(redirected);
		Vector3 bearerPos = Vector3.fromEntityCenter(bearer);

		Vector3 redirection = entityPos.subtract(bearerPos);
		redirection = redirection.normalize();

		if (redirected instanceof AbstractArrowEntity && ((AbstractArrowEntity) redirected).getOwner() == bearer) {

			if (redirected instanceof TridentEntity) {
				TridentEntity trident = (TridentEntity) redirected;

				if (trident.clientSideReturnTridentTickCount > 0)
					return;
			}

			redirected.setDeltaMovement(redirected.getDeltaMovement().x * 1.75D, redirected.getDeltaMovement().y * 1.75D, redirected.getDeltaMovement().z * 1.75D);
		} else {
			redirected.setDeltaMovement(redirection.x, redirection.y, redirection.z);
		}

		if (redirected instanceof DamagingProjectileEntity) {
			DamagingProjectileEntity redirectedProjectile = (DamagingProjectileEntity) redirected;
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
