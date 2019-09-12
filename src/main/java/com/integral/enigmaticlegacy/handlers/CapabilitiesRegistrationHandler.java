package com.integral.enigmaticlegacy.handlers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.items.AngelBlessing;
import com.integral.enigmaticlegacy.items.EnderRing;
import com.integral.enigmaticlegacy.items.EnigmaticAmulet;
import com.integral.enigmaticlegacy.items.EnigmaticItem;
import com.integral.enigmaticlegacy.items.EscapeScroll;
import com.integral.enigmaticlegacy.items.EyeOfNebula;
import com.integral.enigmaticlegacy.items.GolemHeart;
import com.integral.enigmaticlegacy.items.HeavenScroll;
import com.integral.enigmaticlegacy.items.IronRing;
import com.integral.enigmaticlegacy.items.MagmaHeart;
import com.integral.enigmaticlegacy.items.MagnetRing;
import com.integral.enigmaticlegacy.items.Megasponge;
import com.integral.enigmaticlegacy.items.MiningCharm;
import com.integral.enigmaticlegacy.items.MonsterCharm;
import com.integral.enigmaticlegacy.items.OceanStone;
import com.integral.enigmaticlegacy.items.SuperMagnetRing;
import com.integral.enigmaticlegacy.items.VoidPearl;
import com.integral.enigmaticlegacy.items.XPScroll;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

/**
 * Handler for registering item's capabilities implemented in ICurio interface.
 * @author Integral
 */

public class CapabilitiesRegistrationHandler {
	
	public static void registerCapabilities(AttachCapabilitiesEvent<ItemStack> evt) {
		
		ItemStack stack = evt.getObject();

	    if (stack.getItem() instanceof EnigmaticItem) {

	    EnigmaticItem enigmaticItem = new EnigmaticItem(EnigmaticItem.setupIntegratedProperties());
	    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
	      LazyOptional<ICurio> curio = LazyOptional.of(() -> enigmaticItem);

	      @Nonnull
	      @Override
	      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
	                                               @Nullable Direction side) {

	        return CuriosCapability.ITEM.orEmpty(cap, curio);
	      }
	    });
	  } else if (stack.getItem() instanceof XPScroll) {

		  XPScroll xpTome = new XPScroll(XPScroll.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> xpTome);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
	  } else if (stack.getItem() instanceof EnigmaticAmulet) {
		  
		  EnigmaticAmulet enigmaticAmulet = new EnigmaticAmulet(EnigmaticAmulet.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> enigmaticAmulet);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof MagnetRing) {
		  
		  MagnetRing magnetRing = new MagnetRing(MagnetRing.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> magnetRing);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof EscapeScroll) {
		  
		  EscapeScroll escapeTome = new EscapeScroll(EscapeScroll.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> escapeTome);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
	  } else if (stack.getItem() instanceof HeavenScroll) {
		  
		  HeavenScroll heavenTome = new HeavenScroll(HeavenScroll.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> heavenTome);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
	  } else if (stack.getItem() instanceof SuperMagnetRing) {
		  
		  SuperMagnetRing superMagnetRing = new SuperMagnetRing(SuperMagnetRing.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> superMagnetRing);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof GolemHeart) {
		  
		  GolemHeart golemHeart = new GolemHeart(GolemHeart.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> golemHeart);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof Megasponge) {
		  
		  Megasponge megasponge = new Megasponge(Megasponge.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> megasponge);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof EyeOfNebula) {
		  
		  EyeOfNebula eyeOfNebula = new EyeOfNebula(EyeOfNebula.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> eyeOfNebula);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof VoidPearl) {
		  
		  VoidPearl voidPearl = new VoidPearl(VoidPearl.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> voidPearl);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof MagmaHeart) {
		  
		  MagmaHeart magmaHeart = new MagmaHeart(MagmaHeart.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> magmaHeart);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof OceanStone) {
		  
		  OceanStone oceanStone = new OceanStone(OceanStone.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> oceanStone);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof AngelBlessing) {
		  
		  AngelBlessing angelBlessing = new AngelBlessing(AngelBlessing.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> angelBlessing);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof MonsterCharm) {
		  
		  MonsterCharm monsterCharm = new MonsterCharm(MonsterCharm.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> monsterCharm);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof MiningCharm) {
		  
		  MiningCharm miningCharm = new MiningCharm(MiningCharm.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> miningCharm);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof EnderRing) {
		  
		  EnderRing enderRing = new EnderRing(EnderRing.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> enderRing);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  } else if (stack.getItem() instanceof IronRing) {
		  
		  IronRing ironRing = new IronRing(IronRing.setupIntegratedProperties());
		    evt.addCapability(CuriosCapability.ID_ITEM, new ICapabilityProvider() {
		      LazyOptional<ICurio> curio = LazyOptional.of(() -> ironRing);

		      @Nonnull
		      @Override
		      public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap,
		                                               @Nullable Direction side) {

		        return CuriosCapability.ITEM.orEmpty(cap, curio);
		      }
		    });
		    
	  }
	    
	    
	   
	}
}
