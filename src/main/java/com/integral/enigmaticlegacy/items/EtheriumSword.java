package com.integral.enigmaticlegacy.items;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.api.items.IMultiblockMiningTool;
import com.integral.enigmaticlegacy.api.items.IPerhaps;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.integral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.integral.enigmaticlegacy.objects.CooldownMap;
import com.integral.enigmaticlegacy.objects.Vector3;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.item.SwordItem;
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

public class EtheriumSword extends SwordItem implements IPerhaps, IMultiblockMiningTool {
	public CooldownMap etheriumSwordCooldowns = new CooldownMap();

	public EtheriumSword() {
		super(EnigmaticMaterials.ETHERIUM, 6, -2.6F, ItemBaseTool.getDefaultProperties().rarity(Rarity.RARE).isBurnable());
		this.setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_sword"));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.etheriumSword4", TextFormatting.GOLD, ConfigHandler.ETHERIUM_SWORD_COOLDOWN.getValue() / 20F);
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
	public boolean isForMortals() {
		return ConfigHandler.ETHERIUM_TOOLS_ENABLED.getValue();
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (hand == Hand.OFF_HAND)
			return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));

		if (player.isCrouching()) {
			this.toggleAreaEffects(player, stack);

			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		} else if (!player.world.isRemote) {
			if (!player.getCooldownTracker().hasCooldown(this) && this.areaEffectsEnabled(player, stack)) {
				Vector3 look = new Vector3(player.getLookVec());
				Vector3 dir = look.multiply(1D);

				this.knockBack(player, 1.0F, dir.x, dir.z);
				world.playSound(null, player.getPosition(), SoundEvents.ENTITY_SKELETON_SHOOT, SoundCategory.PLAYERS, 1.0F, (float) (0.6F + (Math.random() * 0.1D)));

				player.getCooldownTracker().setCooldown(this, ConfigHandler.ETHERIUM_SWORD_COOLDOWN.getValue());

				player.setActiveHand(hand);
				return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
			}
		}

		player.setActiveHand(hand);
		return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (context.getPlayer().isCrouching())
			return this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType();
		else
			return super.onItemUse(context);
	}

	public void knockBack(PlayerEntity entityIn, float strength, double xRatio, double zRatio) {
		entityIn.isAirBorne = true;
		Vector3d vec3d = new Vector3d(0D, 0D, 0D);
		Vector3d vec3d1 = (new Vector3d(xRatio, 0.0D, zRatio)).normalize().scale(strength);

		EnigmaticLegacy.packetInstance.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) entityIn), new PacketPlayerMotion(vec3d.x / 2.0D - vec3d1.x, entityIn.isOnGround() ? Math.min(0.4D, vec3d.y / 2.0D + strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z));
		entityIn.setMotion(vec3d.x / 2.0D - vec3d1.x, entityIn.isOnGround() ? Math.min(0.4D, vec3d.y / 2.0D + strength) : vec3d.y, vec3d.z / 2.0D - vec3d1.z);

	}

}
