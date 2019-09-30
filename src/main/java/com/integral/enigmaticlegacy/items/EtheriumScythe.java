package com.integral.enigmaticlegacy.items;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.AOEMiningHelper;
import com.integral.enigmaticlegacy.helpers.IPerhaps;
import com.integral.enigmaticlegacy.helpers.LoreHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EtheriumScythe extends SwordItem implements IPerhaps {
	
 public static Properties integratedProperties = new Item.Properties();
 protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.getDefaultState(), Blocks.GRASS_PATH, Blocks.FARMLAND.getDefaultState(), Blocks.DIRT, Blocks.FARMLAND.getDefaultState(), Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState()));
 public Set<Material> effectiveMaterials;
 private final float speed;
 private final float attackDamage;
 
 public EtheriumScythe(Properties properties, IItemTier tier, float attackSpeedIn, int attackDamageIn) {
		super(tier, attackDamageIn, attackSpeedIn, properties);
		
		this.speed = attackSpeedIn;
		this.attackDamage = attackDamageIn;
		
		effectiveMaterials = Sets.newHashSet();
		effectiveMaterials.add(Material.LEAVES);
		effectiveMaterials.add(Material.BAMBOO);
		effectiveMaterials.add(Material.BAMBOO_SAPLING);
		effectiveMaterials.add(Material.SEA_GRASS);
		effectiveMaterials.add(Material.PLANTS);
		effectiveMaterials.add(Material.OCEAN_PLANT);
		effectiveMaterials.add(Material.TALL_PLANTS);
		effectiveMaterials.add(Material.CACTUS);
	}
 
 public static Properties setupIntegratedProperties() {
	 integratedProperties.group(EnigmaticLegacy.enigmaticTab);
	 integratedProperties.maxStackSize(1);
	 integratedProperties.rarity(Rarity.RARE);
	 
	 return integratedProperties;
 
 }
 
 @OnlyIn(Dist.CLIENT)
 public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
	 if (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() == -1)
		 return;
	 
	 if(ControlsScreen.hasShiftDown()) {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe1", ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue());
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe2", ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue());
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumScythe3");
	 } else {
		 LoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
	 }
 }
 
 @Override
 public boolean isForMortals() {
	 return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
 }
 
 public static boolean attemptTransformLand(World world, PlayerEntity player, BlockPos blockpos, ItemStack item, Hand hand) {
     
     if (world.isAirBlock(blockpos.up())) {
        BlockState blockstate = HOE_LOOKUP.get(world.getBlockState(blockpos).getBlock());
        if (blockstate != null) {
           world.playSound(player, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
           if (!world.isRemote) {
              world.setBlockState(blockpos, blockstate, 11);
              if (player != null) {
                 item.damageItem(1, player, (p_220043_1_) -> {
                    p_220043_1_.sendBreakAnimation(hand);
                 });
              }
           }

           return true;
        }
     }

     return false;
  }
 
 public ActionResultType onItemUse(ItemUseContext context) {
	 ActionResultType type = Items.DIAMOND_HOE.onItemUse(context);
	 
	 if (context.isPlacerSneaking())
		 return type;
	 
	 int supRad = (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue()-1)/2;
	 
	 if (type == ActionResultType.SUCCESS) 
	 for (int x = -supRad; x <= supRad; x++) {
		 for (int z = -supRad; z <= supRad; z++) {
			 if (x == 0 & z == 0)
				 continue;
			 
			 attemptTransformLand(context.getWorld(), context.getPlayer(), context.getPos().add(x, 0, z), context.getItem(), context.getHand());
		 }
	 }

	 
	 return type;
 }
 
 @Override
 public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {

     if (entityLiving instanceof PlayerEntity && !entityLiving.isSneaking() && this.effectiveMaterials.contains(state.getMaterial()) && !world.isRemote && ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue() != -1) {
    	 Direction face = Direction.UP;
    	 
         AOEMiningHelper.harvestCube(world, (PlayerEntity)entityLiving, face, pos.add(0, (ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue()-1)/2, 0), effectiveMaterials, ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue(), ConfigHandler.ETHERIUM_SCYTHE_VOLUME.getValue(), false, pos, stack, (objPos, objState) -> { stack.damageItem(1, entityLiving, p -> p.sendBreakAnimation(MobEntity.getSlotForItemStack(stack))); });
     }
     
     return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
 }
 
 @Override
 public float getDestroySpeed(ItemStack stack, BlockState state) {
     Material material = state.getMaterial();
     return !this.effectiveMaterials.contains(material) ? super.getDestroySpeed(stack, state) : this.getTier().getEfficiency();
  }
  
}



