package com.integral.enigmaticlegacy;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.integral.enigmaticlegacy.brewing.SpecialBrewingRecipe;
import com.integral.enigmaticlegacy.brewing.ValidationBrewingRecipe;
import com.integral.enigmaticlegacy.config.ConfigHandler;
import com.integral.enigmaticlegacy.config.ConfigHelper;
import com.integral.enigmaticlegacy.crafting.ModRecipeSerializers;
import com.integral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticKeybindHandler;
import com.integral.enigmaticlegacy.handlers.EnigmaticMaterials;
import com.integral.enigmaticlegacy.handlers.OneSpecialHandler;
import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.EnderRing;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet;
import com.integral.enigmaticlegacy.items.EnigmaticItem;
import com.integral.enigmaticlegacy.items.EscapeScroll;
import com.integral.enigmaticlegacy.items.EtheriumIngot;
import com.integral.enigmaticlegacy.items.ExtradimensionalEye;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.ForbiddenAxe;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.HastePotion;
import com.integral.enigmaticlegacy.items.HeavenScroll;
import com.integral.enigmaticlegacy.items.IronRing;
import com.integral.enigmaticlegacy.items.LootGenerator;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.MagnetRing;
import com.integral.enigmaticlegacy.items.Megasponge;
import com.integral.enigmaticlegacy.items.MendingMixture;
import com.integral.enigmaticlegacy.items.MiningCharm;
import com.integral.enigmaticlegacy.items.MonsterCharm;
import com.integral.enigmaticlegacy.items.OceanStone;
import com.integral.enigmaticlegacy.items.RecallPotion;
import com.integral.enigmaticlegacy.items.RelicOfTesting;
import com.integral.enigmaticlegacy.items.SuperMagnetRing;
import com.integral.enigmaticlegacy.items.ThiccScroll;
import com.integral.enigmaticlegacy.items.UnholyGrail;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.items.XPScroll;
import com.integral.enigmaticlegacy.packets.PacketConfirmTeleportation;
import com.integral.enigmaticlegacy.packets.PacketEnderRingKey;
import com.integral.enigmaticlegacy.packets.PacketPlayerMotion;
import com.integral.enigmaticlegacy.packets.PacketPlayerRotations;
import com.integral.enigmaticlegacy.packets.PacketPlayerSetlook;
import com.integral.enigmaticlegacy.packets.PacketPortalParticles;
import com.integral.enigmaticlegacy.packets.PacketRecallParticles;
import com.integral.enigmaticlegacy.packets.PacketRequestTeleportation;
import com.integral.enigmaticlegacy.packets.PacketSlotUnlocked;
import com.integral.enigmaticlegacy.packets.PacketSpellstoneKey;
import com.integral.enigmaticlegacy.packets.PacketXPScrollKey;
import com.integral.enigmaticlegacy.triggers.BeheadingTrigger;
import com.integral.enigmaticlegacy.triggers.UseUnholyGrailTrigger;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod("enigmaticlegacy")
//@ObjectHolder(EnigmaticLegacy.MODID)
public class EnigmaticLegacy {

	public static EnigmaticLegacy enigmaticLegacy;
	public static final Logger enigmaticLogger = LogManager.getLogger("Enigmatic Legacy");
	public static SimpleChannel packetInstance;
	
	public static final String MODID = "enigmaticlegacy";
	public static final String VERSION = "1.1.0";
	public static final String RELEASE_TYPE = "Release";
	public static final String NAME = "Enigmatic Legacy";
	
	public static final int howCoolAmI = Integer.MAX_VALUE;
	
	public static EnigmaticEventHandler enigmaticHandler;
	public static EnigmaticKeybindHandler keybindHandler;
	public static final OneSpecialHandler butImAsGuiltyAsThe = new OneSpecialHandler();
	public static List<String> damageTypesFire = new ArrayList<String>();
	public static ConfigHandler configHandler;
	public static SoundEvent HHON;
	public static SoundEvent HHOFF;
	
	public static boolean configLoaded;
	
