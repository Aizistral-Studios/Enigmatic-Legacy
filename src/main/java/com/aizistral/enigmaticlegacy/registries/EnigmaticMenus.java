package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnigmaticMenus {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, EnigmaticLegacy.MODID);

    public static final RegistryObject<MenuType<LoreInscriberContainer>> LORE_INSCRIBER_CONTAINER = register("lore_inscriber_container", LoreInscriberContainer::new);

    public static void registerMenus(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(
            String name, IContainerFactory<T> containerFactory
    ) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(containerFactory));
    }
}
