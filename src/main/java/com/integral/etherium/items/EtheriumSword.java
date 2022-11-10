package com.integral.etherium.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.objects.CooldownMap;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.etherium.core.EtheriumUtil;
import com.integral.etherium.core.IEtheriumConfig;
import com.integral.etherium.core.IEtheriumTool;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumSword extends SwordItem implements IEtheriumTool {
	public CooldownMap etheriumSwordCooldowns = new CooldownMap();
	private final IEtheriumConfig config;

	public EtheriumSword(IEtheriumConfig config) {
		super(config.getToolMaterial(), 6, -2.6F, EtheriumUtil.defaultProperties(config, EtheriumSword.class).fireResistant());
		this.setRegistryName(new ResourceLocation(config.getOwnerMod(), "etherium_sword"));
		this.config = config;
	}

	@Override
	public IEtheriumConfig getConfig() {
		return this.config;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword4", ChatFormatting.GOLD, this.config.getSwordCooldown() / 20F);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword5");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (!this.areaEffectsAllowed(stack)) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.abilityDisabled");
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (hand == InteractionHand.OFF_HAND || player.getOffhandItem().getUseAnimation() == UseAnim.BLOCK)
			return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		} else if (!player.level.isClientSide) {
			if (!player.getCooldowns().isOnCooldown(this) && this.areaEffectsEnabled(player, stack)) {
				Vector3 look = new Vector3(player.getLookAngle());
				Vector3 dir = look.multiply(1D);

				this.config.knockBack(player, 1.0F, dir.x, dir.z);
				world.playSound(null, player.blockPosition(), SoundEvents.SKELETON_SHOOT, SoundSource.PLAYERS, 1.0F, (float) (0.6F + (Math.random() * 0.1D)));

				player.getCooldowns().addCooldown(this, this.config.getSwordCooldown());

				player.startUsingItem(hand);
				return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
			}
		}

		player.startUsingItem(hand);
		return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getPlayer().isCrouching())
			return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
		else
			return super.useOn(context);
	}

}
