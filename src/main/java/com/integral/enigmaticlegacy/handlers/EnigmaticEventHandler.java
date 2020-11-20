package com.integral.enigmaticlegacy.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IItemCurio;
import com.integral.enigmaticlegacy.api.items.ItemizedCurioInstance;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.enchantments.CeaselessEnchantment;
import com.integral.enigmaticlegacy.config.JsonConfigHandler;
import com.integral.enigmaticlegacy.entities.PermanentItemEntity;
import com.integral.enigmaticlegacy.gui.EnderChestInventoryButton;
import com.integral.enigmaticlegacy.helpers.CrossbowHelper;
import com.integral.enigmaticlegacy.helpers.EnigmaticEnchantmentHelper;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.helpers.OverlayPositionHelper;
import com.integral.enigmaticlegacy.helpers.OverlayPositionHelper.AnchorPoint;
import com.integral.enigmaticlegacy.helpers.OverlayPositionHelper.OverlayPosition;
import com.integral.enigmaticlegacy.helpers.PotionHelper;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.BerserkEmblem;
import com.integral.enigmaticlegacy.items.CursedRing;
import com.integral.enigmaticlegacy.items.CursedScroll;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet.AmuletColor;
import com.integral.enigmaticlegacy.items.EtheriumArmor;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.ForbiddenAxe;
import com.integral.enigmaticlegacy.items.ForbiddenFruit;
import com.integral.enigmaticlegacy.items.HunterGuide;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.MiningCharm;
import com.integral.enigmaticlegacy.items.MonsterCharm;
import com.integral.enigmaticlegacy.items.OceanStone;
import com.integral.enigmaticlegacy.items.RevelationTome;
import com.integral.enigmaticlegacy.items.TheTwist;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.enigmaticlegacy.objects.CooldownMap;
import com.integral.enigmaticlegacy.objects.DamageSourceNemesisCurse;
import com.integral.enigmaticlegacy.objects.DimensionalPosition;
import com.integral.enigmaticlegacy.objects.Perhaps;
import com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack;
import com.integral.enigmaticlegacy.objects.TransientPlayerData;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketForceArrowRotations;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketSetEntryState;
import com.integral.enigmaticlegacy.packets.clients.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.integral.enigmaticlegacy.packets.server.PacketAnvilField;
import com.integral.enigmaticlegacy.packets.server.PacketEnderRingKey;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.integral.enigmaticlegacy.triggers.RevelationTomeBurntTrigger;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.FindInteractionAndLookTargetTask;
import net.minecraft.entity.ai.brain.task.ForgetAttackTargetTask;
import net.minecraft.entity.ai.brain.task.LookAtEntityTask;
import net.minecraft.entity.ai.brain.task.SupplementedTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.SilverfishEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ServerRecipeBook;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.EnchantWithLevels;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.client.gui.CuriosScreen;

import static com.integral.enigmaticlegacy.EnigmaticLegacy.*;
import static com.integral.enigmaticlegacy.objects.RegisteredMeleeAttack.getRegisteredMeleeAttack;
import static com.integral.enigmaticlegacy.config.JsonConfigHandler.*;

/**
 * Generic event handler of the whole mod.
 * @author Integral
 */

@Mod.EventBusSubscriber(modid = EnigmaticLegacy.MODID)
public class EnigmaticEventHandler {
	private static final String NBT_KEY_FIRSTJOIN = "enigmaticlegacy.firstjoin";
	private static final String NBT_KEY_CURSEDGIFT = "enigmaticlegacy.cursedgift";
	private static final String NBT_KEY_ENABLESPELLSTONE = "enigmaticlegacy.spellstones_enabled";
	private static final String NBT_KEY_ENABLERING = "enigmaticlegacy.rings_enabled";
	private static final String NBT_KEY_ENABLESCROLL = "enigmaticlegacy.scrolls_enabled";

	public static final ResourceLocation FIREBAR_LOCATION = new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/firebar.png");
	public static final ResourceLocation ICONS_LOCATION = new ResourceLocation(EnigmaticLegacy.MODID, "textures/gui/generic_icons.png");

