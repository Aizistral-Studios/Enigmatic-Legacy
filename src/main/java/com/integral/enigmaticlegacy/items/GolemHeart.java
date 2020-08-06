package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
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

public class GolemHeart extends ItemAdvancedCurio implements ISpellstone {

	public Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
	public Multimap<Attribute, AttributeModifier> attributesNoArmor = HashMultimap.create();

	public GolemHeart() {
		super(ItemAdvancedCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "golem_heart"));

		this.immunityList.add(DamageSource.CACTUS.damageType);
		this.immunityList.add(DamageSource.CRAMMING.damageType);
		this.immunityList.add(DamageSource.IN_WALL.damageType);
		this.immunityList.add(DamageSource.FALLING_BLOCK.damageType);
		this.immunityList.add(DamageSource.SWEET_BERRY_BUSH.damageType);

		this.resistanceList.put(DamageSource.GENERIC.damageType, () -> ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asModifierInverted());
		this.resistanceList.put("mob", () -> ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asModifierInverted());
		this.resistanceList.put("explosion", () -> ConfigHandler.GOLEM_HEART_EXPLOSION_RESISTANCE.getValue().asModifierInverted());
		this.resistanceList.put("explosion.player", () -> ConfigHandler.GOLEM_HEART_EXPLOSION_RESISTANCE.getValue().asModifierInverted());
		this.resistanceList.put("player", () -> ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asModifierInverted());

		this.resistanceList.put(DamageSource.MAGIC.damageType, () -> (float) ConfigHandler.GOLEM_HEART_VULNERABILITY_MODIFIER.getValue());
		this.resistanceList.put(DamageSource.DRAGON_BREATH.damageType, () -> (float) ConfigHandler.GOLEM_HEART_VULNERABILITY_MODIFIER.getValue());
	}

	public void initAttributes() {
		this.attributesDefault.put(Attributes.field_233826_i_, new AttributeModifier(UUID.fromString("15faf191-bf21-4654-b359-cc1f4f1243bf"), "GolemHeart DAB", ConfigHandler.GOLEM_HEART_DEFAULT_ARMOR.getValue(), AttributeModifier.Operation.ADDITION));
		this.attributesDefault.put(Attributes.field_233820_c_, new AttributeModifier(UUID.fromString("10faf191-bf21-4554-b359-cc1f4f1233bf"), "GolemHeart KR", ConfigHandler.GOLEM_HEART_KNOCKBACK_RESISTANCE.getValue().asModifier(false), AttributeModifier.Operation.ADDITION));

		this.attributesNoArmor.put(Attributes.field_233826_i_, new AttributeModifier(UUID.fromString("14faf191-bf23-4654-b359-cc1f4f1243bf"), "GolemHeart SAB", ConfigHandler.GOLEM_HEART_SUPER_ARMOR.getValue(), AttributeModifier.Operation.ADDITION));
		this.attributesNoArmor.put(Attributes.field_233827_j_, new AttributeModifier(UUID.fromString("11faf181-bf23-4354-b359-cc1f5f1253bf"), "GolemHeart STB", ConfigHandler.GOLEM_HEART_SUPER_ARMOR_TOUGHNESS.getValue(), AttributeModifier.Operation.ADDITION));
		this.attributesNoArmor.put(Attributes.field_233820_c_, new AttributeModifier(UUID.fromString("12faf181-bf21-4554-b359-cc1f4f1254bf"), "GolemHeart KR", ConfigHandler.GOLEM_HEART_KNOCKBACK_RESISTANCE.getValue().asModifier(false), AttributeModifier.Operation.ADDITION));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.GOLEM_HEART_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeartCooldown", TextFormatting.GOLD, ((ConfigHandler.GOLEM_HEART_COOLDOWN.getValue())) / 20.0F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart4", TextFormatting.GOLD, (int) ConfigHandler.GOLEM_HEART_DEFAULT_ARMOR.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart6", TextFormatting.GOLD, (int) ConfigHandler.GOLEM_HEART_SUPER_ARMOR.getValue(), (int) ConfigHandler.GOLEM_HEART_SUPER_ARMOR_TOUGHNESS.getValue());
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart7", TextFormatting.GOLD, ConfigHandler.GOLEM_HEART_KNOCKBACK_RESISTANCE.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart8", TextFormatting.GOLD, ConfigHandler.GOLEM_HEART_MELEE_RESISTANCE.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart9", TextFormatting.GOLD, ConfigHandler.GOLEM_HEART_EXPLOSION_RESISTANCE.getValue().asPercentage() + "%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.golemHeart11");
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
	public void onUnequip(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;
			
			AttributeModifierManager map = player.func_233645_dx_();
			map.func_233785_a_(this.attributesDefault);
			map.func_233785_a_(this.attributesNoArmor);
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living) {
		if (living instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) living;

			AttributeModifierManager map = player.func_233645_dx_();
			int armorAmount = 0;
			
			for (ItemStack stack : player.getArmorInventoryList()) {
				if (!stack.isEmpty())
					armorAmount++;
			}

			if (armorAmount != 0) {
				// Removes attributes
				map.func_233785_a_(this.attributesDefault);
				map.func_233785_a_(this.attributesNoArmor);

				// Applies new attributes
				map.func_233793_b_(this.attributesDefault);
			} else {
				// Removes attributes
				map.func_233785_a_(this.attributesDefault);
				map.func_233785_a_(this.attributesNoArmor);

				// Applies new attributes
				map.func_233793_b_(this.attributesNoArmor);
			}

		}
	}

}
