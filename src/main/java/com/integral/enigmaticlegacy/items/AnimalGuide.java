package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Optional;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AnimalGuide extends ItemBase implements IVanishable {

	public AnimalGuide() {
		super(getDefaultProperties().maxStackSize(1).rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "animal_guide"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.animalGuide1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.animalGuide2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.animalGuide3");

			if (Minecraft.getInstance().player != null && SuperpositionHandler.isTheCursedOne(Minecraft.getInstance().player)) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.animalGuide4");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.animalGuide5");
			}
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof PlayerEntity) || worldIn.isRemote)
			return;

		PlayerEntity player = (PlayerEntity) entityIn;

		List<HoglinEntity> hoglins = entityIn.world.getEntitiesWithinAABB(HoglinEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, 24));

		for (HoglinEntity hoglin : hoglins)
			if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.animalGuide)) {
				if (hoglin.getAttackTarget() == player || hoglin.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null) == player) {
					hoglin.setAttackTarget(null);
					hoglin.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, Optional.empty());
					hoglin.getBrain().setMemory(MemoryModuleType.PACIFIED, true);
				}
			}

	}

}