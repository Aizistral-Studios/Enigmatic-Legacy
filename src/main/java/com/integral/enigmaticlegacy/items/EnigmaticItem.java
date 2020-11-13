package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
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

	public HashMap<PlayerEntity, Boolean> flightMap = new HashMap<PlayerEntity, Boolean>();

	public EnigmaticItem() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC).isImmuneToFire());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_item"));

		this.immunityList.add(DamageSource.FALL.damageType);
		this.immunityList.add(DamageSource.FLY_INTO_WALL.damageType);
		this.immunityList.add(DamageSource.CACTUS.damageType);
		this.immunityList.add(DamageSource.CRAMMING.damageType);
		this.immunityList.add(DamageSource.DROWN.damageType);
		this.immunityList.add(DamageSource.HOT_FLOOR.damageType);
		this.immunityList.add(DamageSource.LAVA.damageType);
		this.immunityList.add(DamageSource.IN_FIRE.damageType);
		this.immunityList.add(DamageSource.ON_FIRE.damageType);
		this.immunityList.add(DamageSource.IN_WALL.damageType);
		this.immunityList.add(DamageSource.OUT_OF_WORLD.damageType);
		this.immunityList.add(DamageSource.STARVE.damageType);
		this.immunityList.add(DamageSource.SWEET_BERRY_BUSH.damageType);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

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
		if (living.isBurning()) {
			living.extinguish();
		}

		List<EffectInstance> effects = new ArrayList<EffectInstance>(living.getActivePotionEffects());

		for (EffectInstance effect : effects) {
			if (!effect.getPotion().isBeneficial()) {
				living.removePotionEffect(effect.getPotion());
			}
		}

	}

	public void handleEnigmaticFlight(final PlayerEntity player) {
		try {
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem)) {
				this.flightMap.put(player, true);
				if (!player.abilities.allowFlying) {
					player.abilities.allowFlying = true;
					player.sendPlayerAbilities();
				}
			} else if (this.flightMap.get(player)) {
				if (!player.isCreative()) {
					player.abilities.allowFlying = false;
					player.abilities.isFlying = false;
					player.sendPlayerAbilities();
				}
				this.flightMap.put(player, false);
			}
		} catch (NullPointerException ex) {
			this.flightMap.put(player, false);
		}
	}

	@Override
	public void triggerActiveAbility(World world, ServerPlayerEntity player, ItemStack stack) {
		if (world.isRemote || SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		this.launchWitherSkull(world, player, Item.random.nextDouble() <= 0.25);
		SuperpositionHandler.setSpellstoneCooldown(player, spellstoneCooldown.getValue());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

	private void launchWitherSkull(World world, PlayerEntity player, boolean invulnerable) {
		world.playEvent((PlayerEntity) null, 1024, new BlockPos(player.getPositionVec()), 0);

		Vector3 look = new Vector3(player.getLookVec()).multiply(1, 0, 1);

		double playerRot = Math.toRadians(player.rotationYaw + 90);
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

		witherskullentity.setPosition(end.x, end.y, end.z);

		world.addEntity(witherskullentity);

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(witherskullentity.getPosX(), witherskullentity.getPosY(), witherskullentity.getPosZ(), 64, witherskullentity.world.getDimensionKey())), new PacketWitherParticles(witherskullentity.getPosX(), witherskullentity.getPosY() + (witherskullentity.getHeight() / 2), witherskullentity.getPosZ(), 8, false));

	}

}
