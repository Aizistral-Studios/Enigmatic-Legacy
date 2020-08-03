package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ObfuscatedFields;
import com.integral.enigmaticlegacy.items.generic.ItemAdvancedCurio;
import com.integral.enigmaticlegacy.mixin.AccessorFoodStats;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VoidPearl extends ItemAdvancedCurio implements ISpellstone {
	public List<String> healList = new ArrayList<String>();
	public DamageSource theDarkness;

	public VoidPearl() {
		super(ItemAdvancedCurio.getDefaultProperties().maxStackSize(1).rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "void_pearl"));

		this.immunityList.add(DamageSource.DROWN.damageType);
		this.immunityList.add(DamageSource.IN_WALL.damageType);

		this.healList.add(DamageSource.WITHER.damageType);
		this.healList.add(DamageSource.MAGIC.damageType);

		this.theDarkness = new DamageSource("darkness");
		this.theDarkness.setDamageIsAbsolute();
		this.theDarkness.setDamageBypassesArmor();
		this.theDarkness.setMagicDamage();

	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.VOID_PEARL_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearlCooldown", TextFormatting.GOLD, ((ConfigHandler.VOID_PEARL_COOLDOWN.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl12", TextFormatting.GOLD, ConfigHandler.VOID_PEARL_UNDEAD_PROBABILITY.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.voidPearl13");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", TextFormatting.LIGHT_PURPLE, KeyBinding.getDisplayString("key.spellstoneAbility").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living) {

		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;
			

			FoodStats stats = player.getFoodStats();
			stats.setFoodLevel(20);
			
			// TODO Ask the God why this doesn't work
			//((AccessorFoodStats) stats).setFoodSaturationLevel(0);

			
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
					if (victim.world.getNeighborAwareLightSubtracted(victim.func_233580_cy_(), 0) < 3) {

						if (victim instanceof PlayerEntity) {
							PlayerEntity playerVictim = (PlayerEntity) victim;
							if (SuperpositionHandler.hasCurio(playerVictim, EnigmaticLegacy.voidPearl)) {
								playerVictim.addPotionEffect(new EffectInstance(Effects.WITHER, 80, 1, false, true));
								continue;
							}
						}

						//if (player.ticksExisted % 20 == 0) {
						victim.attackEntityFrom(this.theDarkness, (float) ConfigHandler.VOID_PEARL_BASE_DARKNESS_DAMAGE.getValue());
						living.world.playSound(null, victim.func_233580_cy_(), SoundEvents.ENTITY_PHANTOM_BITE, SoundCategory.PLAYERS, 1.0F, (float) (0.3F + (Math.random() * 0.4D)));
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
