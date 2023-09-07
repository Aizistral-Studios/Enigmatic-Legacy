package com.aizistral.enigmaticlegacy.mixin;

import static net.minecraftforge.client.ForgeHooksClient.getTooltipFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.mojang.datafixers.util.Either;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * I am ashamed to do this, but Forge still leaves me no choice.
 *
 * @author Aizistral
 */

@SuppressWarnings("unused")
@Mixin(value = ForgeHooksClient.class, remap = false)
public class MixinForgeHooksClient {

	//@Inject(method = "gatherTooltipComponents(Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Ljava/util/Optional;IIILnet/minecraft/client/gui/Font;Lnet/minecraft/client/gui/Font;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
	private static void onGatherTooltipComponents(ItemStack stack, List<? extends FormattedText> textElements,
			Optional<TooltipComponent> itemComponent, int mouseX, int screenWidth, int screenHeight,
			Font forcedFont, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> info) {

		if (stack != null && ForgeRegistries.ITEMS.getKey(stack.getItem()).getNamespace().equals(EnigmaticLegacy.MODID)) {
			Font font = getTooltipFont(stack, fallbackFont);
			List<Either<FormattedText, TooltipComponent>> elements = textElements.stream().map((Function<FormattedText, Either<FormattedText, TooltipComponent>>) Either::left).collect(Collectors.toCollection(ArrayList::new));
			itemComponent.ifPresent(c -> elements.add(1, Either.right(c)));

			var event = new RenderTooltipEvent.GatherComponents(stack, screenWidth, screenHeight, elements, -1);
			MinecraftForge.EVENT_BUS.post(event);
			if (event.isCanceled()) {
				info.setReturnValue(List.of());
				return;
			}
			// text wrapping
			int tooltipTextWidth = event.getTooltipElements().stream().mapToInt(either -> either.map(font::width, component -> 0)).max().orElse(0);

			boolean needsWrap = false;

			int tooltipX = mouseX + 12;
			if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
				tooltipX = mouseX - 16 - tooltipTextWidth;
				if (tooltipX < 4) // if the tooltip doesn't fit on the screen
				{
					if (mouseX > screenWidth / 2) {
						tooltipTextWidth = mouseX - 12 - 8;
					} else {
						tooltipTextWidth = screenWidth - 16 - mouseX;
					}
					needsWrap = true;
				}
			}

			if (event.getMaxWidth() > 0 && tooltipTextWidth > event.getMaxWidth()) {
				tooltipTextWidth = event.getMaxWidth();
				needsWrap = true;
			}

			int tooltipTextWidthF = tooltipTextWidth;
			/*
			if (needsWrap) {
				info.setReturnValue(event.getTooltipElements().stream().flatMap(either -> either.map(text -> font.split(text, tooltipTextWidthF).stream().map(ClientTooltipComponent::create), component -> Stream.of(ClientTooltipComponent.create(component)))).toList());
				return;
			}
			 */
			info.setReturnValue(event.getTooltipElements().stream().map(either -> either.map(text -> ClientTooltipComponent.create(text instanceof Component ? ((Component) text).getVisualOrderText() : Language.getInstance().getVisualOrder(text)), ClientTooltipComponent::create)).toList());
			return;
		}
	}

}
