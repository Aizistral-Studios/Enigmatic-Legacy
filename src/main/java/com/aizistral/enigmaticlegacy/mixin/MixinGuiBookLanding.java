package com.aizistral.enigmaticlegacy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.gui.AcknowlegmentNameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookLanding;

@Mixin(GuiBookLanding.class)
public abstract class MixinGuiBookLanding extends GuiBook {
	@Shadow(remap = false)
	BookTextRenderer text;
	AcknowlegmentNameRenderer renderer;

	public MixinGuiBookLanding() {
		super(null, null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("RETURN"), cancellable = false, require = 1, remap = true)
	private void onInit(CallbackInfo info) {
		if (EnigmaticLegacy.MODID.equals(this.book.id.getNamespace())) {
			this.renderer = new AcknowlegmentNameRenderer(this, () -> this.font);
		}
	}

	@Inject(method = "drawHeader", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
	private void onDrawHeader(GuiGraphics graphics, CallbackInfo info) {
		if (EnigmaticLegacy.MODID.equals(this.book.id.getNamespace())) {
			info.cancel();
			this.renderer.drawHeader(graphics);
		}
	}

}
