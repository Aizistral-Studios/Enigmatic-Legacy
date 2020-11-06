package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.triggers.CursedRingEquippedTrigger;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CursedRing extends ItemBaseCurio {
	public static Omniconfig.PerhapsParameter painMultiplier;
	public static Omniconfig.PerhapsParameter monsterDamageDebuff;
	public static Omniconfig.PerhapsParameter armorDebuff;
	public static Omniconfig.PerhapsParameter experienceBonus;
	public static Omniconfig.IntParameter fortuneBonus;
	public static Omniconfig.IntParameter lootingBonus;
	public static Omniconfig.IntParameter enchantingBonus;

	public static Omniconfig.PerhapsParameter knockbackDebuff;
	public static Omniconfig.DoubleParameter neutralAngerRange;
	public static Omniconfig.DoubleParameter neutralXRayRange;
	public static Omniconfig.DoubleParameter endermenRandomportRange;
	public static Omniconfig.DoubleParameter endermenRandomportFrequency;
	public static Omniconfig.BooleanParameter saveTheBees;

	public static Omniconfig.BooleanParameter ultraHardcore;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		String prevCategory = builder.getCurrentCategory();
		builder.popCategory();
		builder.pushCategory("The Seven Curses", "Config options directly affecting Ring of the Seven Curses");
		builder.pushPrefix("CursedRing");

		painMultiplier = builder
				.comment("Defines how much damage bearers of the ring receive from any source. Measured as percentage.")
				.getPerhaps("PainModifier", 200);

		monsterDamageDebuff = builder
				.comment("How much damage monsters receive from bearers of the ring will be decreased, in percents.")
				.getPerhaps("MonsterDamageDebuff", 50);

		armorDebuff = builder
				.comment("How much less effective armor will be for those who bear the ring. Measured as percetage.")
				.max(100)
				.getPerhaps("ArmorDebuff", 30);

		experienceBonus = builder
				.comment("How much experience will drop from mobs to bearers of the ring, measured in percents.")
				.getPerhaps("ExperienceBonus", 400);

		fortuneBonus = builder
				.comment("How many bonus Fortune levels ring provides")
				.getInt("FortuneBonus", 1);

		lootingBonus = builder
				.comment("How many bonus Looting levels ring provides")
				.getInt("LootingBonus", 1);

		enchantingBonus = builder
				.comment("How much additional Enchanting Power ring provides in Enchanting Table.")
				.getInt("EnchantingBonus", 10);

		ultraHardcore = builder
				.comment("If true, Ring of the Seven Curses will be equipped into player's ring slot right away when "
						+ "entering a new world, instead of just being added to their inventory.")
				.getBoolean("UltraHardcode", false);



		knockbackDebuff = builder
				.comment("How much knockback bearers of the ring take, measured in percents.")
				.getPerhaps("KnockbackDebuff", 200);

		neutralAngerRange = builder
				.comment("Range in which neutral creatures are angered against bearers of the ring.")
				.min(4)
				.getDouble("NeutralAngerRange", 24);

		neutralXRayRange = builder
				.comment("Range in which neutral creatures can see and target bearers of the ring even if they can't directly see them.")
				.getDouble("NeutralXRayRange", 4);

		endermenRandomportFrequency = builder
				.comment("Allows to adjust how frequently Endermen will try to randomly teleport to player bearing the ring, even "
						+ "if they can't see the player and are not angered yet. Lower value = less probability of this happening.")
				.min(0.01)
				.getDouble("EndermenRandomportFrequency", 1);

		endermenRandomportRange = builder
				.comment("Range in which Endermen can try to randomly teleport to bearers of the ring.")
				.min(8)
				.getDouble("EndermenRandomportRange", 32);

		builder.popCategory();
		builder.pushCategory("Save the Bees", "This category exists solely because of Jusey1z who really wanted to protect his bees."
				+ Configuration.NEW_LINE + "Btw Jusey, when I said 'very cute though', I meant you. Bees are cute either of course.");

		saveTheBees = builder
				.comment("If true, bees will never affected by the Second Curse of Ring of the Seven Curses.")
				.getBoolean("DontTouchMyBees", false);

		builder.popCategory();
		builder.popPrefix();
		builder.pushCategory(prevCategory);

	}

	public CursedRing() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "cursed_ring"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing3");
			if (painMultiplier.getValue().asMultiplier() == 2.0) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing4");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing4_alt", TextFormatting.GOLD, painMultiplier+"%");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing6", TextFormatting.GOLD, armorDebuff+"%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing7", TextFormatting.GOLD, monsterDamageDebuff+"%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing12", TextFormatting.GOLD, lootingBonus);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing13", TextFormatting.GOLD, fortuneBonus);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing14", TextFormatting.GOLD, experienceBonus+"%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing15", TextFormatting.GOLD, enchantingBonus);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing16");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing17");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing18");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing1");

			if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing2_creative");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing2");
			}

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}


	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
		Multimap<Attribute, AttributeModifier> atrributeMap = ArrayListMultimap.create();

		atrributeMap.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("457d0ac3-69e4-482f-b636-22e0802da6bd"), "enigmaticlegacy:armor_modifier", -armorDebuff.getValue().asModifier(), AttributeModifier.Operation.MULTIPLY_TOTAL));
		atrributeMap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("95e70d83-3d50-4241-a835-996e1ef039bb"), "enigmaticlegacy:armor_toughness_modifier", -armorDebuff.getValue().asModifier(), AttributeModifier.Operation.MULTIPLY_TOTAL));

		return atrributeMap;
	}

	@Override
	public boolean showAttributesTooltip(String identifier) {
		return false;
	}

	@Override
	public boolean canUnequip(String identifier, LivingEntity living) {
		if (living instanceof PlayerEntity && ((PlayerEntity) living).isCreative())
			return super.canUnequip(identifier, living);
		else
			return false;
	}

	@Override
	public boolean canRightClickEquip() {
		return false;
	}

	@Override
	public void onUnequip(String identifier, int index, LivingEntity entityLivingBase) {
		// NO-OP
	}

	@Override
	public void onEquip(String identifier, int index, LivingEntity entityLivingBase) {
		// TODO Use Curios trigger for this
		if (entityLivingBase instanceof ServerPlayerEntity) {
			CursedRingEquippedTrigger.INSTANCE.trigger((ServerPlayerEntity) entityLivingBase);
		}
	}

	@Override
	public DropRule getDropRule(LivingEntity livingEntity) {
		return DropRule.ALWAYS_KEEP;
	}

	public boolean isItemDeathPersistent(ItemStack stack) {
		return stack.getItem().equals(this) || stack.getItem().equals(EnigmaticLegacy.enigmaticAmulet);
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// TODO Dirty self-equipping tricks
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity livingPlayer) {
		if (livingPlayer.world.isRemote || !(livingPlayer instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) livingPlayer;

		if (player.isCreative() || player.isSpectator())
			return;

		List<LivingEntity> genericMobs = livingPlayer.world.getEntitiesWithinAABB(LivingEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, neutralAngerRange.getValue()));
		List<EndermanEntity> endermen = livingPlayer.world.getEntitiesWithinAABB(EndermanEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, endermenRandomportRange.getValue()));

		for (EndermanEntity enderman : endermen) {
			if (random.nextDouble() <= (0.002 * endermenRandomportFrequency.getValue())) {
				if (enderman.teleportToEntity(player) && player.canEntityBeSeen(enderman)) {
					enderman.setAttackTarget(player);
				}
			}

		}

		for (LivingEntity checkedEntity : genericMobs) {
			if (checkedEntity instanceof IAngerable) {
				IAngerable neutral = (IAngerable) checkedEntity;

				if (neutral instanceof TameableEntity) {
					if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.animalGuide) || ((TameableEntity)neutral).isTamed()) {
						continue;
					}
				} else if (neutral instanceof IronGolemEntity) {
					if (((IronGolemEntity)neutral).isPlayerCreated()) {
						continue;
					}
				} else if (neutral instanceof BeeEntity) {
					if (saveTheBees.getValue() || SuperpositionHandler.hasItem(player, EnigmaticLegacy.animalGuide)) {
						continue;
					}
				}

				if (neutral.getAttackTarget() == null || !neutral.getAttackTarget().isAlive()) {
					if (player.canEntityBeSeen(checkedEntity) || player.getDistance(checkedEntity) <= neutralXRayRange.getValue()) {
						neutral.setAttackTarget(player);
					} else {
						continue;
					}
				}
			}
		}

	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		Map<Enchantment, Integer> list = EnchantmentHelper.getEnchantments(book);

		if (list.containsKey(Enchantments.VANISHING_CURSE))
			return false;
		else
			return super.isBookEnchantable(stack, book);
	}

	public double getAngerRange() {
		return neutralAngerRange.getValue();
	}

	@Override
	public int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
		return super.getFortuneBonus(identifier, livingEntity, curio, index) + fortuneBonus.getValue();
	}

	@Override
	public int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
		return super.getLootingBonus(identifier, livingEntity, curio, index) + lootingBonus.getValue();
	}

}