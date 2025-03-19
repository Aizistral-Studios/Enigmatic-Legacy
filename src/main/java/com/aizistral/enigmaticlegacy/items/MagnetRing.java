package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.objects.TransientPlayerData;
import com.aizistral.enigmaticlegacy.objects.Vector3;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

public class MagnetRing extends ItemBaseCurio {
	public static final String disabledMagnetTag = "DisabledMagnetEffects";
	public static Omniconfig.IntParameter range;
	public static Omniconfig.BooleanParameter invertShift;
	public static Omniconfig.BooleanParameter inventoryButtonEnabled;
	public static Omniconfig.IntParameter buttonOffsetX;
	public static Omniconfig.IntParameter buttonOffsetY;
	public static Omniconfig.IntParameter buttonOffsetXCreative;
	public static Omniconfig.IntParameter buttonOffsetYCreative;

	@SubscribeConfig(receiveClient = true)
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("MagnetRing");

		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			range = builder.comment("The radius in which Magnetic Ring will attract items.")
					.min(1)
					.max(256)
					.getInt("Range", 8);

			invertShift = builder.comment("Inverts the Shift behaviour of Magnetic Ring and Dislocation Ring.")
					.getBoolean("InvertShift", false);
		} else {
			inventoryButtonEnabled = builder
					.comment("Whether or not button for toggling magnet effects should be added to inventory GUI when player has Ring of Ender equipped.")
					.getBoolean("ButtonEnabled", true);

			buttonOffsetX = builder
					.comment("Allows to set offset for Magnet Effects button on X axis.")
					.minMax(32768)
					.getInt("ButtonOffsetX", 0);

			buttonOffsetY = builder
					.comment("Allows to set offset for Magnet Effects button on Y axis.")
					.minMax(32768)
					.getInt("ButtonOffsetY", 0);

			buttonOffsetXCreative = builder
					.comment("Allows to set offset for Magnet Effects button on X axis, for creative inventory specifically.")
					.minMax(32768)
					.getInt("ButtonOffsetXCreative", 0);

			buttonOffsetYCreative = builder
					.comment("Allows to set offset for Magnet Effects button on Y axis, for creative inventory specifically.")
					.minMax(32768)
					.getInt("ButtonOffsetYCreative", 0);

		}

		builder.popPrefix();
	}

	public MagnetRing() {
		this(ItemBaseCurio.getDefaultProperties().rarity(Rarity.RARE));
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
	public void curioTick(SlotContext context, ItemStack stack) {
		LivingEntity living = context.entity();

		if ((invertShift.getValue() ? !living.isShiftKeyDown() : living.isShiftKeyDown()) || !(living instanceof Player))
			return;

		if (this.hasMagnetEffectsDisabled((Player) living))
			return;

		double x = living.getX();
		double y = living.getY() + 0.75;
		double z = living.getZ();

		List<ItemEntity> items = living.level().getEntitiesOfClass(ItemEntity.class, new AABB(x - range.getValue(), y - range.getValue(), z - range.getValue(), x + range.getValue(), y + range.getValue(), z + range.getValue()));
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

	@Override
	public boolean canEquip(SlotContext context, ItemStack stack) {
		return super.canEquip(context, stack) && !SuperpositionHandler.hasCurio(context.entity(), EnigmaticItems.SUPER_MAGNET_RING);
	}

	public boolean hasMagnetEffectsDisabled(Player player) {
		return TransientPlayerData.get(player).getDisabledMagnetRingEffects();
	}

	protected boolean canPullItem(ItemEntity item) {
		ItemStack stack = item.getItem();
		if (!item.isAlive() || stack.isEmpty() || item.getPersistentData().getBoolean("PreventRemoteMovement"))
			return false;

		return true;
	}

}
