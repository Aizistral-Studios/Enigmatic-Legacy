package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.enigmaticlegacy.triggers.CursedRingEquippedTrigger;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

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
	public static Omniconfig.BooleanParameter enableSpecialDrops;
	public static Omniconfig.BooleanParameter enableLore;
	public static Omniconfig.BooleanParameter concealAbilities;
	public static Omniconfig.BooleanParameter disableInsomnia;

	public static Omniconfig.BooleanParameter ultraHardcore;
	public static Omniconfig.BooleanParameter autoEquip;
	public static final List<ResourceLocation> neutralAngerBlacklist = new ArrayList<>();

	public static Omniconfig.DoubleParameter superCursedTime;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		String prevCategory = builder.getCurrentCategory();
		builder.popCategory();

		builder.pushCategory("The Seven Curses", "Config options directly affecting Ring of the Seven Curses");
		builder.pushPrefix("CursedRing");

		if (builder.config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
			enableLore = builder
					.comment("Set to false to disable displaying lore on Ring of the Seven Curses. Useful if you are a modpack"
							+ " developer wanting to have your own.")
					.getBoolean("DisplayLore", true);
			concealAbilities = builder
					.comment("If true, tooltip of Ring of the Seven Curses cannot be read before it is equipped. Fun way to"
							+ " teach players that not every mystery is worth investigating.")
					.getBoolean("ConcealAbilities", false);
		} else {
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

			enableSpecialDrops = builder
					.comment("Set to false to disable ALL special drops that can be obtained from vanilla mobs when "
							+ "bearing Ring of the Seven Curses.")
					.getBoolean("EnableSpecialDrops", true);

			ultraHardcore = builder
					.comment("If true, Ring of the Seven Curses will be equipped into player's ring slot right away when "
							+ "entering a new world, instead of just being added to their inventory.")
					.getBoolean("UltraHardcore", false);

			autoEquip = builder
					.comment("If true, Ring of the Seven Curses will be equipped into player's ring slot right away when "
							+ "it enters their inventory. This is different from ultra hardcore option as the way through "
							+ "which ring ends up in player's inventory does not matter.")
					.getBoolean("AutoEquip", false);

			disableInsomnia = builder
					.comment("Set to true to prevent curse of insomnia from actually doing anything.")
					.getBoolean("disableInsomnia", false);

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

			superCursedTime = builder
					.comment("A fraction of time the player should bear the Seven Curses to use Abyssal Artifacts.")
					.getDouble("SuperCursedTime", 0.995);

			builder.popCategory();
			builder.pushCategory("Save the Bees", "This category exists solely because of Jusey1z who really wanted to protect his bees."
					+ Configuration.NEW_LINE + "Btw Jusey, when I said 'very cute though', I meant you. Bees are cute either of course.");

			saveTheBees = builder
					.comment("If true, bees will never affected by the Second Curse of Ring of the Seven Curses.")
					.getBoolean("DontTouchMyBees", false);

			// Ugly but gets the job done
			neutralAngerBlacklist.clear();

			// See https://github.com/Aizistral-Studios/Enigmatic-Legacy/pull/412
			neutralAngerBlacklist.add(new ResourceLocation("the_bumblezone", "bee_queen"));

			String[] blacklist = builder.config.getStringList("CursedRingNeutralAngerBlacklist", "The Seven Curses", new String[0], "List of entities that should never be affected"
					+ " by the Second Curse of Ring of the Seven Curses. Examples: minecraft:iron_golem, minecraft:zombified_piglin. Changing this option required game restart to take effect.");

			Arrays.stream(blacklist).forEach(entry -> neutralAngerBlacklist.add(new ResourceLocation(entry)));
		}

		builder.popCategory();
		builder.popPrefix();
		builder.pushCategory(prevCategory);
	}

	public CursedRing() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing3");
			if (painMultiplier.getValue().asMultiplier() == 2.0) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing4");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing4_alt", ChatFormatting.GOLD, painMultiplier+"%");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing6", ChatFormatting.GOLD, armorDebuff+"%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing7", ChatFormatting.GOLD, monsterDamageDebuff+"%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing12", ChatFormatting.GOLD, lootingBonus);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing13", ChatFormatting.GOLD, fortuneBonus);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing14", ChatFormatting.GOLD, experienceBonus+"%");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing15", ChatFormatting.GOLD, enchantingBonus);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing16");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing17");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing18");
		} else {
			if (enableLore.getValue()) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore1");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore2");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore3");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore4");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore5");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore6");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRingLore7");
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			}
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eternallyBound1");

			if (Minecraft.getInstance().player != null && SuperpositionHandler.canUnequipBoundRelics(Minecraft.getInstance().player)) {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eternallyBound2_creative");
			} else {
				ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.eternallyBound2");
			}

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> atrributeMap = ArrayListMultimap.create();

		atrributeMap.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("457d0ac3-69e4-482f-b636-22e0802da6bd"), "enigmaticlegacy:armor_modifier", -armorDebuff.getValue().asModifier(), AttributeModifier.Operation.MULTIPLY_TOTAL));
		atrributeMap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("95e70d83-3d50-4241-a835-996e1ef039bb"), "enigmaticlegacy:armor_toughness_modifier", -armorDebuff.getValue().asModifier(), AttributeModifier.Operation.MULTIPLY_TOTAL));

		return atrributeMap;
	}

	@Override
	public List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
		tooltips.clear();
		return tooltips;
	}

	@Override
	public boolean canUnequip(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player && SuperpositionHandler.canUnequipBoundRelics(player))
			return super.canUnequip(context, stack);
		else
			return false;
	}

	@Override
	public boolean canEquipFromUse(SlotContext context, ItemStack stack) {
		return false;
	}

	@Override
	public void onUnequip(SlotContext context, ItemStack newStack, ItemStack stack) {
		SuperpositionHandler.setCurrentWorldCursed(false);
	}

	@Override
	public void onEquip(SlotContext context, ItemStack prevStack, ItemStack stack) {
		// TODO Use Curios trigger for this
		if (context.entity() instanceof ServerPlayer player) {
			CursedRingEquippedTrigger.INSTANCE.trigger(player);
		}

		SuperpositionHandler.setCurrentWorldCursed(true);
	}

	@Override
	public DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
		return DropRule.ALWAYS_KEEP;
	}

	public boolean isItemDeathPersistent(ItemStack stack) {
		return stack.getItem().equals(this) || stack.getItem().equals(EnigmaticItems.ENIGMATIC_AMULET);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// TODO Dirty self-equipping tricks
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity().level().isClientSide || !(context.entity() instanceof Player))
			return;

		Player player = (Player) context.entity();

		if (player.isCreative() || player.isSpectator())
			return;

		List<LivingEntity> genericMobs = player.level().getEntitiesOfClass(LivingEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, neutralAngerRange.getValue()));
		List<EnderMan> endermen = player.level().getEntitiesOfClass(EnderMan.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, endermenRandomportRange.getValue()));

		for (EnderMan enderman : endermen) {
			if (random.nextDouble() <= (0.002 * endermenRandomportFrequency.getValue())) {
				if (enderman.teleportTowards(player) && player.hasLineOfSight(enderman)) {
					enderman.setTarget(player);
				}
			}
		}

		for (LivingEntity checkedEntity : genericMobs) {
			double visibility = player.getVisibilityPercent(checkedEntity);
			double angerDistance = Math.max(neutralAngerRange.getValue() * visibility, neutralXRayRange.getValue());

			if (checkedEntity.distanceToSqr(player.getX(), player.getY(), player.getZ()) > angerDistance * angerDistance) {
				continue;
			}

			if (checkedEntity instanceof Piglin && !SuperpositionHandler.hasCurio(player, EnigmaticItems.AVARICE_SCROLL)) {
				Piglin piglin = (Piglin) checkedEntity;

				if (piglin.getTarget() == null || !piglin.getTarget().isAlive()) {
					if (player.hasLineOfSight(checkedEntity) || player.distanceTo(checkedEntity) <= neutralXRayRange.getValue()) {
						PiglinAi.wasHurtBy(piglin, player);
					} else {
						continue;
					}
				}

			} else if (checkedEntity instanceof NeutralMob) {
				NeutralMob neutral = (NeutralMob) checkedEntity;

				if (neutralAngerBlacklist.contains(ForgeRegistries.ENTITY_TYPES.getKey(checkedEntity.getType()))) {
					continue;
				}

				if (neutral instanceof TamableAnimal tamable && tamable.isTame()) {
					continue;
				} else if (SuperpositionHandler.hasItem(player, EnigmaticItems.ANIMAL_GUIDEBOOK)) {
					if (EnigmaticItems.ANIMAL_GUIDEBOOK.isTamableAnimal(checkedEntity)) {
						continue;
					}
				} else if (neutral instanceof IronGolem golem && golem.isPlayerCreated()) {
					continue;
				} else if (neutral instanceof Bee) {
					if (saveTheBees.getValue() || SuperpositionHandler.hasItem(player, EnigmaticItems.ANIMAL_GUIDEBOOK)) {
						continue;
					}
				}

				if (neutral.getTarget() == null || !neutral.getTarget().isAlive()) {
					if (player.hasLineOfSight(checkedEntity) || player.distanceTo(checkedEntity) <= neutralXRayRange.getValue()) {
						neutral.setTarget(player);
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
	public int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack curio) {
		return super.getFortuneLevel(slotContext, lootContext, curio) + fortuneBonus.getValue();
	}

	@Override
	public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack curio) {
		return super.getLootingLevel(slotContext, source, target, baseLooting, curio) + lootingBonus.getValue();
	}

}