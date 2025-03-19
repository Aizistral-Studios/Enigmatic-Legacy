package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.api.quack.IProperShieldUser;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

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
		if (SuperpositionHandler.onDamageSourceBlocking(((LivingEntity)(Object)this), this.useItem, source, info)) {
			info.setReturnValue(true);
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
