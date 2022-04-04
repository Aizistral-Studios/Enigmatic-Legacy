package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class AnimalGuide extends ItemBase implements Vanishable {
	public static final List<ResourceLocation> animalExclusionList = new ArrayList<>();

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		animalExclusionList.clear();
		String[] blacklist = builder.config.getStringList("AnimalGuideAnimalExclusionList", builder.getCurrentCategory(), new String[0],
				"List of entities that should count towards the curse-altering effect of Guide to Animal Companionship, and thus remain"
						+ " neutral to players bearing Ring of the Seven Curses if they posses such guide. Examples: minecraft:iron_golem, minecraft:zombified_piglin");

		Arrays.stream(blacklist).forEach(entry -> animalExclusionList.add(new ResourceLocation(entry)));
	}

	public AnimalGuide() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "animal_guide"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
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

		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.animalGuideCreative1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.animalGuideCreative2");
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!(entityIn instanceof Player) || worldIn.isClientSide)
			return;

		Player player = (Player) entityIn;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (player.isCreative()) {
			if (!player.level.isClientSide) {
				boolean tamable = this.isTamableAnimal(target);
				TextComponent reply = new TextComponent(tamable ?
						"Yep, this animal seems tamable." : "Nope, this animal is not considered tamable.");
				player.sendMessage(reply.withStyle(tamable ? ChatFormatting.GREEN : ChatFormatting.RED), player.getUUID());
			}

			return InteractionResult.SUCCESS;
		} else
			return InteractionResult.PASS;
	}

	public boolean isProtectedAnimal(LivingEntity animal) {
		return !(animal instanceof NeutralMob) || animal instanceof Hoglin || animal instanceof Bee || animal instanceof Wolf;
	}

	public boolean isTamableAnimal(LivingEntity entity) {
		if (entity instanceof TamableAnimal)
			return true;
		else
			return animalExclusionList.contains(entity.getType().getRegistryName());
	}

	// Props to Forge for making what I tried below impossible

	/*
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (player.isCreative()) {
			if (!player.getLevel().isClientSide) {

				var entries = ForgeRegistries.ENTITIES.getEntries();

				EnigmaticLegacy.logger.info("Checking up the list of " + entries.size() + " entities to find which are affected by curse-altering effect of Guide to Animal Companionship...");
				ForgeRegistries.ENTITIES.getEntries().forEach(entry -> {
					EntityType<?> type = entry.getValue();

					if (TamableAnimal.class.isAssignableFrom(type.getBaseClass()) || animalExclusionList.contains(type.getRegistryName())) {
						EnigmaticLegacy.logger.info(" - Entity: " + type.getRegistryName() + ", Class: " + type.getBaseClass());
					}
				});

				EnigmaticLegacy.logger.info("The analysis is complete.");
				player.sendMessage(new TranslatableComponent("message.enigmaticlegacy.animal_analysis_complete").withStyle(ChatFormatting.DARK_PURPLE), player.getUUID());
			}

			return InteractionResultHolder.consume(player.getItemInHand(hand));
		} else
			return super.use(level, player, hand);
	}
	 */

}