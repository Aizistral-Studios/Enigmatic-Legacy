package com.integral.enigmaticlegacy.objects;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import top.theillusivec4.curios.api.CuriosApi;

public class FortuneBonusModifier extends LootModifier {

	protected FortuneBonusModifier(ILootCondition[] conditions) {
		super(conditions);
	}

	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		ItemStack tool = context.get(LootParameters.TOOL);

		if (tool == null || tool.getOrCreateTag().getBoolean("HasEnigmaticFortuneBonus"))
			return generatedLoot;

		Entity entity = context.get(LootParameters.THIS_ENTITY);
		BlockState blockState = context.get(LootParameters.BLOCK_STATE);
		if (blockState == null || !(entity instanceof PlayerEntity))
			return generatedLoot;

		PlayerEntity player = (PlayerEntity) entity;
		int fortuneBonus = 0;

		if (SuperpositionHandler.isTheCursedOne(player)) {
			fortuneBonus += 1;
		}

		if (SuperpositionHandler.hasCurio(player, EnigmaticLegacy.miningCharm)) {
			fortuneBonus += 1;
		}

		if (fortuneBonus <= 0)
			return generatedLoot;

		ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();
		fakeTool.getOrCreateTag().putBoolean("HasEnigmaticFortuneBonus", true);

		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(fakeTool);
		enchantments.put(Enchantments.FORTUNE, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, fakeTool) + fortuneBonus);
		EnchantmentHelper.setEnchantments(enchantments, fakeTool);
		LootContext.Builder builder = new LootContext.Builder(context);
		builder.withParameter(LootParameters.TOOL, fakeTool);
		LootContext newContext = builder.build(LootParameterSets.BLOCK);
		LootTable lootTable = context.getWorld().getServer().getLootTableManager().getLootTableFromLocation(blockState.getBlock().getLootTable());
		return lootTable.generate(newContext);
	}

	public static class Serializer extends GlobalLootModifierSerializer<FortuneBonusModifier> {

		@Override
		public FortuneBonusModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
			return new FortuneBonusModifier(conditions);
		}

		@Override
		public JsonObject write(FortuneBonusModifier instance) {
			return this.makeConditions(instance.conditions);
		}
	}
}
