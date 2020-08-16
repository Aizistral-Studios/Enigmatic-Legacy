package com.integral.enigmaticlegacy.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.MixinEnvironment.Side;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.gui.containers.EnigmaticEnchantmentContainer;
import com.integral.enigmaticlegacy.helpers.AdvancedSpawnLocationHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper.AnvilParser;
import com.integral.enigmaticlegacy.items.generic.ItemAdvancedCurio;
import com.integral.enigmaticlegacy.objects.CooldownMap;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.ObfuscatedFields;
import com.integral.enigmaticlegacy.helpers.PatchouliHelper;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketSetEntryState;
import com.integral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;
import vazkii.patchouli.api.BookDrawScreenEvent;
import vazkii.patchouli.client.book.gui.GuiBookLanding;
import vazkii.patchouli.client.book.gui.button.GuiButtonBookEdit;

/**
 * Generic event handler of the whole mod.
 * @author Integral
 */

@Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID)
public class EnigmaticEventHandler {

	private static final String NBT_KEY_FIRSTJOIN = "enigmaticlegacy.firstjoin";
	private static final String NBT_KEY_ENABLESPELLSTONE = "enigmaticlegacy.spellstones_enabled";
	private static final String NBT_KEY_ENABLERING = "enigmaticlegacy.rings_enabled";
	private static final String NBT_KEY_ENABLESCROLL = "enigmaticlegacy.scrolls_enabled";

	// private static final String NBT_KEY_CREEPERKEY =
	// "enigmaticlegacy.scrolls_enabled";

	public static CooldownMap deferredToast = new CooldownMap();
	public static List<IToast> scheduledToasts = new ArrayList<IToast>();
	public static Random theySeeMeRollin = new Random();
	public static HashMap<PlayerEntity, String> anvilFields = new HashMap<PlayerEntity, String>();
	public static HashMap<PlayerEntity, Boolean> hadEnigmaticAmulet = new HashMap<PlayerEntity, Boolean>();

	/*
	@SubscribeEvent
	public void onContainerOpen(PlayerContainerEvent.Open event) {
		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

			if (event.getContainer() instanceof EnchantmentContainer && !(event.getContainer() instanceof EnigmaticEnchantmentContainer)) {
				try {
					player.openContainer = EnigmaticEnchantmentContainer.fromOld((EnchantmentContainer) event.getContainer(), player);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			System.out.println(player.openContainer.getClass());


		}
	}
	*/

