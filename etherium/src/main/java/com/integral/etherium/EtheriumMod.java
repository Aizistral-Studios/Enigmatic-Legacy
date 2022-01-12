package com.integral.etherium;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.integral.enigmaticlegacy.api.materials.EnigmaticArmorMaterials;
import com.integral.enigmaticlegacy.api.materials.EnigmaticMaterials;
import com.integral.enigmaticlegacy.packets.clients.PacketPlayerMotion;
import com.integral.etherium.core.EtheriumConfig;
import com.integral.etherium.core.EtheriumEventHandler;
import com.integral.etherium.core.TestLootGenerator;
import com.integral.etherium.items.EnderRod;
import com.integral.etherium.items.EtheriumArmor;
import com.integral.etherium.items.EtheriumAxe;
import com.integral.etherium.items.EtheriumIngot;
import com.integral.etherium.items.EtheriumOre;
import com.integral.etherium.items.EtheriumPickaxe;
import com.integral.etherium.items.EtheriumScythe;
import com.integral.etherium.items.EtheriumShovel;
import com.integral.etherium.items.EtheriumSword;
import com.integral.etherium.proxy.CommonProxy;
import com.integral.etherium.proxy.ClientProxy;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(EtheriumMod.MODID)
public class EtheriumMod {
	public static final String MODID = "etherium";
	public static final String VERSION = "1.0.0";
	public static final String RELEASE_TYPE = "Release";
	public static final String NAME = "Etherium";
	private static final String PTC_VERSION = "1";

	public static final Logger logger = LogManager.getLogger("Etherium");
	public static final CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public static SimpleChannel packetInstance;

	public static SoundEvent HHON;
	public static SoundEvent HHOFF;
	public static SoundEvent SHIELD_TRIGGER;

	public static EtheriumOre etheriumOre;
	public static EtheriumIngot etheriumIngot;

	public static EtheriumArmor etheriumHelmet;
	public static EtheriumArmor etheriumChestplate;
	public static EtheriumArmor etheriumLeggings;
	public static EtheriumArmor etheriumBoots;

	public static EtheriumPickaxe etheriumPickaxe;
	public static EtheriumAxe etheriumAxe;
	public static EtheriumShovel etheriumShovel;
	public static EtheriumSword etheriumSword;
	public static EtheriumScythe etheriumScythe;

	public static EnderRod enderRod;
	public static TestLootGenerator lootGenerator;
	
	public EtheriumMod() {
		EtheriumConfig etheriumConfig = new EtheriumConfig();

		EnigmaticMaterials.setEtheriumConfig(etheriumConfig);
		EnigmaticArmorMaterials.setEtheriumConfig(etheriumConfig);
		
		etheriumOre = new EtheriumOre(etheriumConfig);
		etheriumIngot = new EtheriumIngot(etheriumConfig);
		
		etheriumHelmet = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.HEAD).setRegistryName(new ResourceLocation(MODID, "etherium_helmet"));
		etheriumChestplate = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.CHEST).setRegistryName(new ResourceLocation(MODID, "etherium_chestplate"));
		etheriumLeggings = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.LEGS).setRegistryName(new ResourceLocation(MODID, "etherium_leggings"));
		etheriumBoots = (EtheriumArmor) new EtheriumArmor(etheriumConfig, EquipmentSlot.FEET).setRegistryName(new ResourceLocation(MODID, "etherium_boots"));

		etheriumPickaxe = new EtheriumPickaxe(etheriumConfig);
		etheriumAxe = new EtheriumAxe(etheriumConfig);
		etheriumShovel = new EtheriumShovel(etheriumConfig);
		etheriumSword = new EtheriumSword(etheriumConfig);
		etheriumScythe = new EtheriumScythe(etheriumConfig);
		
		enderRod = new EnderRod(etheriumConfig);
		lootGenerator = new TestLootGenerator(etheriumConfig);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
		MinecraftForge.EVENT_BUS.register(new EtheriumEventHandler(etheriumConfig, etheriumOre));
		
		logger.info("Mod instance constructed successfully.");
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		packetInstance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main")).networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals).serverAcceptedVersions(PTC_VERSION::equals).simpleChannel();
		
		packetInstance.registerMessage(1, PacketPlayerMotion.class, PacketPlayerMotion::encode, PacketPlayerMotion::decode, PacketPlayerMotion::handle);
	}
	
	public void onLoadComplete(final FMLLoadCompleteEvent event) {
		proxy.loadComplete(event);
	}
	
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			logger.info("Initializing items registration...");

			event.getRegistry().registerAll(
					etheriumOre,
					etheriumIngot,
					etheriumHelmet,
					etheriumChestplate,
					etheriumLeggings,
					etheriumBoots,
					etheriumPickaxe,
					etheriumAxe,
					etheriumShovel,
					etheriumSword,
					etheriumScythe,
					enderRod
					);

			logger.info("Items registered successfully.");
		}
		
		@SubscribeEvent
		public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
			logger.info("Initializing sounds registration...");

			HHON = registerSound("misc.hhon");
			HHOFF = registerSound("misc.hhoff");
			SHIELD_TRIGGER = registerSound("misc.shield_trigger");

			logger.info("Sounds registered successfully.");
		}
		
		private static SoundEvent registerSound(final String soundName) {
			final ResourceLocation location = new ResourceLocation(MODID, soundName);
			final SoundEvent event = new SoundEvent(location);
			event.setRegistryName(location);
			ForgeRegistries.SOUND_EVENTS.register(event);
			return event;
		}

	}
}
