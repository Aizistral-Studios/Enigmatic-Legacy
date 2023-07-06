package com.aizistral.enigmaticlegacy.mixin.apotheosis;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shadows.apotheosis.Apoth;
import shadows.apotheosis.ench.asm.EnchHooks;
import shadows.apotheosis.ench.table.ApothEnchantContainer;
import shadows.apotheosis.ench.table.IEnchantableItem;

import java.util.List;
import java.util.Map;

/**
 * Priority is set higher to run after other modifications (e.g. changes to the result of getEnchantmentList)<br>
 * (<a href="https://github.com/Daripher/Passive-Skill-Tree">Example Mod</a>)
 */
@Mixin(value = ApothEnchantContainer.class, priority = 1500, remap = false)
public class MixinApothEnchantContainer extends EnchantmentMenu {
    public MixinApothEnchantContainer(int containerID, final Inventory playerInventory) {
        super(containerID, playerInventory);
    }

    @Unique
    private ItemStack enigmaticLegacy$copyBeforeEnchanted;

    @Unique
    private List<EnchantmentInstance> enigmaticLegacy$storedEnchantmentList;

    @Inject(method = "lambda$clickMenuButton$0", at = @At(value = "HEAD"))
    public void storeBeforeEnchant(ItemStack toEnchant, int id, final Player player, int cost, final ItemStack lapis, int level, final Level world, final BlockPos pos, final CallbackInfo ci) {
        enigmaticLegacy$copyBeforeEnchanted = toEnchant.copy();
    }

    @ModifyVariable(method = "lambda$clickMenuButton$0", at = @At(value = "STORE"), name = "list")
    public List<EnchantmentInstance> storeListOfEnchants(final List<EnchantmentInstance> list) {
        enigmaticLegacy$storedEnchantmentList = list;
        return list;
    }

    @Inject(method = "lambda$clickMenuButton$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/ResourceLocation;)V", shift = At.Shift.BEFORE))
    public void handleEnchanterPearl(final ItemStack toEnchant, int id, final Player player, int cost, final ItemStack lapis, int level, final Level world, final BlockPos pos, final CallbackInfo ci) {
        if (EnigmaticItems.ENCHANTER_PEARL.isPresent(player)) {
            if (enigmaticLegacy$storedEnchantmentList.get(0).enchantment == Apoth.Enchantments.INFUSION.get()) {
                // Ignore Infusion enchants
                return;
            }

            // Use Apotheosis enchantment method
            enigmaticLegacy$copyBeforeEnchanted = ((IEnchantableItem) enigmaticLegacy$copyBeforeEnchanted.getItem()).onEnchantment(enigmaticLegacy$copyBeforeEnchanted, enigmaticLegacy$storedEnchantmentList);

            // The enchantment result gets directly set in the `enchantSlots` not in the ItemStack
            ItemStack enchantedItem = this.enchantSlots.getItem(0);
            enchantedItem = enigmaticLegacy$mergeEnchantments(enchantedItem, enigmaticLegacy$copyBeforeEnchanted, false, false);
            enchantedItem = SuperpositionHandler.maybeApplyEternalBinding(enchantedItem);

            enchantSlots.setItem(0, enchantedItem);
        }
    }

    /** Enchanter Pearl disables lapis cost (Ignore the "There are no possible signatures for this injector") */
    @ModifyVariable(method = "clickMenuButton", at = @At(value = "STORE"), name = "lapis")
    public ItemStack fakeLapis(final ItemStack lapis, /* Method arguments: */ final Player player) {
        // Ignore the "There are no possible signatures for this injector" error
        if (EnigmaticItems.ENCHANTER_PEARL.isPresent(player)) {
            ItemStack fakeLapis = Items.LAPIS_LAZULI.getDefaultInstance();
            fakeLapis.setCount(64);
            return fakeLapis;
        }

        return lapis;
    }

    /** Copied from {@link SuperpositionHandler#mergeEnchantments} to use {@link EnchHooks}, seemed simpler than handling class loading etc. */
    @Unique
    private ItemStack enigmaticLegacy$mergeEnchantments(final ItemStack input, final ItemStack mergeFrom, boolean overmerge, boolean onlyTreasure) {
        ItemStack returnedStack = input.copy();
        Map<Enchantment, Integer> inputEnchants = EnchantmentHelper.getEnchantments(returnedStack);
        Map<Enchantment, Integer> mergedEnchants = EnchantmentHelper.getEnchantments(mergeFrom);

        for(Enchantment mergedEnchant : mergedEnchants.keySet()) {
            if (mergedEnchant != null) {
                int inputEnchantLevel = inputEnchants.getOrDefault(mergedEnchant, 0);
                int mergedEnchantLevel = mergedEnchants.get(mergedEnchant);

                if (!overmerge) {
                    // +1 when the levels match, otherwise pick the higher one
                    mergedEnchantLevel = inputEnchantLevel == mergedEnchantLevel ? Math.min(mergedEnchantLevel + 1, EnchHooks.getMaxLevel(mergedEnchant)) : Math.max(mergedEnchantLevel, inputEnchantLevel);
                } else {
                    // Always add +1 if both items have the enchantment
                    mergedEnchantLevel = inputEnchantLevel > 0 ? Math.max(mergedEnchantLevel, inputEnchantLevel) + 1 : Math.max(mergedEnchantLevel, inputEnchantLevel);
                    mergedEnchantLevel = Math.min(mergedEnchantLevel, EnchHooks.getMaxLevel(mergedEnchant));
                }

                boolean compatible = mergedEnchant.canEnchant(input);
                if (input.getItem() instanceof EnchantedBookItem) {
                    compatible = true;
                }

                for(Enchantment originalEnchant : inputEnchants.keySet()) {
                    if (originalEnchant != mergedEnchant && !mergedEnchant.isCompatibleWith(originalEnchant)) {
                        compatible = false;
                    }
                }

                if (compatible) {
                    if (!onlyTreasure || EnchHooks.isTreasureOnly(mergedEnchant) || mergedEnchant.isCurse()) {
                        inputEnchants.put(mergedEnchant, mergedEnchantLevel);
                    }
                }
            }
        }

        EnchantmentHelper.setEnchantments(inputEnchants, returnedStack);
        return returnedStack;
    }
}