	public static final CooldownMap deferredToast = new CooldownMap();
	public static final List<IToast> scheduledToasts = new ArrayList<>();
	public static final HashMap<LivingEntity, Float> knockbackThatBastard = new HashMap<>();
	public static final Random theySeeMeRollin = new Random();
	public static final Multimap<PlayerEntity, Item> postmortalPossession = ArrayListMultimap.create();
	public static final Multimap<PlayerEntity, GuardianEntity> angeredGuardians = ArrayListMultimap.create();

	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent.Stop event) {
		if (event.getItem().getItem() instanceof CrossbowItem && event.getEntityLiving() instanceof PlayerEntity) {
			CrossbowItem crossbow = (CrossbowItem) event.getItem().getItem();
			ItemStack crossbowStack = event.getItem();

			/*
			 * Make sure that our crossbow actually has any of this mod's enchantments,
			 * since otherwise overriding it's behavior is unneccessary.
			 */

			if (!EnigmaticEnchantmentHelper.hasCustomCrossbowEnchantments(crossbowStack))
				return;

			// Cancelling the event to prevent vanilla functionality from working
			event.setCanceled(true);

			/*
			 * Same code as in CrossbowItem#onPlayerStoppedUsing, but CrossbowItem#hasAmmo is
			 * replaced with altered method from CrossbowHelper, to enforce custom behavior.
			 */

			int i = crossbow.getUseDuration(crossbowStack) - event.getDuration();
			float f = CrossbowItem.getCharge(i, crossbowStack);
			if (f >= 1.0F && !CrossbowItem.isCharged(crossbowStack) && CrossbowHelper.hasAmmo(event.getEntityLiving(), crossbowStack)) {
				CrossbowItem.setCharged(crossbowStack, true);
				SoundCategory soundcategory = event.getEntityLiving() instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
				event.getEntityLiving().world.playSound((PlayerEntity) null, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (theySeeMeRollin.nextFloat() * 0.5F + 1.0F) + 0.2F);
			}

		}
	}

	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent event) {
		if (event instanceof PlayerInteractEvent.RightClickItem || event instanceof PlayerInteractEvent.RightClickBlock || event instanceof PlayerInteractEvent.EntityInteract) {
			if (event.getItemStack().getItem() instanceof CrossbowItem) {
				ItemStack itemstack = event.getItemStack();

				/*
				 * Make sure that our crossbow actually has any of this mod's enchantments,
				 * since otherwise overriding it's behavior is unneccessary.
				 */

				if (!EnigmaticEnchantmentHelper.hasCustomCrossbowEnchantments(itemstack))
					return;

				// Cancelling the event to prevent vanilla functionality from working
				event.setCanceled(true);

				/*
				 * Same code as in CrossbowItem#onItemRightClick, but CrossbowItem#fireProjectiles is
				 * replaced with altered method from CrossbowHelper, to enforce custom behavior.
				 */

				if (CrossbowItem.isCharged(itemstack)) {
					CrossbowHelper.fireProjectiles(event.getWorld(), event.getPlayer(), event.getHand(), itemstack, CrossbowItem.func_220013_l(itemstack), 1.0F);
					CrossbowItem.setCharged(itemstack, false);
					event.setCancellationResult(ActionResultType.CONSUME);
				} else if (!event.getPlayer().findAmmo(itemstack).isEmpty() || (CeaselessEnchantment.allowNoArrow.getValue() && EnigmaticEnchantmentHelper.hasCeaselessEnchantment(itemstack))) {
					if (!CrossbowItem.isCharged(itemstack)) {
						((CrossbowItem) Items.CROSSBOW).isLoadingStart = false;
						((CrossbowItem) Items.CROSSBOW).isLoadingMiddle = false;
						event.getPlayer().setActiveHand(event.getHand());
					}
					event.setCancellationResult(ActionResultType.CONSUME);
				} else {
					event.setCancellationResult(ActionResultType.FAIL);
				}
			}
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	@OnlyIn(Dist.CLIENT)
	public void onOverlayRender(RenderGameOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();

		if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
			TransientPlayerData data = TransientPlayerData.get(mc.player);

			if (data.getFireImmunityTimer() <= 0 || !SuperpositionHandler.hasCurio(mc.player, EnigmaticLegacy.magmaHeart))
				return;

			float partialTick = event.getPartialTicks();
			float barFiller = data.getFireImmunityFraction(partialTick);
			MatrixStack matrixStack = event.getMatrixStack();
			int x = event.getWindow().getScaledWidth() / 2 - 91;

			int xCorrection = 0;
			int yCorrection = 0;
			boolean renderXP = false;

			if (!renderXP) {
				event.setCanceled(true);
			}

			mc.getTextureManager().bindTexture(FIREBAR_LOCATION);

			if (true) {
				int k = (int)(barFiller * 183.0F);
				int l = event.getWindow().getScaledHeight() - 32 + 3;
				AbstractGui.blit(matrixStack, x+xCorrection, l+yCorrection, 0, 0, 182, 5, 256, 256);
				if (k > 0) {
					AbstractGui.blit(matrixStack, x+xCorrection, l+yCorrection, 0, 5, k, 5, 256, 256);
				}
			}

			if (true) {
				String title = I18n.format("gui.enigmaticlegacy.blazing_core_bar_title");
				int i1 = (event.getWindow().getScaledWidth() - mc.fontRenderer.getStringWidth(title)) / 2;
				int j1 = event.getWindow().getScaledHeight() - 31 - 4;

				int stringXCorrection = 0;
				int stringYCorrection = 0;

				try {
					stringXCorrection = Integer.parseInt(I18n.format("gui.enigmaticlegacy.blazing_core_bar_offsetX"));
					stringYCorrection = Integer.parseInt(I18n.format("gui.enigmaticlegacy.blazing_core_bar_offsetY"));
				} catch (Exception ex) {
					// NO-OP
				}

				i1 += xCorrection + stringXCorrection;
				j1 += yCorrection + stringYCorrection;

				int boundaryColor = 5832704;

				mc.fontRenderer.drawString(matrixStack, title, i1 + 1, j1, boundaryColor);
				mc.fontRenderer.drawString(matrixStack, title, i1 - 1, j1, boundaryColor);
				mc.fontRenderer.drawString(matrixStack, title, i1, j1 + 1, boundaryColor);
				mc.fontRenderer.drawString(matrixStack, title, i1, j1 - 1, boundaryColor);
				mc.fontRenderer.drawString(matrixStack, title, i1, j1, 16770638);
			}

		} else if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
			if (SuperpositionHandler.hasCurio(mc.player, EnigmaticLegacy.oceanStone) || SuperpositionHandler.hasCurio(mc.player, EnigmaticLegacy.voidPearl)) {
				if (OceanStone.preventOxygenBarRender.getValue()) {
					event.setCanceled(true);
				}
			}
		} else if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD) {

			if (EnigmaticLegacy.forbiddenFruit.haveConsumedFruit(mc.player)) {

				if (!ForbiddenFruit.renderHungerBar.getValue()) {
					event.setCanceled(true);
					return;
				} else if (!ForbiddenFruit.replaceHungerBar.getValue())
					return;

				event.setCanceled(true);
				mc.getTextureManager().bindTexture(ICONS_LOCATION);

				int width = event.getWindow().getScaledWidth();
				int height = event.getWindow().getScaledHeight();

				PlayerEntity player = mc.player;
				RenderSystem.enableBlend();
				int left = width / 2 + 91;
				int top = height - ForgeIngameGui.right_height;

				ForgeIngameGui.right_height += 10;
				boolean unused = false;// Unused flag in vanilla, seems to be part of a 'fade out' mechanic

				FoodStats stats = mc.player.getFoodStats();
				int level = stats.getFoodLevel();

				int barPx = 8;
				int barNum = 10;

				for (int i = 0; i < barNum; ++i)
				{
					int idx = i * 2 + 1;
					int x = left - i * barPx - 9;
					int y = top;
					int icon = 16;
					byte background = 0;

					/*
					if (mc.player.isPotionActive(Effects.HUNGER))
					{
						icon += 36;
						background = 13;
					}
					if (unused)
					{
						background = 1; //Probably should be a += 1 but vanilla never uses this
					}
					 */

					if (player.getFoodStats().getSaturationLevel() <= 0.0F && mc.ingameGUI.getTicks() % (level * 3 + 1) == 0)
					{
						y = top + (theySeeMeRollin.nextInt(3) - 1);
					}

					mc.ingameGUI.blit(event.getMatrixStack(), x, y, 0, 0, 9, 9);

					/*
					if (idx < level) {
						mc.ingameGUI.blit(event.getMatrixStack(), x, y, icon + 36, 27, 9, 9);
					} else if (idx == level) {
						mc.ingameGUI.blit(event.getMatrixStack(), x, y, icon + 45, 27, 9, 9);
					}
					 */
				}
				RenderSystem.disableBlend();

				mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
			}

		}
	}

	@SubscribeEvent
	public void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
		BlockPos where = event.getPos();
		boolean shouldBoost = false;

		int radius = 16;
		List<PlayerEntity> players = event.getWorld().getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(where.add(-radius, -radius, -radius), where.add(radius, radius, radius)));

		for (PlayerEntity player : players)
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing)) {
				shouldBoost = true;
			}

		if (shouldBoost) {
			event.setLevel(event.getLevel()+CursedRing.enchantingBonus.getValue());
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public void onItemBurnt(FurnaceFuelBurnTimeEvent event) {

		if (event.getItemStack() != null && event.getItemStack().getItem() instanceof RevelationTome) {
			if (ServerLifecycleHooks.getCurrentServer() != null && ItemNBTHelper.verifyExistance(event.getItemStack(), RevelationTome.lastHolderTag)) {
				ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(ItemNBTHelper.getUUID(event.getItemStack(), RevelationTome.lastHolderTag, MathHelper.getRandomUUID()));

				if (player != null) {
					RevelationTomeBurntTrigger.INSTANCE.trigger(player);
				}
			}
		}

	}


	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onInventoryGuiInit(GuiScreenEvent.InitGuiEvent.Post evt) {
		Screen screen = evt.getGui();

		if (screen instanceof InventoryScreen || screen instanceof CreativeScreen || screen instanceof CuriosScreen) {

			ContainerScreen<?> gui = (ContainerScreen<?>) screen;
			boolean isCreative = screen instanceof CreativeScreen;
			Tuple<Integer, Integer> offsets = EnderChestInventoryButton.getOffsets(isCreative);
			int x = offsets.getA();
			int y = offsets.getB();

			evt.addWidget(new EnderChestInventoryButton(gui, gui.getGuiLeft() + x, (gui.height / 2 - 22) + y, 20, 18, 0, 0, 19,
					new ResourceLocation(
							"enigmaticlegacy:textures/gui/ender_chest_button.png"),(button) -> {
								EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(), new PacketEnderRingKey(true));
							}));
		}
	}


	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onFogRender(EntityViewRenderEvent.FogDensity event) {

		if (event.getInfo().getFluidState().isTagged(FluidTags.LAVA) && SuperpositionHandler.hasCurio(Minecraft.getInstance().player, EnigmaticLegacy.magmaHeart)) {
			event.setCanceled(true);
			event.setDensity((float) MagmaHeart.lavafogDensity.getValue());
		}

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onEntityTick(TickEvent.ClientTickEvent event) {

		if (event.phase == event.phase.END) {
			try {
				PlayerEntity player = Minecraft.getInstance().player;

				/*
				 * This event seems to be the only way to detect player logging out of server
				 * on client side, since PlayerEvent.PlayerLoggedOutEvent seems to only be fired
				 * on the server side.
				 */

				if (player == null) {
					if (OmniconfigWrapper.onRemoteServer) {

						/*
						 * After we log out of remote server, dismiss config values it
						 * sent us and load our own ones from local file.
						 */

						for (OmniconfigWrapper wrapper : OmniconfigWrapper.wrapperRegistry.values()) {

							EnigmaticLegacy.logger.info("Dismissing values of " + wrapper.config.getConfigFile().getName() + " in favor of local config...");

							for (Omniconfig.GenericParameter param : wrapper.retrieveInvocationList()) {
								if (param.isSynchronized()) {
									String oldValue = param.valueToString();
									param.invoke(wrapper.config);

									EnigmaticLegacy.logger.info("Value of '" + param.getId() + "' was restored to '" + param.valueToString() + "'; former server-forced value: " + oldValue);
								}
							}
						}

						OmniconfigWrapper.onRemoteServer = false;

					}
				}


				/*
				 * Handler for displaying queued Toasts on client.
				 */

				EnigmaticEventHandler.deferredToast.tick(player);

				if (EnigmaticEventHandler.deferredToast.getCooldown(player) == 1) {
					Minecraft.getInstance().getToastGui().add(EnigmaticEventHandler.scheduledToasts.get(0));
					EnigmaticEventHandler.scheduledToasts.remove(0);

					if (EnigmaticEventHandler.scheduledToasts.size() > 0) {
						EnigmaticEventHandler.deferredToast.put(player, 5);
					}
				}

			} catch (Exception ex) {
				// DO NOTHING
			}

		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLooting(LootingLevelEvent event) {

		if (event.getDamageSource() != null)
			if (event.getDamageSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getDamageSource().getTrueSource();

				// NO-OP
			}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void miningStuff(PlayerEvent.BreakSpeed event) {

		/*
		 * Handler for calculating mining speed boost from wearing Charm of Treasure Hunter.
		 */

		float originalSpeed = event.getOriginalSpeed();
		float correctedSpeed = originalSpeed;

		float miningBoost = 1.0F;
		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.miningCharm)) {
			miningBoost += MiningCharm.breakSpeedBonus.getValue().asModifier();
		}

		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.cursedScroll)) {
			miningBoost += CursedScroll.miningBoost.getValue().asModifier()*SuperpositionHandler.getCurseAmount(event.getPlayer());
		}

		if (EnigmaticLegacy.enigmaticAmulet.ifHasColor(event.getPlayer(), AmuletColor.GREEN)) {
			miningBoost += 0.25F;
		}

		if (!event.getPlayer().isOnGround())
			if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.heavenScroll) || SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.fabulousScroll) || SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.enigmaticItem)) {
				correctedSpeed *= 5F;
			}

		if (event.getPlayer().areEyesInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(event.getPlayer())) {
			if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.oceanStone)) {
				correctedSpeed *= 5F;
			}
		}

		/*
		if (SuperpositionHandler.hasCurio(event.getPlayer(), EnigmaticLegacy.cursedRing) && event.getPlayer().inventory.getDestroySpeed(event.getState()) == 1.0F) {
			correctedSpeed += 3.0F;
		}
		 */

		correctedSpeed = correctedSpeed * miningBoost;
		correctedSpeed -= event.getOriginalSpeed();

		event.setNewSpeed(event.getNewSpeed() + correctedSpeed);
	}

	@SubscribeEvent
	public void onHarvestCheck(PlayerEvent.HarvestCheck event) {
		if (event.getEntityLiving() instanceof PlayerEntity)
			if (!event.canHarvest() && SuperpositionHandler.hasCurio(event.getEntityLiving(), EnigmaticLegacy.cursedRing)) {
				//event.setCanHarvest(event.getTargetBlock().getHarvestLevel() <= 2);
			}
	}

	@SubscribeEvent
	public void onLivingKnockback(LivingKnockBackEvent event) {
		if (knockbackThatBastard.containsKey(event.getEntityLiving())) {
			float knockbackPower = knockbackThatBastard.get(event.getEntityLiving());
			event.setStrength(event.getStrength() * knockbackPower);
			knockbackThatBastard.remove(event.getEntityLiving());
		}

		if (event.getEntityLiving() instanceof PlayerEntity && SuperpositionHandler.hasCurio(event.getEntityLiving(), EnigmaticLegacy.cursedRing)) {
			event.setStrength(event.getStrength()*CursedRing.knockbackDebuff.getValue().asModifier());
		}
	}

	@SubscribeEvent
	public void onPlayerTick(LivingEvent.LivingUpdateEvent event) {

		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Temporary handler for fixing player state corruption.
			 * I still do not know why it happens.
			 */

			if (Float.isNaN(player.getHealth())) {
				player.setHealth(0F);
			}

			if (Float.isNaN(player.getAbsorptionAmount())) {
				player.setAbsorptionAmount(0F);
			}

			if (EnigmaticLegacy.forbiddenFruit.haveConsumedFruit(player)) {
				FoodStats foodStats = player.getFoodStats();
				foodStats.setFoodLevel(20);
				foodStats.foodSaturationLevel = 0F;

				if (player.isPotionActive(Effects.HUNGER)) {
					player.removePotionEffect(Effects.HUNGER);
				}
			}

			if (player.isSleeping() && player.getSleepTimer() > 90 && SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing)) {
				player.sleepTimer = 90;
			}

			if (player.isBurning() && SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing)) {
				player.forceFireTicks(player.getFireTimer()+2);
			}

			/*
			 * Handler for faster effect ticking, if player is bearing Blazing Core.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart))
				if (!player.getActivePotionEffects().isEmpty()) {
					Collection<EffectInstance> effects = player.getActivePotionEffects();

					for (EffectInstance effect : effects) {
						if (effect.getPotion().equals(Effects.FIRE_RESISTANCE)) {
							if (player.ticksExisted % 2 == 0 && effect.duration > 0) {
								effect.duration += 1;
							}
						} else {
							effect.tick(player, () -> {});
						}
					}

				}

			/*
			 * Handler for removing debuffs from players protected by Etherium Armor Shield.
			 * Removed as of Release 2.5.0.
			 */

			/*
			if (EnigmaticLegacy.etheriumChestplate.hasShield(player))
				if (!player.getActivePotionEffects().isEmpty()) {
					List<EffectInstance> effects = new ArrayList<EffectInstance>(player.getActivePotionEffects());

					for (EffectInstance effect : effects) {
						if (!effect.getPotion().isBeneficial()) {
							player.removePotionEffect(effect.getPotion());
						}
					}
				}
			 */

			if (player instanceof ServerPlayerEntity) {

				/*
				 * Handler for players' spellstone cooldowns.
				 */

				if (SuperpositionHandler.hasSpellstoneCooldown(player)) {
					SuperpositionHandler.tickSpellstoneCooldown(player, 1);
				}

				TransientPlayerData data = TransientPlayerData.get(player);
				data.setFireImmunityTimer(data.getFireImmunityTimer() - (player.isBurning() ? 100 : 200));

				if (data.needsSync) {
					data.syncToPlayer();
					data.needsSync = false;
				}

				EnigmaticLegacy.enigmaticItem.handleEnigmaticFlight(player);

			}

		}

	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
		ItemStack stack = evt.getObject();

		/*
		 * Handler for registering item's capabilities implemented in ICurio interface,
		 * for Enigmatic Legacy's namespace specifically.
		 *
		 * Insisting on bad design choices since 2016!
		 */

		if (stack.getItem() instanceof IItemCurio) {
			ItemizedCurioInstance itemizedCurio = new ItemizedCurioInstance((IItemCurio)stack.getItem(), stack);

			evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
				LazyOptional<ICurio> curio = LazyOptional.of(() -> itemizedCurio);

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
			 * Updates Enigmatic Amulet/Scroll of Postmortal Recall possession status for LivingDropsEvent.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticAmulet) || SuperpositionHandler.hasItem(player, EnigmaticLegacy.enigmaticAmulet)) {
				postmortalPossession.put(player, EnigmaticLegacy.enigmaticAmulet);
			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.escapeScroll)) {
				postmortalPossession.put(player, EnigmaticLegacy.escapeScroll);

				if (!player.world.isRemote) {
					ItemStack tomeStack = SuperpositionHandler.getCurioStack(player, EnigmaticLegacy.escapeScroll);
					PermanentItemEntity droppedTomeStack = new PermanentItemEntity(player.world, player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), tomeStack.copy());
					droppedTomeStack.setPickupDelay(10);
					player.world.addEntity(droppedTomeStack);

					tomeStack.shrink(1);
				}
			}

		}

	}

	@SubscribeEvent
	public void onCurioDrops(DropRulesEvent event) {
		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

			if (SuperpositionHandler.hasItem(player, cursedStone) && player.world.getDimensionKey() == proxy.getNetherKey()) {
				BlockPos deathPos = player.getPosition();

				if (this.isThereLava(player.world, deathPos)) {
					BlockPos surfacePos = deathPos;

					while (true) {
						BlockPos nextAbove = surfacePos.add(0, 1, 0);
						if (this.isThereLava(player.world, nextAbove)) {
							surfacePos = nextAbove;
							continue;
						} else {
							break;
						}
					}

					boolean confirmLavaPool = true;

					for(int i = -3; i <= 2; ++i) {
						final int fi = i;

						boolean checkArea = BlockPos.getAllInBox(surfacePos.add(-3, i, -3), surfacePos.add(3, i, 3))
								.map(blockPos -> {
									if (fi <= 0)
										return this.isThereLava(player.world, blockPos);
									else
										return player.world.isAirBlock(blockPos) || this.isThereLava(player.world, blockPos);
								})
								.reduce((prevResult, nextElement) -> {
									return prevResult && nextElement;
								}).orElse(false);

						confirmLavaPool = confirmLavaPool && checkArea;
					}

					if (confirmLavaPool) {
						// System.out.println("Lava pool confirmed!");
						for(List<ItemStack> list : player.inventory.allInventories) {
							for(ItemStack itemstack : list) {
								if (!itemstack.isEmpty() && itemstack.getItem() == cursedStone) {
									itemstack.setCount(0);
								}
							}
						}

						event.addOverride(stack -> stack != null && stack.getItem() == cursedRing, DropRule.DESTROY);
						soulCrystal.setLostCrystals(player, soulCrystal.getLostCrystals(player)+1);
						player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1.0F, 0.5F);
					}
				}
			}
		}
	}

	private boolean isThereLava(World world, BlockPos pos) {
		FluidState fluidState = world.getBlockState(pos).getFluidState();
		if (fluidState != null && fluidState.isTagged(FluidTags.LAVA) && fluidState.isSource())
			return true;
		else
			return false;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLivingDeath(LivingDeathEvent event) {

		if (event.getEntityLiving() instanceof PlayerEntity & !event.getEntityLiving().world.isRemote) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Immortality handler for Heart of Creation and Pearl of the Void.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem) || player.inventory.hasItemStack(new ItemStack(EnigmaticLegacy.enigmaticItem)) || event.getSource() instanceof DamageSourceNemesisCurse) {
				event.setCanceled(true);
				player.setHealth(1);
			} else if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl) && Math.random() <= VoidPearl.undeadProbability.getValue().asMultiplier(false)) {
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
			 * Removed as of Release 2.5.0.

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				if (event.getAmount() <= 1.0F) {
					event.setAmount((float) (event.getAmount() / (1.5F * ConfigHandler.VOID_PEARL_REGENERATION_MODIFIER.getValue())));
				}
			}
			 */

			if (event.getAmount() <= 1.0F)
				if (forbiddenFruit.haveConsumedFruit(player)) {
					event.setAmount(event.getAmount()*ForbiddenFruit.regenerationSubtraction.getValue().asModifierInverted());
				}

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedScroll)) {
				event.setAmount(event.getAmount() + (event.getAmount()*(CursedScroll.regenBoost.getValue().asModifier()*SuperpositionHandler.getCurseAmount(player))));
			}
		}

	}



	@SubscribeEvent
	public void onProjectileImpact(ProjectileImpactEvent event) {
		if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
			EntityRayTraceResult result = (EntityRayTraceResult) event.getRayTraceResult();
			if (result.getEntity() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) result.getEntity();

				if (!player.world.isRemote) {
					boolean trigger = false;
					double chance = 0.0D;

					if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.angelBlessing)) {
						trigger = true;
						chance += AngelBlessing.deflectChance.getValue().asModifier(false);
					}

					if (EnigmaticLegacy.enigmaticAmulet.ifHasColor(player, AmuletColor.VIOLET)) {
						trigger = true;
						chance += 0.15;
					}


					if (trigger && Math.random() <= chance) {
						event.setCanceled(true);

						Entity arrow = event.getEntity();
						arrow.setMotion(arrow.getMotion().scale(-1.0D));
						arrow.prevRotationYaw = arrow.rotationYaw + 180.0F;
						arrow.rotationYaw = arrow.rotationYaw + 180.0F;

						if (arrow instanceof AbstractArrowEntity && !(arrow instanceof TridentEntity)) {
							((AbstractArrowEntity) arrow).setShooter(player);
						} else if (arrow instanceof DamagingProjectileEntity) {
							((DamagingProjectileEntity) arrow).setShooter(player);

							((DamagingProjectileEntity) arrow).accelerationX = -((DamagingProjectileEntity) arrow).accelerationX;
							((DamagingProjectileEntity) arrow).accelerationY = -((DamagingProjectileEntity) arrow).accelerationY;
							((DamagingProjectileEntity) arrow).accelerationZ = -((DamagingProjectileEntity) arrow).accelerationZ;
						}

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 64.0D, player.world.getDimensionKey())), new PacketForceArrowRotations(arrow.getEntityId(), arrow.rotationYaw, arrow.rotationPitch, arrow.getMotion().x, arrow.getMotion().y, arrow.getMotion().z, arrow.getPosX(), arrow.getPosY(), arrow.getPosZ()));

						player.world.playSound(null, player.getPosition(), EnigmaticLegacy.DEFLECT, SoundCategory.PLAYERS, 1.0F, 0.95F + (float) (Math.random() * 0.1D));
					}
				} /*else {
					if (event.getEntity().getTags().contains("enigmaticlegacy.redirected")) {
						event.setCanceled(true);
						event.getEntity().removeTag("enigmaticlegacy.redirected");
					}
				}*/
			}
		}
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event) {

		if (event.getEntityLiving().world.isRemote)
			return;

		/*
		 * Handler for immunities and projectile deflection.
		 */

		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			if (event.getSource().getImmediateSource() instanceof DamagingProjectileEntity || event.getSource().getImmediateSource() instanceof AbstractArrowEntity) {

				/*if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.angelBlessing) && Math.random() <= ConfigHandler.ANGEL_BLESSING_DEFLECT_CHANCE.getValue().asModifier(false)) {
					event.setCanceled(true);
					Entity arrow = event.getSource().getImmediateSource();

					if (!arrow.world.isRemote) {
						CompoundNBT data = arrow.serializeNBT();
						Entity entity = arrow.getType().create(arrow.world);
						entity.deserializeNBT(data);
						entity.setUniqueId(MathHelper.getRandomUUID());
						entity.setPositionAndUpdate(arrow.getPosX(), arrow.getPosY(), arrow.getPosZ());
						entity.setMotion(arrow.getMotion().scale(-1.0D));

						entity.rotationYaw = arrow.rotationYaw + 180.0F;
						entity.prevRotationYaw = arrow.rotationYaw + 180.0F;

						if (entity instanceof AbstractArrowEntity && !(entity instanceof TridentEntity)) {
							((AbstractArrowEntity) entity).setShooter(player);
						} else if (entity instanceof DamagingProjectileEntity) {
							((DamagingProjectileEntity) entity).setShooter(player);

							((DamagingProjectileEntity) entity).accelerationX = -((DamagingProjectileEntity) arrow).accelerationX;
							((DamagingProjectileEntity) entity).accelerationY = -((DamagingProjectileEntity) arrow).accelerationY;
							((DamagingProjectileEntity) entity).accelerationZ = -((DamagingProjectileEntity) arrow).accelerationZ;
						}

						arrow.remove();
						arrow.forceSetPosition(0, 0, 0);

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 64.0D, player.world.getDimensionKey())), new PacketForceArrowRotations(arrow.getEntityId(), arrow.rotationYaw, arrow.rotationPitch, arrow.getMotion().x, arrow.getMotion().y, arrow.getMotion().z));

						//player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.PLAYERS, 1.0F, 0.95F + (float) (Math.random() * 0.1D));
						player.world.playSound(null, player.getPosition(), EnigmaticLegacy.DEFLECT, SoundCategory.PLAYERS, 1.0F, 0.95F + (float) (Math.random() * 0.1D));

						player.world.addEntity(entity);
					}

				} else*/ if (EnigmaticLegacy.etheriumChestplate.hasShield(player)) {
					event.setCanceled(true);

					player.world.playSound(null, player.getPosition(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}
			}

			if (event.getSource().damageType == DamageSource.FALL.damageType) {
				if (EnigmaticLegacy.enigmaticAmulet.ifHasColor(player, AmuletColor.MAGENTA) && event.getAmount() <= 2.0f) {
					event.setCanceled(true);
				}
			}

			List<ItemStack> advancedCurios = SuperpositionHandler.getAdvancedCurios(player);
			if (advancedCurios.size() > 0) {
				for (ItemStack advancedCurioStack : advancedCurios) {
					ItemSpellstoneCurio advancedCurio = (ItemSpellstoneCurio) advancedCurioStack.getItem();

					if (advancedCurio.immunityList.contains(event.getSource().damageType)) {
						event.setCanceled(true);
					}

					/*
					 * This used to heal player from debuff damage if they have Pearl of the Void.
					 * Removed in 2.5.0 Release.
					 */

					/*
					if (advancedCurio == EnigmaticLegacy.voidPearl && EnigmaticLegacy.voidPearl.healList.contains(event.getSource().damageType)) {
						player.heal(event.getAmount());
						event.setCanceled(true);
					}
					 */
				}
			}

			/*
			 * Handler for Eye of the Nebula dodge effect.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.eyeOfNebula) && !event.isCanceled()) {
				if (EyeOfNebula.dodgeProbability.getValue().roll() && player.hurtResistantTime <= 10 && event.getSource().getTrueSource() instanceof LivingEntity) {

					for (int counter = 0; counter <= 32; counter++) {
						if (SuperpositionHandler.validTeleportRandomly(player, player.world, (int) EyeOfNebula.dodgeRange.getValue())) {
							break;
						}
					}

					player.hurtResistantTime = 20;
					event.setCanceled(true);
				}
			}

			if (!player.abilities.disableDamage && player.getActivePotionEffect(Effects.FIRE_RESISTANCE) == null)
				if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart) && !event.isCanceled() && event.getSource().damageType.equals(DamageSource.LAVA.damageType)) {
					TransientPlayerData data = TransientPlayerData.get(player);

					if (data.getFireImmunityTimer() < data.getFireImmunityTimerCap()) {
						if (data.getFireImmunityTimer() < (data.getFireImmunityTimerCap() - (data.getFireDiff()))) {
							event.setCanceled(true);
						}

						if (data.getFireImmunityTimer() == 0 && !player.world.isRemote) {
							player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, 0.5F + (theySeeMeRollin.nextFloat() * 0.5F));
							EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 32, player.world.getDimensionKey())), new PacketWitherParticles(player.getPosX(), player.getPosYHeight(0.25D), player.getPosZ(), 8, false, 1));
						}

						data.setFireImmunityTimer(data.getFireImmunityTimer()+200);
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
					if (ItemNBTHelper.getString(player.getHeldItemMainhand(), "BoundDimension", "minecraft:overworld").equals(event.getEntityLiving().world.getDimensionKey().getLocation().toString())) {
						event.setCanceled(true);
						ItemStack stack = player.getHeldItemMainhand();

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), 128, event.getEntityLiving().world.getDimensionKey())), new PacketPortalParticles(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + (event.getEntityLiving().getHeight() / 2), event.getEntityLiving().getPosZ(), 96, 1.5D, false));

						event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
						event.getEntityLiving().setPositionAndUpdate(ItemNBTHelper.getDouble(stack, "BoundX", 0D), ItemNBTHelper.getDouble(stack, "BoundY", 0D), ItemNBTHelper.getDouble(stack, "BoundZ", 0D));
						event.getEntityLiving().world.playSound(null, event.getEntityLiving().getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

						EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), 128, event.getEntityLiving().world.getDimensionKey())), new PacketRecallParticles(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + (event.getEntityLiving().getHeight() / 2), event.getEntityLiving().getPosZ(), 48, false));

						if (!player.abilities.isCreativeMode) {
							stack.shrink(1);
						}
					}

			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == EnigmaticLegacy.theTwist && SuperpositionHandler.isTheCursedOne(player)) {
				float knockbackPower = TheTwist.knockbackBonus.getValue().asModifier(true);
				knockbackThatBastard.put(event.getEntityLiving(), (event.getEntityLiving() instanceof PhantomEntity ? (knockbackPower*1.5F) : knockbackPower));
			}
		}

		if (event.getEntityLiving() instanceof AnimalEntity) {
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();

				if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.animalGuide)) {
					event.setCanceled(true);
				}

			}
		}

	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onEntityDamaged(LivingDamageEvent event) {
		if (event.getSource().getImmediateSource() instanceof PlayerEntity && !event.getSource().getImmediateSource().world.isRemote) {
			PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();

			if (EnigmaticLegacy.enigmaticAmulet.ifHasColor(player, AmuletColor.BLACK)) {
				player.heal(event.getAmount() * 0.1F);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityAttacked(AttackEntityEvent event) {
		if (!event.getPlayer().world.isRemote) {
			RegisteredMeleeAttack.registerAttack(event.getPlayer());
		}
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {

		// TODO The priorities are messed up as fuck. We gotta do something about it.

		/*
		 * Ideally fixed numerical increases should come first, then percentage alterations,
		 * and the last in order - handlers that use the event for notification purpose, doing
		 * something other with it than altering damage dealt.
		 */

		if (event.getSource().getTrueSource() instanceof LivingEntity) {
			LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
			Entity immediateSource = event.getSource().getImmediateSource();

			if (immediateSource instanceof TridentEntity || immediateSource instanceof LivingEntity) {
				ItemStack mainhandStack = ItemStack.EMPTY;
				if (immediateSource instanceof TridentEntity) {
					if (((TridentEntity)immediateSource).thrownStack != null) {
						mainhandStack = ((TridentEntity)immediateSource).thrownStack;
					}
				} else {
					if (((LivingEntity)immediateSource).getHeldItemMainhand() != null) {
						mainhandStack = ((LivingEntity)immediateSource).getHeldItemMainhand();
					}
				}

				int torrentLevel = 0;
				int wrathLevel = 0;

				if ((torrentLevel = EnchantmentHelper.getEnchantmentLevel(torrentEnchantment, mainhandStack)) > 0) {
					event.setAmount(event.getAmount() + torrentEnchantment.bonusDamageByCreature(attacker, event.getEntityLiving(), torrentLevel));
				}

				if ((wrathLevel = EnchantmentHelper.getEnchantmentLevel(wrathEnchantment, mainhandStack)) > 0) {
					event.setAmount(event.getAmount() + wrathEnchantment.bonusDamageByCreature(attacker, event.getEntityLiving(), wrathLevel));
				}
			}
		}

		if (event.getSource().getImmediateSource() instanceof AbstractArrowEntity) {
			AbstractArrowEntity arrow = (AbstractArrowEntity) event.getSource().getImmediateSource();

			for (String tag : arrow.getTags()) {

				if (tag.startsWith(CrossbowHelper.sharpshooterTagPrefix)) {

					/*
					 * Since our custom tag is just a prefix + enchantment level, we can remove
					 * that prefix from received String and freely parse remaining Integer.
					 */

					event.setAmount(6 + (3 * Integer.parseInt(tag.replace(CrossbowHelper.sharpshooterTagPrefix, ""))));
					break;
				}
			}
		} else if (event.getSource().getImmediateSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getImmediateSource();

			/*
			 * Handler for applying Withering to victims of bearer of the Void Pearl.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.voidPearl)) {
				event.getEntityLiving().addPotionEffect(new EffectInstance(Effects.WITHER, VoidPearl.witheringTime.getValue(), VoidPearl.witheringLevel.getValue(), false, true));
			}

			if (player.getHeldItemMainhand() != null) {
				ItemStack mainhandStack = player.getHeldItemMainhand();

				if (mainhandStack.getItem() == EnigmaticLegacy.theTwist) {
					if (SuperpositionHandler.isTheCursedOne(player)) {
						if (!event.getEntityLiving().isNonBoss() || event.getEntityLiving() instanceof PlayerEntity) {
							event.setAmount(event.getAmount()*TheTwist.bossDamageBonus.getValue().asModifier(true));
						}
					} else {
						event.setCanceled(true);
					}
				}

				int slayerLevel = 0;
				if ((slayerLevel = EnchantmentHelper.getEnchantmentLevel(slayerEnchantment, mainhandStack)) > 0) {
					event.setAmount(event.getAmount() + slayerEnchantment.bonusDamageByCreature(player, event.getEntityLiving(), slayerLevel));
				}

				if (EnigmaticEnchantmentHelper.hasNemesisCurseEnchantment(mainhandStack)) {
					float supposedDamage = event.getAmount()*0.35F;

					player.attackEntityFrom(new DamageSourceNemesisCurse(event.getEntityLiving()), supposedDamage);
				}
			}
		}


		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Handler for resistance lists.
			 */

			List<ItemStack> advancedCurios = SuperpositionHandler.getAdvancedCurios(player);
			if (advancedCurios.size() > 0) {
				for (ItemStack advancedCurioStack : advancedCurios) {
					ItemSpellstoneCurio advancedCurio = (ItemSpellstoneCurio) advancedCurioStack.getItem();
					CreatureEntity trueSource = event.getSource().getTrueSource() instanceof CreatureEntity ? (CreatureEntity)event.getSource().getTrueSource() : null;

					if (event.getSource().damageType.startsWith("explosion") && advancedCurio == EnigmaticLegacy.golemHeart && SuperpositionHandler.hasAnyArmor(player)) {
						continue;
					} else if (advancedCurio == magmaHeart && trueSource != null && (trueSource.getCreatureAttribute() == CreatureAttribute.WATER || trueSource instanceof DrownedEntity)) {
						event.setAmount(event.getAmount() * 2F);
					} else if (advancedCurio == eyeOfNebula && player.isInWater()) {
						event.setAmount(event.getAmount() * 2F);
					} else if (advancedCurio == oceanStone && trueSource != null && (trueSource.getCreatureAttribute() == CreatureAttribute.WATER || trueSource instanceof DrownedEntity)) {
						event.setAmount(event.getAmount() * OceanStone.underwaterCreaturesResistance.getValue().asModifierInverted());
					}

					if (advancedCurio.resistanceList.containsKey(event.getSource().damageType)) {
						event.setAmount(event.getAmount() * advancedCurio.resistanceList.get(event.getSource().damageType).get());
					}
				}
			}

			if (event.getSource().damageType == DamageSource.FALL.damageType) {
				if (EnigmaticLegacy.enigmaticAmulet.ifHasColor(player, AmuletColor.MAGENTA)) {
					event.setAmount(event.getAmount() - 2.0f);
				}
			}

			/*
			 * Handler for damaging feedback of Blazing Core.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.magmaHeart)) {
				if (event.getSource().getTrueSource() instanceof LivingEntity && EnigmaticLegacy.magmaHeart.nemesisList.contains(event.getSource().damageType)) {
					LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
					if (!attacker.isImmuneToFire()) {
						attacker.attackEntityFrom(new EntityDamageSource(DamageSource.ON_FIRE.damageType, player), (float) MagmaHeart.damageFeedback.getValue());
						attacker.setFire(MagmaHeart.ignitionFeedback.getValue());
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
					attacker.applyKnockback(0.75F, vec.x, vec.z);
					player.world.playSound(null, player.getPosition(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
					player.world.playSound(null, player.getPosition(), EnigmaticLegacy.SHIELD_TRIGGER, SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}

				event.setAmount(event.getAmount() * EtheriumArmor.shieldReduction.getValue().asModifierInverted());
			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.berserkEmblem)) {
				event.setAmount(event.getAmount() * (1.0F - (SuperpositionHandler.getMissingHealthPool(player)*(float)BerserkEmblem.damageResistance.getValue())));
			}

			/*
			 * Handler for doubling damage on bearers of Ring of the Seven Curses.
			 */

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing)) {
				event.setAmount(event.getAmount()*CursedRing.painMultiplier.getValue().asModifier());
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
						event.setAmount(event.getAmount() * MonsterCharm.undeadDamageBonus.getValue().asModifier(true));
					} else if (monster.isAggressive() || monster instanceof CreeperEntity) {

						if (monster instanceof EndermanEntity || monster instanceof ZombifiedPiglinEntity || monster instanceof BlazeEntity || monster instanceof GuardianEntity || monster instanceof ElderGuardianEntity || !monster.isNonBoss()) {
							// NO-OP
						} else {
							event.setAmount(event.getAmount() * MonsterCharm.hostileDamageBonus.getValue().asModifier(true));
						}

					}
				}

				/*
				 * Handler for damage debuff of Ring of the Seven Curses.
				 */

				if (SuperpositionHandler.isTheCursedOne(player)) {
					if (event.getSource().getImmediateSource() != player || player.getHeldItemMainhand().getItem() != EnigmaticLegacy.theTwist) {
						event.setAmount(event.getAmount()*CursedRing.monsterDamageDebuff.getValue().asModifierInverted());
					}
				}

			}
		}

		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			Entity immediateSource = event.getSource().getImmediateSource();

			float damageBoost = 0F;

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.berserkEmblem)) {
				damageBoost += event.getAmount()*(SuperpositionHandler.getMissingHealthPool(player)*(float)BerserkEmblem.attackDamage.getValue());
			}

			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedScroll)) {
				damageBoost += event.getAmount()*(CursedScroll.damageBoost.getValue().asModifier()*SuperpositionHandler.getCurseAmount(player));
			}

			event.setAmount(event.getAmount()+damageBoost);
		}

		if (event.getEntityLiving() instanceof TameableEntity) {
			TameableEntity pet = (TameableEntity) event.getEntityLiving();

			if (pet.isTamed()) {
				LivingEntity owner = pet.getOwner();

				if (owner instanceof PlayerEntity && SuperpositionHandler.hasItem((PlayerEntity)owner, EnigmaticLegacy.hunterGuide)) {
					if (owner.world == pet.world && owner.getDistance(pet) <= HunterGuide.effectiveDistance.getValue()) {
						event.setCanceled(true);
						owner.attackEntityFrom(event.getSource(), SuperpositionHandler.hasItem((PlayerEntity)owner, EnigmaticLegacy.animalGuide) ? (event.getAmount()*HunterGuide.synergyDamageReduction.getValue().asModifierInverted()) : event.getAmount());
					}
				}
			}
		}

	}

	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone evt) {
		PlayerEntity newPlayer = evt.getPlayer();
		PlayerEntity player = evt.getOriginal();

		/*
		 * Handler for destroying three random items in player's inventory on death,
		 * granted they bear Ring of the Seven Curses.
		 */

		/*
		if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing) & !player.world.isRemote) {
			List<ItemStack> bigInventoryList = new ArrayList<>();

			for (int i = 0; i < 3; i++) {
				NonNullList<ItemStack> inventoryList = null;

				if (i == 0) {
					inventoryList = player.inventory.mainInventory;
				}
				if (i == 1) {
					//inventoryList = player.inventory.armorInventory;
				}
				if (i == 2) {
					//inventoryList = player.inventory.offHandInventory;
				}

				if (inventoryList != null) {
					inventoryList.forEach(stack -> {
						if (!stack.isEmpty() && !EnigmaticLegacy.cursedRing.isItemDeathPersistent(stack) && stack != player.getHeldItemMainhand()) {
							bigInventoryList.add(stack);
						}
					});
				}
			}

		 *
			CuriosApi.getCuriosHelper().getEquippedCurios(player).ifPresent(handler -> {
				for (int index = 0; index < handler.getSlots(); index++) {
					if (handler.getStackInSlot(index) != null && !handler.getStackInSlot(index).isEmpty() && !EnigmaticLegacy.cursedRing.isItemDeathPersistent(handler.getStackInSlot(index))) {
						bigInventoryList.add(handler.getStackInSlot(index));
					}
				}
			});
		 *

			bigInventoryList.forEach(stack -> {
				System.out.println(stack);
			});

			final int itemsToDestroy = bigInventoryList.size() >= 3 ? 3 : bigInventoryList.size();
			List<Integer> rolledIndexes = new ArrayList<>();

			for (int i = 0; i < itemsToDestroy; i++) {
				int randomIndex = EnigmaticEventHandler.theySeeMeRollin.nextInt(itemsToDestroy);

				while (rolledIndexes.contains(randomIndex)) {
					EnigmaticEventHandler.theySeeMeRollin.nextInt(itemsToDestroy);
				}

				rolledIndexes.add(randomIndex);
				bigInventoryList.get(randomIndex).setCount(0);
			}
		}

		 */

		EnigmaticLegacy.soulCrystal.updatePlayerSoulMap(newPlayer);
	}
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) entity;
			EnigmaticLegacy.soulCrystal.updatePlayerSoulMap(player);
			TransientPlayerData.get(player).syncToPlayer();
		}

		if (entity instanceof CreatureEntity && ((CreatureEntity)entity).getCreatureAttribute() == CreatureAttribute.ARTHROPOD) {
			((CreatureEntity)entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((CreatureEntity)entity, PlayerEntity.class, (targetEntity) -> targetEntity instanceof PlayerEntity && SuperpositionHandler.hasAntiInsectAcknowledgement((PlayerEntity)targetEntity), 6, 1, 1.3, EntityPredicates.CAN_AI_TARGET::test));
		}

		if (entity instanceof PiglinEntity) {
			// NO-OP
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onExperienceDrop(LivingExperienceDropEvent event) {
		PlayerEntity player = event.getAttackingPlayer();

		int bonusExp = 0;

		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			if (this.hadEnigmaticAmulet((PlayerEntity) event.getEntityLiving()) && !event.getEntityLiving().world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
				event.setCanceled(true);
			}
		} else if (event.getEntityLiving() instanceof MonsterEntity) {
			if (player != null && SuperpositionHandler.hasCurio(player, EnigmaticLegacy.monsterCharm)) {
				bonusExp += event.getOriginalExperience() * (EnigmaticLegacy.monsterCharm.bonusXPModifier-1.0F);
			}
		}

		if (player != null && SuperpositionHandler.hasCurio(player, EnigmaticLegacy.cursedRing)) {
			bonusExp += event.getOriginalExperience() * CursedRing.experienceBonus.getValue().asMultiplier();
		}

		event.setDroppedExperience(event.getDroppedExperience() + bonusExp);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLivingDrops(LivingDropsEvent event) {

		if (event.getEntityLiving() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
			boolean droppedCrystal = false;
			boolean hadEscapeScroll = this.hadEscapeScroll(player);

			DimensionalPosition dimPoint = hadEscapeScroll ? SuperpositionHandler.getRespawnPoint(player) : new DimensionalPosition(player.getPosX(), player.getPosY(), player.getPosZ(), player.world);

			if (hadEscapeScroll) {
				player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 128, player.world.getDimensionKey())), new PacketPortalParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 100, 1.25F, false));



				for (ItemEntity dropIt : event.getDrops()) {
					ItemEntity alternativeDrop = new ItemEntity(dimPoint.world, dimPoint.posX, dimPoint.posY, dimPoint.posZ, dropIt.getItem());
					alternativeDrop.setPositionAndUpdate(dimPoint.posX, dimPoint.posY, dimPoint.posZ);
					alternativeDrop.setMotion(theySeeMeRollin.nextDouble()-0.5, theySeeMeRollin.nextDouble()-0.5, theySeeMeRollin.nextDouble()-0.5);
					dimPoint.world.addEntity(alternativeDrop);
					dropIt.setItem(ItemStack.EMPTY);
				}

				event.getDrops().clear();

				dimPoint.world.playSound(null, new BlockPos(dimPoint.posX, dimPoint.posY, dimPoint.posZ), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(dimPoint.posX, dimPoint.posY, dimPoint.posZ, 128, dimPoint.world.getDimensionKey())), new PacketRecallParticles(dimPoint.posX, dimPoint.posY, dimPoint.posZ, 48, false));
			}

			if (this.hadEnigmaticAmulet(player) && !event.getDrops().isEmpty() && EnigmaticLegacy.enigmaticAmulet.isVesselEnabled()) {
				ItemStack soulCrystal = SuperpositionHandler.shouldPlayerDropSoulCrystal(player) ? EnigmaticLegacy.soulCrystal.createCrystalFrom(player) : null;
				ItemStack storageCrystal = EnigmaticLegacy.storageCrystal.storeDropsOnCrystal(event.getDrops(), player, soulCrystal);
				PermanentItemEntity droppedStorageCrystal = new PermanentItemEntity(dimPoint.world, dimPoint.getPosX(), dimPoint.getPosY() + 1.5, dimPoint.getPosZ(), storageCrystal);
				droppedStorageCrystal.setOwnerId(player.getUniqueID());
				dimPoint.world.addEntity(droppedStorageCrystal);
				EnigmaticLegacy.logger.info("Summoned Extradimensional Storage Crystal for " + player.getGameProfile().getName() + " at X: " + dimPoint.getPosX() + ", Y: " + dimPoint.getPosY() + ", Z: " + dimPoint.getPosZ());
				event.getDrops().clear();

				if (soulCrystal != null) {
					droppedCrystal = true;
				}

			} else if (SuperpositionHandler.shouldPlayerDropSoulCrystal(player)) {
				ItemStack soulCrystal = EnigmaticLegacy.soulCrystal.createCrystalFrom(player);
				PermanentItemEntity droppedSoulCrystal = new PermanentItemEntity(dimPoint.world, dimPoint.getPosX(), dimPoint.getPosY() + 1.5, dimPoint.getPosZ(), soulCrystal);
				droppedSoulCrystal.setOwnerId(player.getUniqueID());
				dimPoint.world.addEntity(droppedSoulCrystal);
				EnigmaticLegacy.logger.info("Teared Soul Crystal from " + player.getGameProfile().getName() + " at X: " + dimPoint.getPosX() + ", Y: " + dimPoint.getPosY() + ", Z: " + dimPoint.getPosZ());

				droppedCrystal = true;
			}

			ResourceLocation soulLossAdvancement = new ResourceLocation(EnigmaticLegacy.MODID, "book/soul_loss");

			if (droppedCrystal) {
				SuperpositionHandler.grantAdvancement(player, soulLossAdvancement);
			} else if (!droppedCrystal && SuperpositionHandler.hasAdvancement(player, soulLossAdvancement)) {
				SuperpositionHandler.revokeAdvancement(player, soulLossAdvancement);
			}

			postmortalPossession.removeAll(player);

			return;
		}


		/*
		 * Beheading handler for Axe of Executioner.
		 */

		if (event.getEntityLiving().getClass() == SkeletonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.SKELETON_SKULL, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
				}
			}
		} else if (event.getEntityLiving().getClass() == WitherSkeletonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe) {

				if (!SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.WITHER_SKELETON_SKULL) && this.theySeeMeRollin(event.getLootingLevel())) {
					this.addDrop(event, new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
				}

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity && SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.WITHER_SKELETON_SKULL)) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
				}
			}
		} else if (event.getEntityLiving().getClass() == ZombieEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.ZOMBIE_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.ZOMBIE_HEAD, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
				}
			}
		} else if (event.getEntityLiving().getClass() == CreeperEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.CREEPER_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.CREEPER_HEAD, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
				}
			}
		} else if (event.getEntityLiving().getClass() == EnderDragonEntity.class && event.isRecentlyHit() && event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity) {
			ItemStack weap = ((PlayerEntity) event.getSource().getTrueSource()).getHeldItemMainhand();
			if (weap != null && weap.getItem() == EnigmaticLegacy.forbiddenAxe && !SuperpositionHandler.ifDroplistContainsItem(event.getDrops(), Items.DRAGON_HEAD) && this.theySeeMeRollin(event.getLootingLevel())) {
				this.addDrop(event, new ItemStack(Items.DRAGON_HEAD, 1));

				if (event.getSource().getTrueSource() instanceof ServerPlayerEntity) {
					BeheadingTrigger.INSTANCE.trigger((ServerPlayerEntity) event.getSource().getTrueSource());
				}
			}
		}

		/*
		 * Unique drops for Ring of the Seven Curses.
		 */

		if (event.isRecentlyHit() && event.getSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity && SuperpositionHandler.isTheCursedOne((PlayerEntity) event.getSource().getTrueSource())) {
			PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
			LivingEntity killed = event.getEntityLiving();

			if (SuperpositionHandler.hasCurio(player, avariceScroll)) {
				this.addDropWithChance(event, new ItemStack(Items.EMERALD, 1), 15);
			}

			if (killed.getClass() == ShulkerEntity.class) {
				this.addDropWithChance(event, new ItemStack(EnigmaticLegacy.astralDust, 1), 20);
			} else if (killed.getClass() == SkeletonEntity.class || killed.getClass() == StrayEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.ARROW, 3, 15));
			} else if (killed.getClass() == ZombieEntity.class || killed.getClass() == HuskEntity.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.SLIME_BALL, 1, 3), 25);
			} else if (killed.getClass() == SpiderEntity.class || killed.getClass() == CaveSpiderEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.STRING, 2, 12));
			} else if (killed.getClass() == GuardianEntity.class) {
				this.addDropWithChance(event, new ItemStack(Items.NAUTILUS_SHELL, 1), 15);
				this.addDrop(event, this.getRandomSizeStack(Items.PRISMARINE_CRYSTALS, 2, 5));
			} else if (killed.getClass() == ElderGuardianEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.PRISMARINE_CRYSTALS, 4, 16));
				this.addDrop(event, this.getRandomSizeStack(Items.PRISMARINE_SHARD, 7, 28));
				this.addOneOf(event,
						new ItemStack(guardianHeart, 1),
						new ItemStack(Items.HEART_OF_THE_SEA, 1),
						new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1),
						new ItemStack(Items.ENDER_EYE, 1),
						EnchantmentHelper.addRandomEnchantment(theySeeMeRollin, new ItemStack(Items.TRIDENT, 1), 25+theySeeMeRollin.nextInt(15), true));
			} else if (killed.getClass() == EndermanEntity.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.ENDER_EYE, 1, 2), 40);
			} else if (killed.getClass() == BlazeEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.BLAZE_POWDER, 0, 5));
				//this.addDropWithChance(event, new ItemStack(EnigmaticLegacy.livingFlame, 1), 15);
			} else if (killed.getClass() == ZombifiedPiglinEntity.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.GOLD_INGOT, 1, 3), 40);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.GLOWSTONE_DUST, 1, 7), 30);
			} else if (killed.getClass() == WitchEntity.class) {
				this.addDropWithChance(event, new ItemStack(Items.GHAST_TEAR, 1), 30);
				this.addDrop(event, this.getRandomSizeStack(Items.PHANTOM_MEMBRANE, 1, 3));
			} else if (killed.getClass() == WitchEntity.class) {
				this.addDropWithChance(event, new ItemStack(Items.GHAST_TEAR, 1), 30);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.PHANTOM_MEMBRANE, 1, 3), 50);
			} else if (killed.getClass() == PillagerEntity.class || killed.getClass() == VindicatorEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 0, 4));
			} else if (killed.getClass() == VillagerEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 2, 6));
			} else if (killed.getClass() == CreeperEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.GUNPOWDER, 4, 12));
			} else if (killed.getClass() == PiglinBruteEntity.class) {
				this.addDropWithChance(event, new ItemStack(Items.NETHERITE_SCRAP, 1), 20);
			} else if (killed.getClass() == EvokerEntity.class) {
				this.addDrop(event, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 5, 20));
				this.addDropWithChance(event, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1), 10);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.ENDER_PEARL, 1, 3), 30);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.BLAZE_ROD, 2, 4), 30);
				this.addDropWithChance(event, this.getRandomSizeStack(Items.EXPERIENCE_BOTTLE, 4, 10), 50);
			} else if (killed.getClass() == WitherSkeletonEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.BLAZE_POWDER, 0, 3));
				this.addDropWithChance(event, new ItemStack(Items.GHAST_TEAR, 1), 20);
				this.addDropWithChance(event, new ItemStack(Items.NETHERITE_SCRAP, 1), 7);
			} else if (killed.getClass() == GhastEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.PHANTOM_MEMBRANE, 1, 4));
			} else if (killed.getClass() == DrownedEntity.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.LAPIS_LAZULI, 1, 3), 30);
			} else if (killed.getClass() == VexEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.GLOWSTONE_DUST, 0, 2));
				this.addDropWithChance(event, new ItemStack(Items.PHANTOM_MEMBRANE, 1), 30);
			} else if (killed.getClass() == PhantomEntity.class) {
				// NO-OP
			} else if (killed.getClass() == PiglinEntity.class) {
				this.addDropWithChance(event, this.getRandomSizeStack(Items.GOLD_INGOT, 2, 4), 50);
			} else if (killed.getClass() == RavagerEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.EMERALD, 3, 10));
				this.addDrop(event, this.getRandomSizeStack(Items.LEATHER, 2, 7));
				this.addDropWithChance(event, this.getRandomSizeStack(Items.DIAMOND, 0, 4), 50);
			} else if (killed.getClass() == SilverfishEntity.class) {
				// NO-OP
			} else if (killed.getClass() == MagmaCubeEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(Items.BLAZE_POWDER, 0, 1));
			} else if (killed.getClass() == ChickenEntity.class) {
				this.addDropWithChance(event, new ItemStack(Items.EGG, 1), 50);
			} else if (killed.getClass() == WitherEntity.class) {
				this.addDrop(event, this.getRandomSizeStack(evilEssence, 1, 4));
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
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -12F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 35), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.angelBlessing, 65));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);
		} else if (SuperpositionHandler.getMergedEnder$EarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -10F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.eyeOfNebula, 35), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 65));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getAirDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -10F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.angelBlessing, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getEarthenDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -20F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.golemHeart, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getNetherDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -24F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.magmaHeart, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getWaterDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -20F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.oceanStone, 100));

			LootTable modified = event.getTable();
			modified.addPool(poolSpellstones);
			event.setTable(modified);

		} else if (SuperpositionHandler.getEnderDungeons().contains(event.getName())) {
			LootPool poolSpellstones = SuperpositionHandler.constructLootPool("spellstones", -12F, 1F, SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.eyeOfNebula, 90), SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.voidPearl, 10));

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
					ItemLootEntry.builder(Items.PUMPKIN_PIE).weight(25).acceptFunction(SetCount.builder(RandomValueRange.of(4.0F, 16F))),
					SuperpositionHandler.getWaterDungeons().contains(event.getName()) || event.getName().equals(LootTables.CHESTS_PILLAGER_OUTPOST) ? null : SuperpositionHandler.createOptionalLootEntry(EnigmaticLegacy.earthHeart, 7)
					);

			LootTable modified = event.getTable();
			modified.addPool(epic);

			if (event.getName() != LootTables.CHESTS_SHIPWRECK_SUPPLY) {
				modified.addPool(overworldLiterature);
			}

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
					ItemLootEntry.builder(Items.POTION).weight(15).acceptFunction(SetNBT.builder(fireResistancePotion.getTag())),
					SuperpositionHandler.getBastionChests().contains(event.getName()) ? ItemLootEntry.builder(forbiddenFruit).weight(4) : null
					);

			LootTable modified = event.getTable();

			if (!event.getName().equals(LootTables.BASTION_TREASURE)) {
				modified.addPool(epic);
			} else {
				LootPool scroll = SuperpositionHandler.constructLootPool("darkest_scroll", 0F, 1F,
						ItemLootEntry.builder(EnigmaticLegacy.darkestScroll).weight(100).acceptFunction(SetCount.builder(RandomValueRange.of(1F, 1F)))
						);

				modified.addPool(scroll);
			}

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

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayerEntity))
			return;

		if (!OmniconfigWrapper.syncAllToPlayer((ServerPlayerEntity) event.getPlayer())) {
			OmniconfigWrapper.onRemoteServer = false;
			EnigmaticLegacy.logger.info("Logging in to local integrated server; no synchronization is required.");
		}

		try {

			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

			/*
			 * Handler for bestowing Enigmatic Amulet to the player, when they first join
			 * the world.
			 */

			if (OmniconfigHandler.isItemEnabled(EnigmaticLegacy.enigmaticAmulet))
				if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_FIRSTJOIN)) {

					ItemStack enigmaticAmulet = new ItemStack(EnigmaticLegacy.enigmaticAmulet);
					EnigmaticLegacy.enigmaticAmulet.setInscription(enigmaticAmulet, player.getGameProfile().getName());

					if (!EnigmaticAmulet.seededColorGen.getValue()) {
						EnigmaticLegacy.enigmaticAmulet.setRandomColor(enigmaticAmulet);
					} else {
						EnigmaticLegacy.enigmaticAmulet.setSeededColor(enigmaticAmulet);
					}

					if (player.inventory.getStackInSlot(8).isEmpty()) {
						player.inventory.setInventorySlotContents(8, enigmaticAmulet);
					} else {
						if (!player.inventory.addItemStackToInventory(enigmaticAmulet)) {
							ItemEntity dropAmulet = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), enigmaticAmulet);
							player.world.addEntity(dropAmulet);
						}
					}

					SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_FIRSTJOIN, true);
				}

			/*
			 * Another one for Ring of the Seven Curses.
			 */

			if (OmniconfigHandler.isItemEnabled(EnigmaticLegacy.cursedRing))
				if (!SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_CURSEDGIFT)) {
					ItemStack cursedRingStack = new ItemStack(EnigmaticLegacy.cursedRing);

					if (!CursedRing.ultraHardcore.getValue()) {
						if (player.inventory.getStackInSlot(7).isEmpty()) {
							player.inventory.setInventorySlotContents(7, cursedRingStack);
						} else {
							if (!player.inventory.addItemStackToInventory(cursedRingStack)) {
								ItemEntity dropRing = new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), cursedRingStack);
								player.world.addEntity(dropRing);
							}
						}
					} else {
						CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
							if (!player.world.isRemote) {
								Map<String, ICurioStacksHandler> curios = handler.getCurios();

								for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
									IDynamicStackHandler stackHandler = entry.getValue().getStacks();

									for (int i = 0; i < stackHandler.getSlots(); i++) {
										ItemStack present = stackHandler.getStackInSlot(i);
										Set<String> tags = CuriosApi.getCuriosHelper().getCurioTags(cursedRing);
										String id = entry.getKey();

										if (present.isEmpty() && (tags.contains(id) || tags.contains("curio")) && cursedRing
												.canEquip(id, player, cursedRingStack)) {
											stackHandler.setStackInSlot(i, cursedRingStack);
											//cursedRing.onEquip(id, i, player);
											cursedRing.playRightClickEquipSound(player, cursedRingStack);
										}
									}
								}

							}
						});
					}

					SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_CURSEDGIFT, true);
				}

			/*
			 * Handlers for fixing missing Curios slots upong joining the world.
			 */

			if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_spellstone"))) {
				CuriosApi.getSlotHelper().unlockSlotType("spellstone", event.getPlayer());
				SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE, true);
			}

			if (SuperpositionHandler.hasAdvancement(player, new ResourceLocation(EnigmaticLegacy.MODID, "main/discover_scroll"))) {
				CuriosApi.getSlotHelper().unlockSlotType("scroll", event.getPlayer());
				SuperpositionHandler.setPersistentBoolean(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL, true);
			}

			ServerRecipeBook book = player.getRecipeBook();
			if (OmniconfigHandler.retriggerRecipeUnlocks.getValue()) {
				for (IRecipe<?> theRecipe : player.world.getRecipeManager().getRecipes()) {
					if (book.isUnlocked(theRecipe)) {
						CriteriaTriggers.RECIPE_UNLOCKED.trigger(player, theRecipe);
					}
				}
			}

		} catch (Exception ex) {
			EnigmaticLegacy.logger.error("Failed to check player's advancements upon joining the world!");
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

					if (SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESCROLL)) {
						CuriosApi.getSlotHelper().unlockSlotType("scroll", event.getPlayer());
					}
					if (SuperpositionHandler.hasPersistentTag(player, EnigmaticEventHandler.NBT_KEY_ENABLESPELLSTONE)) {
						CuriosApi.getSlotHelper().unlockSlotType("spellstone", event.getPlayer());
					}

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
	public void onAnvilUpdate(AnvilUpdateEvent event) {

		if (event.getLeft().getCount() == 1)
			if (event.getRight().getItem().equals(EnigmaticLegacy.loreFragment) && event.getRight().getChildTag("display") != null) {
				event.setCost(4);
				event.setMaterialCost(1);
				event.setOutput(ItemLoreHelper.mergeDisplayData(event.getRight(), event.getLeft().copy()));
			}
	}

	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {

		if (event.getPlayer() != null && !event.getPlayer().world.isRemote) {
			if (event.getInventory().count(EnigmaticLegacy.enchantmentTransposer) == 1 && event.getCrafting().getItem() == Items.ENCHANTED_BOOK) {
				event.getPlayer().world.playSound(null, event.getPlayer().getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));
			} else if (event.getCrafting().getItem() == cursedStone) {
				event.getPlayer().world.playSound(null, event.getPlayer().getPosition(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1.0F, (float) (0.9F + (Math.random() * 0.1F)));
			}
		}

	}


	@SubscribeEvent
	public void onAttackTargetSet(LivingSetAttackTargetEvent event) {
		if (event.getTarget() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getTarget();

			if (event.getEntityLiving() instanceof MobEntity) {
				MobEntity insect = (MobEntity) event.getEntityLiving();

				if (insect.getCreatureAttribute() == CreatureAttribute.ARTHROPOD)
					if (SuperpositionHandler.hasAntiInsectAcknowledgement(player)) {
						insect.setAttackTarget(null);
					}

				if (insect instanceof GuardianEntity && insect.getClass() != ElderGuardianEntity.class) {
					if (SuperpositionHandler.hasItem(player, EnigmaticLegacy.guardianHeart) && SuperpositionHandler.isTheCursedOne(player)) {
						boolean isBlacklisted = angeredGuardians.containsEntry(player, insect);

						if (insect.getRevengeTarget() != player && !isBlacklisted) {
							insect.setAttackTarget(null);
						} else {
							if (!isBlacklisted) {
								angeredGuardians.put(player, (GuardianEntity)insect);
							}
						}
					}
				}

			}

		}
	}


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

	public void addDropWithChance(LivingDropsEvent event, ItemStack drop, int chance) {
		if (theySeeMeRollin.nextInt(100) < chance) {
			this.addDrop(event, drop);
		}
	}

	public ItemStack getRandomSizeStack(Item item, int minAmount, int maxAmount) {
		return new ItemStack(item, minAmount + theySeeMeRollin.nextInt(maxAmount-minAmount+1));
	}

	public void addOneOf(LivingDropsEvent event, ItemStack... itemStacks) {
		int chosenStack = theySeeMeRollin.nextInt(itemStacks.length);
		this.addDrop(event, itemStacks[chosenStack]);
	}

	/**
	 * Calculates the chance for Axe of Executioner to behead an enemy.
	 *
	 * @param lootingLevel Amount of looting levels applied to axe or effective otherwise.
	 * @return True if chance works and head should drop, false otherwise.
	 * @author Integral
	 */

	private boolean theySeeMeRollin(int lootingLevel) {
		int chance = Math.min(ForbiddenAxe.beheadingBase.getValue().asPercentage() + (ForbiddenAxe.beheadingBonus.getValue().asPercentage() * lootingLevel), 100);
		return new Perhaps(chance).roll();
	}

	private boolean hadEnigmaticAmulet(PlayerEntity player) {
		return EnigmaticEventHandler.postmortalPossession.containsKey(player) ? EnigmaticEventHandler.postmortalPossession.containsEntry(player, EnigmaticLegacy.enigmaticAmulet) : false;
	}

	private boolean hadEscapeScroll(PlayerEntity player) {
		return EnigmaticEventHandler.postmortalPossession.containsKey(player) ? EnigmaticEventHandler.postmortalPossession.containsEntry(player, EnigmaticLegacy.escapeScroll) : false;
	}


}
