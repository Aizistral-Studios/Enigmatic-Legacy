package com.integral.enigmaticlegacy.items.generic;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.client.models.UnseenArmorModel;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fml.DistExecutor;

import net.minecraft.world.item.Item.Properties;

@SuppressWarnings("deprecation")
public abstract class ItemBaseArmor extends ArmorItem implements IItemRenderProperties {
	@OnlyIn(Dist.CLIENT)
	private HumanoidModel<?> model;

	public ItemBaseArmor(ArmorMaterial materialIn, EquipmentSlot slot, Properties builder) {
		super(materialIn, slot, builder);
	}

	public ItemBaseArmor(ArmorMaterial materialIn, EquipmentSlot slot) {
		this(materialIn, slot, ItemBaseArmor.getDefaultProperties());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	@SuppressWarnings("unchecked")
	public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A original) {
		return (A) this.provideArmorModelForSlot(armorSlot, original);
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

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.tab(EnigmaticLegacy.enigmaticTab);
		props.stacksTo(1);
		props.rarity(Rarity.COMMON);

		return props;
	}

}
