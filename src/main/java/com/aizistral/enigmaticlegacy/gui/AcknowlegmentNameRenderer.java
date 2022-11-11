package com.aizistral.enigmaticlegacy.gui;

import java.util.function.Supplier;

import com.aizistral.enigmaticlegacy.items.TheAcknowledgment;
import com.aizistral.enigmaticlegacy.items.TheInfinitum;
import com.aizistral.enigmaticlegacy.items.TheTwist;
import com.aizistral.enigmaticlegacy.registry.EnigmaticItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;

/**
 * Such is the price, I guess.
 * @author Aizistral
 */

@OnlyIn(Dist.CLIENT)
public class AcknowlegmentNameRenderer {
	private final Minecraft minecraft = Minecraft.getInstance();
	private final GuiBook gui;
	private final Book book;
	private final Supplier<Font> font;
	private final Component name;

	public AcknowlegmentNameRenderer(GuiBook gui, Supplier<Font> font) {
		this.gui = gui;
		this.book = gui.book;
		this.font = font;

		Component customName = this.book.getBookItem().getHoverName();
		ItemStack stack = this.minecraft.player.getOffhandItem();

		if (stack == null || !(stack.getItem() instanceof TheAcknowledgment)) {
			stack = this.minecraft.player.getMainHandItem();
		}

		if (stack != null) {
			if (stack.getItem() instanceof TheTwist) {
				customName = EnigmaticItems.theTwist.getName(stack);
			} else if (stack.getItem() instanceof TheInfinitum) {
				customName = EnigmaticItems.theInfinitum.getName(stack);
			}
		}

		this.name = customName;
	}

	public void drawHeader(PoseStack ms) {
		RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
		GuiBook.drawFromTexture(ms, this.gui.book, -8, 12, 0, 180, 140, 31);

		int color = this.book.nameplateColor;
		this.font.get().draw(ms, this.name, 13, 16, color);
		Component toDraw = this.book.getSubtitle().withStyle(this.book.getFontStyle());
		this.font.get().draw(ms, toDraw, 24, 24, color);
	}

}
