package com.aizistral.enigmaticlegacy.datagen;

import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(final DataGenerator generator, final BlockTagsProvider blockTagsProvider, final String modId, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, modId, existingFileHelper);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void addTags() {
        tag(Tags.Items.TOOLS)
                .add(EnigmaticItems.ENDER_SLAYER)
                .add(EnigmaticItems.FORBIDDEN_AXE)
                .add(EnigmaticItems.ETHERIUM_SWORD)
                .add(EnigmaticItems.ETHERIUM_PICKAXE)
                .add(EnigmaticItems.ETHERIUM_AXE)
                .add(EnigmaticItems.ETHERIUM_SHOVEL)
                .add(EnigmaticItems.ETHERIUM_SCYTHE)
                .add(EnigmaticItems.INFERNAL_SHIELD)
                .add(EnigmaticItems.ASTRAL_BREAKER);
        tag(Tags.Items.TOOLS_SWORDS)
                .add(EnigmaticItems.ENDER_SLAYER)
                .add(EnigmaticItems.FORBIDDEN_AXE)
                .add(EnigmaticItems.ETHERIUM_SWORD);
        // TODO :: astral_breaker -> add pickaxe / axe / shovel?
        tag(Tags.Items.TOOLS_PICKAXES).add(EnigmaticItems.ETHERIUM_PICKAXE);
        tag(Tags.Items.TOOLS_AXES).add(EnigmaticItems.ETHERIUM_AXE);
        tag(Tags.Items.TOOLS_SHOVELS).add(EnigmaticItems.ETHERIUM_SHOVEL);
        tag(Tags.Items.TOOLS_HOES).add(EnigmaticItems.ETHERIUM_SCYTHE);
        tag(Tags.Items.TOOLS_SHIELDS).add(EnigmaticItems.INFERNAL_SHIELD);

        tag(Tags.Items.ARMORS)
                .add(EnigmaticItems.ETHERIUM_HELMET)
                .add(EnigmaticItems.ETHERIUM_CHESTPLATE)
                .add(EnigmaticItems.ETHERIUM_LEGGINGS)
                .add(EnigmaticItems.ETHERIUM_BOOTS);
        tag(Tags.Items.ARMORS_HELMETS).add(EnigmaticItems.ETHERIUM_HELMET);
        tag(Tags.Items.ARMORS_CHESTPLATES).add(EnigmaticItems.ETHERIUM_CHESTPLATE);
        tag(Tags.Items.ARMORS_LEGGINGS).add(EnigmaticItems.ETHERIUM_LEGGINGS);
        tag(Tags.Items.ARMORS_BOOTS).add(EnigmaticItems.ETHERIUM_BOOTS);

        tag(Tags.Items.INGOTS)
                .add(EnigmaticItems.ETHERIUM_INGOT)
                .add(EnigmaticItems.EVIL_INGOT);
        tag(Tags.Items.NUGGETS).add(EnigmaticItems.ETHERIUM_NUGGET);
        tag(Tags.Items.ORES).add(EnigmaticItems.ETHERIUM_ORE);

        tag(ItemTags.COMPASSES)
                .add(EnigmaticItems.SOUL_COMPASS)
                .add(EnigmaticItems.THE_INFINITUM);
    }
}
