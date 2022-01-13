package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class XPScroll extends ItemBaseCurio {
	public static Omniconfig.DoubleParameter xpCollectionRange;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("XPScroll");

		xpCollectionRange = builder
				.comment("Range in which Scroll of Ageless Wisdom collects experience orbs when active.")
				.min(1)
				.max(128)
				.getDouble("CollectionRange", 16.0);

		builder.popPrefix();
	}

	public final int xpPortion = 5;

	public XPScroll() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "xp_scroll"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		TranslatableComponent cMode;
		if (!ItemNBTHelper.getBoolean(stack, "IsActive", false)) {
			cMode = new TranslatableComponent("tooltip.enigmaticlegacy.xpTomeDeactivated");
		} else if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true)) {
			cMode = new TranslatableComponent("tooltip.enigmaticlegacy.xpTomeAbsorption");
		} else {
			cMode = new TranslatableComponent("tooltip.enigmaticlegacy.xpTomeExtraction");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome4_5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome11", ChatFormatting.GOLD, (int) xpCollectionRange.getValue());
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeMode", null, cMode.getString());
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeStoredXP");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeUnits", ChatFormatting.GOLD, ItemNBTHelper.getInt(stack, "XPStored", 0), ExperienceHelper.getLevelForExperience(ItemNBTHelper.getInt(stack, "XPStored", 0)));

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", ChatFormatting.LIGHT_PURPLE, KeyMapping.createNameSupplier("key.xpScroll").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand handIn) {
		ItemStack stack = player.getItemInHand(handIn);
		this.trigger(world, stack, player, handIn, true);

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);

	}

	public void trigger(Level world, ItemStack stack, Player player, InteractionHand hand, boolean swing) {

		if (!player.isCrouching()) {

			if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true)) {
				ItemNBTHelper.setBoolean(stack, "AbsorptionMode", false);
				world.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			} else {
				ItemNBTHelper.setBoolean(stack, "AbsorptionMode", true);
				world.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		} else {

			if (ItemNBTHelper.getBoolean(stack, "IsActive", false)) {
				ItemNBTHelper.setBoolean(stack, "IsActive", false);
				world.playSound(null, player.blockPosition(), EnigmaticLegacy.HHOFF, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			} else {
				ItemNBTHelper.setBoolean(stack, "IsActive", true);
				world.playSound(null, player.blockPosition(), EnigmaticLegacy.HHON, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			}
		}

		if (swing) {
			player.swing(hand);
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "IsActive", false);
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity entity, ItemStack stack) {

		ItemStack itemstack = SuperpositionHandler.getCurioStack(entity, EnigmaticLegacy.xpScroll);

		if (!(entity instanceof Player) || entity.level.isClientSide || !ItemNBTHelper.getBoolean(itemstack, "IsActive", false))
			return;

		Player player = (Player) entity;
		Level world = player.level;

		if (ItemNBTHelper.getBoolean(itemstack, "AbsorptionMode", true)) {

			if (ExperienceHelper.getPlayerXP(player) >= this.xpPortion) {
				ExperienceHelper.drainPlayerXP(player, this.xpPortion);
				ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + this.xpPortion);
			} else if (ExperienceHelper.getPlayerXP(player) > 0 & ExperienceHelper.getPlayerXP(player) < this.xpPortion) {
				int exp = ExperienceHelper.getPlayerXP(player);
				ExperienceHelper.drainPlayerXP(player, exp);
				ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + exp);
			}

		} else {
			int xp = ItemNBTHelper.getInt(itemstack, "XPStored", 0);

			if (xp >= this.xpPortion) {
				ItemNBTHelper.setInt(itemstack, "XPStored", xp - this.xpPortion);
				ExperienceHelper.addPlayerXP(player, this.xpPortion);
			} else if (xp > 0 & xp < this.xpPortion) {
				ItemNBTHelper.setInt(itemstack, "XPStored", 0);
				ExperienceHelper.addPlayerXP(player, xp);
			}

		}

		List<ExperienceOrb> orbs = world.getEntitiesOfClass(ExperienceOrb.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, xpCollectionRange.getValue()));
		for (ExperienceOrb processed : orbs) {
			if (!processed.isAlive()) {
				continue;
			}

			player.takeXpDelay = 0;
			processed.playerTouch(player);
			//processed.setPositionAndUpdate(player.posX, player.posY, player.posZ);
		}
	}

	@Override
	public boolean canRightClickEquip(ItemStack stack) {
		return false;
	}

}
