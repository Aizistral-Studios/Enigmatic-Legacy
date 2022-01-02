package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SuperMagnetRing extends MagnetRing {
	public static Omniconfig.IntParameter range;
	public static Omniconfig.BooleanParameter soundEnabled;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("SuperMagnetRing");

		range = builder
				.comment("The radius in which Dislocation Ring will collect items.")
				.min(1)
				.max(256)
				.getInt("Range", 16);

		soundEnabled = builder
				.comment("Whether or not Dislocation Ring should play sound effect when teleporting items to player.")
				.getBoolean("Sound", false);

		builder.popPrefix();
	}

	public SuperMagnetRing() {
		super(ItemBaseCurio.getDefaultProperties().rarity(Rarity.EPIC));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "super_magnet_ring"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.superMagnetRing1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.superMagnetRing2", TextFormatting.GOLD, range.getValue());
			ItemLoreHelper.addLocalizedString(list, invertShift.getValue() ? "tooltip.enigmaticlegacy.superMagnetRing3_alt" : "tooltip.enigmaticlegacy.superMagnetRing3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if (invertShift.getValue() ? !living.isCrouching() : living.isCrouching() || living.level.isClientSide || !(living instanceof PlayerEntity))
			return;

		PlayerEntity player = (PlayerEntity) living;

		double x = living.getX();
		double y = living.getY() + 0.75;
		double z = living.getZ();

		List<ItemEntity> items = living.level.getEntitiesOfClass(ItemEntity.class, new AxisAlignedBB(x - range.getValue(), y - range.getValue(), z - range.getValue(), x + range.getValue(), y + range.getValue(), z + range.getValue()));
		int pulled = 0;
		for (ItemEntity item : items)
			if (this.canPullItem(item)) {
				if (pulled > 512) {
					break;
				}

				if (!SuperpositionHandler.canPickStack(player, item.getItem())) {
					continue;
				}

				/*
				EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(item.getPosX(), item.getPosY(), item.getPosZ(), 24, item.world.getDimensionKey())), new PacketPortalParticles(item.getPosX(), item.getPosY() + (item.getHeight() / 2), item.getPosZ(), 24, 0.75D, true));

				if (ConfigHandler.SUPER_MAGNET_RING_SOUND.getValue())
					item.world.playSound(null, item.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2D)));
				 */

				item.setNoPickUpDelay();
				item.playerTouch(player);

				pulled++;
			}

	}

	@Override
	protected boolean canPullItem(ItemEntity item) {
		return super.canPullItem(item);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
		return false;
	}

}
