package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.ICursed;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DarkMirror extends ItemBase implements ICursed {

	public DarkMirror() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.RARE).maxStackSize(1));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "dark_mirror"));
	}

	/*
	@Override
	public int getUseDuration(ItemStack stack) {
		return 80;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		Vector3 vec = Vector3.fromEntityCenter(player);

		for (int counter = 0; counter <= 3; counter++) {
			player.world.addParticle(ParticleTypes.PORTAL, true, vec.x, vec.y, vec.z, (random.nextDouble() - 0.5D) * 3D, (random.nextDouble() - 0.5D) * 3D, (random.nextDouble() - 0.5D) * 3D);
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;

			if (player instanceof ServerPlayerEntity) {
				SuperpositionHandler.backToSpawn((ServerPlayerEntity) player);
				player.getCooldownTracker().setCooldown(this, 200);
			}
		}

		return stack;
	}

	 */


	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.darkMirror1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.darkMirror2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.darkMirror3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (EnigmaticLegacy.proxy.isInVanillaDimension(player) && SuperpositionHandler.isTheCursedOne(player) && !player.getCooldownTracker().hasCooldown(this)) {
			player.setActiveHand(hand);

			if (player instanceof ServerPlayerEntity) {
				SuperpositionHandler.backToSpawn((ServerPlayerEntity) player);
				player.getCooldownTracker().setCooldown(this, 200);
			}

			return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
		} else
			return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}

}
