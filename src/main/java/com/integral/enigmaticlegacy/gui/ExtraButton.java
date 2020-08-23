package com.integral.enigmaticlegacy.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.client.CuriosClientConfig;
import top.theillusivec4.curios.client.CuriosClientConfig.Client;
import top.theillusivec4.curios.client.CuriosClientConfig.Client.ButtonCorner;
import top.theillusivec4.curios.client.KeyRegistry;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;
import top.theillusivec4.curios.common.network.client.CPacketOpenVanilla;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;

public class ExtraButton extends ImageButton {

	  private final ContainerScreen<?> parentGui;
	  private boolean isRecipeBookVisible = false;

	  public ExtraButton(ContainerScreen<?> parentGui, int xIn, int yIn, int widthIn, int heightIn,
	      int textureOffsetX, int textureOffsetY, int yDiffText, ResourceLocation resource) {

	    super(xIn, yIn, widthIn, heightIn, textureOffsetX, textureOffsetY, yDiffText, resource,
	        (button) -> {
	          Minecraft mc = Minecraft.getInstance();

	          if (parentGui instanceof CuriosScreen && mc.player != null) {
	            InventoryScreen inventory = new InventoryScreen(mc.player);
	            ItemStack stack = mc.player.inventory.getItemStack();
	            mc.player.inventory.setItemStack(ItemStack.EMPTY);
	            mc.displayGuiScreen(inventory);
	            mc.player.inventory.setItemStack(stack);
	            NetworkHandler.INSTANCE
	                .send(PacketDistributor.SERVER.noArg(), new CPacketOpenVanilla());
	          } else {

	            if (parentGui instanceof InventoryScreen) {
	              InventoryScreen inventory = (InventoryScreen) parentGui;
	              RecipeBookGui recipeBookGui = inventory.getRecipeGui();

	              if (recipeBookGui.isVisible()) {
	                recipeBookGui.toggleVisibility();
	              }
	            }
	            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketOpenCurios());
	          }
	        });
	    this.parentGui = parentGui;
	  }

	  @Override
	  public void func_230431_b_(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY,
	      float partialTicks) {

	    if (this.parentGui instanceof InventoryScreen) {
	      boolean lastVisible = this.isRecipeBookVisible;
	      this.isRecipeBookVisible = ((InventoryScreen) this.parentGui).getRecipeGui().isVisible();

	      if (lastVisible != this.isRecipeBookVisible) {
	        Tuple<Integer, Integer> offsets = CuriosScreen.getButtonOffset(false);
	        this.setPosition(this.parentGui.getGuiLeft() + offsets.getA(),
	            this.parentGui.field_230709_l_ / 2 + offsets.getB());
	      }
	    } else if (this.parentGui instanceof CreativeScreen) {
	      CreativeScreen gui = (CreativeScreen) this.parentGui;
	      boolean isInventoryTab = gui.getSelectedTabIndex() == ItemGroup.INVENTORY.getIndex();
	      this.field_230693_o_ = isInventoryTab;

	      if (!isInventoryTab) {
	        return;
	      }
	    }
	    super.func_230431_b_(matrixStack, mouseX, mouseY, partialTicks);
	  }
	}

