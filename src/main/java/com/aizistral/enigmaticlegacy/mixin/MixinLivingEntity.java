package com.aizistral.enigmaticlegacy.mixin;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.quack.IProperShieldUser;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.packets.clients.PacketSyncEntityEffects;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IProperShieldUser {
	@Shadow
	public abstract boolean hasEffect(MobEffect pEffect);

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

	@Inject(method = "updateInvisibilityStatus", at = @At("TAIL"))
	private void updateInvisibilityStatus(CallbackInfo ci) {
		if (isInvisible()) return;
		this.setInvisible(hasEffect(EnigmaticEffects.IMPERCEPTIBLE));
	}

	@Inject(method = "onEffectAdded", at = @At("TAIL"))
	private void onEffectAdded(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfo ci) {
		syncEntityEffects(pEffectInstance, PacketSyncEntityEffects.Action.ADD);

		handleImperceptibleEffect();
	}

	@Inject(method = "onEffectUpdated", at = @At("TAIL"))
	private void onEffectUpdated(MobEffectInstance effect, boolean forced, Entity target, CallbackInfo ci) {
		syncEntityEffects(effect, PacketSyncEntityEffects.Action.UPDATE);
	}

	@Inject(method = "onEffectRemoved", at = @At("TAIL"))
	private void onEffectRemoved(MobEffectInstance effect, CallbackInfo ci) {
		syncEntityEffects(effect, PacketSyncEntityEffects.Action.REMOVE);
	}

	@Unique
	private void handleImperceptibleEffect() {
		if (!hasEffect(EnigmaticEffects.IMPERCEPTIBLE)) return;

		LivingEntity entity = (LivingEntity) (Object) this;
		List<Mob> targeters = level().getEntitiesOfClass(Mob.class, getBoundingBox().inflate(32), e ->
				e.getTarget() != null && e.getTarget() == entity
		);

		for (Mob e : targeters) {
			e.setTarget(null);
		}
	}

	@Unique
	private void syncEntityEffects(MobEffectInstance effect, PacketSyncEntityEffects.Action update) {
		Level level = getCommandSenderWorld();

		if (level.isClientSide()) return;

		CompoundTag data = new CompoundTag();

		effect.save(data);

		LivingEntity entity = (LivingEntity) (Object) this;
		EnigmaticLegacy.packetInstance.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
				new PacketSyncEntityEffects(getId(), data, update)
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
