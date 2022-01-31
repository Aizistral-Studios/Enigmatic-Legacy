package com.integral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.quack.IProperShieldUser;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.items.InfernalShield;
import com.integral.enigmaticlegacy.items.MagmaHeart;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IProperShieldUser {
	@Shadow protected ItemStack useItem;
	@Shadow protected int useItemRemaining;

	public MixinLivingEntity(EntityType<?> type, Level world) {
		super(type, world);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "isDamageSourceBlocked", at = @At("HEAD"), cancellable = true)
	private void onDamageSourceBlocking(DamageSource source, CallbackInfoReturnable<Boolean> info) {
		if (((LivingEntity)(Object)this) instanceof Player player && this.useItem != null) {
			boolean blocking = this.isActuallyReallyBlocking();

			if (blocking && this.useItem.getItem() instanceof InfernalShield) {
				boolean piercingArrow = false;
				Entity entity = source.getDirectEntity();

				if (entity instanceof AbstractArrow) {
					AbstractArrow abstractarrow = (AbstractArrow)entity;
					if (abstractarrow.getPierceLevel() > 0) {
						piercingArrow = true;
					}
				}

				piercingArrow = false; // defend against Piercing... for now

				if (!source.isBypassArmor() && this.isActuallyReallyBlocking() && !piercingArrow) {
					Vec3 sourcePos = source.getSourcePosition();
					if (sourcePos != null) {
						Vec3 lookVec = this.getViewVector(1.0F);
						Vec3 sourceToSelf = sourcePos.vectorTo(this.position()).normalize();
						sourceToSelf = new Vec3(sourceToSelf.x, 0.0D, sourceToSelf.z);
						if (sourceToSelf.dot(lookVec) < 0.0D) {
							info.setReturnValue(true);

							int strength = -1;

							if (player.hasEffect(EnigmaticLegacy.blazingStrengthEffect)) {
								MobEffectInstance effectInstance = player.getEffect(EnigmaticLegacy.blazingStrengthEffect);
								strength = effectInstance.getAmplifier();
								player.removeEffect(EnigmaticLegacy.blazingStrengthEffect);
								strength = strength > 2 ? 2 : strength;
							}

							player.addEffect(new MobEffectInstance(EnigmaticLegacy.blazingStrengthEffect, 1200, strength + 1, true, true));

							if (source.getDirectEntity() instanceof LivingEntity living && living.isAlive()) {
								if (!living.fireImmune()) {
									living.invulnerableTime = 0;
									living.hurt(new EntityDamageSource(DamageSource.ON_FIRE.msgId, player), 4F);
									living.setSecondsOnFire(4);
									EnigmaticEventHandler.knockbackThatBastard.remove(living);
								}
							}


							return;
						}
					}
				}

				info.setReturnValue(false);
				return;
			}
		}
	}

	@Override
	public boolean isActuallyReallyBlocking() {
		if (this.isUsingItem() && !this.useItem.isEmpty()) {
			Item item = this.useItem.getItem();
			if (item.getUseAnimation(this.useItem) != UseAnim.BLOCK)
				return false;
			else
				return item.getUseDuration(this.useItem) - this.useItemRemaining >= 0; // 0 for no warm-up time
		} else
			return false;
	}

	@Shadow
	public abstract boolean isUsingItem();

}
