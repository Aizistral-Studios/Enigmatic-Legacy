package com.aizistral.enigmaticlegacy.gui;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;

public class GUIUtils {
	public static final int DEFAULT_BACKGROUND_COLOR = 0xF0100010;
	public static final int DEFAULT_BORDER_COLOR_START = 0x505000FF;
	public static final int DEFAULT_BORDER_COLOR_END = (DEFAULT_BORDER_COLOR_START & 0xFEFEFE) >> 1 | DEFAULT_BORDER_COLOR_START & 0xFF000000;

	public static void drawGradientRect(Matrix4f mat, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor) {
		float startAlpha = (startColor >> 24 & 255) / 255.0F;
		float startRed = (startColor >> 16 & 255) / 255.0F;
		float startGreen = (startColor >> 8 & 255) / 255.0F;
		float startBlue = (startColor & 255) / 255.0F;
		float endAlpha = (endColor >> 24 & 255) / 255.0F;
		float endRed = (endColor >> 16 & 255) / 255.0F;
		float endGreen = (endColor >> 8 & 255) / 255.0F;
		float endBlue = (endColor & 255) / 255.0F;

		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder buffer = tessellator.getBuilder();
		buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		buffer.vertex(mat, right, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
		buffer.vertex(mat, left, top, zLevel).color(startRed, startGreen, startBlue, startAlpha).endVertex();
		buffer.vertex(mat, left, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
		buffer.vertex(mat, right, bottom, zLevel).color(endRed, endGreen, endBlue, endAlpha).endVertex();
		tessellator.end();

		RenderSystem.disableBlend();
	}

}
