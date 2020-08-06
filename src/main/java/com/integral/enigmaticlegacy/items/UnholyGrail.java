package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnholyGrail extends ItemBase {

	public UnholyGrail() {
		super(ItemBase.getDefaultProperties().maxStackSize(1).rarity(Rarity.EPIC).func_234689_a_());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "unholy_grail"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.UNHOLY_GRAIL_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.func_231173_s_()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.unholyGrail1");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (!(entityLiving instanceof PlayerEntity))
			return stack;

		PlayerEntity player = (PlayerEntity) entityLiving;

		if (!worldIn.isRemote) {
			player.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 2, false, true));
			player.addPotionEffect(new EffectInstance(Effects.POISON, 160, 1, false, true));
			player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 240, 0, false, true));
			player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200, 1, false, true));
			player.addPotionEffect(new EffectInstance(Effects.HUNGER, 160, 2, false, true));
			player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 240, 0, false, true));

			UseUnholyGrailTrigger.INSTANCE.trigger((ServerPlayerEntity) player);

		}

		player.addStat(Stats.ITEM_USED.get(this));

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

}
