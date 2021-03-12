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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		TranslationTextComponent cMode;
		if (!ItemNBTHelper.getBoolean(stack, "IsActive", false)) {
			cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeDeactivated");
		} else if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true)) {
			cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeAbsorption");
		} else {
			cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeExtraction");
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
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome11", TextFormatting.GOLD, (int) xpCollectionRange.getValue());
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeMode", null, cMode);
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeStoredXP");
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeUnits", TextFormatting.GOLD, ItemNBTHelper.getInt(stack, "XPStored", 0), ExperienceHelper.getLevelForExperience(ItemNBTHelper.getInt(stack, "XPStored", 0)));

		try {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", TextFormatting.LIGHT_PURPLE, KeyBinding.getDisplayString("key.xpScroll").get().getString().toUpperCase());
		} catch (NullPointerException ex) {
			// Just don't do it lol
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
		ItemStack stack = player.getHeldItem(handIn);
		this.trigger(world, stack, player, handIn, true);

		return new ActionResult<>(ActionResultType.SUCCESS, stack);

	}

	public void trigger(World world, ItemStack stack, PlayerEntity player, Hand hand, boolean swing) {

		if (!player.isCrouching()) {

			if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true)) {
				ItemNBTHelper.setBoolean(stack, "AbsorptionMode", false);
				world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			} else {
				ItemNBTHelper.setBoolean(stack, "AbsorptionMode", true);
				world.playSound(null, player.getPosition(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2F)));
			}
		} else {

			if (ItemNBTHelper.getBoolean(stack, "IsActive", false)) {
				ItemNBTHelper.setBoolean(stack, "IsActive", false);
				world.playSound(null, player.getPosition(), EnigmaticLegacy.HHOFF, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			} else {
				ItemNBTHelper.setBoolean(stack, "IsActive", true);
				world.playSound(null, player.getPosition(), EnigmaticLegacy.HHON, SoundCategory.PLAYERS, (float) (0.8F + (Math.random() * 0.2F)), (float) (0.8F + (Math.random() * 0.2F)));
			}
		}

		if (swing) {
			player.swingArm(hand);
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "IsActive", false);
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity entity, ItemStack stack) {

		ItemStack itemstack = SuperpositionHandler.getCurioStack(entity, EnigmaticLegacy.xpScroll);

		if (!(entity instanceof PlayerEntity) || entity.world.isRemote || !ItemNBTHelper.getBoolean(itemstack, "IsActive", false))
			return;

		PlayerEntity player = (PlayerEntity) entity;
		World world = player.world;

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

		List<ExperienceOrbEntity> orbs = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, xpCollectionRange.getValue()));
		for (ExperienceOrbEntity processed : orbs) {
			if (!processed.isAlive()) {
				continue;
			}

			player.xpCooldown = 0;
			processed.onCollideWithPlayer(player);
			//processed.setPositionAndUpdate(player.posX, player.posY, player.posZ);
		}
	}

	@Override
	public boolean canRightClickEquip(ItemStack stack) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

}
