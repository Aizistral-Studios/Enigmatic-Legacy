package com.integral.etherium.core;

import java.util.Optional;

import com.integral.enigmaticlegacy.objects.Perhaps;
import com.integral.etherium.items.EtheriumAxe;
import com.integral.etherium.items.EtheriumPickaxe;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.IArmorMaterial;
import net.minecraft.world.item.IItemTier;
import net.minecraft.world.item.ItemGroup;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.ModList;

public interface IEtheriumConfig {

	public Ingredient getRepairMaterial();

	public ItemGroup getCreativeTab();

	public String getOwnerMod();

	public IArmorMaterial getArmorMaterial();

	public IItemTier getToolMaterial();

	public Perhaps getShieldThreshold();

	public Perhaps getShieldReduction();

	public boolean disableAOEShiftInhibition();

	public SoundEvent getAOESoundOn();

	public SoundEvent getAOESoundOff();

	public SoundEvent getShieldTriggerSound();

	public int getAxeMiningVolume();

	public int getScytheMiningVolume();

	public int getPickaxeMiningRadius();

	public int getPickaxeMiningDepth();

	public int getShovelMiningRadius();

	public int getShovelMiningDepth();

	public int getSwordCooldown();

	public void knockBack(Player entityIn, float strength, double xRatio, double zRatio);

	public boolean isStandalone();

	public default Optional<Material> getSorceryMaterial(String name) {
		if (ModList.get().isLoaded("astralsorcery")) {
			try {
				Class<?> sorceryBlockMaterials = Class.forName("hellfirepvp.astralsorcery.common.lib.MaterialsAS");
				Material material = (Material) sorceryBlockMaterials.getField(name).get(null);
				return Optional.ofNullable(material);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return Optional.empty();
	}

}
