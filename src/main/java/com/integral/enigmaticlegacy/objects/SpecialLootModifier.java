package com.integral.enigmaticlegacy.objects;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class SpecialLootModifier extends LootModifier {
	public static final Supplier<Codec<SpecialLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, SpecialLootModifier::new)));

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
				if (OmniconfigHandler.isItemEnabled(EnigmaticLegacy.architectEye))
					if (!SuperpositionHandler.hasPersistentTag(player, "LootedArchitectEye")) {
						SuperpositionHandler.setPersistentBoolean(player, "LootedArchitectEye", true);
						generatedLoot.add(new ItemStack(EnigmaticLegacy.architectEye, 1));
					}

				if (SuperpositionHandler.hasPersistentTag(player, "LootedIchorBottle")) {
					generatedLoot.removeIf(stack -> stack.is(EnigmaticLegacy.ichorBottle));
				} else if (generatedLoot.stream().anyMatch(stack -> stack.is(EnigmaticLegacy.ichorBottle))) {
					SuperpositionHandler.setPersistentBoolean(player, "LootedIchorBottle", true);
				}
			}

			if (OmniconfigHandler.isItemEnabled(EnigmaticLegacy.astralFruit))
				if ("minecraft:chests/end_city_treasure".equals(String.valueOf(context.getQueriedLootTableId()))) {
					if (!SuperpositionHandler.hasPersistentTag(player, "LootedFirstEndCityChest")) {
						SuperpositionHandler.setPersistentBoolean(player, "LootedFirstEndCityChest", true);

						if (SuperpositionHandler.isTheCursedOne(player)) {
							generatedLoot.add(new ItemStack(EnigmaticLegacy.astralFruit, 1));
						}
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
