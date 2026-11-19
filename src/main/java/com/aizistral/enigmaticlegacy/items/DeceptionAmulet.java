package com.aizistral.enigmaticlegacy.items;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.items.ICursed;
import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.helpers.ItemLoreHelper;
import com.aizistral.enigmaticlegacy.items.generic.ItemBase;
import com.aizistral.enigmaticlegacy.items.generic.ItemBaseCurio;
import com.aizistral.enigmaticlegacy.packets.clients.PacketPortalParticles;
import com.aizistral.enigmaticlegacy.registries.EnigmaticEffects;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Team;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class DeceptionAmulet extends ItemBaseCurio implements ICursed {

	private static final int EFFECT_DURATION = 5 * 60 * 20;
	private static final int COOLDOWN = 10 * 60 * 20;

	private static final float RESTORED_HEALTH_PERCENT = 0.15F;

	public DeceptionAmulet() {
		super(ItemBase.getDefaultProperties().rarity(Rarity.EPIC).stacksTo(1).fireResistant().durability(2));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> list, TooltipFlag flagIn) {
		if (Screen.hasShiftDown()) {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet1");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet2");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet3");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet4");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet5");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet6");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet7");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet8");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet9");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet10");
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.deceptionAmulet11");
		} else {
			ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.holdShift");
		}

		ItemLoreHelper.addLocalizedString(list, "tooltip.enigmaticlegacy.void");
		ItemLoreHelper.indicateCursedOnesOnly(list);
	}

	public static boolean tryActivate(ServerPlayer player, ItemStack amulet) {
		ItemCooldowns cooldowns = player.getCooldowns();
		boolean activated = SuperpositionHandler.isTheCursedOne(player) && !cooldowns.isOnCooldown(EnigmaticItems.DECEPTION_AMULET);

		if (activated) {
			activate(player, amulet);
			cooldowns.addCooldown(EnigmaticItems.DECEPTION_AMULET, COOLDOWN);
		}

		return activated;
	}

	private static void activate(ServerPlayer player, ItemStack amulet) {
		player.setHealth(player.getMaxHealth() * RESTORED_HEALTH_PERCENT);

		player.addEffect(new MobEffectInstance(
				EnigmaticEffects.IMPERCEPTIBLE,
				EFFECT_DURATION,
				0,
				false,
				false,
				true
		));

		EnigmaticLegacy.packetInstance.send(
				PacketDistributor.PLAYER.with(() -> player),
				new PacketPortalParticles(
						player.getX(),
						player.getY() + (player.getBbHeight() / 2.0D),
						player.getZ(),
						48,
						0.75D,
						false
				)
		);

		ForgeRegistries.SOUND_EVENTS.getHolder(SoundEvents.WITHER_HURT).ifPresent(sound ->
				player.connection.send(new ClientboundSoundPacket(
						sound,
						SoundSource.PLAYERS,
						player.getX(),
						player.getY(),
						player.getZ(),
						0.5F,
						0.7F,
						player.level().getRandom().nextLong()
				))
		);


//		EnigmaticLegacy.packetInstance.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
//				new PacketSpawnFakeCorpse(player)
//		);

		sendFakeDeathMessage(player);

		CuriosApi.getCuriosHelper().findFirstCurio(player, EnigmaticItems.DECEPTION_AMULET).ifPresent(result ->
				amulet.hurtAndBreak(1, player, p ->
						CuriosApi.getCuriosHelper().onBrokenCurio(result.slotContext())
				)
		);
	}


	private static void sendFakeDeathMessage(ServerPlayer player) {
		MinecraftServer server = player.getServer();
		if (server == null) return;

		Level level = player.level();

		boolean flag = level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);

		// Copy-paste from the mojunk code (with slight changes) because they
		// didn't separate it with other method and just slapped it in to ServerPlayer#die method
		if (!flag) return;
		PlayerList playerList = server.getPlayerList();

		Component component = player.getCombatTracker().getDeathMessage();

		String s = component.getString(256);
		Component component1 = Component.translatable("death.attack.message_too_long", Component.literal(s).withStyle(ChatFormatting.YELLOW));
		Component component2 = Component.translatable("death.attack.even_more_magic", player.getDisplayName()).withStyle((p_143420_) -> {
			return p_143420_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component1));
		});

		Team team = player.getTeam();
		if (team != null && team.getDeathMessageVisibility() != Team.Visibility.ALWAYS) {
			if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
				playerList.broadcastSystemToTeam(player, component);
			} else if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
				playerList.broadcastSystemToAllExceptTeam(player, component);
			}
		} else {
			playerList.broadcastSystemMessage(component, false);
		}
	}
}
