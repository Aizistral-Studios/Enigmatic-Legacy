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
				if (event.getSource().getDirectEntity() instanceof LivingEntity) {
					LivingEntity attacker = ((LivingEntity) event.getSource().getEntity());
					Vector3 vec = Vector3.fromEntityCenter(player).subtract(Vector3.fromEntityCenter(event.getSource().getEntity())).normalize();
					attacker.knockback(0.75F, vec.x, vec.z);
					player.level.playSound(null, player.blockPosition(), this.config.getShieldTriggerSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
					player.level.playSound(null, player.blockPosition(), this.config.getShieldTriggerSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}

				event.setAmount(event.getAmount() * this.config.getShieldReduction().asModifierInverted());
			}
		}
	}

	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent event) {
		if (event.getEntityLiving().level.isClientSide)
			return;

		/*
		 * Handler for immunities and projectile deflection.
		 */

		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();

			if (event.getSource().getDirectEntity() instanceof DamagingProjectileEntity || event.getSource().getDirectEntity() instanceof AbstractArrowEntity) {
				if (EtheriumArmor.hasShield(player)) {
					event.setCanceled(true);

					player.level.playSound(null, player.blockPosition(), this.config.getShieldTriggerSound(), SoundCategory.PLAYERS, 1.0F, 0.9F + (float) (Math.random() * 0.1D));
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		if (!this.config.isStandalone())
			return;

		if (event.getName().equals(LootTables.END_CITY_TREASURE)) {
			LootPool epic = constructLootPool("etherium", -11F, 2F,
					ItemLootEntry.lootTableItem(this.etheriumOre)
					.setWeight(60)
					.apply(SetCount.setCount(RandomValueRange.between(1.0F, 2F)))
					);

			LootTable modified = event.getTable();
			modified.addPool(epic);
			event.setTable(modified);
		}
	}

	private static LootPool constructLootPool(String poolName, float minRolls, float maxRolls, @Nullable LootEntry.Builder<?>... entries) {
		Builder poolBuilder = LootPool.lootPool();
		poolBuilder.name(poolName);
		poolBuilder.setRolls(RandomValueRange.between(minRolls, maxRolls));

		for (LootEntry.Builder<?> entry : entries) {
			if (entry != null) {
				poolBuilder.add(entry);
			}
		}

		LootPool constructedPool = poolBuilder.build();

		return constructedPool;

	}

}
