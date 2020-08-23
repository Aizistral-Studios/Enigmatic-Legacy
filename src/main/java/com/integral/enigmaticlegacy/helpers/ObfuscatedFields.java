package com.integral.enigmaticlegacy.helpers;

import java.lang.reflect.Field;

import com.integral.enigmaticlegacy.EnigmaticLegacy;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.WorldOptionsScreen;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.FoodStats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * List of obfuscated private/protected fields from various classes,
 * that are required to be alterable by the mod.
 * @author Integral
 */

public class ObfuscatedFields {

	/**
	 * Class: CreateWorldScreen.class,
	 * Deobfuscated Name: worldNameField
	 */
	public static Field worldNameField;

	/**
	 * Class: CreateWorldScreen.class,
	 * Deobfuscated Name: worldSeedField
	 */
	public static Field worldSeedField;

	/**
	 * Class: FoodStats.class,
	 * Deobfuscated Name: foodSaturationLevel
	 */
	public static Field foodSaturationField;

	/**
	 * Class: BeaconTileEntity.class,
	 * Deobfuscated Name: beamSegments
	 */
	public static Field beamSegmentsField;

	/**
	 * Class: EnchantmentContainer.class,
	 * Deobfuscated Name: beamSegments
	 */
	public static Field tableInventoryField;

	/**
	 * Class: EnchantmentContainer.class,
	 * Deobfuscated Name: field_217006_g
	 */
	public static Field worldPosCallableField;

	/**
	 * Class: ModelRenderer.class,
	 * Deobfuscated Name: cubeList
	 */
	public static Field cubeListField;

	public static void extractCommonFields() {
		try {
			ObfuscatedFields.foodSaturationField = ObfuscationReflectionHelper.findField(FoodStats.class, "field_75125_b");
			ObfuscatedFields.beamSegmentsField = ObfuscationReflectionHelper.findField(BeaconTileEntity.class, "field_174909_f");
			ObfuscatedFields.tableInventoryField = ObfuscationReflectionHelper.findField(EnchantmentContainer.class, "field_75168_e");
			ObfuscatedFields.worldPosCallableField = ObfuscationReflectionHelper.findField(EnchantmentContainer.class, "field_217006_g");
		} catch (Exception ex) {
			EnigmaticLegacy.enigmaticLogger.fatal("Could not extract common obfuscated fields via reflection!");
			EnigmaticLegacy.enigmaticLogger.catching(ex);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void extractClientFields() {
		try {
			ObfuscatedFields.worldNameField = ObfuscationReflectionHelper.findField(CreateWorldScreen.class, "field_146333_g");
			ObfuscatedFields.worldSeedField = ObfuscationReflectionHelper.findField(WorldOptionsScreen.class, "field_239033_g_");
		} catch (Exception ex) {
			EnigmaticLegacy.enigmaticLogger.fatal("Could not extract client obfuscated fields via reflection!");
			EnigmaticLegacy.enigmaticLogger.catching(ex);
		}
	}

}
