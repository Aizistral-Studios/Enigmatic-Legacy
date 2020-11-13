package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemAdvancedCurio;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class AngelBlessing extends ItemAdvancedCurio implements ISpellstone {

	protected double range = 4.0D;

	public AngelBlessing() {
		super(ItemAdvancedCurio.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "angel_blessing"));

		this.immunityList.add(DamageSource.FALL.damageType);
		this.immunityList.add(DamageSource.FLY_INTO_WALL.damageType);
	}


	@Override
	public boolean isForMortals() {
		return ConfigHandler.ANGEL_BLESSING_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessingCooldown", ((ConfigHandler.ANGEL_BLESSING_COOLDOWN.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.angelBlessing6");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.spellstoneAbility").get().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		Vector3 accelerationVec = new Vector3(player.getLookVec());
		Vector3 motionVec = new Vector3(player.getMotion());

		if (player.isElytraFlying()) {
			accelerationVec = accelerationVec.multiply(ConfigHandler.ANGEL_BLESSING_ACCELERATION_MODIFIER_ELYTRA.getValue());
			accelerationVec = accelerationVec.multiply(1 / (motionVec.mag() * 2.25D));
		} else
			accelerationVec = accelerationVec.multiply(ConfigHandler.ANGEL_BLESSING_ACCELERATION_MODIFIER.getValue());

		Vector3 finalMotion = new Vector3(motionVec.x + accelerationVec.x, motionVec.y + accelerationVec.y, motionVec.z + accelerationVec.z);

		EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> player), new PacketPlayerMotion(finalMotion.x, finalMotion.y, finalMotion.z));
		player.setMotion(finalMotion.x, finalMotion.y, finalMotion.z);

		world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.PLAYERS, 1.0F, (float) (0.6F + (Math.random() * 0.1D)));

		SuperpositionHandler.setSpellstoneCooldown(player, ConfigHandler.ANGEL_BLESSING_COOLDOWN.getValue());
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {

		List<DamagingProjectileEntity> projectileEntities = living.world.getEntitiesWithinAABB(DamagingProjectileEntity.class, new AxisAlignedBB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));
		List<AbstractArrowEntity> arrowEntities = living.world.getEntitiesWithinAABB(AbstractArrowEntity.class, new AxisAlignedBB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));
		List<PotionEntity> potionEntities = living.world.getEntitiesWithinAABB(PotionEntity.class, new AxisAlignedBB(living.getPosX() - this.range, living.getPosY() - this.range, living.getPosZ() - this.range, living.getPosX() + this.range, living.getPosY() + this.range, living.getPosZ() + this.range));

		for (DamagingProjectileEntity entity : projectileEntities)
			this.redirect(living, entity);

		for (AbstractArrowEntity entity : arrowEntities)
			this.redirect(living, entity);

		for (PotionEntity entity : potionEntities)
			this.redirect(living, entity);

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

		if (redirected instanceof AbstractArrowEntity && ((AbstractArrowEntity) redirected).getShooter() == bearer) {

			if (redirected instanceof TridentEntity) {
				TridentEntity trident = (TridentEntity) redirected;

				if (trident.returningTicks > 0)
					return;
			}

			redirected.setMotion(redirected.getMotion().x * 1.75D, redirected.getMotion().y * 1.75D, redirected.getMotion().z * 1.75D);
		} else
			redirected.setMotion(redirection.x, redirection.y, redirection.z);

		if (redirected instanceof DamagingProjectileEntity) {
			DamagingProjectileEntity redirectedProjectile = (DamagingProjectileEntity) redirected;
			redirectedProjectile.accelerationX = (redirection.x / 4.0);
			redirectedProjectile.accelerationY = (redirection.y / 4.0);
			redirectedProjectile.accelerationZ = (redirection.z / 4.0);
		}
	}

}
