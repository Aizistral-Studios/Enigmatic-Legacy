package com.aizistral.enigmaticlegacy.client.models;

import java.util.Collections;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

/** Armor model that wraps another armor model */
public class UnseenArmorModel<T extends LivingEntity> extends HumanoidModel<T> {

	/** Creates a model part containing all elements of the given model */
	private static ModelPart copyOf(HumanoidModel<?> base) {
		ImmutableMap.Builder<String, ModelPart> mapBuilder = ImmutableMap.builder();
		mapBuilder.put("head", base.head);
		mapBuilder.put("hat", base.hat);
		mapBuilder.put("body", base.body);
		mapBuilder.put("right_arm", base.rightArm);
		mapBuilder.put("left_arm", base.leftArm);
		mapBuilder.put("right_leg", base.rightLeg);
		mapBuilder.put("left_leg", base.leftLeg);
		return new ModelPart(Collections.emptyList(), mapBuilder.build());
	}

	public UnseenArmorModel(HumanoidModel<T> base) {
		super(copyOf(base), base::renderType);
	}

	@Override
	public void renderToBuffer(PoseStack p_102034_, VertexConsumer p_102035_, int p_102036_, int p_102037_, float p_102038_, float p_102039_, float p_102040_, float p_102041_) {
		// NO-OP
	}

}
