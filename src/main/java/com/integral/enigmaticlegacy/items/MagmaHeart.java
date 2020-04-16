package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class MagmaHeart extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public static List<String> immunityList = new ArrayList<String>();
	public static HashMap<String, Float> resistanceList = new HashMap<String, Float>();
	public static List<String> nemesisList = new ArrayList<String>();

	public MagmaHeart(Properties properties) {
		super(properties);

		MagmaHeart.immunityList.add(DamageSource.LAVA.damageType);
		MagmaHeart.immunityList.add(DamageSource.IN_FIRE.damageType);
		MagmaHeart.immunityList.add(DamageSource.ON_FIRE.damageType);
		MagmaHeart.immunityList.add(DamageSource.HOT_FLOOR.damageType);
		//immunityList.add("fireball");

		MagmaHeart.nemesisList.add("mob");
		MagmaHeart.nemesisList.add(DamageSource.GENERIC.damageType);
		MagmaHeart.nemesisList.add("player");
		//nemesisList.add("arrow");
	}

	public static Properties setupIntegratedProperties() {
		MagmaHeart.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		MagmaHeart.integratedProperties.maxStackSize(1);
		MagmaHeart.integratedProperties.rarity(Rarity.UNCOMMON);

		return MagmaHeart.integratedProperties;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.magmaHeart))
			return false;
		else
			return true;
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MAGMA_HEART_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart1");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeartCooldown", ((ConfigHandler.BLAZING_CORE_COOLDOWN.getValue())) / 20.0F);
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart3");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart4");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart5");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart6");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart7");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart8");
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

	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (SuperpositionHandler.hasSpellstoneCooldown(player))
			return;
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity living) {
		if (living.isBurning())
			living.extinguish();
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public void onEquipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

}
