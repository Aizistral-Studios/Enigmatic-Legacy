package com.aizistral.enigmaticlegacy.datagen;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Generator {
    @SubscribeEvent
    public static void configureDataGen(final GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new ModItemTagsProvider(generator, new BlockTagsProvider(generator, EnigmaticLegacy.MODID, existingFileHelper), EnigmaticLegacy.MODID, existingFileHelper));
    }
}
