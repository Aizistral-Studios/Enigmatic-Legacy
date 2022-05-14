package com.integral.enigmaticlegacy.client;

import java.time.Clock;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class QuoteHandler {
	public static final QuoteHandler INSTANCE = new QuoteHandler();
	private Quote currentQuote = null;
	private long startedPlaying = -1;

	private QuoteHandler() {
		// NO-OP
	}

	private double getPlayTime() {
		long millis = System.currentTimeMillis() - this.startedPlaying;
		return ((double)millis) / 1000;
	}

	public void playQuote(Quote quote) {
		if (this.currentQuote == null) {
			this.currentQuote = quote;

			SimpleSoundInstance instance = new SimpleSoundInstance(quote.getSound().getLocation(),
					SoundSource.MASTER, 0.5F, 1, false, 0, SoundInstance.Attenuation.NONE, 0, 0, 0, true);

			Minecraft.getInstance().getSoundManager().play(instance);

			this.startedPlaying = System.currentTimeMillis();
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onOverlayRender(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;

		if (Minecraft.getInstance().screen != null || this.currentQuote == null)
			return;

		this.drawQuote(event.getMatrixStack(), event.getWindow());
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onOverlayRender(ScreenEvent.DrawScreenEvent.Post event) {
		if (this.currentQuote != null) {
			this.drawQuote(event.getPoseStack(), Minecraft.getInstance().getWindow());
			Minecraft.getInstance().getSoundManager().resume();
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void drawQuote(PoseStack stack, Window window) {
		if (this.currentQuote.getSubtitles().getDuration() - this.getPlayTime() <= 0.1) {
			this.currentQuote = null;
			this.startedPlaying = -1;
			return;
		}

		Subtitles subtitles = this.currentQuote.getSubtitles();

		Font font = Minecraft.getInstance().font;
		String[] text = SuperpositionHandler.wrapString(subtitles.getLine(this.getPlayTime()), font, 260);

		int alphaMod = 0xFF;

		if (this.getPlayTime() < 0.5) {
			alphaMod *= this.getPlayTime() / 0.5;
		} else if (this.currentQuote.getSubtitles().getDuration() - this.getPlayTime() < 0.5) {
			alphaMod *= (this.currentQuote.getSubtitles().getDuration() - this.getPlayTime()) / 0.5;
		}

		if (alphaMod < 0) {
			alphaMod = 0xFF;
		}

		int width = window.getGuiScaledWidth() / 2 - SuperpositionHandler.greatestWidth(font, text) / 2;
		int height = window.getGuiScaledHeight() - 70 - ((font.lineHeight + 2) * (text.length - 1));

		stack.pushPose();
		stack.scale(1F, 1F, 1F);

		int fromX = width, fromY = height, toX = fromX + SuperpositionHandler.greatestWidth(font, text),
				toY = fromY + (font.lineHeight * text.length) + 2 * text.length - 1;
		int color1 = 0x000000 | (((int)(alphaMod * 0.266)) << 24);
		int color2 = ChatFormatting.YELLOW.getColor() | ((alphaMod) << 24);

		GuiUtils.drawGradientRect(stack.last().pose(), 0, fromX - 4, fromY - 4, toX + 4, toY + 4, color1, color1);
		GuiUtils.drawGradientRect(stack.last().pose(), 0, fromX - 6, fromY - 6, toX + 6, toY + 6, color1, color1);
		GuiUtils.drawGradientRect(stack.last().pose(), 0, fromX - 8, fromY - 8, toX + 8, toY + 8, color1, color1);

		int counter = 0;
		for (String line : text) {
			font.drawShadow(stack, line, window.getGuiScaledWidth() / 2 - font.width(line) / 2, height + (counter * (font.lineHeight + 2)), color2, true);
			counter++;
		}

		stack.popPose();
	}


}