	public static Item enigmaticItem;
	public static Item xpScroll;
	public static Item enigmaticAmulet;
	public static Item magnetRing;
	public static Item extradimensionalEye;
	public static Item relicOfTesting;
	public static Item recallPotion;
	public static Item forbiddenAxe;
	public static Item escapeScroll;
	public static Item heavenScroll;
	public static Item superMagnetRing;
	public static Item golemHeart;
	public static Item megaSponge;
	public static Item unholyGrail;
	public static Item eyeOfNebula;
	public static Item magmaHeart;
	public static Item voidPearl;
	public static Item oceanStone;
	public static Item angelBlessing;
	public static Item monsterCharm;
	public static Item miningCharm;
	public static Item enderRing;
	public static Item mendingMixture;
	public static Item lootGenerator;
	public static Item thiccScroll;
	public static Item ironRing;
	public static Item hastePotionDefault;
	public static Item hastePotionExtended;
	public static Item hastePotionEmpowered;
	public static Item hastePotionExtendedEmpowered;
	public static Item etheriumIngot;
	
	public EnigmaticLegacy() {
		
		enigmaticLegacy = this;
		
		enigmaticHandler = new EnigmaticEventHandler();
		keybindHandler = new EnigmaticKeybindHandler();
		
		configHandler = new ConfigHandler();
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::intermodStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);
		FMLJavaModLoadingContext.get().getModEventBus().register(new ModRecipeSerializers());
		
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(enigmaticHandler);
		MinecraftForge.EVENT_BUS.register(keybindHandler);
		
		//FMLJavaModLoadingContext.get().getModEventBus().register(enigmaticHandler);
		