	@SubscribeEvent
	public void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
		//event.setLevel(50);
	}

	/*
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onBook(BookDrawScreenEvent event) {
		if (event.gui instanceof GuiBookLanding) {

			//System.out.println("Fired!");

			GuiBookLanding gui = (GuiBookLanding) event.gui;

			if (gui.book.id.equals(EnigmaticLegacy.theAcknowledgment.getRegistryName()))

				gui.removeButtonsIf(button -> {
					return button instanceof GuiButtonBookEdit;
				});

		}
	}
	*/

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onFogRender(EntityViewRenderEvent.FogDensity event) {

		if (event.getInfo().getFluidState().isTagged(FluidTags.LAVA) && SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.magmaHeart)) {
			event.setCanceled(true);
			event.setDensity((float) ConfigHandler.MAGMA_HEART_LAVAFOG_DENSITY.getValue());
		}

	}


	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onEntityTick(TickEvent.ClientTickEvent event) {
		try {
			PlayerEntity player = Minecraft.getInstance().player;

			/*
			 * Handler for displaying queued Toasts on client.
			 */

			EnigmaticEventHandler.deferredToast.tick(player);

			if (EnigmaticEventHandler.deferredToast.getCooldown(player) == 1) {
				Minecraft.getInstance().getToastGui().add(EnigmaticEventHandler.scheduledToasts.get(0));
				EnigmaticEventHandler.scheduledToasts.remove(0);

				if (EnigmaticEventHandler.scheduledToasts.size() > 0)
					EnigmaticEventHandler.deferredToast.put(player, 5);
			}

		} catch (Exception ex) {
			// DO NOTHING
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLooting(LootingLevelEvent event) {

		/*
		 * Handler for adding additional Looting level, if player is bearing Emblem of
		 * Monster Slayer.
		 */

		if (event.getDamageSource() != null)
			if (event.getDamageSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getDamageSource().getTrueSource();
				if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm))
					if (ConfigHandler.MONSTER_CHARM_BONUS_LOOTING.getValue())
						event.setLootingLevel(event.getLootingLevel() + 1);
			}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onExperienceDrop(LivingExperienceDropEvent event) {
		PlayerEntity player = event.getAttackingPlayer();

		if (event.getEntityLiving() instanceof MonsterEntity)
			if (player != null && SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm))
				event.setDroppedExperience((int) (event.getDroppedExperience() * EnigmaticLegacy.monsterCharm.bonusXPModifier));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void miningStuff(PlayerEvent.BreakSpeed event) {

		/*
		 * Handler for calculating mining speed boost from wearing Charm of Treasure
		 * Hunter.
		 */

		float originalSpeed = event.getOriginalSpeed();
		float newSpeed = originalSpeed;

		float miningBoost = 1.0F;
		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.miningCharm))
			miningBoost += ConfigHandler.MINING_CHARM_BREAK_BOOST.getValue().asModifier(false);

		if (!event.getPlayer().func_233570_aj_())
			if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.heavenScroll) || SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.fabulousScroll) || SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.enigmaticItem)) {
				newSpeed = newSpeed * 5F;
			}

		newSpeed = newSpeed * miningBoost;
		event.setNewSpeed(newSpeed);
	}

	@SubscribeEvent
	public void onBlockDropsHarvest(HarvestDropsEvent event) {

		// Oh my god it happens!
		// System.out.println("Event fired!");
	}

	@SubscribeEvent
	public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {

		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Handler for faster effect ticking, if player is bearing Blazing Core.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart))
				if (!player.getActivePotionEffects().isEmpty()) {
					Collection<EffectInstance> effects = player.getActivePotionEffects();

					for (EffectInstance effect : effects) {
						effect.tick(player, () -> {});
					}

				}

			/*
			 * Handler for removing debuffs from players protected by Etherium Armor Shield.
			 */

			if (EnigmaticLegacy.etheriumChestplate.hasShield(player))
				if (!player.getActivePotionEffects().isEmpty()) {
					List<EffectInstance> effects = new ArrayList<EffectInstance>(player.getActivePotionEffects());

					for (EffectInstance effect : effects) {
						if (!effect.getPotion().isBeneficial())
							player.removePotionEffect(effect.getPotion());
					}
				}

			if (event.getEntityLiving().world.isRemote)
				return;

			/*
			 * Handler for players' spellstone cooldowns.
			 */

			if (SuperpositionHandler.hasSpellstoneCooldown(player))
				SuperpositionHandler.setSpellstoneCooldown(player, SuperpositionHandler.getSpellstoneCooldown(player) - 1);

			EnigmaticLegacy.etheriumSword.etheriumSwordCooldowns.tick(player);
			EnigmaticLegacy.enigmaticItem.handleEnigmaticFlight(player);

		}

	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {

		ItemStack stack = evt.getObject();

		/*
		 * Handler for registering item's capabilities implemented in ICurio interface,
		 * for Enigmatic Legacy's namespace specifically.
		 */

		if (stack.getItem() instanceof ICurio && stack.getItem().getRegistryName().getNamespace().equals(EnigmaticLegacy.MODID)) {
			ICurio curioCapabilities = (ICurio) stack.getItem();

			evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
				LazyOptional<ICurio> curio = LazyOptional.of(() -> curioCapabilities);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

					return CuriosCapability.ITEM.orEmpty(cap, this.curio);
				}
			});
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onConfirmedDeath(LivingDeathEvent event) {

		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

			/*
			 * Updates Enigmatic Amulet possession status for LivingDropsEvent.
			 */

			EnigmaticEventHandler.hadEnigmaticAmulet.put(player, SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticAmulet) || SuperpositionHandler.hasItem(player, EnigmaticLegacy.enigmaticAmulet));


			/*
			 * Handler for Scroll of Postmortal Recall.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.escapeScroll) & !player.world.isRemote) {
				ItemStack tomeStack = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.escapeScroll);
				PermanentItemEntity droppedTomeStack = new PermanentItemEntity(player.world, player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), tomeStack.copy());
				droppedTomeStack.setPickupDelay(10);
				player.world.addEntity(droppedTomeStack);

				tomeStack.shrink(1);

				SuperpositionHandler.backToSpawn(player);
			}

		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingDeath(LivingDeathEvent event) {

		if (event.getEntityLiving() instanceof PlayerEntity & !event.getEntityLiving().world.isRemote) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Immortality handler for Heart of Creation and Pearl of the Void.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem) || player.inventory.hasItemStack(new ItemStack(EnigmaticLegacy.enigmaticItem))) {
				event.setCanceled(true);
				player.setHealth(1);
			} else if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl) && Math.random() <= ConfigHandler.VOID_PEARL_UNDEAD_PROBABILITY.getValue().asMultiplier(false)) {
				event.setCanceled(true);
				player.setHealth(1);
			}
		}

	}

	@SubscribeEvent
	public void onLivingHeal(LivingHealEvent event) {

		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Regeneration slowdown handler for Pearl of the Void.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				if (event.getAmount() <= 1.0F) {
					event.setAmount((float) (event.getAmount() / (1.5F * ConfigHandler.VOID_PEARL_REGENERATION_MODIFIER.getValue())));
				}
			}
		}

	}

	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event) {

		if (event.getEntityLiving().world.isRemote)
			return;

		/*
		 * Handler for immunities.
		 */

		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			if (EnigmaticLegacy.etheriumChestplate.hasShield(player)) {
				if (event.getSource().getImmediateSource() instanceof DamagingProjectileEntity || event.getSource().getImmediateSource() instanceof AbstractArrowEntity) {
					event.setCanceled(true);
					player.world.playSound(null, player.func_233580_cy_(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
					player.world.playSound(null, player.func_233580_cy_(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}
			}

			List<ItemStack> advancedCurios = SuperpositionHandler.getAdvancedCurios(player);
			if (advancedCurios.size() > 0) {
				for (ItemStack advancedCurioStack : advancedCurios) {
					ItemAdvancedCurio advancedCurio = (ItemAdvancedCurio) advancedCurioStack.getItem();

					if (advancedCurio.immunityList.contains(event.getSource().damageType))
						event.setCanceled(true);

					if (advancedCurio == EnigmaticLegacy.voidPearl && EnigmaticLegacy.voidPearl.healList.contains(event.getSource().damageType)) {
						player.heal(event.getAmount());
						event.setCanceled(true);
					}
				}
			}

			/*
			 * Handler for Eye of the Nebula dodge effect.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.eyeOfNebula) && !event.isCanceled()) {
				if (Math.random() <= ConfigHandler.EYE_OF_NEBULA_DODGE_PROBABILITY.getValue().asMultiplier(false) && player.hurtResistantTime <= 10 && event.getSource().getTrueSource() instanceof LivingEntity) {

					for (int counter = 0; counter <= 32; counter++) {
						if (SuperpositionHandler.validTeleportRandomly(player, player.world, (int) ConfigHandler.EYE_OF_NEBULA_DODGE_RANGE.getValue()))
							break;
					}

					player.hurtResistantTime = 20;
					event.setCanceled(true);
				}
			}

		} else if (event.getSource().getImmediateSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();

			/*
			 * Handler for triggering Extradimensional Eye's effects when player left-clicks
			 * (or, more technically correct, directly attacks) the entity with the Eye in
			 * main hand.
			 */

			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == EnigmaticLegacy.extradimensionalEye)
				if (ItemNBTHelper.verifyExistance(player.getHeldItemMainhand(), "BoundDimension"))
					if (ItemNBTHelper.getString(player.getHeldItemMainhand(), "BoundDimension", "minecraft:overworld").equals(event.getEntityLiving().world.func_234923_W_().func_240901_a_().toString())) {
						event.setCanceled(true);
						ItemStack stack = player.getHeldItemMainhand();

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), 128, event.getEntityLiving().world.func_234923_W_())), new PacketPortalParticles(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + (event.getEntityLiving().getHeight() / 2), event.getEntityLiving().getPosZ(), 96, 1.5D, false));

						event.getEntityLiving().world.playSound(null, event.getEntityLiving().func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						event.getEntityLiving().setPositionAndUpdate(ItemNBTHelper.getDouble(stack, "BoundX", 0D), ItemNBTHelper.getDouble(stack, "BoundY", 0D), ItemNBTHelper.getDouble(stack, "BoundZ", 0D));
						event.getEntityLiving().world.playSound(null, event.getEntityLiving().func_233580_cy_(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), 128, event.getEntityLiving().world.func_234923_W_())), new PacketRecallParticles(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + (event.getEntityLiving().getHeight() / 2), event.getEntityLiving().getPosZ(), 48, false));

						if (!player.abilities.isCreativeMode)
							stack.shrink(1);
					}
		}

	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {

		if (event.getEntityLiving() instanceof PlayerEntity & !event.getEntityLiving().world.isRemote) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Handler for resistance lists.
			 */

			List<ItemStack> advancedCurios = SuperpositionHandler.getAdvancedCurios(player);
			if (advancedCurios.size() > 0) {
				for (ItemStack advancedCurioStack : advancedCurios) {
					ItemAdvancedCurio advancedCurio = (ItemAdvancedCurio) advancedCurioStack.getItem();

					if (advancedCurio.resistanceList.containsKey(event.getSource().damageType)) {
						event.setAmount(event.getAmount() * advancedCurio.resistanceList.get(event.getSource().damageType).get());
					}

					if (advancedCurio == EnigmaticLegacy.oceanStone) {
						Entity attacker = event.getSource().getTrueSource();
						if (attacker instanceof DrownedEntity || attacker instanceof GuardianEntity || attacker instanceof ElderGuardianEntity)
							event.setAmount(event.getAmount() * ConfigHandler.OCEAN_STONE_UNDERWATER_CREATURES_RESISTANCE.getValue().asModifierInverted());
					}
				}
			}

			/*
			 * Handler for damaging feedback of Blazing Core.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart)) {
				if (event.getSource().getTrueSource() instanceof LivingEntity && EnigmaticLegacy.magmaHeart.nemesisList.contains(event.getSource().damageType)) {
					LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
					if (!attacker.func_230279_az_()) {
						attacker.attackEntityFrom(new EntityDamageSource(DamageSource.ON_FIRE.damageType, player), (float) ConfigHandler.BLAZING_CORE_DAMAGE_FEEDBACK.getValue());
						attacker.setFire(ConfigHandler.BLAZING_CORE_IGNITION_FEEDBACK.getValue());
					}
				}

			}

			/*
			 * Handler for knockback feedback and damage reduction of Etherium Armor Shield.
			 */

			if (EnigmaticLegacy.etheriumChestplate.hasShield(player)) {
				if (event.getSource().getImmediateSource() instanceof LivingEntity) {
					LivingEntity attacker = ((LivingEntity) event.getSource().getTrueSource());
					Vector3 vec = Vector3.fromEntityCenter(player).subtract(Vector3.fromEntityCenter(event.getSource().getTrueSource())).normalize();
					attacker.func_233627_a_(0.75F, vec.x, vec.y);
					player.world.playSound(null, player.func_233580_cy_(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
					player.world.playSound(null, player.func_233580_cy_(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}

				event.setAmount(event.getAmount() * ConfigHandler.ETHERIUM_ARMOR_SHIELD_REDUCTION.getValue().asModifierInverted());
			}

		} else if (event.getEntityLiving() instanceof MonsterEntity) {
			MonsterEntity monster = (MonsterEntity) event.getEntityLiving();

			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

				/*
				 * Handler for damage bonuses of Charm of Monster Slayer.
				 */

				if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm)) {
					if (monster.isEntityUndead()) {
						event.setAmount(event.getAmount() * ConfigHandler.MONSTER_CHARM_UNDEAD_DAMAGE.getValue().asModifier(true));
					} else if (monster.isAggressive() || monster instanceof CreeperEntity) {

						if (monster instanceof EndermanEntity || monster instanceof ZombifiedPiglinEntity || monster instanceof BlazeEntity || monster instanceof GuardianEntity || monster instanceof ElderGuardianEntity || !monster.isNonBoss()) {
						} else {
							event.setAmount(event.getAmount() * ConfigHandler.MONSTER_CHARM_AGGRESSIVE_DAMAGE.getValue().asModifier(true));
						}

					}
				}

				if (monster instanceof CreeperEntity)
					monster.setLastAttackedEntity(player);
				// TODO Test if required

			}
		}

		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

			/*
			 * Handler for applying Withering to victims of bearer of the Void Pearl.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.WITHER, ConfigHandler.VOID_PEARL_WITHERING_EFFECT_TIME.getValue(), ConfigHandler.VOID_PEARL_WITHERING_EFFECT_LEVEL.getValue(), false, true));
			}
		}

	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone evt) {
		PlayerEntity player = evt.getPlayer();

		EnigmaticLegacy.soulCrystal.updatePlayerSoulMap(player);
	}

	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent evt) {
		Entity entity = evt.getEntity();

		if (entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) entity;
			EnigmaticLegacy.soulCrystal.updatePlayerSoulMap(player);
		}

	}

	@SubscribeEvent
	public void onExperienceDrops(LivingExperienceDropEvent event) {
		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			if (this.hadEnigmaticAmulet((PlayerEntity) event.getEntityLiving()) && !event.getEntityLiving().world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event) {

		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
			Boolean droppedCrystal = false;

			if (this.hadEnigmaticAmulet(player) && !event.getDrops().isEmpty() && EnigmaticLegacy.enigmaticAmulet.isVesselEnabled()) {
				ItemStack soulCrystal = SuperpositionHandler.shouldPlayerDropSoulCrystal(player) ? EnigmaticLegacy.soulCrystal.createCrystalFrom(player) : null;
				ItemStack storageCrystal = EnigmaticLegacy.storageCrystal.storeDropsOnCrystal(event.getDrops(), player, soulCrystal);
				PermanentItemEntity droppedStorageCrystal = new PermanentItemEntity(player.world, player.getPosX(), player.getPosY() + 1.5, player.getPosZ(), storageCrystal);
				droppedStorageCrystal.setOwnerId(player.getUniqueID());
				player.world.addEntity(droppedStorageCrystal);
				EnigmaticLegacy.enigmaticLogger.info("Summoned Extradimensional Storage Crystal for " + player.getGameProfile().getName() + " at X: " + player.getPosX() + ", Y: " + player.getPosY() + ", Z: " + player.getPosZ());
				event.getDrops().clear();

				if (soulCrystal != null)
					droppedCrystal = true;

			} else if (SuperpositionHandler.shouldPlayerDropSoulCrystal(player)) {
				ItemStack soulCrystal = EnigmaticLegacy.soulCrystal.createCrystalFrom(player);
				PermanentItemEntity droppedSoulCrystal = new PermanentItemEntity(player.world, player.getPosX(), player.getPosY() + 1.5, player.getPosZ(), soulCrystal);
				droppedSoulCrystal.setOwnerId(player.getUniqueID());
				player.world.addEntity(droppedSoulCrystal);
				EnigmaticLegacy.enigmaticLogger.info("Teared Soul Crystal from " + player.getGameProfile().getName() + " at X: " + player.getPosX() + ", Y: " + player.getPosY() + ", Z: " + player.getPosZ());

				droppedCrystal = true;
			}

			ResourceLocation soulLossAdvancement = new ResourceLocation(EnigmaticLegacy.MODID, "book/soul_loss");

			if (droppedCrystal) {
				SuperpositionHandler.grantAdvancement(player, soulLossAdvancement);
			} else if (!droppedCrystal && SuperpositionHandler.hasAdvancement(player, soulLossAdvancement)) {
				SuperpositionHandler.revokeAdvancement(player, soulLossAdvancement);
			}

		}


		/*
		 * Beheading handler for Axe of Executioner.
		 */

		if (event.getEntityLiving().getClass() == SkeletonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.SKELETON_SKULL, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
			}
		} else if (event.getEntityLiving().getClass() == WitherSkeletonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe) {

				if (!SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.WITHER_SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel()))
					this.addDrop(event, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity && SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.WITHER_SKELETON_SKULL))
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
			}
		} else if (event.getEntityLiving().getClass() == ZombieEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.ZOMBIE_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.ZOMBIE_HEAD, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
			}
		} else if (event.getEntityLiving().getClass() == CreeperEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.CREEPER_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.CREEPER_HEAD, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
			}
		} else if (event.getEntityLiving().getClass() == EnderDragonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.DRAGON_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.DRAGON_HEAD, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLootTablesLoaded(LootTableLoadEvent event) {

		List<ResourceLocation> underwaterRuins = new ArrayList<ResourceLocation>();
		underwaterRuins.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
		underwaterRuins.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);


		LootPool overworldLiterature = SuperpositionHandler.constructLootPool("overworldLiterature", -7F, 2F,
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.overworldRevelationTome, 100).acceptFunction(LootFunctionRevelation.of(RandomValueRange.of(1, 3), RandomValueRange.of(50, 500))));
		LootPool netherLiterature = SuperpositionHandler.constructLootPool("netherLiterature", -7F, 2F,
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.netherRevelationTome, 100).acceptFunction(LootFunctionRevelation.of(RandomValueRange.of(1, 3), RandomValueRange.of(100, 700))));
		LootPool endLiterature = SuperpositionHandler.constructLootPool("endLiterature", -7F, 2F,
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.endRevelationTome, 100).acceptFunction(LootFunctionRevelation.of(RandomValueRange.of(1, 4), RandomValueRange.of(200, 1000))));
		/*
		 * Handlers for adding spellstones to dungeon loot.
		 */

		if (SuperpositionHandler.getMergedAir$EarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -8F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 35), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.angelBlessing, 65));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
		} else if (SuperpositionHandler.getMergedEnder$EarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -4F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.eyeOfNebula, 35), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 65));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getAirDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -4F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.angelBlessing, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getEarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -8F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getNetherDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -12F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.magmaHeart, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getWaterDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -7F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.oceanStone, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getEnderDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -7F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.eyeOfNebula, 90), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.voidPearl, 10));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		}

		/*
		 * Handlers for adding epic dungeon loot to most dungeons.
		 */

		if (SuperpositionHandler.getOverworldDungeons().contains(event.getName())) {
			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_PICKAXE, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_AXE, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_SWORD, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.IRON_SHOVEL, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.itemEntryBuilderED(Items.BOW, 10, 20F, 30F, 1.0F, 0.8F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.ironRing, 20),
				ItemLootEntry.builder(EnigmaticLegacy.commonPotionBase).weight(20).acceptFunction(SetNBT.builder(PotionHelper.createAdvancedPotion(EnigmaticLegacy.commonPotionBase, EnigmaticLegacy.HASTE).getTag())),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.magnetRing, 8),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.unholyGrail, 4),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.loreInscriber, 5),
				// TODO Maybe reconsider
				// SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.oblivionStone, 4),
				ItemLootEntry.builder(Items.CLOCK).weight(10),
				ItemLootEntry.builder(Items.COMPASS).weight(10),
				ItemLootEntry.builder(Items.EMERALD).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4F))),
				ItemLootEntry.builder(Items.SLIME_BALL).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 10F))),
				ItemLootEntry.builder(Items.LEATHER).weight(35).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 8F))),
				ItemLootEntry.builder(Items.PUMPKIN_PIE).weight(25).acceptFunction(SetCount.builder(RandomValueRange.of(4.0F, 16F)))
				);

			LootTable modified = event.getTable();
			modified.addPool(epic);

			if (event.getName() != LootTables.CHESTS_SHIPWRECK_SUPPLY)
				modified.addPool(overworldLiterature);

			event.setTable(modified);

		} else if (SuperpositionHandler.getNetherDungeons().contains(event.getName())) {
			ItemStack fireResistancePotion = new ItemStack(Items.POTION);
			fireResistancePotion = PotionUtils.addPotionToItemStack(fireResistancePotion, Potions.LONG_FIRE_RESISTANCE);

			LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_PICKAXE, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_AXE, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_SWORD, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.itemEntryBuilderED(Items.GOLDEN_SHOVEL, 10, 25F, 30F, 1.0F, 1.0F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.oblivionStone, 8),
				ItemLootEntry.builder(Items.EMERALD).weight(30).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 7F))),
				ItemLootEntry.builder(Items.WITHER_ROSE).weight(25).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4F))),
				ItemLootEntry.builder(Items.GHAST_TEAR).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2F))),
				ItemLootEntry.builder(Items.LAVA_BUCKET).weight(30),
				ItemLootEntry.builder(Items.POTION).weight(15).acceptFunction(SetNBT.builder(fireResistancePotion.getTag()))
				);

			LootTable modified = event.getTable();

			if (!event.getName().equals(LootTables.field_237380_L_))
				modified.addPool(epic);

			modified.addPool(netherLiterature);
			event.setTable(modified);

		} else if (event.getName().equals(LootTables.CHESTS_END_CITY_TREASURE)) {
				LootPool epic = SuperpositionHandler.constructLootPool("epic", 1F, 2F,
				ItemLootEntry.builder(Items.ENDER_PEARL).weight(40).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 5F))),
				ItemLootEntry.builder(Items.ENDER_EYE).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2F))),
				ItemLootEntry.builder(Items.GLISTERING_MELON_SLICE).weight(30).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4F))),
				ItemLootEntry.builder(Items.GOLDEN_CARROT).weight(30).acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4F))),
				ItemLootEntry.builder(Items.PHANTOM_MEMBRANE).weight(25).acceptFunction(SetCount.builder(RandomValueRange.of(3.0F, 7F))),
				ItemLootEntry.builder(Items.ENCHANTING_TABLE).weight(10),
				ItemLootEntry.builder(Items.CAKE).weight(15),
				ItemLootEntry.builder(Items.END_CRYSTAL).weight(7),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.loreInscriber, 10),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.recallPotion, 15),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.mendingMixture, 40),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.astralDust, 85, 1F, 4F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.etheriumOre, 60, 1F, 2F),
				SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.extradimensionalEye, 20)
				);

			LootTable modified = event.getTable();
			modified.addPool(epic);
			modified.addPool(endLiterature);
			event.setTable(modified);
		}

		/*
		 * Handlers for adding special loot to some dungeons.
		 */

		if (SuperpositionHandler.getLibraries().contains(event.getName())) {
			LootPool special = SuperpositionHandler.constructLootPool("el_special", 2F, 3F,
					ItemLootEntry.builder(EnigmaticLegacy.thiccScroll).weight(20).acceptFunction(SetCount.builder(RandomValueRange.of(2F, 6F))),
					ItemLootEntry.builder(EnigmaticLegacy.loreFragment).weight(10).acceptFunction(SetCount.builder(RandomValueRange.of(1F, 2F))));

			LootPool literature = SuperpositionHandler.constructLootPool("literature", -4F, 3F,
					SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.overworldRevelationTome, 100).acceptFunction(LootFunctionRevelation.of(RandomValueRange.of(1, 3), RandomValueRange.of(50, 500))));

			LootTable modified = event.getTable();
			modified.addPool(special);
			modified.addPool(literature);
			event.setTable(modified);
		}


		 if (event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_BIG) || event.getName().equals(LootTables.CHESTS_UNDERWATER_RUIN_SMALL)) {
				LootPool special = SuperpositionHandler.constructLootPool("el_special", -5F, 1F,
						ItemLootEntry.builder(Items.TRIDENT).acceptFunction(SetDamage.func_215931_a(RandomValueRange.of(0.5F, 1.0F))).acceptFunction(EnchantWithLevels.func_215895_a(RandomValueRange.of(15F, 40F)).func_216059_e())
						);

				LootTable modified = event.getTable();
				modified.addPool(special);
				event.setTable(modified);
		}

	}

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayerEntity))
			return;

		try {

			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

			/*
			 * Handler for bestowing Enigmatic Amulet to the player, when they first join
			 * the world.
			 */

			if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_FIRSTJOIN)) {

				ItemStack stack = new ItemStack(EnigmaticLegacy.enigmaticAmulet);
				ItemNBTHelper.setString(stack, "Inscription", player.getDisplayName().getString());

				if (player.inventory.getStackInSlot(8).isEmpty()) {
					player.inventory.setInventorySlotContents(8, stack);
				} else {
					if (!player.inventory.addItemStackToInventory(stack)) {
						ItemEntity dropIt = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), stack);
						player.world.addEntity(dropIt);
					}
				}

				SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_FIRSTJOIN, true);
			}

			/*
			 * Handlers for fixing missing Curios slots upong joining the world.
			 */

			//if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE) || SuperpositionHandler.isSlotLocked("spellstone", player))
				if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_spellstone"))) {
					CuriosApi.getSlotHelper().unlockSlotType("spellstone", event.getPlayer());
					SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE, true);
				}

			//if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL) || SuperpositionHandler.isSlotLocked("scroll", player))
				if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_scroll"))) {
					CuriosApi.getSlotHelper().unlockSlotType("scroll", event.getPlayer());
					SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL, true);
				}

			//if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLERING) || SuperpositionHandler.isSlotLocked("ring", player))
				if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_ring"))) {
					CuriosApi.getSlotHelper().unlockSlotType("ring", event.getPlayer());
					SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLERING, true);
				}

		} catch (Exception ex) {
			EnigmaticLegacy.enigmaticLogger.error("Failed to check player's advancements upon joining the world!");
			ex.printStackTrace();
		}

	}

	@SubscribeEvent
	public void onAdvancement(AdvancementEvent event) {

		String id = event.getAdvancement().getId().toString();
		PlayerEntity player = event.getPlayer();

		if (player instanceof ServerPlayerEntity && id.startsWith(EnigmaticLegacy.MODID+":book/")) {
			EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player) , new PacketSetEntryState(false, id.replace("book/", "")));
		}

		/*
		 * Handler for permanently unlocking Curio slots to player once they obtain
		 * respective advancement.
		 */

		if (id.equals(EnigmaticLegacy.MODID + ":main/discover_spellstone")) {
			//if (SuperpositionHandler.isSlotLocked("spellstone", player)) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketSlotUnlocked("spellstone"));
			//}

			CuriosApi.getSlotHelper().unlockSlotType("spellstone", player);
			SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE, true);
		} else if (id.equals(EnigmaticLegacy.MODID + ":main/discover_scroll")) {
			//if (SuperpositionHandler.isSlotLocked("scroll", player)) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketSlotUnlocked("scroll"));
			//}

			CuriosApi.getSlotHelper().unlockSlotType("scroll", player);
			SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL, true);
		} else if (id.equals(EnigmaticLegacy.MODID + ":main/discover_ring")) {
			//if (SuperpositionHandler.isSlotLocked("ring", player)) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new PacketSlotUnlocked("ring"));
			//}

			CuriosApi.getSlotHelper().unlockSlotType("ring", player);
			SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLERING, true);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		PlayerEntity player = event.getPlayer();

		/*
		 * Handler for re-enabling disabled Curio slots that are supposed to be
		 * permanently unlocked, when the player respawns.
		 */

		if (!player.world.isRemote)
			if (!event.isEndConquered()) {

				if (!player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {

					if (SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLERING))
						CuriosApi.getSlotHelper().unlockSlotType("ring", event.getPlayer());
					if (SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL))
						CuriosApi.getSlotHelper().unlockSlotType("scroll", event.getPlayer());
					if (SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE))
						CuriosApi.getSlotHelper().unlockSlotType("spellstone", event.getPlayer());

				}
			}

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onAnvilOpen(GuiScreenEvent.InitGuiEvent event) {
		if (event.getGui() instanceof AnvilScreen) {

			AnvilScreen screen = (AnvilScreen) event.getGui();

			EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketAnvilField(""));

			try {
				for (Field f : screen.getClass().getDeclaredFields()) {

					f.setAccessible(true);

					if (f.get(screen) instanceof TextFieldWidget) {
						TextFieldWidget widget = (TextFieldWidget) f.get(screen);
						widget.setMaxStringLength(64);
					}

				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onWorldCreation(GuiScreenEvent.InitGuiEvent event) {

		if (event.getGui() instanceof CreateWorldScreen && true) {

			/*
			 * Handler for setting in random world name and respective seed when creating a
			 * new world.
			 */

			CreateWorldScreen screen = (CreateWorldScreen) event.getGui();

			try {
				String localizedWorld = I18n.format("world.enigmaticlegacy.name");
				String number = SuperpositionHandler.generateRandomWorldNumber();
				String name = localizedWorld + number;

				TextFieldWidget nameWidget = (TextFieldWidget) ObfuscatedFields.worldNameField.get(screen);
				TextFieldWidget seedWidget = (TextFieldWidget) ObfuscatedFields.worldSeedField.get(screen.field_238934_c_);

				nameWidget.setText(name);
				seedWidget.setText(number);

				ObfuscatedFields.worldSeedField.set(screen, nameWidget);
				ObfuscatedFields.worldSeedField.set(screen.field_238934_c_, seedWidget);

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	@SubscribeEvent
	public void onAnvilRepair(AnvilRepairEvent event) {
		if (!SuperpositionHandler.hasStoredAnvilField(event.getPlayer()) || event.getPlayer().world.isRemote || event.getItemInput().getItem() != EnigmaticLegacy.loreFragment || event.getIngredientInput().getItem() != EnigmaticLegacy.loreInscriber)
			return;

		AnvilParser parser = AnvilParser.parseField(EnigmaticEventHandler.anvilFields.get(event.getPlayer()));

		if (!parser.getFormattedString().equals("") && !parser.shouldRemoveString()) {
			if (parser.isLoreString()) {
				if (parser.getLoreIndex() != -1)
					ItemLoreHelper.setLoreString(event.getItemResult(), parser.getFormattedString(), parser.getLoreIndex());
				else
					ItemLoreHelper.setLastLoreString(event.getItemResult(), parser.getFormattedString());
			} else {
				ItemLoreHelper.setDisplayName(event.getItemResult(), parser.getFormattedString());
			}
		}

		event.setBreakChance(0.01F);

		event.getPlayer().addItemStackToInventory(event.getIngredientInput().copy());
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event) {

		if (event.getLeft().getCount() == 1)
			if (event.getLeft().getItem().equals(EnigmaticLegacy.loreFragment) && event.getRight().getItem().equals(EnigmaticLegacy.loreInscriber) && event.getName() != null) {
				ItemStack returned = event.getLeft().copy();

				DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {

					if (!event.getName().equals(returned.getDisplayName().getString()))
						EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketAnvilField(event.getName()));

				});

				AnvilParser parser = AnvilParser.parseField(event.getName());

				if (!parser.getFormattedString().equals("") || parser.shouldRemoveString()) {

					if (parser.isLoreString()) {
						if (parser.getLoreIndex() != -1)
							ItemLoreHelper.setLoreString(returned, parser.getFormattedString(), parser.getLoreIndex());
						else
							ItemLoreHelper.addLoreString(returned, parser.getFormattedString());
					} else if (parser.shouldRemoveString()) {
						ItemLoreHelper.removeLoreString(returned, parser.getLoreIndex());
					} else {
						ItemLoreHelper.setDisplayName(returned, parser.getFormattedString());
					}

					event.setCost(1);
					event.setMaterialCost(1);
					event.setOutput(returned);
				}

			} else if (event.getRight().getItem().equals(EnigmaticLegacy.loreFragment) && event.getRight().getChildTag("display") != null) {
				event.setCost(4);
				event.setMaterialCost(1);
				event.setOutput(ItemLoreHelper.mergeDisplayData(event.getRight(), event.getLeft().copy()));
			}
	}

	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {

		if (event.getPlayer() != null && !event.getPlayer().world.isRemote) {
			if (event.getInventory().count(EnigmaticLegacy.enchantmentTransposer) == 1 && event.getCrafting().getItem() == Items.ENCHANTED_BOOK)
				event.getPlayer().world.playSound(null, event.getPlayer().func_233580_cy_(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));
		}

	}

	/*
	@SubscribeEvent
	public void onAttackTargetSet(LivingSetAttackTargetEvent event) {
		if (event.getEntityLiving() instanceof CreeperEntity && event.getTarget() instanceof PlayerEntity) {
			if (SuperpositionHandler.hasCurio(event.getTarget(), EnigmaticLegacy.enigmaticAmulet) && event.getEntityLiving().getLastAttackedEntity() != event.getTarget())
				((CreeperEntity)event.getEntityLiving()).setAttackTarget(null);
				//((CreeperEntity)event.getEntityLiving()).setAttackTarget(null);

				// TODO Finish

		}
	}
	*/

	/**
	 * Adds passed ItemStack to LivingDropsEvent.
	 *
	 * @author Integral
	 */

	public void addDrop(LivingDropsEvent event, ItemStack drop) {
		ItemEntity entityitem = new ItemEntity(event.getEntityLiving().world, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), drop);
		entityitem.setPickupDelay(10);
		event.getDrops().add(entityitem);
	}

	/**
	 * Calculates the chance for Axe of Executioner to behead an enemy.
	 *
	 * @param lootingLevel Amount of looting levels applied to axe or effective
	 *                     otherwise.
	 * @return True if chance works and head should drop, false otherwise.
	 * @author Integral
	 */

	public boolean theySeeMeRollin(int lootingLevel) {
		double chance = ConfigHandler.FORBIDDEN_AXE_BEHEADING_BASE.getValue().asMultiplier(false) + (ConfigHandler.FORBIDDEN_AXE_BEHEADING_BONUS.getValue().asMultiplier(false) * lootingLevel);

		if (Math.random() <= chance)
			return true;
		else
			return false;
	}

	public boolean hadEnigmaticAmulet(PlayerEntity player) {
		return EnigmaticEventHandler.hadEnigmaticAmulet.containsKey(player) ? EnigmaticEventHandler.hadEnigmaticAmulet.get(player) : false;
	}


}
