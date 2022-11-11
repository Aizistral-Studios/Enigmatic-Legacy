package com.aizistral.enigmaticlegacy.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.aizistral.enigmaticlegacy.api.generic.SubscribeConfig;
import com.aizistral.enigmaticlegacy.blocks.BlockEndAnchor;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.GenericBlockItem;
import com.aizistral.enigmaticlegacy.objects.AnchorSearchResult;
import com.aizistral.enigmaticlegacy.registries.EnigmaticBlocks;
import com.aizistral.omniconfig.Configuration;
import com.aizistral.omniconfig.wrappers.Omniconfig;
import com.aizistral.omniconfig.wrappers.OmniconfigWrapper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EndAnchor extends GenericBlockItem {
	public static Omniconfig.BooleanParameter endExclusiveMode;
	public static Omniconfig.PerhapsParameter chargeSaveChance;
	public static final List<ResourceLocation> DIMENSION_BLACKLIST = new ArrayList<>();

	@SubscribeConfig
	public static void onConfig(OmniconfigWrapper builder) {
		builder.pushPrefix("EndAnchor");

		if (builder.config.getSidedType() != Configuration.SidedConfigType.CLIENT) {
			endExclusiveMode = builder
					.comment("If true, Dimensional Anchor will only work in The End.")
					.sync()
					.getBoolean("EndExclusiveMode", false);

			chargeSaveChance = builder
					.comment("Chance that Dimensional Anchor will not spend a charge upon player respawn")
					.getPerhaps("ChargeSaveChance", 35);
		}

		builder.popPrefix();

		DIMENSION_BLACKLIST.clear();
		String[] blacklist = builder.config.getStringList("EndAnchorDimensionBlacklist", builder.getCurrentCategory(), new String[0],
				"List of specific dimensions in which Dimensional Anchor will not work. If you want it to only work in The End - use EndExclusiveMode instead."
						+ " Examples: minecraft:overworld, minecraft:the_nether");

		Arrays.stream(blacklist).forEach(entry -> DIMENSION_BLACKLIST.add(new ResourceLocation(entry)));
	}

	public EndAnchor() {
		super(EnigmaticBlocks.END_ANCHOR, getDefaultProperties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		if (Screen.hasShiftDown()) {
			if (endExclusiveMode.getValue()) {
				ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.endAnchor1_alt");
			} else if (!DIMENSION_BLACKLIST.isEmpty()) {
				ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.endAnchor1_alt2");
			} else {
				ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.endAnchor1");
			}
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.endAnchor2");
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.endAnchor3", ChatFormatting.GOLD, chargeSaveChance + "%");
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.endAnchor4");
		} else {
			ItemLoreHelper.addLocalizedString(tooltip, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	public static AnchorSearchResult findAndUseEndAnchor(ServerLevel level, BlockPos pos, float angle, boolean forced, boolean keep) {
		AnchorSearchResult result = findEndAnchor(level, pos, angle, forced, keep);
		if (result.found()) {
			boolean usedCharge = false;

			if (!keep && result.location().isPresent()) {
				usedCharge = useEndAnchor(level, pos, level.getBlockState(pos));
			}

			return new AnchorSearchResult(result.location(), result.found(), usedCharge);
		} else
			return result;
	}

	public static AnchorSearchResult findEndAnchor(ServerLevel level, BlockPos pos, float angle, boolean forced, boolean keep) {
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		if (block instanceof BlockEndAnchor && state.getValue(BlockEndAnchor.CHARGE) > 0 && BlockEndAnchor.canSetSpawn(level)) {
			Optional<Vec3> optional = BlockEndAnchor.findStandUpPosition(EntityType.PLAYER, level, pos);
			return new AnchorSearchResult(optional, true, false);
		}

		return new AnchorSearchResult(Optional.empty(), false, false);
	}

	public static boolean useEndAnchor(ServerLevel level, BlockPos pos, BlockState state) {
		if (!EndAnchor.chargeSaveChance.getValue().roll()) {
			level.setBlock(pos, state.setValue(RespawnAnchorBlock.CHARGE, Integer.valueOf(state.getValue(RespawnAnchorBlock.CHARGE) - 1)), 3);
			return true;
		} else
			return false;
	}

}