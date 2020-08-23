package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemAdvancedCurio;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MagmaHeart extends ItemAdvancedCurio implements ISpellstone {

	public List<String> nemesisList = new ArrayList<String>();

	public MagmaHeart() {
		super(ItemAdvancedCurio.getDefaultProperties().rarity(Rarity.UNCOMMON).func_234689_a_());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "magma_heart"));

		this.immunityList.add(DamageSource.LAVA.damageType);
		this.immunityList.add(DamageSource.IN_FIRE.damageType);
		this.immunityList.add(DamageSource.ON_FIRE.damageType);
		this.immunityList.add(DamageSource.HOT_FLOOR.damageType);
		//immunityList.add("fireball");

		this.nemesisList.add("mob");
		this.nemesisList.add(DamageSource.GENERIC.damageType);
		this.nemesisList.add("player");
		//nemesisList.add("arrow");
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.MAGMA_HEART_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeartCooldown", TextFormatting.GOLD, ((ConfigHandler.BLAZING_CORE_COOLDOWN.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magmaHeart9");
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
		if (living.isBurning())
			living.extinguish();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

}
