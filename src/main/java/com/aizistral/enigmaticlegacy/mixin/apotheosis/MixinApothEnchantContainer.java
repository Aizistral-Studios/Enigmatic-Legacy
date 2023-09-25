package com.aizistral.enigmaticlegacy.mixin.apotheosis;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
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
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Pseudo
@Mixin(targets = "shadows.apotheosis.ench.table.ApothEnchantmentMenu", priority = 1500) // Probably not needed anymore
public abstract class MixinApothEnchantContainer extends EnchantmentMenu {
    @Unique
    private ItemStack enigmaticLegacy$copyBeforeEnchanted;
    @Unique
    private List<EnchantmentInstance> enigmaticLegacy$storedEnchantmentList;

    @Unique
    private Method enigmaticLegacy$onEnchantment;
    @Unique
    private Method enigmaticLegacy$getMaxLevel;
    @Unique
    private Method enigmaticLegacy$isTreasureOnly;

    public MixinApothEnchantContainer(int containerID, final Inventory playerInventory) {
        super(containerID, playerInventory);
    }

    /**
     * {@link net.minecraft.world.Container#setItem(int, ItemStack)} seems to clear all information (enchantment stats, costs, etc.) from the instance - that's why we get the list of enchantments while that information is still available
     */
    @Inject(method = "lambda$clickMenuButton$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.BEFORE, ordinal = 1))
    public void prepareDoubleRoll(final ItemStack toEnchant, int id, final Player player, int cost, final ItemStack lapis, int level, final Level world, final BlockPos pos, final CallbackInfo ci) {
        if (EnigmaticItems.ENCHANTER_PEARL.isPresent(player)) {
            enigmaticLegacy$copyBeforeEnchanted = toEnchant.copy();

            int storedValue = enchantmentSeed.get();

            enchantmentSeed.set(random.nextInt());
            enigmaticLegacy$storedEnchantmentList = getEnchantmentList(enigmaticLegacy$copyBeforeEnchanted, id, level);
            /*
            Not sure if this is needed (gets overwritten by `player.getEnchantmentSeed()` shortly afterward anyway
            (List of enchantments has already been collected (using the same seed as the list of clues) at this point)
            */
            enchantmentSeed.set(storedValue);
        }
    }

    /**
     * Handle the double enchanting effect
     */
    @Inject(method = "lambda$clickMenuButton$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;awardStat(Lnet/minecraft/resources/ResourceLocation;)V", shift = At.Shift.BEFORE))
    public void handleEnchanterPearl(final ItemStack toEnchant, int id, final Player player, int cost, final ItemStack lapis, int level, final Level world, final BlockPos pos, final CallbackInfo ci) {
        if (EnigmaticItems.ENCHANTER_PEARL.isPresent(player)) {
            if (enigmaticLegacy$storedEnchantmentList == null || enigmaticLegacy$storedEnchantmentList.get(0).enchantment.getClass().getSimpleName().equals("InertEnchantment")) {
                // Ignore Infusion enchants
                return;
            }

            enigmaticLegacy$initMethods();

            try {
                // Use Apotheosis enchantment method
                enigmaticLegacy$copyBeforeEnchanted = (ItemStack) enigmaticLegacy$onEnchantment.invoke(enigmaticLegacy$copyBeforeEnchanted.getItem(), enigmaticLegacy$copyBeforeEnchanted, enigmaticLegacy$storedEnchantmentList);

                /*
                The enchantment result gets directly set in the `enchantSlots` not in the ItemStack
                If the modifications are done using `toEnchant` then it will cause enchanted books to be reverted back to being tomes (if they previously were tomes) when merging
                */
                ItemStack enchantedItem = enchantSlots.getItem(0);
                enchantedItem = enigmaticLegacy$mergeEnchantments(enchantedItem, enigmaticLegacy$copyBeforeEnchanted, false, false);
                enchantedItem = SuperpositionHandler.maybeApplyEternalBinding(enchantedItem);

                enchantSlots.setItem(0, enchantedItem);
            } catch (IllegalAccessException | InvocationTargetException e) {
                EnigmaticLegacy.LOGGER.error("Could not invoke `onEnchantment` for [" + enigmaticLegacy$copyBeforeEnchanted.getDisplayName() + "]", e);
            }
        }
    }

