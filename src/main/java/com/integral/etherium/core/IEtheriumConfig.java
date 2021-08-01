package com.integral.etherium.core;

import com.integral.enigmaticlegacy.objects.Perhaps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.SoundEvent;

public interface IEtheriumConfig {

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

	public void knockBack(PlayerEntity entityIn, float strength, double xRatio, double zRatio);

	public boolean isStandalone();
}
