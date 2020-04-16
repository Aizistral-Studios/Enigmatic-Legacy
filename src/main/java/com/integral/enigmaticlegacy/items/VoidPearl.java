package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.ObfuscatedFields;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class VoidPearl extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public static List<String> immunityList = new ArrayList<String>();
	public static List<String> healList = new ArrayList<String>();
	public static HashMap<String, Supplier<Float>> resistanceList = new HashMap<String, Supplier<Float>>();
	public static DamageSource theDarkness;

	public VoidPearl(Properties properties) {
		super(properties);

		VoidPearl.immunityList.add(DamageSource.DROWN.damageType);
		VoidPearl.immunityList.add(DamageSource.IN_WALL.damageType);

		VoidPearl.healList.add(DamageSource.WITHER.damageType);
		VoidPearl.healList.add(DamageSource.MAGIC.damageType);

		VoidPearl.theDarkness = new DamageSource("darkness");
		VoidPearl.theDarkness.setDamageIsAbsolute();
		VoidPearl.theDarkness.setDamageBypassesArmor();
		VoidPearl.theDarkness.setMagicDamage();

	}

	public static Properties setupIntegratedProperties() {
		VoidPearl.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		VoidPearl.integratedProperties.maxStackSize(1);
		VoidPearl.integratedProperties.rarity(Rarity.EPIC);

		return VoidPearl.integratedProperties;

	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.VOID_PEARL_ENABLED.getValue();
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.voidPearl))
			return false;
		else
			return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl1");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearlCooldown", ((ConfigHandler.VOID_PEARL_COOLDOWN.getValue())) / 20.0F);
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl3");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl4");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl5");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl6");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl7");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl8");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl9");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl10");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl11");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl12", ConfigHandler.VOID_PEARL_UNDEAD_PROBABILITY.getValue().asPercentage() + "%");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl13");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.spellstoneAbility").get().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	public void triggerActiveAbility(World world, PlayerEntity player, ItemStack stack) {
		// Insert existential void here
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public void onEquipped(String identifier, LivingEntity living) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity living) {
		// Insert existential void here
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {

		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			FoodStats stats = player.getFoodStats();
			stats.setFoodLevel(20);

			try {
				ObfuscatedFields.foodSaturationField.setFloat(stats, 0F);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (player.getAir() < 300)
				player.setAir(300);

			if (player.ticksExisted % 10 == 0) {
				List<LivingEntity> entities = living.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(player.getPosX() - ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.getPosY() - ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.getPosZ() - ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.getPosX() + ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.getPosY() + ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue(), player.getPosZ() + ConfigHandler.VOID_PEARL_SHADOW_RANGE.getValue()));

				if (entities.contains(player))
					entities.remove(player);

				for (LivingEntity victim : entities) {
					if (victim.world.getNeighborAwareLightSubtracted(victim.getPosition(), 0) < 3) {

						if (victim instanceof PlayerEntity) {
							PlayerEntity playerVictim = (PlayerEntity) victim;
							if (SuperpositionHandler.hasCurio(playerVictim, EnigmaticLegacy.voidPearl)) {
								playerVictim.addPotionEffect(new EffectInstance(Effects.WITHER, 80, 1, false, true));
								continue;
							}
						}

						//if (player.ticksExisted % 20 == 0) {
						victim.attackEntityFrom(VoidPearl.theDarkness, (float) ConfigHandler.VOID_PEARL_BASE_DARKNESS_DAMAGE.getValue());
						living.world.playSound(null, victim.getPosition(), SoundEvents.ENTITY_PHANTOM_BITE, SoundCategory.PLAYERS, 1.0F, (float) (0.3F + (Math.random() * 0.4D)));
						//}

						victim.addPotionEffect(new EffectInstance(Effects.WITHER, 80, 1, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 100, 0, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.HUNGER, 160, 2, false, true));
						victim.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 100, 3, false, true));
					}
				}
			}
		}

	}

}
