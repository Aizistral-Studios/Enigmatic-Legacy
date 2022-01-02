package com.integral.enigmaticlegacy.client.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

import net.minecraft.client.renderer.RenderState.TextureState;
import net.minecraft.client.renderer.RenderType.State;

public abstract class RenderTypes extends RenderType {

    private RenderTypes(String name, VertexFormat fmt, int glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable) {
        super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
        throw new IllegalStateException();
    }

    public static RenderType unlit(ResourceLocation textureLocation) {
        State renderState = State.builder()
                .setTextureState(new TextureState(textureLocation, false, false))
                .setTransparencyState(RenderState.NO_TRANSPARENCY)
                .setAlphaState(RenderState.DEFAULT_ALPHA)
                .setCullState(RenderState.NO_CULL)
                .setLightmapState(RenderState.LIGHTMAP)
                .setOverlayState(RenderState.OVERLAY)
                .createCompositeState(true);
        return RenderType.create("enigmatic_entity_unlit", DefaultVertexFormats.NEW_ENTITY, GL11.GL_QUADS, 256, true, false, renderState);
    }

    public static RenderType thyUnlit() {
        State renderState = State.builder()
        		.setTextureState(RenderState.BLOCK_SHEET_MIPPED)
                .setTransparencyState(RenderState.LIGHTNING_TRANSPARENCY)
                .setAlphaState(RenderState.DEFAULT_ALPHA)
                .setCullState(RenderState.NO_CULL)
                .setLightmapState(RenderState.LIGHTMAP)
                .setOverlayState(RenderState.OVERLAY)
                .setOutputState(RenderState.TRANSLUCENT_TARGET)
                .createCompositeState(true);
        return RenderType.create("enigmatic_block_unlit", DefaultVertexFormats.BLOCK, 7, 262144, true, true, renderState);
    }

}
