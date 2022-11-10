package com.integral.enigmaticlegacy.mixin;

import static com.integral.enigmaticlegacy.EnigmaticLegacy.abyssalHeart;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.integral.enigmaticlegacy.api.quack.IAbyssalHeartBearer;
import com.integral.enigmaticlegacy.client.Quote;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.objects.Vector3;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(EnderDragon.class)
public abstract class MixinEnderDragon extends Mob implements Enemy, IAbyssalHeartBearer {
	@Shadow
	public int dragonDeathTime;
	private Player abyssalHeartOwner;

	protected MixinEnderDragon() {
		super(null, null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "tickDeath", at = @At("RETURN"), require = 1)
	private void onTickDeath(CallbackInfo info) {
		if (this.dragonDeathTime == 200 && this.level instanceof ServerLevel) {
			if (this.abyssalHeartOwner != null) {
				int heartsGained = SuperpositionHandler.getPersistentInteger(this.abyssalHeartOwner, "AbyssalHeartsGained", 0);

				Vector3 center = Vector3.fromEntityCenter(this);
				PermanentItemEntity heart = new PermanentItemEntity(this.level, center.x, center.y, center.z, new ItemStack(abyssalHeart, 1));
				heart.setOwnerId(this.abyssalHeartOwner.getUUID());
				this.level.addFreshEntity(heart);

				SuperpositionHandler.setPersistentInteger(this.abyssalHeartOwner, "AbyssalHeartsGained", heartsGained + 1);
			}

			List<ServerPlayer> players = this.level.getEntitiesOfClass(ServerPlayer.class,
					SuperpositionHandler.getBoundingBoxAroundEntity(this, 256));

			players.forEach(player -> Quote.WITH_DRAGONS.playOnceIfUnlocked(player, 140));
		}
	}

	@Override
	public void dropAbyssalHeart(Player player) {
		this.abyssalHeartOwner = player;
	}

}
