package com.integral.enigmaticlegacy.items;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.IAngerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobEntity;
import net.minecraft.world.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.ElderGuardianEntity;
import net.minecraft.world.entity.monster.GuardianEntity;
import net.minecraft.world.entity.monster.MonsterEntity;
import net.minecraft.world.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.world.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.world.entity.monster.piglin.PiglinBruteBrain;
import net.minecraft.world.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.world.entity.monster.piglin.PiglinEntity;
import net.minecraft.world.entity.monster.piglin.PiglinTasks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.item.alchemy.EffectInstance;
import net.minecraft.world.item.alchemy.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;

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
		super(getDefaultProperties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "guardian_heart"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
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
		if (entity instanceof Player && !world.isClientSide) {
			Player player = (Player) entity;
			List<MonsterEntity> genericMobs = player.level.getEntitiesOfClass(MonsterEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(player, abilityRange.getValue()));

			if (SuperpositionHandler.isTheCursedOne(player) && Inventory.isHotbarSlot(itemSlot) && !player.getCooldowns().isOnCooldown(this)) {
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
					List<MonsterEntity> surroundingMobs = player.level.getEntitiesOfClass(MonsterEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(theOne, enrageRange.getValue()), (living) -> { return living.isAlive() && theOne.canSee(living); });
					MonsterEntity closestMonster = SuperpositionHandler.getClosestEntity(surroundingMobs, (monster) -> monster != theOne, vec.x, vec.y, vec.z);

					//System.out.println("Closest monster: " + closestMonster);

					if (closestMonster != null) {
						this.setAttackTarget(theOne, closestMonster);
						theOne.addEffect(new EffectInstance(Effects.BLINDNESS, 300, 0, false, true));
						theOne.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 300, 1, false, false));
						theOne.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 300, 1, false, false));
						//theOne.addPotionEffect(new EffectInstance(Effects., 400, 1, false, true));

						for (MonsterEntity otherMonster : surroundingMobs) {
							this.setAttackTarget(otherMonster, theOne);
						}

						player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.HOSTILE, 1.0F, 1.0F);

						if (player instanceof ServerPlayer) {
							ServerPlayer serverPlayer = (ServerPlayer) player;
							EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(theOne.getX(), theOne.getY(), theOne.getZ(), 64, theOne.level.dimension())), new PacketGenericParticleEffect(theOne.getX(), theOne.getEyeY(), theOne.getZ(), 0, false, Effect.GUARDIAN_CURSE));
						}

						player.getCooldowns().addCooldown(this, abilityCooldown.getValue());
					}
				}

			}

			for (MonsterEntity monster : genericMobs) {
				if (monster instanceof GuardianEntity && monster.getClass() != ElderGuardianEntity.class) {
					final GuardianEntity guardian = (GuardianEntity) monster;

					if (guardian.getTarget() == null) {
						List<MonsterEntity> surroundingMobs = player.level.getEntitiesOfClass(MonsterEntity.class, SuperpositionHandler.getBoundingBoxAroundEntity(guardian, 12), (living) -> { return living.isAlive() && guardian.canSee(living); });
						MonsterEntity closestMonster = SuperpositionHandler.getClosestEntity(surroundingMobs, (checked) -> { return !(checked instanceof GuardianEntity); }, guardian.getX(), guardian.getY(), guardian.getZ());

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
				monster.setTarget(otherMonster);
				monster.setLastHurtByMob(otherMonster);

				if (monster instanceof PiglinBruteEntity) {
					PiglinBruteBrain.wasHurtBy((PiglinBruteEntity) monster, otherMonster);
				} else if (monster instanceof PiglinEntity) {
					PiglinTasks.wasHurtBy((PiglinEntity) monster, otherMonster);
				}

				//monster.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(monster, MonsterEntity.class, true));
			} else if (monster instanceof IAngerable) {
				((IAngerable)monster).setTarget(otherMonster);
			} else {
				monster.setTarget(otherMonster);
				monster.setLastHurtByMob(otherMonster);
			}
		}
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, Player playerIn, Hand handIn) {
		return super.use(worldIn, playerIn, handIn);
	}

}
