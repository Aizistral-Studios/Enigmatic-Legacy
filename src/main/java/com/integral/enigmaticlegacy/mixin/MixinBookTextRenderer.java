package com.integral.enigmaticlegacy.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.config.OmniconfigHandler;

import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.common.base.PatchouliConfig.ConfigAccess;
import vazkii.patchouli.common.base.PatchouliConfig.TextOverflowMode;
import vazkii.patchouli.common.book.Book;

/**
 * Surgical precision, relentless perfection.
 * The rest of developers may not understand, but they will comply.
 * @author Aizistral
 */

@Mixin(value = BookTextRenderer.class, remap = false)
public class MixinBookTextRenderer {
	@Shadow @Final
	private Book book;

	@Redirect(method = "<init>(Lvazkii/patchouli/client/book/gui/GuiBook;Lnet/minecraft/network/chat/Component;IIIII)V", at = @At(
			target = "Lvazkii/patchouli/common/base/PatchouliConfig$ConfigAccess;overflowMode()Ljava/util/function/Supplier;",
			value = "INVOKE"), require = 1)
	private Supplier<TextOverflowMode> redirectOverflowMode(ConfigAccess access) {
		if (EnigmaticLegacy.MODID.equals(this.book.getModNamespace()))
			return OmniconfigHandler.acknowledgmentOverflowMode::getValue;
		else
			return access.overflowMode();
	}

}
