package com.integral.enigmaticlegacy.client.models;

import com.integral.enigmaticlegacy.config.JsonConfigHandler;
import com.integral.enigmaticlegacy.helpers.ObfuscatedFields;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unchecked")
public class SpecialArmorModelRenderer extends ModelRenderer {
	public static boolean CONFIG_UPDATED = false;
	//public static HashMap<String, Float> offsetMap = new HashMap<>();
	private GenericArmorModel model;

	public static enum AnchorType {
		HEAD("head"),
		CHEST("chest"),
		ARM_LEFT("arm_left"),
		ARM_RIGHT("arm_right"),
		LEG_LEFT("leg_left"),
		LEG_RIGHT("leg_right");

		public final String name;

		private AnchorType(String paramName) {
			this.name = paramName;
		}
	}

	public SpecialArmorModelRenderer(GenericArmorModel model, @Nullable List<SpecialArmorModelRenderer> includeIn) {
		super(model);
		model.accept(this);
		this.setTextureSize(model.textureWidth, model.textureHeight);

		if (includeIn != null)
			includeIn.add(this);

		this.model = model;
	}

	public SpecialArmorModelRenderer(GenericArmorModel model, int texOffX, int texOffY) {
		this(model.textureWidth, model.textureHeight, texOffX, texOffY);
		model.accept(this);

		this.model = model;
	}

	public SpecialArmorModelRenderer(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
		super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn);
		this.setTextureSize(textureWidthIn, textureHeightIn);
		this.setTextureOffset(textureOffsetXIn, textureOffsetYIn);
	}

	@Override
	public SpecialArmorModelRenderer setTextureOffset(int x, int y) {
		super.setTextureOffset(x, y);
		return this;
	}

	public void addBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn, float offsetX, float offsetY, float offsetZ) {
		super.addBox(x + offsetX, y + offsetY, z + offsetZ, width, height, depth, delta, mirrorIn);
	}

	public void addBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn, AnchorType anchorType) {
		float offsetX = 0F, offsetY = 0F, offsetZ = 0F;

		if (this.model instanceof DarkArmorModel) {
			offsetX = DarkArmorModel.darkArmorOffsetMap.get(anchorType.name + "_x");
			offsetY = DarkArmorModel.darkArmorOffsetMap.get(anchorType.name + "_y");
			offsetZ = DarkArmorModel.darkArmorOffsetMap.get(anchorType.name + "_z");

		} else {

			//offsetX = JsonConfigHandler.getFloat(anchorType.name + "_x");
			//offsetY = JsonConfigHandler.getFloat(anchorType.name + "_y");
			//offsetZ = JsonConfigHandler.getFloat(anchorType.name + "_z");

		}

		float deltaX = 0F, deltaY = 0F, deltaZ = 0F;

		deltaX = width - ((int)width);
		deltaY = height - ((int)height);
		deltaZ = depth - ((int)depth);

		deltaX /= 2;
		deltaY /= 2;
		deltaZ /= 2;

		this.addBox(x + offsetX + deltaX, y + offsetY + deltaY, z + offsetZ + deltaZ, (int)width, (int)height, (int)depth, deltaX+delta, deltaY+delta, deltaZ+delta, mirrorIn);
	}

	public void addBox(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirrorIn) {
		super.mirror = mirrorIn;
		super.addBox(x, y, z, width, height, depth, deltaX + 0.01F, deltaY + 0.01F, deltaZ + 0.01F);
	}

	public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn, AnchorType anchorType) {
		float offsetX = 0F, offsetY = 0F, offsetZ = 0F;

		if (this.model instanceof DarkArmorModel) {
			offsetX = DarkArmorModel.darkArmorOffsetMap.get(anchorType.name + "_x");
			offsetY = DarkArmorModel.darkArmorOffsetMap.get(anchorType.name + "_y");
			offsetZ = DarkArmorModel.darkArmorOffsetMap.get(anchorType.name + "_z");

		} else {

			//offsetX = JsonConfigHandler.getFloat(anchorType.name + "_x");
			//offsetY = JsonConfigHandler.getFloat(anchorType.name + "_y");
			//offsetZ = JsonConfigHandler.getFloat(anchorType.name + "_z");

		}

		this.rotationPointX = rotationPointXIn + offsetX;
		this.rotationPointY = rotationPointYIn + offsetY;
		this.rotationPointZ = rotationPointZIn + offsetZ;
	}
}