package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.crafting.BindToPlayerRecipe.IBound;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBase;
import com.integral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.clients.PacketRecallParticles;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class WormholePotion extends ItemBase implements IBound {

	public WormholePotion() {
		super(ItemBase.getDefaultProperties().maxStackSize(1).rarity(Rarity.RARE));
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "wormhole_potion"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.wormholePotion1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.wormholePotion2");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		if (ItemNBTHelper.verifyExistance(stack, "BoundPlayer")) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.boundToPlayer", TextFormatting.DARK_RED, ItemNBTHelper.getString(stack, "BoundPlayer", "Herobrine"));
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (!(entityLiving instanceof PlayerEntity))
			return stack;

		PlayerEntity player = (PlayerEntity) entityLiving;

		if (player instanceof ServerPlayerEntity) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
		}

		PlayerEntity receiver = this.getBoundPlayer(worldIn, stack);

		if (!worldIn.isRemote && receiver != null) {

			Vector3d vec = receiver.getPositionVec();

			while (vec.distanceTo(receiver.getPositionVec()) < 1.0D)
				vec = receiver.getPositionVec().add((Item.random.nextDouble() - 0.5D) * 4D, 0, (Item.random.nextDouble() - 0.5D) * 4D);

			worldIn.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 128, player.world.func_234923_W_())), new PacketPortalParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 100, 1.25F, false));

			player.setPositionAndUpdate(vec.x, vec.y + 0.25, vec.z);
			worldIn.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));

			EnigmaticLegacy.packetInstance.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getPosX(), player.getPosY(), player.getPosZ(), 128, player.world.func_234923_W_())), new PacketRecallParticles(player.getPosX(), player.getPosY() + (player.getHeight() / 2), player.getPosZ(), 48, false));
		}

		if (!player.abilities.isCreativeMode) {

			stack.shrink(1);

			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));

		}

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
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		PlayerEntity receiver = this.getBoundPlayer(world, stack);

		if (receiver != null/* && receiver != player */) {
			player.setActiveHand(hand);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}

		return new ActionResult<>(ActionResultType.FAIL, stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

}
