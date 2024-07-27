package com.aizistral.enigmaticlegacy.items;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.helpers.ItemNBTHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseTool;
import com.aizistral.enigmaticlegacy.registries.EnigmaticSounds;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TheJudgement extends ItemBase {
	public static final float ATTACK_DAMAGE = Float.POSITIVE_INFINITY;
	public static final double ATTACK_RADIUS = 64D;
	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public TheJudgement() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).fireResistant());

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier",
				ATTACK_DAMAGE - 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier",
				28, AttributeModifier.Operation.ADDITION));
		this.defaultModifiers = builder.build();
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (target.level().isClientSide)
			return false;

		Level level = target.level();
		boolean player = attacker instanceof Player;
		AABB box = target.getBoundingBox().inflate(64);
		DamageSource source = player ? level.damageSources().playerAttack((Player) attacker)
				: level.damageSources().mobAttack(attacker);

		var targets = level.getEntitiesOfClass(LivingEntity.class, box, e -> e != attacker && e != target
				&& e.distanceToSqr(target) < ATTACK_RADIUS * ATTACK_RADIUS);
		targets.forEach(t -> t.hurt(source, ATTACK_DAMAGE));

		if (this.noDrops(stack)) {
			var drops = level.getEntitiesOfClass(Entity.class, box, e -> (e instanceof ItemEntity
					|| e instanceof ExperienceOrb) && e.distanceToSqr(target) < ATTACK_RADIUS * ATTACK_RADIUS);
			drops.forEach(e -> e.hurt(source, ATTACK_DAMAGE));
		}

		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		boolean prevNoDrops = this.noDrops(stack);

		this.setNoDrops(stack, !prevNoDrops);
		world.playSound(null, player.blockPosition(), prevNoDrops ? EnigmaticSounds.CHARGED_OFF
				: EnigmaticSounds.CHARGED_ON, SoundSource.PLAYERS, 1F, 0.8F + random.nextFloat() * 0.2F);
		player.swing(hand);

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override // TODO Do this for other right-clickables
	public InteractionResult useOn(UseOnContext context) {
		return this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		return this.noDrops(pStack);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			int mode = this.noDrops(stack) ? 1 : 0;

			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theJudgement1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theJudgement2",
					ChatFormatting.GOLD, (int) ATTACK_RADIUS);
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theJudgement3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theJudgement4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theJudgement5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.theJudgement6",
					null, Component.translatable("tooltip.enigmaticlegacy.theJudgementMode" + mode));
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}
	}

	private boolean noDrops(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, "noDrops", false);
	}

	private void setNoDrops(ItemStack stack, boolean value) {
		ItemNBTHelper.setBoolean(stack, "noDrops", value);
	}

}
