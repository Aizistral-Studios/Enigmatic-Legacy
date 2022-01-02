package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.api.items.ISpellstone;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemSpellstoneCurio;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

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
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_item"));

		this.immunityList.add(DamageSource.FALL.msgId);
		this.immunityList.add(DamageSource.FLY_INTO_WALL.msgId);
		this.immunityList.add(DamageSource.CACTUS.msgId);
		this.immunityList.add(DamageSource.CRAMMING.msgId);
		this.immunityList.add(DamageSource.DROWN.msgId);
		this.immunityList.add(DamageSource.HOT_FLOOR.msgId);
		this.immunityList.add(DamageSource.LAVA.msgId);
		this.immunityList.add(DamageSource.IN_FIRE.msgId);
		this.immunityList.add(DamageSource.ON_FIRE.msgId);
		this.immunityList.add(DamageSource.IN_WALL.msgId);
		this.immunityList.add(DamageSource.OUT_OF_WORLD.msgId);
		this.immunityList.add(DamageSource.STARVE.msgId);
		this.immunityList.add(DamageSource.SWEET_BERRY_BUSH.msgId);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItemCooldown", TextFormatting.GOLD, spellstoneCooldown.getValue() / 20F);
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
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (living.isOnFire()) {
			living.clearFire();
		}

		List<EffectInstance> effects = new ArrayList<EffectInstance>(living.getActiveEffects());

		for (EffectInstance effect : effects) {
			if (effect.getEffect().getRegistryName().equals(new ResourceLocation("mana-and-artifice", "chrono-exhaustion"))) {
				continue;
			}

			if (!effect.getEffect().isBeneficial()) {
				living.removeEffect(effect.getEffect());
			}
		}

	}

	public void handleEnigmaticFlight(final Player player) {
		try {
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem)) {
				this.flightMap.put(player, true);
				if (!player.abilities.mayfly) {
					player.abilities.mayfly = true;
					player.onUpdateAbilities();
				}
			} else if (this.flightMap.get(player)) {
				if (!player.isCreative()) {
					player.abilities.mayfly = false;
					player.abilities.flying = false;
					player.onUpdateAbilities();
				}
				this.flightMap.put(player, false);
			}
		} catch (NullPointerException ex) {
			this.flightMap.put(player, false);
		}
	}

	@Override
	public void triggerActiveAbility(World world, ServerPlayer player, ItemStack stack) {
		if (world.isClientSide || SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		this.launchWitherSkull(world, player, Item.random.nextDouble() <= 0.25);
		SuperpositionHandler.setSpellstoneCooldown(player, spellstoneCooldown.getValue());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

	private void launchWitherSkull(World world, Player player, boolean invulnerable) {
		world.levelEvent((Player) null, 1024, new BlockPos(player.position()), 0);

		Vector3 look = new Vector3(player.getLookAngle()).multiply(1, 0, 1);

		double playerRot = Math.toRadians(player.yRot + 90);
		if (look.x == 0 && look.z == 0) {
			look = new Vector3(Math.cos(playerRot), 0, Math.sin(playerRot));
		}

		look = look.normalize().multiply(-2);

		double div = -0.75 + (Item.random.nextDouble() * 0.75);
		double mod = -0.5D + (Item.random.nextDouble() * 6D);

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

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(witherskullentity.getX(), witherskullentity.getY(), witherskullentity.getZ(), 64, witherskullentity.level.dimension())), new PacketWitherParticles(witherskullentity.getX(), witherskullentity.getY() + (witherskullentity.getBbHeight() / 2), witherskullentity.getZ(), 8, false));

	}

}
