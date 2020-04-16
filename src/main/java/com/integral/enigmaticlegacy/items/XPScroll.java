package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ExperienceHelper;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.capability.ICurio;

public class XPScroll extends Item implements ICurio, IPerhaps {

	public static Properties integratedProperties = new Item.Properties();
	public static final int xpPortion = 5;

	public XPScroll(Properties properties) {
		super(properties);

	}

	public static Properties setupIntegratedProperties() {
		XPScroll.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		XPScroll.integratedProperties.maxStackSize(1);
		XPScroll.integratedProperties.rarity(Rarity.UNCOMMON);

		return XPScroll.integratedProperties;

	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.XP_SCROLL_ENABLED.getValue();
	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.xpScroll))
			return false;
		else
			return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		TranslationTextComponent cMode;
		if (!ItemNBTHelper.getBoolean(stack, "IsActive", false))
			cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeDeactivated");
		else if (ItemNBTHelper.getBoolean(stack, "AbsorptionMode", true))
			cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeAbsorption");
		else
			cMode = new TranslationTextComponent("tooltip.enigmaticlegacy.xpTomeExtraction");

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome1");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome3");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome4");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome4_5");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome5");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome6");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome7");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome8");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome9");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome10");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTome11", (int) ConfigHandler.XP_SCROLL_COLLECTION_RANGE.getValue());
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeMode", cMode.getFormattedText());
		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeStoredXP");
		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.xpTomeUnits", ItemNBTHelper.getInt(stack, "XPStored", 0), ExperienceHelper.getLevelForExperience(ItemNBTHelper.getInt(stack, "XPStored", 0)));

		try {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.currentKeybind", KeyBinding.getDisplayString("key.xpScroll").get().toUpperCase());
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

		if (!player.isShiftKeyDown()) {

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

		if (swing)
			player.swingArm(hand);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "IsActive", false);
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity entity) {

		ItemStack itemstack = SuperpositionHandler.getCurioStack(entity, EnigmaticLegacy.xpScroll);

		if (!(entity instanceof PlayerEntity) || entity.world.isRemote || !ItemNBTHelper.getBoolean(itemstack, "IsActive", false))
			return;

		PlayerEntity player = (PlayerEntity) entity;
		World world = player.world;

		if (ItemNBTHelper.getBoolean(itemstack, "AbsorptionMode", true)) {

			if (player.experienceTotal >= XPScroll.xpPortion) {
				player.giveExperiencePoints(-XPScroll.xpPortion);
				ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + XPScroll.xpPortion);
			} else if (player.experienceTotal > 0 & ExperienceHelper.getPlayerXP(player) < XPScroll.xpPortion) {
				int exp = player.experienceTotal;
				player.giveExperiencePoints(-exp);
				ItemNBTHelper.setInt(itemstack, "XPStored", ItemNBTHelper.getInt(itemstack, "XPStored", 0) + exp);
			}

		} else {

			int xp = ItemNBTHelper.getInt(itemstack, "XPStored", 0);

			if (xp >= XPScroll.xpPortion) {
				ItemNBTHelper.setInt(itemstack, "XPStored", xp - XPScroll.xpPortion);
				player.giveExperiencePoints(XPScroll.xpPortion);
			} else if (xp > 0 & xp < XPScroll.xpPortion) {
				ItemNBTHelper.setInt(itemstack, "XPStored", 0);
				player.giveExperiencePoints(xp);
			}

		}

		List<ExperienceOrbEntity> orbs = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, ConfigHandler.XP_SCROLL_COLLECTION_RANGE.getValue()));
		for (ExperienceOrbEntity processed : orbs) {
			if (!processed.isAlive())
				continue;

			player.xpCooldown = 0;
			processed.onCollideWithPlayer(player);
			//processed.setPositionAndUpdate(player.posX, player.posY, player.posZ);
		}
	}

	@Override
	public boolean canRightClickEquip() {
		return false;
	}

	@Override
	public void onEquipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

}
