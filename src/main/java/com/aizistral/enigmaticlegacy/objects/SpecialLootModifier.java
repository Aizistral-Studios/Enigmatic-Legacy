package com.aizistral.enigmaticlegacy.objects;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.config.OmniconfigHandler;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.LootModifier;

public class SpecialLootModifier extends LootModifier {
	public static final Supplier<Codec<SpecialLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SpecialLootModifier::new)));
	public static final List<ResourceLocation> SUSPICIOUS_TABLES = ImmutableList.of(
			new ResourceLocation("minecraft", "archaeology/desert_pyramid"),
			new ResourceLocation("minecraft", "archaeology/desert_well"),
			new ResourceLocation("minecraft", "archaeology/ocean_ruin_cold"),
			new ResourceLocation("minecraft", "archaeology/ocean_ruin_warm"),
			new ResourceLocation("minecraft", "archaeology/trail_ruins_common"),
			new ResourceLocation("minecraft", "archaeology/trail_ruins_rare")
			);

	public static Omniconfig.DoubleParameter earthHeartChance;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("CustomLoot");

		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			earthHeartChance = builder
					.comment("The chance for Heart of the Earth to be obtained from suspicious blocks (default is 3%).")
					.getDouble("EarthHeartChance", 0.03);
		}

		builder.popPrefix();
	}

	protected SpecialLootModifier(LootItemCondition[] conditions) {
		super(conditions);
	}

	@Nonnull
	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		ServerLevel level = context.getLevel();
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

		if (entity instanceof ServerPlayer player) {
			if (this.isVanillaChest(context)) {
				if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ENIGMATIC_EYE))
					if (!SuperpositionHandler.hasPersistentTag(player, "LootedArchitectEye")) {
						SuperpositionHandler.setPersistentBoolean(player, "LootedArchitectEye", true);
						generatedLoot.add(new ItemStack(EnigmaticItems.ENIGMATIC_EYE, 1));
					}

				if (SuperpositionHandler.hasPersistentTag(player, "LootedIchorBottle")) {
					generatedLoot.removeIf(stack -> stack.is(EnigmaticItems.ICHOR_BOTTLE));
				} else if (generatedLoot.stream().anyMatch(stack -> stack.is(EnigmaticItems.ICHOR_BOTTLE))) {
					SuperpositionHandler.setPersistentBoolean(player, "LootedIchorBottle", true);
				}
			}

			if (OmniconfigHandler.isItemEnabled(EnigmaticItems.ASTRAL_FRUIT))
				if ("minecraft:chests/end_city_treasure".equals(String.valueOf(context.getQueriedLootTableId()))) {
					if (!SuperpositionHandler.hasPersistentTag(player, "LootedFirstEndCityChest")) {
						SuperpositionHandler.setPersistentBoolean(player, "LootedFirstEndCityChest", true);

						if (SuperpositionHandler.isTheCursedOne(player)) {
							generatedLoot.add(new ItemStack(EnigmaticItems.ASTRAL_FRUIT, 1));
						}
					}
				}

			if (OmniconfigHandler.isItemEnabled(EnigmaticItems.EARTH_HEART))
				if (SUSPICIOUS_TABLES.stream().anyMatch(table -> table.equals(context.getQueriedLootTableId()))) {
					if (context.getRandom().nextDouble() < earthHeartChance.getValue()) {
						generatedLoot.clear();
						generatedLoot.add(new ItemStack(EnigmaticItems.EARTH_HEART, 1));
					}
				}
		}

		return generatedLoot;
	}

	private boolean isVanillaChest(LootContext context) {
		return String.valueOf(context.getQueriedLootTableId()).startsWith("minecraft:chests/");
	}

	@Override
	public Codec<SpecialLootModifier> codec() {
		return CODEC.get();
	}

}
