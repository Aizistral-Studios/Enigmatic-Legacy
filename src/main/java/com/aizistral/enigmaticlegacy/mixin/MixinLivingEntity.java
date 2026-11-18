package com.aizistral.enigmaticlegacy.mixin;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSyncEntityEffects;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.api.quack.IProperShieldUser;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IProperShieldUser {
	@Shadow
	@Final
	private CombatTracker combatTracker;
	@Shadow protected ItemStack useItem;
	@Shadow protected int useItemRemaining;

	public MixinLivingEntity(EntityType<?> type, Level world) {
		super(type, world);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "isDamageSourceBlocked", at = @At("HEAD"), cancellable = true)
	private void onDamageSourceBlocking(DamageSource source, CallbackInfoReturnable<Boolean> info) {
		if (SuperpositionHandler.onDamageSourceBlocking(((LivingEntity)(Object)this), this.useItem, source, info)) {
			info.setReturnValue(true);
		}
	}

	@Inject(method = "onEffectAdded", at = @At("TAIL"))
	private void onEffectAdded(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		syncEntityEffects(entity, pEffectInstance, PacketSyncEntityEffects.Action.ADD);

		handleImperceptibleEffect(entity);
	}

	@Unique
    private static void handleImperceptibleEffect(LivingEntity entity) {
		if (!entity.hasEffect(EnigmaticEffects.IMPERCEPTIBLE)) return;

		List<Mob> targeters = entity.level().getEntitiesOfClass(Mob.class, entity.getBoundingBox().inflate(32), e ->
				e.getTarget() != null && e.getTarget() == entity
		);

		for (Mob e : targeters) {
			e.setTarget(null);
		}
	}

	@Inject(method = "onEffectUpdated", at = @At("TAIL"))
	private void onEffectUpdated(MobEffectInstance effect, boolean forced, Entity target, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		syncEntityEffects(entity, effect, PacketSyncEntityEffects.Action.UPDATE);
	}

	@Inject(method = "onEffectRemoved", at = @At("TAIL"))
	private void onEffectRemoved(MobEffectInstance effect, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		syncEntityEffects(entity, effect, PacketSyncEntityEffects.Action.REMOVE);
	}

	@Unique
	private static void syncEntityEffects(LivingEntity entity, MobEffectInstance effect, PacketSyncEntityEffects.Action update) {
		Level level = entity.getCommandSenderWorld();

		if (level.isClientSide()) return;

		CompoundTag data = new CompoundTag();

		effect.save(data);

		EnigmaticLegacy.packetInstance.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
				new PacketSyncEntityEffects(entity.getId(), data, update)
		);
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
