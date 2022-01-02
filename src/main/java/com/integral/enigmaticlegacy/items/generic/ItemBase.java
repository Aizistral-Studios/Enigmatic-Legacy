package com.integral.enigmaticlegacy.items.generic;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import net.minecraft.world.item.Item.Properties;

public abstract class ItemBase extends Item {
	protected boolean isPlaceholder;

	public ItemBase() {
		this(ItemBase.getDefaultProperties());
	}

	public ItemBase(Properties props) {
		super(props);
		this.isPlaceholder = false;
	}

	@Override
	public void onCraftedBy(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		// Insert existential void here
	}

	public static Properties getDefaultProperties() {
		Properties props = new Item.Properties();

		props.tab(EnigmaticLegacy.enigmaticTab);
		props.stacksTo(64);
		props.rarity(Rarity.COMMON);

		return props;
	}

	public static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
		return Item.getPlayerPOVHitResult(worldIn, player, fluidMode);
	}

	public Item setPlaceholder() {
		this.isPlaceholder = true;
		return this;
	}

	public boolean isPlaceholder() {
		return this.isPlaceholder;
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		ITextComponent superName = super.getName(stack);

		if (this.isPlaceholder) {
			if (superName instanceof TextComponent)
				return ((TextComponent)superName).withStyle(TextFormatting.OBFUSCATED);
		}

		return superName;
	}

	protected static String minimizeNumber(double num) {
		return SuperpositionHandler.minimizeNumber(num);
	}

}
