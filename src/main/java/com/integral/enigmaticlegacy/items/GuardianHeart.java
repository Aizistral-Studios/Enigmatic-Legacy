package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.google.common.collect.Lists;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect;
import com.integral.enigmaticlegacy.packets.clients.PacketGenericParticleEffect.Effect;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteBrain;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class GuardianHeart extends ItemBase implements ICursed, IVanishable {
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

	public static final List<Class<? extends LivingEntity>> excludedMobs = Lists.newArrayList(
			AbstractPiglinEntity.class, GuardianEntity.class);

	public GuardianHeart() {
		super(getDefaultProperties().maxStackSize(1).rarity(Rarity.EPIC).isBurnable());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "guardian_heart"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart1", TextFormatting.GOLD, abilityRange);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart5", TextFormatting.GOLD, abilityRange);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart8", TextFormatting.GOLD, enrageRange);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart11");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.guardianHeart12", TextFormatting.GOLD, (abilityCooldown.getValue()/20.0));
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);

	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (entity instanceof PlayerEntity && !world.isRemote) {
			PlayerEntity player = (PlayerEntity) entity;
			List<MonsterEntity> genericMobs = player.world.getEntitiesWithinAABB(MonsterEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, abilityRange.getValue()));

			if (SuperpositionHandler.isTheCursedOne(player) && PlayerInventory.isHotbar(itemSlot) && !player.getCooldownTracker().hasCooldown(this)) {
				MonsterEntity oneWatched = null;

				for (MonsterEntity monster : genericMobs) {
					if (SuperpositionHandler.doesObserveEntity(player, monster) && !this.isExcluded(monster)) {
						oneWatched = monster;
						break;
					}
				}

				//System.out.println("The One: " + oneWatched);

				if (oneWatched != null && oneWatched.isAlive()) {
					final MonsterEntity theOne = oneWatched;
					Vector3 vec = Vector3.fromEntityCenter(theOne);
					List<MonsterEntity> surroundingMobs = player.world.getEntitiesWithinAABB(MonsterEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(theOne, enrageRange.getValue()), (living) -> { return living.isAlive() && theOne.canEntityBeSeen(living); });
					MonsterEntity closestMonster = SuperpositionHandler.getClosestEntity(surroundingMobs, (monster) -> monster != theOne, vec.x, vec.y, vec.z);

					//System.out.println("Closest monster: " + closestMonster);

					if (closestMonster != null) {
						this.setAttackTarget(theOne, closestMonster);
						theOne.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 300, 0, false, true));
						theOne.addPotionEffect(new EffectInstance(Effects.STRENGTH, 300, 1, false, false));
						theOne.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 300, 1, false, false));
						//theOne.addPotionEffect(new EffectInstance(Effects., 400, 1, false, true));

						for (MonsterEntity otherMonster : surroundingMobs) {
							this.setAttackTarget(otherMonster, theOne);
						}

						player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.0F, 1.0F);

						if (player instanceof ServerPlayerEntity) {
							ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
							EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(theOne.getPosX(), theOne.getPosY(), theOne.getPosZ(), 64, theOne.world.func_234923_W_())), new PacketGenericParticleEffect(theOne.getPosX(), theOne.getPosYEye(), theOne.getPosZ(), 0, false, Effect.GUARDIAN_CURSE));
						}

						player.getCooldownTracker().setCooldown(this, abilityCooldown.getValue());
					}
				}

			}

			for (MonsterEntity monster : genericMobs) {
				if (monster instanceof GuardianEntity && monster.getClass() != ElderGuardianEntity.class) {
					final GuardianEntity guardian = (GuardianEntity) monster;

					if (guardian.getAttackTarget() == null) {
						List<MonsterEntity> surroundingMobs = player.world.getEntitiesWithinAABB(MonsterEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(guardian, 12), (living) -> { return living.isAlive() && guardian.canEntityBeSeen(living); });
						MonsterEntity closestMonster = SuperpositionHandler.getClosestEntity(surroundingMobs, (checked) -> { return !(checked instanceof GuardianEntity); }, guardian.getPosX(), guardian.getPosY(), guardian.getPosZ());

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

	private void setAttackTarget(MonsterEntity monster, MonsterEntity otherMonster) {
		if (monster != null && otherMonster != null && monster != otherMonster) {
			if (monster instanceof AbstractPiglinEntity) {
				((AbstractPiglinEntity)monster).getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, otherMonster);
				monster.setAttackTarget(otherMonster);
				monster.setRevengeTarget(otherMonster);

				if (monster instanceof PiglinBruteEntity) {
					PiglinBruteBrain.func_242353_a((PiglinBruteEntity) monster, otherMonster);
				} else if (monster instanceof PiglinEntity) {
					PiglinTasks.func_234468_a_((PiglinEntity) monster, otherMonster);
				}

				//monster.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(monster, MonsterEntity.class, true));
			} else if (monster instanceof IAngerable) {
				((IAngerable)monster).setAttackTarget(otherMonster);
			} else {
				monster.setAttackTarget(otherMonster);
				monster.setRevengeTarget(otherMonster);
			}
		}
	}

}
