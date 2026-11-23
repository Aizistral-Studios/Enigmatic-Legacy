package com.aizistral.enigmaticlegacy.mixin;

import com.aizistral.enigmaticlegacy.registries.EnigmaticPotions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class MixinPotionItem extends MixinItem {

    // Magic <]:)
    // https://wiki.fabricmc.net/tutorial:mixinheritance
    @Override
    protected void isFoil(ItemStack pStack, CallbackInfoReturnable<Boolean> cir) {
        final boolean isUltimate = EnigmaticPotions.isUltimate(PotionUtils.getPotion(pStack));
        if (isUltimate) cir.setReturnValue(true);
    }
}
