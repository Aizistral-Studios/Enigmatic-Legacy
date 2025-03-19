package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect;
import com.aizistral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect.Effect;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.collect.Lists;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.piglin.PiglinBruteAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class GuardianHeart extends ItemBase implements ICursed, Vanishable {
	public static final List<Class<? extends LivingEntity>> excludedMobs = Lists.newArrayList(
			AbstractPiglin.class, Guardian.class);
	public static Omniconfig.IntParameter abilityRange;
	public static Omniconfig.IntParameter enrageRange;
	public static Omniconfig.IntParameter abilityCooldown;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("GuardianHeart");

		abilityRange = builder
				.comment("Range of active/passive abilities of Heart of the Guardian.")
				.getInt("AbilityRange", 24);

		enrageRange = builder
				.comment("Range in which monster enraged by active ability of Heart of the Guardian will seek new target. Monsters within this range will target enraged monster back.")
				.getInt("EnrageRange", 12);

		abilityCooldown = builder
				.comment("Cooldown of enraging ability of Heart of the Guardian. Measured in ticks.")
				.getInt("ActiveAbilityCooldown", 200);

		builder.popPrefix();
	}

	public GuardianHeart() {
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart1", ChatFormatting.GOLD, abilityRange);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart5", ChatFormatting.GOLD, abilityRange);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart8", ChatFormatting.GOLD, enrageRange);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart12", ChatFormatting.GOLD, (abilityCooldown.getValue()/20.0));
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);

	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof Player && !world.isClientSide) {
			Player player = (Player) entity;
			List<Monster> genericMobs = player.level().getEntitiesOfClass(Monster.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, abilityRange.getValue()));

			if (SuperpositionHandler.isTheCursedOne(player) && Inventory.isHotbarSlot(itemSlot) && !player.getCooldowns().isOnCooldown(this)) {
				Monster oneWatched = null;

				for (Monster monster : genericMobs) {
					if (SuperpositionHandler.doesObserveEntity(player, monster) && !this.isExcluded(monster)) {
						oneWatched = monster;
						break;
					}
				}

				//System.out.println("The One: " + oneWatched);

				if (oneWatched != null && oneWatched.isAlive()) {
					final Monster theOne = oneWatched;
					Vector3 vec = Vector3.fromEntityCenter(theOne);
					List<Monster> surroundingMobs = player.level().getEntitiesOfClass(Monster.class, SuperpositionHandler.getBoundingBoxAroundEntity(theOne, enrageRange.getValue()), (living) -> { return living.isAlive() && theOne.hasLineOfSight(living); });
					Monster closestMonster = SuperpositionHandler.getClosestEntity(surroundingMobs, (monster) -> monster != theOne, vec.x, vec.y, vec.z);

					//System.out.println("Closest monster: " + closestMonster);

					if (closestMonster != null) {
						this.setAttackTarget(theOne, closestMonster);
						theOne.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 300, 0, false, true));
						theOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1, false, false));
						theOne.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1, false, false));
						//theOne.addPotionEffect(new MobEffectInstance(MobEffects., 400, 1, false, true));

						for (Monster otherMonster : surroundingMobs) {
							this.setAttackTarget(otherMonster, theOne);
						}

						player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.HOSTILE, 1.0F, 1.0F);

						if (player instanceof ServerPlayer) {
							ServerPlayer serverPlayer = (ServerPlayer) player;
							EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(theOne.getX(), theOne.getY(), theOne.getZ(), 64, theOne.level().dimension())), new PacketGenericParticleEffect(theOne.getX(), theOne.getEyeY(), theOne.getZ(), 0, false, Effect.GUARDIAN_CURSE));
						}

						player.getCooldowns().addCooldown(this, abilityCooldown.getValue());
					}
				}

			}

			for (Monster monster : genericMobs) {
				if (monster instanceof Guardian && monster.getClass() != ElderGuardian.class) {
					final Guardian guardian = (Guardian) monster;

					if (guardian.getTarget() == null) {
						List<Monster> surroundingMobs = player.level().getEntitiesOfClass(Monster.class, SuperpositionHandler.getBoundingBoxAroundEntity(guardian, 12), (living) -> { return living.isAlive() && guardian.hasLineOfSight(living); });
						Monster closestMonster = SuperpositionHandler.getClosestEntity(surroundingMobs, (checked) -> { return !(checked instanceof Guardian); }, guardian.getX(), guardian.getY(), guardian.getZ());

						if (closestMonster != null) {
							this.setAttackTarget(guardian, closestMonster);
						}
					}
				}
			}

		}
	}

	private boolean isExcluded(LivingEntity entity) {
		boolean excluded = false;
		for(Class<? extends LivingEntity> excludedClass : excludedMobs) {
			if (excludedClass.isInstance(entity)) {
				excluded = true;
			}
		}

		return excluded;
	}

	private void setAttackTarget(Monster monster, Monster otherMonster) {
		if (monster != null && otherMonster != null && monster != otherMonster) {
			if (monster instanceof AbstractPiglin) {
				((AbstractPiglin)monster).getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, otherMonster);
				monster.setTarget(otherMonster);
				monster.setLastHurtByMob(otherMonster);

				if (monster instanceof PiglinBrute) {
					PiglinBruteAi.wasHurtBy((PiglinBrute) monster, otherMonster);
				} else if (monster instanceof Piglin) {
					PiglinAi.wasHurtBy((Piglin) monster, otherMonster);
				}

				//monster.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(monster, Monster.class, true));
			} else if (monster instanceof NeutralMob) {
				((NeutralMob)monster).setTarget(otherMonster);
			} else {
				monster.setTarget(otherMonster);
				monster.setLastHurtByMob(otherMonster);
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		return super.use(worldIn, playerIn, handIn);
	}

}
