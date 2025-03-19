package com.aizistral.enigmaticlegacy.helpers;

import net.minecraft.util.Tuple;

public class OverlayPositionHelper {

	public static enum AnchorPoint {
		CENTER(0, 0),
		TOP(0, -1),
		BOTTOM(0, 1),
		LEFT(-1, 0),
		RIGHT(1, 0),
		TOP_LEFT(-1, -1),
		BOTTOM_LEFT(-1, 1),
		TOP_RIGHT(1, -1),
		BOTTOM_RIGHT(1, 1);

		private final int xState;
		private final int yState;

		private AnchorPoint(int xState, int yState) {
			this.xState = xState;
			this.yState = yState;
		}

		public Tuple<Integer, Integer> calculateBasePos(int scaledWidth, int scaledHeigth) {
			int basePosX = this.xState == -1 ? 0 : (this.xState == 0 ? scaledWidth/2 : (this.xState == 1 ? scaledWidth : 0));
			int basePosY = this.yState == -1 ? 0 : (this.yState == 0 ? scaledHeigth/2 : (this.yState == 1 ? scaledHeigth : 0));

			return new Tuple<>(basePosX, basePosY);
		}
	}

	public static class OverlayPosition {
		public final AnchorPoint point;
		public final int offsetX;
		public final int offsetY;

		public OverlayPosition(AnchorPoint point, int offsetX, int offsetY) {
			this.point = point;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}

		public Tuple<Integer, Integer> calculatePosition(int scaledWidth, int scaledHeigth) {
			Tuple<Integer, Integer> basePos = this.point.calculateBasePos(scaledWidth, scaledHeigth);
			int posX = basePos.getA() + this.offsetX;
			int posY = basePos.getB() + this.offsetY;

			return new Tuple<>(posX, posY);
		}
	}

}
