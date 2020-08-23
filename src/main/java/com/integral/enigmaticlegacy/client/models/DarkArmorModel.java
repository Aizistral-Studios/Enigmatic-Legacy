package com.integral.enigmaticlegacy.client.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.integral.enigmaticlegacy.EnigmaticLegacy;
import com.integral.enigmaticlegacy.client.models.SpecialArmorModelRenderer.AnchorType;
import com.integral.enigmaticlegacy.items.DarkArmor;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

public class DarkArmorModel extends GenericArmorModel {

	public static final HashMap<String, Float> darkArmorOffsetMap;

	static {
		darkArmorOffsetMap = new HashMap<>();

		DarkArmorModel.darkArmorOffsetMap.put("head_x", 0.0F);
		DarkArmorModel.darkArmorOffsetMap.put("head_y", 0.0F);
		DarkArmorModel.darkArmorOffsetMap.put("head_z", 0.0F);

		DarkArmorModel.darkArmorOffsetMap.put("chest_x", 0.0F);
		DarkArmorModel.darkArmorOffsetMap.put("chest_y", 0.0F);
		DarkArmorModel.darkArmorOffsetMap.put("chest_z", 0.0F);

		DarkArmorModel.darkArmorOffsetMap.put("arm_right_x", 5.0F);
		DarkArmorModel.darkArmorOffsetMap.put("arm_right_y", -2.0F);
		DarkArmorModel.darkArmorOffsetMap.put("arm_right_z", 0.0F);

		DarkArmorModel.darkArmorOffsetMap.put("arm_left_x", -5.0F);
		DarkArmorModel.darkArmorOffsetMap.put("arm_left_y", -2.0F);
		DarkArmorModel.darkArmorOffsetMap.put("arm_left_z", 0.0F);

		DarkArmorModel.darkArmorOffsetMap.put("leg_right_x", 1.9F);
		DarkArmorModel.darkArmorOffsetMap.put("leg_right_y", -12.0F);
		DarkArmorModel.darkArmorOffsetMap.put("leg_right_z", 0.0F);

		DarkArmorModel.darkArmorOffsetMap.put("leg_left_x", -1.9F);
		DarkArmorModel.darkArmorOffsetMap.put("leg_left_y", -12.0F);
		DarkArmorModel.darkArmorOffsetMap.put("leg_left_z", 0.0F);

	}

	private final SpecialArmorModelRenderer HelmetMain;
	private final SpecialArmorModelRenderer HornLeft;
	private final SpecialArmorModelRenderer HornRight;
	private final SpecialArmorModelRenderer ChestMain;
	private final SpecialArmorModelRenderer LowerChest;
	private final SpecialArmorModelRenderer BreastplateLeft;
	private final SpecialArmorModelRenderer BreastplateRight;
	private final SpecialArmorModelRenderer BreastplateBase;
	private final SpecialArmorModelRenderer ArmRightMain;
	private final SpecialArmorModelRenderer FistRight;
	private final SpecialArmorModelRenderer ArmLeftMain;
	private final SpecialArmorModelRenderer FistLeft;
	private final SpecialArmorModelRenderer LegLeftMain;
	private final SpecialArmorModelRenderer LegRightMain;
	private final SpecialArmorModelRenderer BootLeft;
	private final SpecialArmorModelRenderer BootRight;

	private final SpecialArmorModelRenderer HelmetAnchor;
	private final SpecialArmorModelRenderer ChestAnchor;
	private final SpecialArmorModelRenderer ArmRightAnchor;
	private final SpecialArmorModelRenderer ArmLeftAnchor;
	private final SpecialArmorModelRenderer LegRightAnchor;
	private final SpecialArmorModelRenderer LegLeftAnchor;