		HHON = SuperpositionHandler.registerSound("misc.hhon");
		HHOFF = SuperpositionHandler.registerSound("misc.hhoff");
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configHandler.COMMON, "enigmatic-legacy-common.toml");
		
	}
	
	public void onLoadComplete(final FMLLoadCompleteEvent event) {
		ConfigHelper.initializeConfigValues();
		configLoaded = true;
		
		ItemStack stackAwkwardPotion = new ItemStack(Items.POTION, 1);
		stackAwkwardPotion = PotionUtils.addPotionToItemStack(stackAwkwardPotion, Potions.AWKWARD);
		ItemStack stackThiccPotion = new ItemStack(Items.POTION, 1);
		stackThiccPotion = PotionUtils.addPotionToItemStack(stackThiccPotion, Potions.THICK);
		
		/*
		HashMap<Ingredient, ItemStack> testMap = new HashMap<Ingredient, ItemStack>();
		testMap.put(Ingredient.fromItems(Items.REDSTONE), new ItemStack(hastePotionExtended));
		testMap.put(Ingredient.fromItems(Items.GLOWSTONE), new ItemStack(hastePotionEmpowered));
		BrewingRecipeRegistry.addRecipe(new ComplexBrewingRecipe(Ingredient.fromItems(hastePotionDefault), testMap));
		*/
		
		if (configHandler.RECALL_POTION_ENABLED.get())
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(IngredientNBT.fromStacks(stackAwkwardPotion), Ingredient.fromItems(Items.ENDER_EYE), new ItemStack(recallPotion)));
		
		if (configHandler.HASTE_POTION_ENABLED.get()) {
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(IngredientNBT.fromStacks(stackAwkwardPotion), Ingredient.fromItems(Items.QUARTZ), new ItemStack(hastePotionDefault)));
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.fromItems(hastePotionDefault), Ingredient.fromItems(Items.REDSTONE), new ItemStack(hastePotionExtended)));
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.fromItems(hastePotionDefault), Ingredient.fromItems(Items.GLOWSTONE_DUST), new ItemStack(hastePotionEmpowered)));
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.fromItems(hastePotionExtended), Ingredient.fromItems(Items.GLOWSTONE_DUST), new ItemStack(hastePotionExtendedEmpowered)));
			BrewingRecipeRegistry.addRecipe(new SpecialBrewingRecipe(Ingredient.fromItems(hastePotionEmpowered), Ingredient.fromItems(Items.REDSTONE), new ItemStack(hastePotionExtendedEmpowered)));
		}
		
		BrewingRecipeRegistry.addRecipe(new ValidationBrewingRecipe(Ingredient.fromItems(hastePotionExtendedEmpowered, recallPotion), null));
	}
	
	public void setup(final FMLCommonSetupEvent event) {
		
		damageTypesFire.add(DamageSource.LAVA.damageType);
		damageTypesFire.add(DamageSource.IN_FIRE.damageType);
		damageTypesFire.add(DamageSource.ON_FIRE.damageType);
		
		packetInstance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(EnigmaticLegacy.MODID, "main"))
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions("1"::equals)
                .serverAcceptedVersions("1"::equals)
                .simpleChannel();
		
		packetInstance.registerMessage(0, PacketRecallParticles.class, PacketRecallParticles::encode, PacketRecallParticles::decode,
				PacketRecallParticles::handle);
		packetInstance.registerMessage(1, PacketEnderRingKey.class, PacketEnderRingKey::encode, PacketEnderRingKey::decode,
				PacketEnderRingKey::handle);
		packetInstance.registerMessage(2, PacketSpellstoneKey.class, PacketSpellstoneKey::encode, PacketSpellstoneKey::decode,
				PacketSpellstoneKey::handle);
		packetInstance.registerMessage(3, PacketPlayerMotion.class, PacketPlayerMotion::encode, PacketPlayerMotion::decode,
				PacketPlayerMotion::handle);
		packetInstance.registerMessage(4, PacketPlayerRotations.class, PacketPlayerRotations::encode, PacketPlayerRotations::decode,
				PacketPlayerRotations::handle);
		packetInstance.registerMessage(5, PacketPlayerSetlook.class, PacketPlayerSetlook::encode, PacketPlayerSetlook::decode,
				PacketPlayerSetlook::handle);
		packetInstance.registerMessage(6, PacketRequestTeleportation.class, PacketRequestTeleportation::encode, PacketRequestTeleportation::decode,
				PacketRequestTeleportation::handle);
		packetInstance.registerMessage(7, PacketConfirmTeleportation.class, PacketConfirmTeleportation::encode, PacketConfirmTeleportation::decode,
				PacketConfirmTeleportation::handle);
		packetInstance.registerMessage(8, PacketPortalParticles.class, PacketPortalParticles::encode, PacketPortalParticles::decode,
				PacketPortalParticles::handle);
		packetInstance.registerMessage(9, PacketXPScrollKey.class, PacketXPScrollKey::encode, PacketXPScrollKey::decode,
				PacketXPScrollKey::handle);
		packetInstance.registerMessage(10, PacketSlotUnlocked.class, PacketSlotUnlocked::encode, PacketSlotUnlocked::decode,
				PacketSlotUnlocked::handle);
		
		
		CriteriaTriggers.register(UseUnholyGrailTrigger.INSTANCE);
		CriteriaTriggers.register(BeheadingTrigger.INSTANCE);
	}
	
	public void clientRegistries(final FMLClientSetupEvent event) {
		//RenderingRegistry.registerEntityRenderingHandler(EternalItemEntity.class, renderManager -> new ItemRenderer(renderManager, Minecraft.getInstance().getItemRenderer()));
		keybindHandler.registerKeybinds();
	}
	
	public void intermodStuff(final InterModEnqueueEvent event) {
		SuperpositionHandler.registerCurioType("charm", 1, true, false, null);
		SuperpositionHandler.registerCurioType("ring", 2, false, false, null);
		SuperpositionHandler.registerCurioType("spellstone", 1, false, false, new ResourceLocation(EnigmaticLegacy.MODID, "textures/slots/spellstone_slot.png"));
		SuperpositionHandler.registerCurioType("scroll", 1, false, false, new ResourceLocation(EnigmaticLegacy.MODID, "textures/slots/scroll_slot.png"));
		
	}
	
	@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, modid = EnigmaticLegacy.MODID)
	public static class RegistryEvents {
		
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
				
			enigmaticItem = new EnigmaticItem(EnigmaticItem.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_item"));
			xpScroll = new XPScroll(XPScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "xp_scroll"));
			enigmaticAmulet = new EnigmaticAmulet(EnigmaticAmulet.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "enigmatic_amulet"));
			magnetRing = new MagnetRing(MagnetRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "magnet_ring"));
			extradimensionalEye = new ExtradimensionalEye(ExtradimensionalEye.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "extradimensional_eye"));
			relicOfTesting = new RelicOfTesting(RelicOfTesting.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "relic_of_testing"));
			recallPotion = new RecallPotion(RecallPotion.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "recall_potion"));
			forbiddenAxe = new ForbiddenAxe(EnigmaticMaterials.FORBIDDENAXE, 6, -1.6F, ForbiddenAxe.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "forbidden_axe"));
			escapeScroll = new EscapeScroll(EscapeScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "escape_scroll"));
			heavenScroll = new HeavenScroll(HeavenScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "heaven_scroll"));
			superMagnetRing = new SuperMagnetRing(SuperMagnetRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "super_magnet_ring"));
			golemHeart = new GolemHeart(GolemHeart.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "golem_heart"));
			megaSponge = new Megasponge(Megasponge.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mega_sponge"));
			unholyGrail = new UnholyGrail(UnholyGrail.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "unholy_grail"));
			eyeOfNebula = new EyeOfNebula(EyeOfNebula.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "eye_of_nebula"));
			magmaHeart = new MagmaHeart(MagmaHeart.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "magma_heart"));
			voidPearl = new VoidPearl(VoidPearl.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "void_pearl"));
			oceanStone = new OceanStone(OceanStone.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ocean_stone"));
			angelBlessing = new AngelBlessing(AngelBlessing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "angel_blessing"));
			monsterCharm = new MonsterCharm(MonsterCharm.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "monster_charm"));
			miningCharm = new MiningCharm(MiningCharm.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mining_charm"));
			enderRing = new EnderRing(EnderRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "ender_ring"));
			mendingMixture = new MendingMixture(MendingMixture.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "mending_mixture"));
			lootGenerator = new LootGenerator(LootGenerator.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "loot_generator"));
			thiccScroll = new ThiccScroll(ThiccScroll.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "thicc_scroll"));
			ironRing = new IronRing(IronRing.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "iron_ring"));
			hastePotionDefault = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.COMMON), 3600, 0).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_default"));
			hastePotionExtended = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.COMMON), 9600, 0).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_extended"));
			hastePotionEmpowered = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.COMMON), 1800, 1).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_empowered"));
			hastePotionExtendedEmpowered = new HastePotion(HastePotion.setupIntegratedProperties(Rarity.RARE), 4800, 1).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "haste_potion_extended_empowered"));
			etheriumIngot = new EtheriumIngot(EtheriumIngot.setupIntegratedProperties()).setRegistryName(new ResourceLocation(EnigmaticLegacy.MODID, "etherium_ingot"));
			
			event.getRegistry().registerAll(
					enigmaticItem,
					golemHeart,
					angelBlessing,
					oceanStone,
					magmaHeart,
					eyeOfNebula,
					voidPearl,
					ironRing,
					enigmaticAmulet,
					thiccScroll,
					xpScroll,
					escapeScroll,
					heavenScroll,
					magnetRing,
					superMagnetRing,
					enderRing,
					monsterCharm,
					miningCharm,
					megaSponge,
					extradimensionalEye,
					forbiddenAxe,
					unholyGrail,
					recallPotion,
					mendingMixture,
					lootGenerator,
					hastePotionDefault,
					hastePotionExtended,
					hastePotionEmpowered,
					hastePotionExtendedEmpowered,
					relicOfTesting,
					etheriumIngot
			);
			
			enigmaticLogger.info("Items registered!");
		}
		
		@SubscribeEvent
		public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
		
			
			enigmaticLogger.info("Sounds registered!");
		}
		
		@SubscribeEvent
        public static void onRecipeRegister(final RegistryEvent.Register<IRecipeSerializer<?>> e) {
			
        }
		
		@SubscribeEvent
    	public static void onEntitiesRegistry(final RegistryEvent.Register<EntityType<?>> entityRegistryEvent) {
			
			/*entityRegistryEvent.getRegistry().registerAll(		    	
					eternalitementity = (EntityType<EternalItemEntity>) EntityType.Builder.<EternalItemEntity>create(EternalItemEntity::new, EntityClassification.MISC).setCustomClientFactory(EternalItemEntity::new)
    		    	.setShouldReceiveVelocityUpdates(true).setUpdateInterval(60)
    		    	.build("eternalitementity").setRegistryName(EnigmaticLegacy.MODID, "eternalitementity")
    		);*/	
    		
    	}
	}
	
	 public static final ItemGroup enigmaticTab = new ItemGroup("enigmaticCreativeTab") {
	      @OnlyIn(Dist.CLIENT)
	      public ItemStack createIcon() {
	         return new ItemStack(EnigmaticLegacy.enigmaticItem);
	      }
	   };
	
}
