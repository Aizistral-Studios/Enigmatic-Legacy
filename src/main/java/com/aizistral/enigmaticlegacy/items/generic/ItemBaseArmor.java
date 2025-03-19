package com.aizistral.enigmaticlegacy.items.generic;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICreativeTabMember;
import com.aizistral.enigmaticlegacy.client.models.UnseenArmorModel;
import com.aizistral.enigmaticlegacy.registries.EnigmaticTabs;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

@SuppressWarnings("deprecation")
public abstract class ItemBaseArmor extends ArmorItem implements ICreativeTabMember {
	@OnlyIn(Dist.CLIENT)
	private HumanoidModel<?> model;

	public ItemBaseArmor(ArmorMaterial materialIn, ArmorItem.Type slot, Properties builder) {
		super(materialIn, slot, builder);
	}

	public ItemBaseArmor(ArmorMaterial materialIn, ArmorItem.Type slot) {
		this(materialIn, slot, ItemBaseArmor.getDefaultProperties());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			@OnlyIn(Dist.CLIENT)
			@SuppressWarnings("unchecked")
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> original) {
				return ItemBaseArmor.this.provideArmorModelForSlot(armorSlot, original);
			}
		});
	}

	@Nullable
	@OnlyIn(Dist.CLIENT)
	public HumanoidModel<?> provideArmorModelForSlot(EquipmentSlot slot, HumanoidModel<?> original) {
		return this.model != null ? this.model : (this.model = new UnseenArmorModel<>(original));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return EnigmaticLegacy.MODID + ":textures/models/armor/unseen_armor.png";
	}

	public boolean hasFullSet(@Nonnull Player player) {
		if (player == null)
			return false;

		for (ItemStack stack : player.getArmorSlots()) {
			if (!(stack.getItem().getClass() == this.getClass()))
				return false;
		}

		return true;
	}

	@Override
	public CreativeModeTab getCreativeTab() {
		return EnigmaticTabs.MAIN;
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.stacksTo(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
