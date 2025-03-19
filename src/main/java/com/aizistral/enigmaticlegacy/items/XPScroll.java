package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ExperienceHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

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
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		MutableComponent cMode;
		if (!ItemNBTHelper.getBoolean(stack, "IsActive", false)) {
			cMode = Component.translatable("tooltip.enigmaticlegacy.xpTomeDeactivated");
		} else if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true)) {
			cMode = Component.translatable("tooltip.enigmaticlegacy.xpTomeAbsorption");
		} else {
			cMode = Component.translatable("tooltip.enigmaticlegacy.xpTomeExtraction");
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
				world.playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_OFF, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			} else {
				ItemNBTHelper.setBoolean(stack, "IsActive", true);
				world.playSound(null, player.blockPosition(), EnigmaticSounds.CHARGED_ON, SoundSource.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
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
	public void curioTick(SlotContext context, ItemStack stack) {
		ItemStack itemstack = SuperpositionHandler.getCurioStack(context.entity(), EnigmaticItems.XP_SCROLL);

		if (!(context.entity() instanceof Player) || context.entity().level().isClientSide || !ItemNBTHelper.getBoolean(itemstack, "IsActive", false))
			return;

		Player player = (Player) context.entity();
		Level world = player.level();
		int xpPortion = this.getXPPortion(player);

		if (ItemNBTHelper.getBoolean(itemstack, "AbsorptionMode", true)) {
			if (ExperienceHelper.getPlayerXP(player) >= xpPortion) {
				ExperienceHelper.drainPlayerXP(player, xpPortion);
				ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + xpPortion);
			} else if (ExperienceHelper.getPlayerXP(player) > 0 & ExperienceHelper.getPlayerXP(player) < xpPortion) {
				int exp = ExperienceHelper.getPlayerXP(player);
				ExperienceHelper.drainPlayerXP(player, exp);
				ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + exp);
			}
		} else {
			int xp = ItemNBTHelper.getInt(itemstack, "XPStored", 0);

			if (xp >= xpPortion) {
				ItemNBTHelper.setInt(itemstack, "XPStored", xp - xpPortion);
				ExperienceHelper.addPlayerXP(player, xpPortion);
			} else if (xp > 0 & xp < xpPortion) {
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
	public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
		return false;
	}

	private int getXPPortion(Player player) {
		int level = ExperienceHelper.getPlayerXPLevel(player);
		int levelXP = ExperienceHelper.getExperienceForLevel(level + 1)
				- ExperienceHelper.getExperienceForLevel(level);
		int portion = levelXP / 5;

		if (level > 100) {
			portion *= 1 + level / 100;
		}

		if (portion > 0)
			return portion;
		else
			return this.xpPortion;
	}

}
