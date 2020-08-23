package com.integral.enigmaticlegacy.items.generic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

@SuppressWarnings("deprecation")
public abstract class ItemBaseArmor extends ArmorItem implements IPerhaps {

	private final LazyValue<BipedModel<?>> model;

	public ItemBaseArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
		super(materialIn, slot, builder);

		this.model = DistExecutor.runForDist(() -> () -> new LazyValue<>(() -> this.provideArmorModelForSlot(slot)),
				() -> () -> null);
	}

	public ItemBaseArmor(IArmorMaterial materialIn, EquipmentSlotType slot) {
		this(materialIn, slot, ItemBaseArmor.getDefaultProperties());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A original) {
		return (A) this.model.getValue();
		//return super.getArmorModel(entityLiving, itemStack, armorSlot, original);
	}

	@OnlyIn(Dist.CLIENT)
	@Nullable
	public BipedModel<?> provideArmorModelForSlot(EquipmentSlotType slot) {
		BipedModel<LivingEntity> model = new BipedModel<LivingEntity>(0.5F);
		model.setVisible(false);
		return model;
	}

	@Nonnull
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return EnigmaticLegacy.MODID + ":textures/models/armor/unseen_armor.png";
	}

	@Override
	public boolean isForMortals() {
		return true;
	}

	public boolean hasFullSet(@Nonnull PlayerEntity player) {
		if (player == null)
			return false;

		for (ItemStack stack : player.getArmorInventoryList()) {
			if (!(stack.getItem().getClass() == this.getClass()))
				return false;
		}

		return true;
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.group(EnigmaticLegacy.enigmaticTab);
		props.maxStackSize(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
