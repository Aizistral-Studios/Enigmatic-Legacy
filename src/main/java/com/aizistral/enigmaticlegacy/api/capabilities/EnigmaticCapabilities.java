package com.aizistral.enigmaticlegacy.api.capabilities;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import top.theillusivec4.curios.api.CuriosApi;

public class EnigmaticCapabilities {
	public static final ResourceLocation ID_PLAYTIME_COUNTER = new ResourceLocation(EnigmaticLegacy.MODID, "playtime_counter");
	public static final Capability<IPlaytimeCounter> PLAYTIME_COUNTER = CapabilityManager.get(new CapabilityToken<>() {});
}
