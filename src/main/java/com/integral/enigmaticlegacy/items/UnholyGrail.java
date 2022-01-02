package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.item.enchantment.IVanishable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAction;
import net.minecraft.world.item.alchemy.EffectInstance;
import net.minecraft.world.item.alchemy.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnholyGrail extends ItemBase implements IVanishable {

	public UnholyGrail() {
		super(ItemBase.getDefaultProperties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "unholy_grail"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.unholyGrail1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (!(entityLiving instanceof Player))
			return stack;

		Player player = (Player) entityLiving;

		if (!worldIn.isClientSide) {
			player.addEffect(new EffectInstance(Effects.WITHER, 100, 2, false, true));
			player.addEffect(new EffectInstance(Effects.POISON, 160, 1, false, true));
			player.addEffect(new EffectInstance(Effects.CONFUSION, 240, 0, false, true));
			player.addEffect(new EffectInstance(Effects.WEAKNESS, 200, 1, false, true));
			player.addEffect(new EffectInstance(Effects.HUNGER, 160, 2, false, true));
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 240, 0, false, true));

			UseUnholyGrailTrigger.INSTANCE.trigger((ServerPlayer) player);

		}

		player.awardStat(Stats.ITEM_USED.get(this));

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, Player playerIn, Hand handIn) {
		playerIn.startUsingItem(handIn);
		return new ActionResult<>(ActionResultType.CONSUME, playerIn.getItemInHand(handIn));
	}

}
