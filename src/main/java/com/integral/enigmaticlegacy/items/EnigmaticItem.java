package com.integral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.entities.UltimateWitherSkullEntity;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.LoreHelper;
import com.integral.enigmaticlegacy.helpers.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketWitherParticles;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.capability.ICurio;

public class EnigmaticItem extends Item implements ICurio {

	public static HashMap<PlayerEntity, Boolean> flightMap = new HashMap<PlayerEntity, Boolean>();
	public static Properties integratedProperties = new Item.Properties();
	public static List<String> immunityList = new ArrayList<String>();

	public EnigmaticItem(Properties properties) {
		super(properties);

		EnigmaticItem.immunityList.add(DamageSource.FALL.damageType);
		EnigmaticItem.immunityList.add(DamageSource.FLY_INTO_WALL.damageType);
		EnigmaticItem.immunityList.add(DamageSource.CACTUS.damageType);
		EnigmaticItem.immunityList.add(DamageSource.CRAMMING.damageType);
		EnigmaticItem.immunityList.add(DamageSource.DROWN.damageType);
		EnigmaticItem.immunityList.add(DamageSource.HOT_FLOOR.damageType);
		EnigmaticItem.immunityList.add(DamageSource.LAVA.damageType);
		EnigmaticItem.immunityList.add(DamageSource.IN_FIRE.damageType);
		EnigmaticItem.immunityList.add(DamageSource.ON_FIRE.damageType);
		EnigmaticItem.immunityList.add(DamageSource.IN_WALL.damageType);
		EnigmaticItem.immunityList.add(DamageSource.OUT_OF_WORLD.damageType);
		EnigmaticItem.immunityList.add(DamageSource.STARVE.damageType);
		EnigmaticItem.immunityList.add(DamageSource.SWEET_BERRY_BUSH.damageType);

	}

	public static Properties setupIntegratedProperties() {
		EnigmaticItem.integratedProperties.group(EnigmaticLegacy.enigmaticTab);
		EnigmaticItem.integratedProperties.maxStackSize(1);
		EnigmaticItem.integratedProperties.rarity(Rarity.EPIC);

		return EnigmaticItem.integratedProperties;

	}

	@Override
	public boolean canEquip(String identifier, LivingEntity living) {
		if (SuperpositionHandler.hasCurio(living, EnigmaticLegacy.enigmaticItem))
			return false;
		else
			return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem1");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem2");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItemCooldown", ConfigHandler.ENIGMATIC_ITEM_COOLDOWN.getValue() / 20F);
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem3");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem4");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem5");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem6");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem7");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem8");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem9");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem10");
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.enigmaticItem11");
		} else {
			LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public boolean canRightClickEquip() {
		return true;
	}

	@Override
	public void onEquipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onUnequipped(String identifier, LivingEntity entityLivingBase) {
		// Insert existential void here
	}

	@Override
	public void onCurioTick(String identifier, int index, LivingEntity entityLivingBase) {
		if (entityLivingBase.isBurning())
			entityLivingBase.extinguish();

		entityLivingBase.clearActivePotions();
	}

	public static void handleEnigmaticFlight(final PlayerEntity player) {
		try {
			if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.enigmaticItem)) {
				EnigmaticItem.flightMap.put(player, true);
				if (!player.abilities.allowFlying) {
					player.abilities.allowFlying = true;
					player.sendPlayerAbilities();
				}
			} else if (EnigmaticItem.flightMap.get(player)) {
				if (!player.isCreative()) {
					player.abilities.allowFlying = false;
					player.abilities.isFlying = false;
					player.sendPlayerAbilities();
				}
				EnigmaticItem.flightMap.put(player, false);
			}
		} catch (NullPointerException ex) {
			EnigmaticItem.flightMap.put(player, false);
		}
	}

	public void triggerActiveAbility(World world, PlayerEntity player, ItemStack stack) {
		if (world.isRemote || SuperpositionHandler.hasSpellstoneCooldown(player))
			return;

		this.launchWitherSkull(world, player, Item.random.nextDouble() <= 0.25);
		SuperpositionHandler.setSpellstoneCooldown(player, ConfigHandler.ENIGMATIC_ITEM_COOLDOWN.getValue());
	}

	private void launchWitherSkull(World world, PlayerEntity player, boolean invulnerable) {
		world.playEvent((PlayerEntity) null, 1024, new BlockPos(player), 0);

		Vector3 look = new Vector3(player.getLookVec()).multiply(1, 0, 1);

		double playerRot = Math.toRadians(player.rotationYaw + 90);
		if (look.x == 0 && look.z == 0)
			look = new Vector3(Math.cos(playerRot), 0, Math.sin(playerRot));

		look = look.normalize().multiply(-2);

		double div = -0.75 + (Item.random.nextDouble() * 0.75);
		double mod = -0.5D + (Item.random.nextDouble() * 6D);

		Vector3 pl = look.add(Vector3.fromEntityCenter(player)).add(0, 1.6, div * 0.1);

		Vector3 axis = look.normalize().crossProduct(new Vector3(-1, 0, -1)).normalize();

		double rot = mod * Math.PI / 4D - Math.PI / 2D;

		Vector3 axis1 = axis.multiply(div * 3.5D + 5D).rotate(rot, look);
		if (axis1.y < 0)
			axis1 = axis1.multiply(1, -1, 1);

		Vector3 end = pl.add(axis1);

		//end = end.add(0, -1, 0);\

		UltimateWitherSkullEntity witherskullentity = new UltimateWitherSkullEntity(world, player);

		if (invulnerable) {
			witherskullentity.setSkullInvulnerable(true);
		}

		witherskullentity.setPosition(end.x, end.y, end.z);

		world.addEntity(witherskullentity);

		EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(witherskullentity.getPosX(), witherskullentity.getPosY(), witherskullentity.getPosZ(), 64, witherskullentity.dimension)), new PacketWitherParticles(witherskullentity.getPosX(), witherskullentity.getPosY() + (witherskullentity.getHeight() / 2), witherskullentity.getPosZ(), 8, false));

	}

}
