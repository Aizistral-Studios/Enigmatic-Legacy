package com.aizistral.enigmaticlegacy.registries;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnigmaticSounds {
	private static final String MISC_CATEGORY = "misc";
	private static final String QUOTE_CATEGORY = "quote";

	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EnigmaticLegacy.MODID);

	// Misc
	public static final RegistryObject<SoundEvent> CHARGED_ON = registerMisc("hhon");
	public static final RegistryObject<SoundEvent> CHARGED_OFF = registerMisc("hhoff");
	public static final RegistryObject<SoundEvent> SHIELD_TRIGGER = registerMisc("shield_trigger");
	public static final RegistryObject<SoundEvent> DEFLECT = registerMisc("deflect");
	public static final RegistryObject<SoundEvent> WRITE = registerMisc("write");
	public static final RegistryObject<SoundEvent> LEARN = registerMisc("learn");
	public static final RegistryObject<SoundEvent> SWORD_HIT_REJECT = registerMisc("sword_hit_reject");
	public static final RegistryObject<SoundEvent> EAT_REVERSE = registerMisc("uneat");
	public static final RegistryObject<SoundEvent> PAN_CLANG = registerMisc("pan_clang");
	public static final RegistryObject<SoundEvent> PAN_CLANG_FR = registerMisc("pan_clang_fr");

	// Quote
	public static final RegistryObject<SoundEvent> NO_PERIL = registerQuote("no_peril");
	public static final RegistryObject<SoundEvent> END_DOORSTEP = registerQuote("end_doorstep");
	public static final RegistryObject<SoundEvent> ONLY_BECAUSE = registerQuote("only_because");
	public static final RegistryObject<SoundEvent> DEMISE_IS = registerQuote("demise_is");
	public static final RegistryObject<SoundEvent> WE_FALL = registerQuote("we_fall");
	public static final RegistryObject<SoundEvent> YOU_WILL_ENDURE = registerQuote("you_will_endure");
	public static final RegistryObject<SoundEvent> OBLIVION_REJECTS = registerQuote("oblivion_rejects");
	public static final RegistryObject<SoundEvent> SETBACK = registerQuote("setback");
	public static final RegistryObject<SoundEvent> DEATH_MAY = registerQuote("death_may");
	public static final RegistryObject<SoundEvent> ETERNITY_TO_KEEP = registerQuote("eternity_to_keep");
	public static final RegistryObject<SoundEvent> VIOLENCE_CALLS = registerQuote("violence_calls");
	public static final RegistryObject<SoundEvent> IMMORTAL = registerQuote("immortal");
	public static final RegistryObject<SoundEvent> APPALING_PRESENCE = registerQuote("appaling_presence");
	public static final RegistryObject<SoundEvent> ITS_DESTRUCTION = registerQuote("its_destruction");

	public static final RegistryObject<SoundEvent> I_WANDERED = registerQuote("i_wandered");
	public static final RegistryObject<SoundEvent> ANOTHER_DEMIGOD = registerQuote("another_demigod");
	public static final RegistryObject<SoundEvent> ANOTHER_EON = registerQuote("another_eon");
	public static final RegistryObject<SoundEvent> PERHAPS_YOU = registerQuote("perhaps_you");
	public static final RegistryObject<SoundEvent> SULFUR_AIR = registerQuote("sulfur_air");
	public static final RegistryObject<SoundEvent> TORTURED_ROCKS = registerQuote("tortured_rocks");
	public static final RegistryObject<SoundEvent> BREATHES_RELIEVED = registerQuote("breathes_relieved");
	public static final RegistryObject<SoundEvent> WHETHER_IT_IS = registerQuote("whether_it_is");
	public static final RegistryObject<SoundEvent> POOR_CREATURE = registerQuote("poor_creature");
	public static final RegistryObject<SoundEvent> HORRIBLE_EXISTENCE = registerQuote("horrible_existence");
	public static final RegistryObject<SoundEvent> COUNTLESS_DEAD = registerQuote("countless_dead");
	public static final RegistryObject<SoundEvent> WITH_DRAGONS = registerQuote("with_dragons");
	public static final RegistryObject<SoundEvent> TERRIFYING_FORM = registerQuote("terrifying_form");
	public static final RegistryObject<SoundEvent> TOLL_PAID = registerQuote("toll_paid");

	public static SoundEvent getQuote(String name) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(EnigmaticLegacy.MODID, QUOTE_CATEGORY + "." + name));
	}

	public static void registerSounds(IEventBus modEventBus) {
		SOUND_EVENTS.register(modEventBus);
	}

	private static RegistryObject<SoundEvent> register(String id) {
		return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(
				new ResourceLocation(EnigmaticLegacy.MODID, id)
		));
	}

	private static RegistryObject<SoundEvent> registerMisc(String id) {
		return registerWithCategory(id, MISC_CATEGORY);
	}

	private static RegistryObject<SoundEvent> registerQuote(String id) {
		return registerWithCategory(id, QUOTE_CATEGORY);
	}

	private static RegistryObject<SoundEvent> registerWithCategory(String id, String category) {
		return register(category + "." + id);
	}
}
