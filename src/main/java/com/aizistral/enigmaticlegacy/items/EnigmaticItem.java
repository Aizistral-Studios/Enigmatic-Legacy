package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.api.items.ISpellstone;
import com.aizistral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;

public class EnigmaticItem extends ItemSpellstoneCurio implements ISpellstone {
	public static Omniconfig.IntParameter spellstoneCooldown;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EnigmaticItem");

		spellstoneCooldown = builder
				.comment("Active ability cooldown for Heart of Creation. Measured in ticks. 20 ticks equal to 1 second.")
				.getInt("Cooldown", 3);

		builder.popPrefix();
	}

	public Map<Player, Boolean> flightMap = new WeakHashMap<>();

	public EnigmaticItem() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC).fireResistant());

		this.immunityList.add(DamageTypes.FALL);
		this.immunityList.add(DamageTypes.FLY_INTO_WALL);
		this.immunityList.add(DamageTypes.CACTUS);
		this.immunityList.add(DamageTypes.CRAMMING);
		this.immunityList.add(DamageTypes.DROWN);
		this.immunityList.add(DamageTypes.HOT_FLOOR);
		this.immunityList.add(DamageTypes.LAVA);
		this.immunityList.add(DamageTypes.IN_FIRE);
		this.immunityList.add(DamageTypes.ON_FIRE);
		this.immunityList.add(DamageTypes.IN_WALL);
		this.immunityList.add(DamageTypes.FELL_OUT_OF_WORLD);
		this.immunityList.add(DamageTypes.STARVE);
		this.immunityList.add(DamageTypes.SWEET_BERRY_BUSH);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItemCooldown", ChatFormatting.GOLD, spellstoneCooldown.getValue() / 20F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(SlotContext context, ItemStack stack) {
		if (context.entity().isOnFire()) {
			context.entity().clearFire();
		}

		List<MobEffectInstance> effects = new ArrayList<MobEffectInstance>(context.entity().getActiveEffects());

		for (MobEffectInstance effect : effects) {
			if (ForgeRegistries.MOB_EFFECTS.getKey(effect.getEffect()).equals(new ResourceLocation("mana-and-artifice", "chrono-exhaustion"))) {
				continue;
			}

			if (!effect.getEffect().isBeneficial()) {
				context.entity().removeEffect(effect.getEffect());
			}
		}

	}

	public void handleEnigmaticFlight(final Player player) {
		try {
			if (SuperpositionHandler.hasCurio(player, EnigmaticItems.ENIGMATIC_ITEM)) {
				this.flightMap.put(player, true);
				if (!player.getAbilities().mayfly) {
					player.getAbilities().mayfly = true;
					player.onUpdateAbilities();
				}
			} else if (this.flightMap.get(player)) {
				if (!player.isCreative()) {
					player.getAbilities().mayfly = false;
					player.getAbilities().flying = false;
					player.onUpdateAbilities();
				}
				this.flightMap.put(player, false);
			}
		} catch (NullPointerException ex) {
			this.flightMap.put(player, false);
		}
	}

	@Override
	public void triggerActiveAbility(Level world, ServerPlayer player, ItemStack stack) {
		if (world.isClientSide || SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		this.launchWitherSkull(world, player, random.nextDouble() <= 0.25);
		SuperpositionHandler.setSpellstoneCooldown(player, spellstoneCooldown.getValue());
	}

	private void launchWitherSkull(Level world, Player player, boolean invulnerable) {
		world.levelEvent((Player) null, 1024, BlockPos.containing(player.position()), 0);

		Vector3 look = new Vector3(player.getLookAngle()).multiply(1, 0, 1);

		double playerRot = Math.toRadians(player.getYRot() + 90);
		if (look.x == 0 && look.z == 0) {
			look = new Vector3(Math.cos(playerRot), 0, Math.sin(playerRot));
		}

		look = look.normalize().multiply(-2);

		double div = -0.75 + (random.nextDouble() * 0.75);
		double mod = -0.5D + (random.nextDouble() * 6D);

		Vector3 pl = look.add(Vector3.fromEntityCenter(player)).add(0, 1.6, div * 0.1);

		Vector3 axis = look.normalize().crossProduct(new Vector3(-1, 0, -1)).normalize();

		double rot = mod * Math.PI / 4D - Math.PI / 2D;

		Vector3 axis1 = axis.multiply(div * 3.5D + 5D).rotate(rot, look);
		if (axis1.y < 0) {
			axis1 = axis1.multiply(1, -1, 1);
		}

		Vector3 end = pl.add(axis1);

		//end = end.add(0, -1, 0);\

		LivingEntity doomedOne = SuperpositionHandler.getObservedEntity(player, world, 3, 64);
		UltimateWitherSkullEntity witherskullentity = doomedOne != null ? new UltimateWitherSkullEntity(world, player, doomedOne) : new UltimateWitherSkullEntity(world, player);

		if (invulnerable) {
			witherskullentity.setSkullInvulnerable(true);
		}

		witherskullentity.setPos(end.x, end.y, end.z);

		world.addFreshEntity(witherskullentity);

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(witherskullentity.getX(), witherskullentity.getY(), witherskullentity.getZ(), 64, witherskullentity.level().dimension())), new PacketWitherParticles(witherskullentity.getX(), witherskullentity.getY() + (witherskullentity.getBbHeight() / 2), witherskullentity.getZ(), 8, false));

	}

}
