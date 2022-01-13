package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.omniconfig.wrappers.Omniconfig;
import com.integral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.world.item.Item.Properties;

public class MagnetRing extends ItemBaseCurio {
	public static Omniconfig.IntParameter range;
	public static Omniconfig.BooleanParameter invertShift;

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("MagnetRing");

		range = builder.comment("The radius in which Magnetic Ring will attract items.")
				.min(1)
				.max(256)
				.getInt("Range", 8);

		invertShift = builder.comment("Inverts the Shift behaviour of Magnetic Ring and Dislocation Ring.")
				.getBoolean("InvertShift", false);

		builder.popPrefix();
	}

	public MagnetRing() {
		this(ItemBaseCurio.getDefaultProperties().rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "magnet_ring"));
	}

	public MagnetRing(Properties properties) {
		super(properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");

		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.magnetRing1", ChatFormatting.GOLD, range.getValue());
			ItemLoreHelper.addLocalizedString(list, invertShift.getValue() ? "tooltip.enigmaticlegacy.magnetRing2_alt" : "tooltip.enigmaticlegacy.magnetRing2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	public void curioTick(String identifier, int index, LivingEntity living, ItemStack stack) {
		if ((invertShift.getValue() ? !living.isShiftKeyDown() : living.isShiftKeyDown()) || !(living instanceof Player))
			return;

		double x = living.getX();
		double y = living.getY() + 0.75;
		double z = living.getZ();

		List<ItemEntity> items = living.level.getEntitiesOfClass(ItemEntity.class, new AABB(x - range.getValue(), y - range.getValue(), z - range.getValue(), x + range.getValue(), y + range.getValue(), z + range.getValue()));
		int pulled = 0;
		for (ItemEntity item : items)
			if (this.canPullItem(item)) {
				if (pulled > 200) {
					break;
				}

				if (!SuperpositionHandler.canPickStack((Player) living, item.getItem())) {
					continue;
				}

				SuperpositionHandler.setEntityMotionFromVector(item, new Vector3(x, y, z), 0.45F);
				item.setNoPickUpDelay();

				//for (int counter = 0; counter <= 2; counter++)
				//	living.world.addParticle(ParticleTypes.WITCH, item.getPosX(), item.getPosY() - item.getYOffset() + item.getHeight() / 2, item.getPosZ(), (Math.random() - 0.5D) * 0.1D, (Math.random() - 0.5D) * 0.1D, (Math.random() - 0.5D) * 0.1D);
				pulled++;
			}

	}

	protected boolean canPullItem(ItemEntity item) {
		ItemStack stack = item.getItem();
		if (!item.isAlive() || stack.isEmpty() || item.getPersistentData().getBoolean("PreventRemoteMovement"))
			return false;

		return true;
	}

}