    /**
     * Without explicitly naming the class path here it will crash due to not finding a proper injection point
     */
    @ModifyVariable(method = "shadows.apotheosis.ench.table.ApothEnchantmentMenu.clickMenuButton(Lnet/minecraft/world/entity/player/Player;I)Z", at = @At(value = "STORE"), name = "lapis")
    public ItemStack fakeLapis(final ItemStack lapis, /* Method parameters: */ final Player player) {
        if (EnigmaticItems.ENCHANTER_PEARL.isPresent(player)) {
            ItemStack fakeLapis = Items.LAPIS_LAZULI.getDefaultInstance();
            fakeLapis.setCount(64);
            return fakeLapis;
        }

        return lapis;
    }

    /**
     * Copied from {@link SuperpositionHandler#mergeEnchantments(ItemStack, ItemStack, boolean, boolean)} to use {@link EnchHooks}, seemed simpler than handling class loading etc.
     */
    @Unique
    private ItemStack enigmaticLegacy$mergeEnchantments(final ItemStack input, final ItemStack mergeFrom, boolean overmerge, boolean onlyTreasure) throws InvocationTargetException, IllegalAccessException {
        ItemStack returnedStack = input.copy();
        Map<Enchantment, Integer> inputEnchants = EnchantmentHelper.getEnchantments(returnedStack);
        Map<Enchantment, Integer> mergedEnchants = EnchantmentHelper.getEnchantments(mergeFrom);

        for (Enchantment mergedEnchant : mergedEnchants.keySet()) {
            if (mergedEnchant != null) {
                int inputEnchantLevel = inputEnchants.getOrDefault(mergedEnchant, 0);
                int mergedEnchantLevel = mergedEnchants.get(mergedEnchant);

                if (!overmerge) {
                    // +1 when the levels match, otherwise pick the higher one
                    mergedEnchantLevel = inputEnchantLevel == mergedEnchantLevel ? Math.min(mergedEnchantLevel + 1, (Integer) enigmaticLegacy$getMaxLevel.invoke(mergedEnchant)) : Math.max(mergedEnchantLevel, inputEnchantLevel);
                } else {
                    // Always add +1 if both items have the enchantment
                    mergedEnchantLevel = inputEnchantLevel > 0 ? Math.max(mergedEnchantLevel, inputEnchantLevel) + 1 : Math.max(mergedEnchantLevel, inputEnchantLevel);
                    mergedEnchantLevel = Math.min(mergedEnchantLevel, (Integer) enigmaticLegacy$getMaxLevel.invoke(mergedEnchant));
                }

                boolean compatible = mergedEnchant.canEnchant(input);
                if (input.getItem() instanceof EnchantedBookItem) {
                    compatible = true;
                }

                for (Enchantment originalEnchant : inputEnchants.keySet()) {
                    if (originalEnchant != mergedEnchant && !mergedEnchant.isCompatibleWith(originalEnchant)) {
                        compatible = false;
                    }
                }

                if (compatible) {
                    if (!onlyTreasure || (Boolean) enigmaticLegacy$isTreasureOnly.invoke(mergedEnchant) || mergedEnchant.isCurse()) {
                        inputEnchants.put(mergedEnchant, mergedEnchantLevel);
                    }
                }
            }
        }

        EnchantmentHelper.setEnchantments(inputEnchants, returnedStack);
        return returnedStack;
    }

    /**
     * Method call to avoid <a href="https://github.com/SpongePowered/Mixin/issues/322">Cannot handle AASTORE opcode (0x53)</a>
     */
    @Unique
    private void enigmaticLegacy$initMethods() {
        if (enigmaticLegacy$onEnchantment == null || enigmaticLegacy$getMaxLevel == null || enigmaticLegacy$isTreasureOnly == null) {
            try {
                Class<?> enchantableItem = Class.forName("shadows.apotheosis.ench.table.IEnchantableItem");
                enigmaticLegacy$onEnchantment = ObfuscationReflectionHelper.findMethod(enchantableItem, "onEnchantment", ItemStack.class, List.class);

                Class<?> enchHooks = Class.forName("shadows.apotheosis.ench.asm.EnchHooks");
                enigmaticLegacy$getMaxLevel = ObfuscationReflectionHelper.findMethod(enchHooks, "getMaxLevel", Enchantment.class);
                enigmaticLegacy$isTreasureOnly = ObfuscationReflectionHelper.findMethod(enchHooks, "isTreasureOnly", Enchantment.class);
            } catch (ClassNotFoundException e) {
                EnigmaticLegacy.LOGGER.error("Could not initialize `EnchHooks` methods", e);

                enigmaticLegacy$getMaxLevel = ObfuscationReflectionHelper.findMethod(Enchantment.class, "m_6586_");
                enigmaticLegacy$isTreasureOnly = ObfuscationReflectionHelper.findMethod(Enchantment.class, "m_6591_");
            }
        }
    }
}
