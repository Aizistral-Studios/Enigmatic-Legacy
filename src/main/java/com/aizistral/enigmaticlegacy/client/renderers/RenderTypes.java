package com.aizistral.enigmaticlegacy.client.renderers;

/*
public abstract class RenderTypes extends RenderType {

	public RenderTypes(String p_173178_, VertexFormat p_173179_, Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
		super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
		throw new IllegalStateException();
	}

    private RenderTypes(String name, VertexFormat fmt, int glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable) {
        super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
        throw new IllegalStateException();
    }

	public static RenderType unlit(ResourceLocation textureLocation) {
		CompositeState renderState = CompositeState.builder()
				.setTextureState(new TextureStateShard(textureLocation, false, false))
				.setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
				.setAlphaState(RenderStateShard.DEFAULT_ALPHA)
				.setCullState(RenderStateShard.NO_CULL)
				.setLightmapState(RenderStateShard.LIGHTMAP)
				.setOverlayState(RenderStateShard.OVERLAY)
				.createCompositeState(true);
		return RenderType.create("enigmatic_entity_unlit", DefaultVertexFormat.NEW_ENTITY, GL11.GL_QUADS, 256, true, false, renderState);
	}

	public static RenderType thyUnlit() {
		CompositeState renderState = CompositeState.builder()
				.setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
				.setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
				.setAlphaState(RenderStateShard.DEFAULT_ALPHA)
				.setCullState(RenderStateShard.NO_CULL)
				.setLightmapState(RenderStateShard.LIGHTMAP)
				.setOverlayState(RenderStateShard.OVERLAY)
				.setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
				.createCompositeState(true);
		return RenderType.create("enigmatic_block_unlit", DefaultVertexFormat.BLOCK, 7, 262144, true, true, renderState);
	}

}
 */
