package com.integral.enigmaticlegacy.api.items;

import java.util.List;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import top.theillusivec4.curios.api.type.capability.ICurio;

import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

public class ItemizedCurioInstance implements ICurio {
	private final ItemStack stackInstance;
	private final IItemCurio curioItem;

	public ItemizedCurioInstance(IItemCurio curio, ItemStack stack) {
		this.curioItem = curio;
		this.stackInstance = stack;
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity livingEntity) {
		return this.curioItem.canEquip(identifier, livingEntity, this.stackInstance);
	}

	@Override
	public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
		return this.curioItem.canRender(identifier, index, livingEntity, this.stackInstance);
	}

	@Override
	public boolean canRightClickEquip() {
		// Entity unknown
		return this.curioItem.canRightClickEquip(this.stackInstance);
	}

	@Override
	public boolean canSync(String identifier, int index, LivingEntity livingEntity) {
		return this.curioItem.canSync(identifier, index, livingEntity);
	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity livingEntity) {
		// Index unknown
		return this.curioItem.canUnequip(identifier, livingEntity, this.stackInstance);
	}

	@Override
	public void curioAnimate(String identifier, int index, LivingEntity livingEntity) {
		this.curioItem.curioAnimate(identifier, index, livingEntity, this.stackInstance);
	}

	@Override
	public void curioBreak(ItemStack stack, LivingEntity livingEntity) {
		this.curioItem.curioBreak(stack, livingEntity);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
		return this.curioItem.getAttributeModifiers(identifier, this.stackInstance);
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity livingEntity) {
		this.curioItem.curioTick(identifier, index, livingEntity, this.stackInstance);
	}

	@Override
	public DropRule getDropRule(LivingEntity livingEntity) {
		// No identifier
		return this.curioItem.getDropRule(livingEntity, this.stackInstance);
	}

	@Override
	public int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curioStack, int index) {
		return this.curioItem.getFortuneBonus(identifier, livingEntity, curioStack, index);
	}

	@Override
	public int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curioStack, int index) {
		return this.curioItem.getLootingBonus(identifier, livingEntity, curioStack, index);
	}

	@Override
	public List<ITextComponent> getTagsTooltip(List<ITextComponent> tagTooltips) {
		return this.curioItem.getTagsTooltip(tagTooltips);
	}

	@Override
	public void onEquip(String identifier, int index, LivingEntity livingEntity) {
		this.curioItem.onEquip(identifier, index, livingEntity, this.stackInstance);
	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity livingEntity) {
		this.curioItem.onUnequip(identifier, index, livingEntity, this.stackInstance);
	}

	@Override
	public void playRightClickEquipSound(LivingEntity livingEntity) {
		// No identifier
		this.curioItem.playRightClickEquipSound(livingEntity, this.stackInstance);
	}

	@Override
	public void readSyncData(CompoundNBT compound) {
		this.curioItem.readSyncData(compound);
	}

	@Override
	public void render(String identifier, int index, PoseStack PoseStack, IRenderTypeBuffer renderTypeBuffer,
			int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch) {

		this.curioItem.render(identifier, index, PoseStack, renderTypeBuffer, light, livingEntity, limbSwing,
				limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, this.stackInstance);
	}

	@Override
	public boolean showAttributesTooltip(String identifier) {
		return this.curioItem.showAttributesTooltip(identifier, this.stackInstance);
	}

	@Override
	public CompoundNBT writeSyncData() {
		return this.curioItem.writeSyncData();
	}

}
