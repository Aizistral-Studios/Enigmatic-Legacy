package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.api.generic.ModRegistry;
import com.aizistral.enigmaticlegacy.gui.containers.LoreInscriberContainer;

import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class EnigmaticMenus extends AbstractRegistry<MenuType<?>> {
	private static final EnigmaticMenus INSTANCE = new EnigmaticMenus();

	@ObjectHolder(value = MODID + ":lore_inscriber_container", registryName = "menu")
	public static final MenuType<LoreInscriberContainer> LORE_INSCRIBER_CONTAINER = null;

	private EnigmaticMenus() {
		super(ForgeRegistries.MENU_TYPES);
		this.register("lore_inscriber_container", () -> IForgeMenuType.create(LoreInscriberContainer::new));
	}

}