	public DarkArmorModel(EquipmentSlotType type) {
		super(type);

		this.textureWidth = 128;
		this.textureHeight = 128;
		float s = 0.01F;

		this.HelmetAnchor = new SpecialArmorModelRenderer(this, null);
		//this.HelmetAnchor.setRotationPoint(0.0F, -9.0F, -5.0F);

		this.ChestAnchor = new SpecialArmorModelRenderer(this, null);
		//this.ChestAnchor.setRotationPoint(0.0F, 1.0F, -3.0F);

		this.ArmRightAnchor = new SpecialArmorModelRenderer(this, null);
		//this.ArmRightAnchor.setRotationPoint(-6.0F, -1.0F, 0.4F);

		this.ArmLeftAnchor = new SpecialArmorModelRenderer(this, null);
		//this.ArmLeftAnchor.setRotationPoint(7.0F, -1.0F, 0.4F);

		this.LegRightAnchor = new SpecialArmorModelRenderer(this, null);
		//this.LegRightAnchor.setRotationPoint(0.0F, 24.0F, 0.0F);

		this.LegLeftAnchor = new SpecialArmorModelRenderer(this, null);
		//this.LegLeftAnchor.setRotationPoint(-0.3F, 19.0F, -3.4F);

		this.HelmetMain = new SpecialArmorModelRenderer(this, this.helmetParts);
		this.HelmetMain.setRotationPoint(0.0F, -9.0F, -5.0F);
		this.HelmetMain.setTextureOffset(0, 0).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F, 0.0F, false, AnchorType.HEAD);
		this.HelmetMain.setTextureOffset(0, 50).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 2.0F, 0.0F, false, AnchorType.HEAD);
		this.HelmetAnchor.addChild(this.HelmetMain);

		this.HornLeft = new SpecialArmorModelRenderer(this, this.helmetParts);
		this.HornLeft.setRotationPoint(-7.0F, 4.0F, 5.0F);
		this.HelmetMain.addChild(this.HornLeft);
		this.HornLeft.setTextureOffset(20, 20).addBox(0.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false, AnchorType.HEAD);
		this.HornLeft.setTextureOffset(9, 38).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false, AnchorType.HEAD);

		this.HornRight = new SpecialArmorModelRenderer(this, this.helmetParts);
		this.HornRight.setRotationPoint(8.0F, 4.0F, 5.0F);
		this.HelmetMain.addChild(this.HornRight);
		this.HornRight.setTextureOffset(0, 6).addBox(-3.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false, AnchorType.HEAD);
		this.HornRight.setTextureOffset(0, 38).addBox(-1.0F, -5.0F, -1.0F, 1.0F, 5.0F, 2.0F, 0.0F, false, AnchorType.HEAD);

		this.ChestMain = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.ChestMain.setRotationPoint(0.0F, 1.0F, -3.0F);
		this.ChestMain.setTextureOffset(0, 20).addBox(-4.2F, -1.2F, 0.7F, 8.4F, 12.3F, 4.6F, 0.0F, false, AnchorType.CHEST);
		//this.Chest.setTextureOffset(0, 69).addBox(-4.2F, 11.0F, 0.7F, 8.4F, 2.0F, 4.6F, 0.0F, false, AnchorType.CHEST);
		//this.Chest.setTextureOffset(0, 83).addBox(0.0F, 11.1F, 0.7F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.CHEST);
		//this.Chest.setTextureOffset(0, 77).addBox(-4.2F, 11.1F, 0.7F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.CHEST);
		this.ChestAnchor.addChild(this.ChestMain);

		this.LowerChest = new SpecialArmorModelRenderer(this, this.leggingsParts);
		this.LowerChest.setRotationPoint(0.0F, 1.0F, -3.0F);
		//this.LowerChest.setTextureOffset(0, 20).addBox(-4.2F, -1.2F, 0.7F, 8.4F, 12.3F, 4.6F, 0.0F, false, AnchorType.CHEST);
		//this.Chest.setTextureOffset(0, 69).addBox(-4.2F, 11.0F, 0.7F, 8.4F, 2.0F, 4.6F, 0.0F, false, AnchorType.CHEST);
		this.LowerChest.setTextureOffset(0, 83).addBox(0.0F, 11.1F, 0.7F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.CHEST);
		this.LowerChest.setTextureOffset(0, 77).addBox(-4.2F, 11.1F, 0.7F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.CHEST);
		this.ChestAnchor.addChild(this.LowerChest);


		this.BreastplateLeft = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.BreastplateLeft.setRotationPoint(-3.1F, 4.0F, -0.2F, AnchorType.CHEST);
		this.ChestMain.addChild(this.BreastplateLeft);
		this.setRotationAngle(this.BreastplateLeft, -0.2618F, 0.0F, 0.0F);
		this.BreastplateLeft.setTextureOffset(55, 0).addBox(-1.1F, -4.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);

		this.BreastplateRight = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.BreastplateRight.setRotationPoint(1.2F, 4.0F, -0.2F, AnchorType.CHEST);
		this.ChestMain.addChild(this.BreastplateRight);
		this.setRotationAngle(this.BreastplateRight, -0.2618F, 0.0F, 0.0F);
		this.BreastplateRight.setTextureOffset(52, 39).addBox(-1.0F, -4.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.0F, false);

		this.BreastplateBase = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.BreastplateBase.setRotationPoint(-2.0F, 6.0F, 0.0F, AnchorType.CHEST);
		this.ChestMain.addChild(this.BreastplateBase);
		this.setRotationAngle(this.BreastplateBase, 0.0873F, 0.0F, 0.0F);
		this.BreastplateBase.setTextureOffset(52, 14).addBox(-1.0F, -4.0F, 0.0F, 6.0F, 8.0F, 1.0F, 0.0F, false);

		this.ArmRightMain = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.ArmRightMain.setRotationPoint(-6.0F, -1.0F, 0.4F);
		this.ArmRightMain.setTextureOffset(30, 0).addBox(-2.7F, 0.0F, -3.1F, 4.7F, 5.0F, 5.4F, 0.0F, false, AnchorType.ARM_RIGHT);
		this.ArmRightMain.setTextureOffset(48, 48).addBox(-2.3F, 5.0F, -2.7F, 4.3F, 4.0F, 4.7F, 0.0F, false, AnchorType.ARM_RIGHT);
		this.ArmRightMain.setTextureOffset(0, 38).addBox(-1.0F, -0.8F, -4.0F, 1.0F, 5.9F, 7.0F, 0.0F, false, AnchorType.ARM_RIGHT);
		this.ArmRightAnchor.addChild(this.ArmRightMain);


		this.FistRight = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.FistRight.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.ArmRightMain.addChild(this.FistRight);
		this.FistRight.setTextureOffset(50, 31).addBox(-1.0F, 11.4F, -3.4F, 1.0F, 2.0F, 6.0F, 0.0F, true, AnchorType.ARM_RIGHT);
		this.FistRight.setTextureOffset(45, 4).addBox(-3.0F, 9.4F, -3.4F, 2.0F, 4.0F, 6.0F, 0.0F, true, AnchorType.ARM_RIGHT);

		this.ArmLeftMain = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.ArmLeftMain.setRotationPoint(7.0F, -1.0F, 0.4F);
		this.ArmLeftMain.setTextureOffset(16, 38).addBox(-3.0F, 0.0F, -3.1F, 4.7F, 5.0F, 5.4F, 0.0F, false, AnchorType.ARM_LEFT);
		this.ArmLeftMain.setTextureOffset(28, 50).addBox(-3.0F, 5.0F, -2.7F, 4.3F, 4.0F, 4.7F, 0.0F, false, AnchorType.ARM_LEFT);
		this.ArmLeftMain.setTextureOffset(36, 13).addBox(-1.0F, -0.8F, -4.0F, 1.0F, 5.9F, 7.0F, 0.0F, false, AnchorType.ARM_LEFT);
		this.ArmLeftAnchor.addChild(this.ArmLeftMain);


		this.FistLeft = new SpecialArmorModelRenderer(this, this.chestplateParts);
		this.FistLeft.setRotationPoint(2.0F, 12.4F, 0.6F);
		this.ArmLeftMain.addChild(this.FistLeft);
		this.FistLeft.setTextureOffset(50, 31).addBox(-3.0F, -1.0F, -4.0F, 1.0F, 2.0F, 6.0F, 0.0F, false, AnchorType.ARM_LEFT);
		this.FistLeft.setTextureOffset(45, 4).addBox(-2.0F, -3.0F, -4.0F, 2.0F, 4.0F, 6.0F, 0.0F, false, AnchorType.ARM_LEFT);

		this.LegLeftMain = new SpecialArmorModelRenderer(this, this.leggingsParts);
		this.LegLeftMain.setRotationPoint(0.0F, 24.0F, 0.0F);
		//this.LegLeft.setTextureOffset(36, 36).addBox(-0.15F, -10.0F, -2.4F, 4.45F, 10.4F, 4.8F, 0.0F, false, AnchorType.LEG_LEFT);
		this.LegLeftMain.setTextureOffset(20, 69).addBox(-0.15F, -10.0F, -2.3F, 4.4F, 2.0F, 4.6F, 0.0F, true, AnchorType.LEG_LEFT);
		//this.LegLeft.setTextureOffset(0, 83).addBox(0.0F, -11.9F, -2.3F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.LEG_LEFT);
		this.LegLeftAnchor.addChild(this.LegLeftMain);

		this.LegRightMain = new SpecialArmorModelRenderer(this, this.leggingsParts);
		this.LegRightMain.setRotationPoint(-0.3F, 19.0F, -3.4F);
		//this.LegRight.setTextureOffset(24, 24).addBox(-4.0F, -5.0F, 1.0F, 4.45F, 10.4F, 4.8F, 0.0F, false, AnchorType.LEG_RIGHT);
		this.LegRightMain.setTextureOffset(0, 69).addBox(-3.95F, -5.0F, 1.1F, 4.4F, 2.0F, 4.6F, 0.01F, false, AnchorType.LEG_RIGHT);
		//this.LegRight.setTextureOffset(0, 77).addBox(-3.9F, -6.9F, 1.1F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.LEG_RIGHT);
		this.LegRightAnchor.addChild(this.LegRightMain);


		this.BootLeft = new SpecialArmorModelRenderer(this, this.bootsParts);
		this.BootLeft.setRotationPoint(0.0F, 24.0F, 0.0F);
		//this.BootLeft.setTextureOffset(0, 0).addBox(0.3F, -7.0F, -3.4F, 4.4F, 2.0F, 1.0F, 0.0F, false, AnchorType.LEG_LEFT);
		this.BootLeft.setTextureOffset(0, 0).addBox(0.3F, -7.0F, -3.4F, 4.4F, 2.0F, 1.0F, 0.0F, false, AnchorType.LEG_LEFT);
		this.BootLeft.setTextureOffset(36, 36).addBox(-0.15F, -10.0F, -2.4F, 4.45F, 10.4F, 4.8F, 0.0F, false, AnchorType.LEG_LEFT);
		//this.BootLeft.setTextureOffset(20, 69).addBox(-0.15F, -10.0F, -2.3F, 4.4F, 2.0F, 4.6F, 0.0F, true, AnchorType.LEG_LEFT);
		//this.LegLeft.setTextureOffset(0, 83).addBox(0.0F, -11.9F, -2.3F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.LEG_LEFT);
		this.LegLeftAnchor.addChild(this.BootLeft);


		this.BootRight = new SpecialArmorModelRenderer(this, this.bootsParts);
		this.BootRight.setRotationPoint(-0.3F, 19.0F, -3.4F);
		//this.BootRight.setTextureOffset(0, 3).addBox(-4.4F, -2.0F, 0.0F, 4.4F, 2.0F, 1.0F, 0.0F, false, AnchorType.LEG_RIGHT);
		this.BootRight.setTextureOffset(24, 24).addBox(-4.0F, -5.0F, 1.0F, 4.45F, 10.4F, 4.8F, 0.0F, false, AnchorType.LEG_RIGHT);
		this.BootRight.setTextureOffset(0, 3).addBox(-4.4F, -2.0F, 0.0F, 4.4F, 2.0F, 1.0F, 0.0F, false, AnchorType.LEG_RIGHT);
		//this.BootRight.setTextureOffset(0, 69).addBox(-3.95F, -5.0F, 1.1F, 4.4F, 2.0F, 4.6F, 0.01F, false, AnchorType.LEG_RIGHT);
		//this.LegRight.setTextureOffset(0, 77).addBox(-3.9F, -6.9F, 1.1F, 4.2F, 1.0F, 4.6F, 0.0F, false, AnchorType.LEG_RIGHT);
		this.LegRightAnchor.addChild(this.BootRight);
	}

	@Override
	public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		this.setRenderingForType(this.slot);

		//IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(, model.getRenderType(EnigmaticLegacy.darkHelmet.TEXTURE), false, false);

		//this.helmet.showModel = this.slot == EquipmentSlotType.HEAD;
		//this.chestplate.showModel = this.slot == EquipmentSlotType.HEAD;
		//this.leggings.showModel = this.slot == EquipmentSlotType.HEAD;
		//this.boots.showModel = this.slot == EquipmentSlotType.HEAD;

		//legR.showModel = slot == EquipmentSlotType.LEGS;
		//legL.showModel = slot == EquipmentSlotType.LEGS;
		//bootL.showModel = slot == EquipmentSlotType.FEET;
		//bootR.showModel = slot == EquipmentSlotType.FEET;
		this.bipedHeadwear.showModel = false;

		this.bipedHead = this.HelmetAnchor;
		this.bipedBody = this.ChestAnchor;
		this.bipedRightArm = this.ArmRightAnchor;
		this.bipedLeftArm = this.ArmLeftAnchor;
		this.bipedRightLeg = this.LegRightAnchor;
		this.bipedLeftLeg = this.LegLeftAnchor;

		super.render(ms, buffer, light, overlay, r, g, b, a);

	}



}
