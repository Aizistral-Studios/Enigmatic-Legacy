package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class SuperMagnetRing extends MagnetRing {

	public SuperMagnetRing() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "super_magnet_ring"));
	}

	@Override
	public boolean isForMortals() {
		return ConfigHandler.SUPER_MAGNET_RING_ENABLED.getValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.superMagnetRing1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.superMagnetRing2", TextFormatting.GOLD, ConfigHandler.SUPER_MAGNET_RING_RANGE.getValue());
			ItemLoreHelper.addLocalizedString(list, ConfigHandler.INVERT_MAGNETS_SHIFT.getValue() ? "tooltip.enigmaticlegacy.superMagnetRing3_alt" : "tooltip.enigmaticlegacy.superMagnetRing3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living) {
		if (ConfigHandler.INVERT_MAGNETS_SHIFT.getValue() ? !living.isCrouching() : living.isCrouching() || living.world.isRemote || !(living instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) living;

		double x = living.getPosX();
		double y = living.getPosY() + 0.75;
		double z = living.getPosZ();

		List<ItemEntity> items = living.world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(x - ConfigHandler.SUPER_MAGNET_RING_RANGE.getValue(), y - ConfigHandler.SUPER_MAGNET_RING_RANGE.getValue(), z - ConfigHandler.SUPER_MAGNET_RING_RANGE.getValue(), x + ConfigHandler.SUPER_MAGNET_RING_RANGE.getValue(), y + ConfigHandler.SUPER_MAGNET_RING_RANGE.getValue(), z + ConfigHandler.SUPER_MAGNET_RING_RANGE.getValue()));
		int pulled = 0;
		for (ItemEntity item : items)
			if (this.canPullItem(item)) {
				if (pulled > 512)
					break;

				if (!SuperpositionHandler.canPickStack(player, item.getItem()))
					continue;

				/*
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(item.getPosX(), item.getPosY(), item.getPosZ(), 24, item.world.func_234923_W_())), new PacketPortalParticles(item.getPosX(), item.getPosY() + (item.getHeight() / 2), item.getPosZ(), 24, 0.75D, true));

				if (ConfigHandler.SUPER_MAGNET_RING_SOUND.getValue())
					item.world.playSound(null, item.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				 */

				item.setNoPickupDelay();
				item.onCollideWithPlayer(player);

				pulled++;
			}

	}

	@Override
	protected boolean canPullItem(ItemEntity item) {
		return super.canPullItem(item);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living) {
		return false;
	}

}
