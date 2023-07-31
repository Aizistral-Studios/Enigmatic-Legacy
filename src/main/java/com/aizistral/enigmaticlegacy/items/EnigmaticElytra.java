package com.aizistral.enigmaticlegacy.items;

import java.util.List;
import java.util.UUID;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.IBindable;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.packets.server.PacketUpdateElytraBoosting;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio.SoundInfo;

public class EnigmaticElytra extends ItemBaseCurio implements IBindable {
	private static final AttributeModifier ELYTRA_MODIFIER = new AttributeModifier(UUID.fromString("44dfce5a-2f09-4f19-bc29-9b0324cd2a40"), EnigmaticLegacy.MODID+":elytra_modifier", 1.0, AttributeModifier.Operation.ADDITION);
	@OnlyIn(Dist.CLIENT)
	private static boolean isBoosting;

	public EnigmaticElytra() {
		super(ItemBaseCurio.getDefaultProperties().durability(5000).fireResistant().rarity(Rarity.EPIC));
		DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.CHEST;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticElytra1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticElytra2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticElytra3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity() instanceof Player player && player.level().isClientSide) {
			this.handleBoosting(player);
		}

		LivingEntity livingEntity = context.entity();
		int ticks = livingEntity.getFallFlyingTicks();

		if (ticks > 0 && livingEntity.isFallFlying()) {
			stack.elytraFlightTick(livingEntity, ticks);
		}
	}


	@OnlyIn(Dist.CLIENT)
	private void handleBoosting(Player player) {
		if (Minecraft.getInstance().player != player)
			return;

		if (Minecraft.getInstance().options.keyJump.isDown() && this.boostPlayer(player)) {
			if (!isBoosting) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(),
						new PacketUpdateElytraBoosting(isBoosting = true));
			}
		} else {
			if (isBoosting) {
				EnigmaticLegacy.packetInstance.send(PacketDistributor.SERVER.noArg(),
						new PacketUpdateElytraBoosting(isBoosting = false));
			}
		}
	}

	private boolean boostPlayer(Player player) {
		if (player.isFallFlying()) {
			Vec3 vec31 = player.getLookAngle();
			Vec3 vec32 = player.getDeltaMovement();
			player.setDeltaMovement(vec32.add(vec31.x * 0.1D + (vec31.x * 1.5D - vec32.x) * 0.5D, vec31.y * 0.1D + (vec31.y * 1.5D - vec32.y) * 0.5D, vec31.z * 0.1D + (vec31.z * 1.5D - vec32.z) * 0.5D));
			return true;
		} else
			return false;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();
		return attributes;
	}

	@Override
	public boolean isValidRepairItem(ItemStack repairedItem, ItemStack material) {
		return material.is(EnigmaticItems.ETHERIUM_INGOT);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
		ItemStack itemstack1 = player.getItemBySlot(equipmentslot);
		if (itemstack1.isEmpty()) {
			player.setItemSlot(equipmentslot, itemstack.copy());
			if (!level.isClientSide()) {
				player.awardStat(Stats.ITEM_USED.get(this));
			}

			itemstack.setCount(0);
			return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
		} else
			return InteractionResultHolder.fail(itemstack);
	}

	@Override
	public SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
		return new SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA, 1F, 1F);
	}

	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		return entity instanceof Player && ElytraItem.isFlyEnabled(stack);
	}

	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		if (entity instanceof Player player) {
			if (!entity.level().isClientSide) {
				int nextFlightTick = flightTicks + 1;

				if (nextFlightTick % 10 == 0) {
					if (nextFlightTick % 20 == 0) {
						stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(EquipmentSlot.CHEST));
					}
					entity.gameEvent(GameEvent.ELYTRA_GLIDE);
				}
			} else {
				this.handleBoosting(player);
			}

			return true;
		} else
			return false;
	}

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			ItemStack stack = null;
			AttributeInstance attribute = event.player.getAttribute(CaelusApi.getInstance().getFlightAttribute());
			attribute.removeModifier(ELYTRA_MODIFIER);

			if (!attribute.hasModifier(ELYTRA_MODIFIER)) {
				stack = SuperpositionHandler.getEnigmaticElytra(event.player);
				if (stack != null && stack.is(this) && ElytraItem.isFlyEnabled(stack)) {
					attribute.addTransientModifier(ELYTRA_MODIFIER);
				}
			}

			if (event.player instanceof ServerPlayer player)
				if (TransientPlayerData.get(player).isElytraBoosting()) {
					this.boostPlayer(player);

					if (stack != null && stack.is(this)) {
						int flightTicks = player.getFallFlyingTicks();
						int nextFlightTick = flightTicks + 1;
						if (nextFlightTick % 5 == 0) {
							stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlot.CHEST));
						}
					}
				}
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onPlayerTickClient(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START && event.player.level().isClientSide()) {
			Player player = event.player;

			if (TransientPlayerData.get(player).isElytraBoosting()) {
				if (!player.isFallFlying()) {
					if (event.player == Minecraft.getInstance().player) {
						this.handleBoosting(player);
						return;
					} else {
						TransientPlayerData.get(player).setElytraBoosting(false);
						return;
					}
				}

				int amount = 3;
				double rangeModifier = 0.1;

				for (int counter = 0; counter <= amount; counter++) {
					Vector3 vec = Vector3.fromEntityCenter(player);
					vec = vec.add(Math.random() - 0.5, -1.0 + Math.random() - 0.5, Math.random() - 0.5);
					player.level().addParticle(ParticleTypes.DRAGON_BREATH, true, vec.x, vec.y, vec.z, ((Math.random()-0.5D)*2.0D)*rangeModifier, ((Math.random()-0.5D)*2.0D)*rangeModifier, ((Math.random()-0.5D)*2.0D)*rangeModifier);
				}
			}
		}
	}

}
