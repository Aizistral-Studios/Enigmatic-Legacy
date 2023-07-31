package com.aizistral.etherium.core;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.etherium.items.EtheriumArmor;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootPool.Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EtheriumEventHandler {
	private final IEtheriumConfig config;
	private final Item etheriumOre;

	public EtheriumEventHandler(IEtheriumConfig config, Item etheriumOre) {
		this.config = config;
		this.etheriumOre = etheriumOre;
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event) {
		if (event.getEntity() instanceof Player player && event.getAmount() > 0) {
			/*
			 * Handler for knockback feedback and damage reduction of Etherium Armor Shield.
			 */

			if (EtheriumArmor.hasShield(player)) {
				if (event.getSource().getDirectEntity() instanceof LivingEntity) {
					LivingEntity attacker = ((LivingEntity) event.getSource().getEntity());
					Vector3 vec = Vector3.fromEntityCenter(player).subtract(Vector3.fromEntityCenter(event.getSource().getEntity())).normalize();
					attacker.knockback(0.75F, vec.x, vec.z);
					player.level().playSound(null, player.blockPosition(), this.config.getShieldTriggerSound(), SoundSource.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
					player.level().playSound(null, player.blockPosition(), this.config.getShieldTriggerSound(), SoundSource.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}

				event.setAmount(event.getAmount() * this.config.getShieldReduction().asModifierInverted());
			}
		}
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event) {
		if (event.getEntity().level().isClientSide)
			return;

		// TODO Figure out a way to account for shield blocking

		/*
		 * Handler for immunities and projectile deflection.
		 */

		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if (event.getSource().getDirectEntity() instanceof AbstractHurtingProjectile || event.getSource().getDirectEntity() instanceof AbstractArrow) {
				if (EtheriumArmor.hasShield(player)) {
					event.setCanceled(true);

					player.level().playSound(null, player.blockPosition(), this.config.getShieldTriggerSound(), SoundSource.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		if (!this.config.isStandalone())
			return;

		if (event.getName().equals(BuiltInLootTables.END_CITY_TREASURE)) {
			LootPool epic = constructLootPool("etherium", -11F, 2F,
					LootItem.lootTableItem(this.etheriumOre)
					.setWeight(60)
					.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2F)))
					);

			LootTable modified = event.getTable();
			modified.addPool(epic);
			event.setTable(modified);
		}
	}

	private static LootPool constructLootPool(String poolName, float minRolls, float maxRolls, @Nullable LootPoolEntryContainer.Builder<?>... entries) {
		Builder poolBuilder = LootPool.lootPool();
		poolBuilder.name(poolName);
		poolBuilder.setRolls(UniformGenerator.between(minRolls, maxRolls));

		for (LootPoolEntryContainer.Builder<?> entry : entries) {
			if (entry != null) {
				poolBuilder.add(entry);
			}
		}

		LootPool constructedPool = poolBuilder.build();

		return constructedPool;

	}

}
