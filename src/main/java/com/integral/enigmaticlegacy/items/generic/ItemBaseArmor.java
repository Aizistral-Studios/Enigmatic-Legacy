package com.integral.enigmaticlegacy.items.generic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.LazyValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import net.minecraft.world.item.Item.Properties;

@SuppressWarnings("deprecation")
public abstract class ItemBaseArmor extends ArmorItem {

	private final LazyValue<BipedModel<?>> model;

	public ItemBaseArmor(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
		super(materialIn, slot, builder);

		this.model = DistExecutor.runForDist(() -> () -> new LazyValue<>(() -> this.provideArmorModelForSlot(slot)),
				() -> () -> null);
	}

	public ItemBaseArmor(ArmorMaterial materialIn, EquipmentSlot slot) {
		this(materialIn, slot, ItemBaseArmor.getDefaultProperties());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A original) {
		return (A) this.model.get();
		//return super.getArmorModel(entityLiving, itemStack, armorSlot, original);
	}

	@OnlyIn(Dist.CLIENT)
	@Nullable
	public BipedModel<?> provideArmorModelForSlot(EquipmentSlot slot) {
		BipedModel<LivingEntity> model = new BipedModel<LivingEntity>(0.5F);
		model.setAllVisible(false);
		return model;
	}

	@Nonnull
	@Override
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

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.tab(EnigmaticLegacy.enigmaticTab);
		props.stacksTo(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
