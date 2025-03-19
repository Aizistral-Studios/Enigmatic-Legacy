package com.aizistral.enigmaticlegacy.items;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.google.common.base.Objects;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// TODO Make wayfinder make soul crystals glow, also give it the mode to point to personal crystals only
public class SoulCompass extends ItemBase implements ICursed {
	@OnlyIn(Dist.CLIENT)
	private CompassWobble wobble;
	@OnlyIn(Dist.CLIENT)
	private CompassWobble wobbleRandom;
	@Nullable @OnlyIn(Dist.CLIENT)
	private BlockPos nearestCrystal;

	public SoulCompass() {
		super(getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).fireResistant());
	}

	@OnlyIn(Dist.CLIENT)
	public void setNearestCrystal(BlockPos nearestCrystal) {
		if (!Objects.equal(this.nearestCrystal, nearestCrystal)) {
			if (nearestCrystal != null && this.nearestCrystal != null) {
				//BlockPos playerPos = Minecraft.getInstance().player.blockPosition();
				//BlockPos oldPos = this.nearestCrystal.subtract(playerPos);
				//BlockPos newPos = nearestCrystal.subtract(playerPos);

				//float bareAngle = this.getAngle(newPos, oldPos) / 360F;

				//if (bareAngle < 0.5F) {
				//	bareAngle += 0.5F;
				//}

				//float angle = Mth.positiveModulo(bareAngle, 1F);
				//angle = 0.5F - (angle - 0.25F);

				this.wobble.rotation = 1.0;
				this.wobble.deltaRotation = 0.3;
				//System.out.println("Bare Angle: " + bareAngle);
				//System.out.println("Angle: " + angle);
			} else {
				this.wobble.rotation = this.wobbleRandom.rotation;
				this.wobble.deltaRotation = this.wobbleRandom.deltaRotation;
			}

			this.nearestCrystal = nearestCrystal;
			//System.out.println("Nearest crystal set: " + nearestCrystal);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public float getAngle(BlockPos one, BlockPos two) {
		float angle = (float) Math.toDegrees(Math.atan2(one.getZ() - two.getZ(), one.getX() - two.getX()));

		if (angle < 0) {
			angle += 360;
		}

		return angle;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.soulCompass1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

	@OnlyIn(Dist.CLIENT)
	public void registerVariants() {
		this.wobble = new CompassWobble(0.1);
		this.wobbleRandom = new CompassWobble(0.6); // 0.6 to make it GO FASTA

		ItemProperties.register(this, new ResourceLocation("angle"), new ClampedItemPropertyFunction() {
			private final CompassWobble wobble = SoulCompass.this.wobble;
			private final CompassWobble wobbleRandom = SoulCompass.this.wobbleRandom;

			@Override
			public float unclampedCall(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity living, int seed) {
				Entity entity = living != null ? living : stack.getEntityRepresentation();

				if (entity == null)
					return 0.0F;
				else {
					if (level == null && entity.level() instanceof ClientLevel) {
						level = (ClientLevel) entity.level();
					}

					assert level != null;

					BlockPos target = this.getTargetPosition();
					long gameTime = level.getGameTime();

					if (target != null && !(entity.position().distanceToSqr(target.getX() + 0.5D, entity.position().y(), target.getZ() + 0.5D) < 1.0E-5F)) {
						boolean isLocalPlayer = living instanceof Player && ((Player)living).isLocalPlayer();
						double bodyRotation = 0.0D;

						if (isLocalPlayer) {
							assert living != null;
							bodyRotation = living.getYRot();
						} else
							return this.randomAngle(gameTime, seed);

						if (!SuperpositionHandler.isTheCursedOne((Player) living)
								|| !SuperpositionHandler.hasExactStack((Player) living, stack)
								|| level.getBiome(living.blockPosition()).is(Biomes.SOUL_SAND_VALLEY))
							return this.randomAngle(gameTime, seed);

						bodyRotation = Mth.positiveModulo(bodyRotation / 360.0D, 1.0D);
						double angle = SoulCompass.this.getAngleTo(Vec3.atCenterOf(target), entity) / ((float)Math.PI * 2F);
						double otherAngle;

						if (this.wobble.shouldUpdate(gameTime)) {
							this.wobble.update(gameTime, 0.5D - (bodyRotation - 0.25D));
						}

						otherAngle = angle + this.wobble.rotation;

						return Mth.positiveModulo((float)otherAngle, 1.0F);
					} else
						return this.randomAngle(gameTime, seed);
				}
			}

			private float randomAngle(long gameTime, int seed) {
				if (this.wobbleRandom.shouldUpdate(gameTime)) {
					this.wobbleRandom.update(gameTime, Math.random());
				}

				double randomAngle = this.wobbleRandom.rotation + this.hash(seed) / 2.14748365E9F;
				return Mth.positiveModulo((float)randomAngle, 1.0F);
			}

			private int hash(int value) {
				return value * 1327217883;
			}

			@Nullable
			private BlockPos getTargetPosition() {
				return SoulCompass.this.nearestCrystal;
			}

			@Nullable
			private BlockPos getSpawnPosition(ClientLevel level) {
				return level.dimensionType().natural() ? level.getSharedSpawnPos() : null;
			}

			@Nullable
			private BlockPos getLodestonePosition(Level level, CompoundTag tag) {
				boolean flag = tag.contains("LodestonePos");
				boolean flag1 = tag.contains("LodestoneDimension");
				if (flag && flag1) {
					Optional<ResourceKey<Level>> optional = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("LodestoneDimension")).result();
					if (optional.isPresent() && level.dimension() == optional.get())
						return NbtUtils.readBlockPos(tag.getCompound("LodestonePos"));
				}

				return null;
			}

			private double getFrameRotation(ItemFrame frame) {
				Direction direction = frame.getDirection();
				int i = direction.getAxis().isVertical() ? 90 * direction.getAxisDirection().getStep() : 0;
				return Mth.wrapDegrees(180 + direction.get2DDataValue() * 90 + frame.getRotation() * 45 + i);
			}
		});
	}

	private double getAngleTo(Vec3 pos, Entity entity) {
		return Math.atan2(pos.z() - entity.getZ(), pos.x() - entity.getX());
	}

	@OnlyIn(Dist.CLIENT)
	private static class CompassWobble {
		double needleMobility;
		double rotation;
		private double deltaRotation;
		private long lastUpdateTick;

		CompassWobble(double needleMobility) {
			this.needleMobility = needleMobility;
		}

		boolean shouldUpdate(long pGameTime) {
			return this.lastUpdateTick != pGameTime;
		}

		void update(long gameTime, double wobbleAmount) {
			this.lastUpdateTick = gameTime;
			double var = wobbleAmount - this.rotation;
			var = Mth.positiveModulo(var + 0.5D, 1.0D) - 0.5D;
			this.deltaRotation += var * this.needleMobility;
			this.deltaRotation *= 0.8D;

			this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0D);
		}
	}

}
