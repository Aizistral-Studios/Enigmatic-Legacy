package com.integral.enigmaticlegacy.helpers;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.enchantments.CeaselessEnchantment;

import net.minecraft.world.entity.ICrossbowUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ProjectileEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
					((PlayerEntity) living).inventory.removeItem(ammo);
				}
			} else {
				itemstack = ammo.copy();
			}

			CrossbowItem.addChargedProjectile(crossbow, itemstack);
			return true;
		}
	}

	public static boolean hasAmmo(LivingEntity entityIn, ItemStack weaponStack) {
		int requestedAmmo = 1;
		boolean isCreative = entityIn instanceof PlayerEntity && ((PlayerEntity) entityIn).abilities.instabuild;
		ItemStack itemstack = entityIn.getProjectile(weaponStack);
		ItemStack itemstack1 = itemstack.copy();

		for (int k = 0; k < requestedAmmo; ++k) {
			if (k > 0) {
				itemstack = itemstack1.copy();
			}

			if (itemstack.isEmpty()) {
				if (isCreative || (EnigmaticEnchantmentHelper.hasCeaselessEnchantment(weaponStack) && CeaselessEnchantment.allowNoArrow.getValue())) {
					itemstack = new ItemStack(Items.ARROW);
					itemstack1 = itemstack.copy();
				}
			}

			if (!CrossbowHelper.tryCharge(entityIn, weaponStack, (itemstack.getItem().getClass() == ArrowItem.class && EnigmaticEnchantmentHelper.hasCeaselessEnchantment(weaponStack)) ? itemstack.copy() : itemstack, k > 0, isCreative))
				return false;
		}

		return true;
	}

	public static void fireProjectiles(World worldIn, LivingEntity shooter, Hand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
		List<ItemStack> list = CrossbowItem.getChargedProjectiles(stack);
		float[] afloat = CrossbowItem.getShotPitches(shooter.getRandom());

		for (int i = 0; i < list.size(); ++i) {
			ItemStack itemstack = list.get(i);
			boolean flag = shooter instanceof PlayerEntity && ((PlayerEntity) shooter).abilities.instabuild;
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

		CrossbowItem.onCrossbowShot(worldIn, shooter, stack);
	}

	private static void fireProjectile(World worldIn, LivingEntity shooter, Hand handIn, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
		if (!worldIn.isClientSide) {
			boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
			ProjectileEntity projectileentity;
			if (flag) {
				projectileentity = new FireworkRocketEntity(worldIn, projectile, shooter, shooter.getX(), shooter.getEyeY() - 0.15F, shooter.getZ(), true);
			} else {
				projectileentity = CrossbowItem.getArrow(worldIn, shooter, crossbow, projectile);
				if (isCreativeMode || projectileAngle != 0.0F || EnigmaticEnchantmentHelper.hasCeaselessEnchantment(crossbow)) {
					((AbstractArrowEntity) projectileentity).pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;	
				}
				
				if (EnigmaticEnchantmentHelper.hasSharpshooterEnchantment(crossbow)) {
					((AbstractArrowEntity) projectileentity).addTag(CrossbowHelper.sharpshooterTagPrefix + EnigmaticEnchantmentHelper.getSharpshooterLevel(crossbow));
				}
			}

			if (shooter instanceof ICrossbowUser) {
				ICrossbowUser icrossbowuser = (ICrossbowUser)shooter;
				icrossbowuser.shootCrossbowProjectile(icrossbowuser.getTarget(), crossbow, projectileentity, projectileAngle);
			} else {
				Vector3d vector3d1 = shooter.getUpVector(1.0F);
				Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
				Vector3d vector3d = shooter.getViewVector(1.0F);
				Vector3f vector3f = new Vector3f(vector3d);
				vector3f.transform(quaternion);
				projectileentity.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);
			}

			crossbow.hurtAndBreak(flag ? 3 : 1, shooter, (p_220017_1_) -> {
				p_220017_1_.broadcastBreakEvent(handIn);
			});
			worldIn.addFreshEntity(projectileentity);
			worldIn.playSound((PlayerEntity)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
		}
	}

}