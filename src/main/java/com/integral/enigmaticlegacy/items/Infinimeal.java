package com.integral.enigmaticlegacy.items;

import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Infinimeal extends ItemBase implements IVanishable {

	public Infinimeal() {
		super(getDefaultProperties().maxStackSize(1).rarity(Rarity.UNCOMMON));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "infinimeal"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.infinimeal1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.infinimeal2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.infinimeal3");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onItemUse(ItemUseContext context) {
		ItemStack stack = context.getItem();
		int savedCount = stack.getCount();

		ActionResultType result = Items.BONE_MEAL.onItemUse(context);
		stack.setCount(savedCount);

		if (result == ActionResultType.PASS) {
			BlockPos pos = context.getPos();
			World world = context.getWorld();
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof CactusBlock || block instanceof SugarCaneBlock) {
				BlockPos topMostPos = this.findTopmostGrowable(world, pos, block, true);
				BlockState topMostState = world.getBlockState(topMostPos);

				if (topMostState.hasProperty(BlockStateProperties.AGE_0_15) && world.isAirBlock(topMostPos.up())) {
					int age = topMostState.get(BlockStateProperties.AGE_0_15);

					int plantHeight;
					for(plantHeight = 1; world.getBlockState(topMostPos.down(plantHeight)).isIn(block); ++plantHeight) {}

					if (plantHeight >= 3)
						return result;

					if (!world.isRemote) {
						world.playEvent(2005, pos, 0);
					}

					age += random.nextInt(20);
					world.setBlockState(topMostPos, topMostState.with(BlockStateProperties.AGE_0_15, Integer.valueOf(Math.min(age, 15))), 4);

					if (world instanceof ServerWorld) {
						block.randomTick(world.getBlockState(topMostPos), (ServerWorld)world, topMostPos, random);
					}

					return ActionResultType.func_233537_a_(world.isRemote);
				}
			} else if (block instanceof VineBlock) {
				if (!block.ticksRandomly(state))
					return result;

				if (world.isRemote) {
					EnigmaticLegacy.proxy.spawnBonemealParticles(world, pos, 0);
				}

				int cycles = 7+random.nextInt(7);

				if (world instanceof ServerWorld) {
					for (int i = 0; i <= cycles; i++) {
						block.randomTick(state, (ServerWorld)world, pos, random);
					}

					state.updateNeighbours(world, pos, 4);
				}

				return ActionResultType.func_233537_a_(world.isRemote);
			} else if (block instanceof NetherWartBlock) {
				if (!block.ticksRandomly(state))
					return result;

				if (!world.isRemote) {
					world.playEvent(2005, pos, 0);
				}

				int cycles = 1+random.nextInt(1);
				cycles*=11;

				if (world instanceof ServerWorld) {
					for (int i = 0; i <= cycles; i++) {
						block.randomTick(state, (ServerWorld)world, pos, random);
					}
				}

				return ActionResultType.func_233537_a_(world.isRemote);
			}
		}

		return result;
	}

	private BlockPos findTopmostGrowable(World world, BlockPos pos, Block block, boolean goUp) {
		BlockPos top = pos;

		while (true) {
			if (world.getBlockState(top) != null && world.getBlockState(top).getBlock() == block) {
				BlockPos nextUp = goUp ? top.up() : top.down();

				if (world.getBlockState(nextUp) == null || world.getBlockState(nextUp).getBlock() != block)
					return top;
				else {
					top = nextUp;
					continue;
				}
			} else
				return pos;
		}
	}

}
