package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.triggers.CursedRingEquippedTrigger;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.EndermanEntity;
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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CursedRing extends ItemBaseCurio {
	public static Omniconfig.DoubleParameter painMultiplier;
	public static Omniconfig.DoubleParameter monsterDamageMultiplier;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("CursedRing");

		painMultiplier = builder
				.comment("Any damage received by bearers of the Ring of the Seven Curses will be multiplied by this value.")
				.getDouble("PainMultiplier", 2.0);

		monsterDamageMultiplier = builder
				.comment("Any damage bearers of the Ring of the Seven Curses deal to monsters will be multiplied by this value.")
				.getDouble("DamageMultiplier", 0.5);

		builder.popPrefix();
	}

	private final double angerRange = 24F;
	public final Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();

	public CursedRing() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "cursed_ring"));

		this.attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("457d0ac3-69e4-482f-b636-22e0802da6bd"), "enigmaticlegacy:armor_modifier", -0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL));
		this.attributesDefault.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("95e70d83-3d50-4241-a835-996e1ef039bb"), "enigmaticlegacy:armor_toughness_modifier", -0.3F, AttributeModifier.Operation.MULTIPLY_TOTAL));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing12");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing13");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing14");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.cursedRing15");
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
		return this.attributesDefault;
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
	public void curioTick(String identifier, int index, LivingEntity livingPlayer) {
		if (livingPlayer.world.isRemote || !(livingPlayer instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) livingPlayer;

		if (player.isCreative())
			return;

		List<LivingEntity> genericMobs = livingPlayer.world.getEntitiesWithinAABB(LivingEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, this.angerRange));
		List<EndermanEntity> endermen = livingPlayer.world.getEntitiesWithinAABB(EndermanEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, this.angerRange+8));

		for (EndermanEntity enderman : endermen) {
			if (random.nextDouble() <= 0.002) {
				if (enderman.teleportToEntity(player) && player.canEntityBeSeen(enderman)) {
					enderman.setAttackTarget(player);
				}
			}

		}

		for (LivingEntity checkedEntity : genericMobs) {
			if (checkedEntity instanceof IAngerable) {
				IAngerable neutral = (IAngerable) checkedEntity;

				if (neutral instanceof TameableEntity) {
					if (((TameableEntity)neutral).isTamed()) {
						continue;
					}
				} else if (neutral instanceof IronGolemEntity) {
					if (((IronGolemEntity)neutral).isPlayerCreated()) {
						continue;
					}
				} else if (neutral instanceof BeeEntity) {
					continue;
				}

				if (!livingPlayer.equals(neutral.getAttackTarget())) {
					if (player.canEntityBeSeen(checkedEntity) || player.getDistance(checkedEntity) <= 4.0F) {
						neutral.setAttackTarget(player);
					} else {
						continue;
					}
				}
			}
		}

	}

	public double getAngerRange() {
		return this.angerRange;
	}

	@Override
	public int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
		return super.getFortuneBonus(identifier, livingEntity, curio, index) + 1;
	}

	@Override
	public int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
		return super.getLootingBonus(identifier, livingEntity, curio, index) + 1;
	}

}