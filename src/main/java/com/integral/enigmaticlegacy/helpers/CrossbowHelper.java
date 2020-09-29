package com.integral.enigmaticlegacy.helpers;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;

public class CrossbowHelper {

	public static final String sharpshooterTagPrefix = EnigmaticLegacy.MODID+":sharpshot:";

	public static boolean tryCharge(LivingEntity living, ItemStack crossbow, ItemStack ammo, boolean bonusCycles, boolean isCreative) {
		if (ammo.isEmpty())
			return false;
		else {
			boolean creativeUsingArrows = isCreative && ammo.getItem() instanceof ArrowItem;
			ItemStack itemstack;
			if (!creativeUsingArrows && !isCreative && !bonusCycles) {
				itemstack = ammo.split(1);
				if (ammo.isEmpty() && living instanceof PlayerEntity) {
					((PlayerEntity) living).inventory.deleteStack(ammo);
				}
			} else {
				itemstack = ammo.copy();
			}

			CrossbowItem.addChargedProjectile(crossbow, itemstack);
			return true;
		}
	}

	public static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
		int requestedAmmo = 1;
		boolean isCreative = entityIn instanceof PlayerEntity && ((PlayerEntity) entityIn).abilities.isCreativeMode;
		ItemStack itemstack = entityIn.findAmmo(stack);
		ItemStack itemstack1 = itemstack.copy();

		for (int k = 0; k < requestedAmmo; ++k) {
			if (k > 0) {
				itemstack = itemstack1.copy();
			}

			if (itemstack.isEmpty() && isCreative) {
				itemstack = new ItemStack(Items.ARROW);
				itemstack1 = itemstack.copy();
			}

			if (!CrossbowHelper.tryCharge(entityIn, stack, (itemstack.getItem().getClass() == ArrowItem.class && EnigmaticEnchantmentHelper.hasCeaselessEnchantment(stack)) ? itemstack.copy() : itemstack, k > 0, isCreative))
				return false;
		}

		return true;
	}

	public static void fireProjectiles(World worldIn, LivingEntity shooter, Hand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
		List<ItemStack> list = CrossbowItem.getChargedProjectiles(stack);
		float[] afloat = CrossbowItem.getRandomSoundPitches(shooter.getRNG());

		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemstack = list.get(i);
			boolean flag = shooter instanceof PlayerEntity && ((PlayerEntity) shooter).abilities.isCreativeMode;
			if (!itemstack.isEmpty()) {
				if (i == 0) {
					CrossbowHelper.fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
				} else if (i == 1) {
					CrossbowHelper.fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, -10.0F);
				} else if (i == 2) {
					CrossbowHelper.fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 10.0F);
				}
			}
		}

		CrossbowItem.fireProjectilesAfter(worldIn, shooter, stack);
	}

	private static void fireProjectile(World worldIn, LivingEntity shooter, Hand handIn, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
		if (!worldIn.isRemote) {
			boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
			ProjectileEntity projectileentity;
			if (flag) {
				projectileentity = new FireworkRocketEntity(worldIn, projectile, shooter, shooter.getPosX(), shooter.getPosYEye() - 0.15F, shooter.getPosZ(), true);
			} else {
				projectileentity = CrossbowItem.createArrow(worldIn, shooter, crossbow, projectile);
				if (isCreativeMode || projectileAngle != 0.0F || EnigmaticEnchantmentHelper.hasSharpshooterEnchantment(crossbow)) {
					((AbstractArrowEntity) projectileentity).pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;

					if (EnigmaticEnchantmentHelper.hasSharpshooterEnchantment(crossbow)) {
						((AbstractArrowEntity) projectileentity).addTag(CrossbowHelper.sharpshooterTagPrefix + EnigmaticEnchantmentHelper.getSharpshooterLevel(crossbow));
					}
				}
			}

			if (shooter instanceof ICrossbowUser) {
				ICrossbowUser icrossbowuser = (ICrossbowUser)shooter;
				icrossbowuser.func_230284_a_(icrossbowuser.getAttackTarget(), crossbow, projectileentity, projectileAngle);
			} else {
				Vector3d vector3d1 = shooter.getUpVector(1.0F);
				Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
				Vector3d vector3d = shooter.getLook(1.0F);
				Vector3f vector3f = new Vector3f(vector3d);
				vector3f.transform(quaternion);
				projectileentity.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(), velocity, inaccuracy);
			}

			crossbow.damageItem(flag ? 3 : 1, shooter, (p_220017_1_) -> {
				p_220017_1_.sendBreakAnimation(handIn);
			});
			worldIn.addEntity(projectileentity);
			worldIn.playSound((PlayerEntity)null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
		}
	}

}