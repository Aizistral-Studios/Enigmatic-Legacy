package com.integral.enigmaticlegacy.client.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public abstract class RenderTypes extends RenderType {

    private RenderTypes(String name, VertexFormat fmt, int glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable) {
        super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
        throw new IllegalStateException();
    }

    public static RenderType unlit(ResourceLocation textureLocation) {
        State renderState = State.getBuilder()
                .texture(new TextureState(textureLocation, false, false))
                .transparency(RenderState.NO_TRANSPARENCY)
                .alpha(RenderState.DEFAULT_ALPHA)
                .cull(RenderState.CULL_DISABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .overlay(RenderState.OVERLAY_ENABLED)
                .build(true);
        return RenderType.makeType("enigmatic_entity_unlit", DefaultVertexFormats.ENTITY, GL11.GL_QUADS, 256, true, false, renderState);
    }

    public static RenderType thyUnlit() {
        State renderState = State.getBuilder()
        		.texture(RenderState.BLOCK_SHEET_MIPPED)
                .transparency(RenderState.LIGHTNING_TRANSPARENCY)
                .alpha(RenderState.DEFAULT_ALPHA)
                .cull(RenderState.CULL_DISABLED)
                .lightmap(RenderState.LIGHTMAP_ENABLED)
                .overlay(RenderState.OVERLAY_ENABLED)
                .target(RenderState.field_239236_S_)
                .build(true);
        return RenderType.makeType("enigmatic_block_unlit", DefaultVertexFormats.BLOCK, 7, 262144, true, true, renderState);
    }

}
