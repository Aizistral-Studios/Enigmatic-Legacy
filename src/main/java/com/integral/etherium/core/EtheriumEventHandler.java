package com.integral.etherium.core;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.etherium.items.EtheriumArmor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.LootPool.Builder;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class EtheriumEventHandler {
	private final IEtheriumConfig config;
	private final Item etheriumOre;

	public EtheriumEventHandler(IEtheriumConfig config, Item etheriumOre) {
		this.config = config;
		this.etheriumOre = etheriumOre;
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			/*
			 * Handler for knockback feedback and damage reduction of Etherium Armor Shield.
			 */

			if (EtheriumArmor.hasShield(player)) {
				if (event.getSource().getImmediateSource() instanceof LivingEntity) {
					LivingEntity attacker = ((LivingEntity) event.getSource().getTrueSource());
					Vector3 vec = Vector3.fromEntityCenter(player).subtract(Vector3.fromEntityCenter(event.getSource().getTrueSource())).normalize();
					attacker.applyKnockback(0.75F, vec.x, vec.z);
					player.world.playSound(null, player.getPosition(), this.config.getShieldTriggerSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
					player.world.playSound(null, player.getPosition(), this.config.getShieldTriggerSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}

				event.setAmount(event.getAmount() * this.config.getShieldReduction().asModifierInverted());
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
				if (EtheriumArmor.hasShield(player)) {
					event.setCanceled(true);

					player.world.playSound(null, player.getPosition(), this.config.getShieldTriggerSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		if (!this.config.isStandalone())
			return;

		if (event.getName().equals(LootTables.CHESTS_END_CITY_TREASURE)) {
			LootPool epic = constructLootPool("etherium", -11F, 2F,
					ItemLootEntry.builder(this.etheriumOre)
					.weight(60)
					.acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2F)))
					);

			LootTable modified = event.getTable();
			modified.addPool(epic);
			event.setTable(modified);
		}
	}

	private static LootPool constructLootPool(String poolName, float minRolls, float maxRolls, @Nullable LootEntry.Builder<?>... entries) {
		Builder poolBuilder = LootPool.builder();
		poolBuilder.name(poolName);
		poolBuilder.rolls(RandomValueRange.of(minRolls, maxRolls));

		for (LootEntry.Builder<?> entry : entries) {
			if (entry != null) {
				poolBuilder.addEntry(entry);
			}
		}

		LootPool constructedPool = poolBuilder.build();

		return constructedPool;

	}

}
