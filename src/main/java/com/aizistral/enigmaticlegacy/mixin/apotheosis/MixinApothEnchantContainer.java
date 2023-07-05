package com.aizistral.enigmaticlegacy.mixin.apotheosis;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import shadows.apotheosis.Apoth;
import shadows.apotheosis.advancements.EnchantedTrigger;
import shadows.apotheosis.ench.table.ApothEnchantContainer;
import shadows.apotheosis.ench.table.EnchantingRecipe;
import shadows.apotheosis.ench.table.IEnchantableItem;
import shadows.apotheosis.util.FloatReferenceHolder;

import java.util.List;

@Mixin(ApothEnchantContainer.class)
public class MixinApothEnchantContainer extends EnchantmentMenu {
    public MixinApothEnchantContainer(int containerID, final Inventory playerInventory) {
        super(containerID, playerInventory);
    }

    @Shadow @Final protected FloatReferenceHolder eterna;
    @Shadow @Final protected FloatReferenceHolder quanta;
    @Shadow @Final protected FloatReferenceHolder arcana;
    @Shadow @Final protected FloatReferenceHolder rectification;

    /**
     * Copied Original method (1.19.2 - 6.2.1)
     */
    @Override
    public boolean clickMenuButton(Player player, int id) {
        // New :: START
        boolean hasPearl = EnigmaticItems.ENCHANTER_PEARL.isPresent(player);
        // New :: END

        int level = this.costs[id];
        ItemStack preFinalItem = this.enchantSlots.getItem(0);
        // New :: Need to do this so that the field is considered final
        ItemStack lapis = hasPearl ? Items.LAPIS_LAZULI.getDefaultInstance() : this.getSlot(1).getItem();

        // New :: START
        if (hasPearl) {
            lapis.setCount(64);
        }
        // New :: END

        int cost = id + 1;
        if ((lapis.isEmpty() || lapis.getCount() < cost) && !player.getAbilities().instabuild) {
            return false;
        } else if (this.costs[id] > 0
                && !preFinalItem.isEmpty()
                && (player.experienceLevel >= cost && player.experienceLevel >= this.costs[id] || player.getAbilities().instabuild)) {
            this.access
                    .execute(
                            (world, pos) -> {
                                // New :: `preFinalItem` was previously `toEnchant` - need to do this to re-assign later for the merge
                                ItemStack toEnchant = preFinalItem;

                                float eterna = this.eterna.get();
                                float quanta = this.quanta.get();
                                float arcana = this.arcana.get();
                                float rectification = this.rectification.get();
                                List<EnchantmentInstance> list = this.getEnchantmentList(toEnchant, id, this.costs[id]);
                                if (!list.isEmpty()) {
                                    // New :: START
                                    ItemStack doubleRoll = ItemStack.EMPTY;

                                    if (hasPearl) {
                                        // Doing this here to work with a clean un-enchanted item
                                        doubleRoll = EnchantmentHelper.enchantItem(player.getRandom(), toEnchant.copy(), Math.min(costs[id] + 7, 40), true);
                                    }
                                    // New :: END

                                    player.onEnchantmentPerformed(toEnchant, cost);
                                    if (((EnchantmentInstance)list.get(0)).enchantment == Apoth.Enchantments.INFUSION.get()) {
                                        EnchantingRecipe match = EnchantingRecipe.findMatch(world, toEnchant, eterna, quanta, arcana);
                                        if (match == null) {
                                            return;
                                        }

                                        this.enchantSlots.setItem(0, match.assemble(toEnchant, eterna, quanta, arcana));
                                    } else {
                                        this.enchantSlots.setItem(0, ((IEnchantableItem)toEnchant.getItem()).onEnchantment(toEnchant, list));
                                    }

                                    if (!player.getAbilities().instabuild) {
                                        lapis.shrink(cost);
                                        if (lapis.isEmpty()) {
                                            this.enchantSlots.setItem(1, ItemStack.EMPTY);
                                        }
                                    }

                                    // New :: START
                                    if (hasPearl) {
                                        toEnchant = SuperpositionHandler.mergeEnchantments(toEnchant, doubleRoll, false, false);
                                        toEnchant = SuperpositionHandler.maybeApplyEternalBinding(toEnchant);

                                        enchantSlots.setItem(0, toEnchant);
                                    }
                                    // New :: END

                                    player.awardStat(Stats.ENCHANT_ITEM);
                                    if (player instanceof ServerPlayer) {
                                        ((EnchantedTrigger) CriteriaTriggers.ENCHANTED_ITEM)
                                                .trigger((ServerPlayer)player, toEnchant, level, eterna, quanta, arcana, rectification);
                                    }

                                    this.enchantSlots.setChanged();
                                    this.enchantmentSeed.set(player.getEnchantmentSeed());
                                    this.slotsChanged(this.enchantSlots);
                                    world.playSound(
                                            (Player)null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F
                                    );
                                }
                            }
                    );
            return true;
        } else {
            return false;
        }
    }

    // TODO :: Keeping this around in case there is a way to avoid overriding the entire method
//    /** Enchanter Pearl disables lapis cost (Ignore the "There are no possible signatures for this injector") */
//    @ModifyVariable(method = "clickMenuButton", at = @At(value = "STORE"), name = "lapis", remap = false)
//    public ItemStack fakeLapis(final ItemStack lapis, /* Method arguments: */ final Player player) {
//        if (EnigmaticItems.ENCHANTER_PEARL.isPresent(player)) {
//            ItemStack dummy = Items.LAPIS_LAZULI.getDefaultInstance();
//            dummy.setCount(64);
//            return dummy;
//        }
//
//        return lapis;
//    }
}
