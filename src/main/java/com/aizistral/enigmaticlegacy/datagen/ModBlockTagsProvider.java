package com.aizistral.enigmaticlegacy.datagen;

import com.aizistral.enigmaticlegacy.registries.EnigmaticBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(final DataGenerator generator, final String modId, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generator, modId, existingFileHelper);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(EnigmaticBlocks.ETHERIUM_BLOCK)
                .add(EnigmaticBlocks.MASSIVE_LAMP)
                .add(EnigmaticBlocks.MASSIVE_SHROOMLAMP)
                .add(EnigmaticBlocks.MASSIVE_REDSTONELAMP)
                .add(EnigmaticBlocks.BIG_LAMP)
                .add(EnigmaticBlocks.BIG_SHROOMLAMP)
                .add(EnigmaticBlocks.BIG_REDSTONELAMP)
                .add(EnigmaticBlocks.END_ANCHOR);

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(EnigmaticBlocks.ETHERIUM_BLOCK)
                .add(EnigmaticBlocks.END_ANCHOR);
    }
}
