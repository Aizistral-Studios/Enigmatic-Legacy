package com.integral.enigmaticlegacy.config;

import com.integral.enigmaticlegacy.objects.Perhaps;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;

public class OmnipotentConfig {

	public static class BooleanParameter implements IComplexParameter<BooleanParameter> {
		public ForgeConfigSpec.BooleanValue configObj;
		private boolean defaultValue;

		public BooleanParameter(boolean defaultValueIn) {
			this.defaultValue = defaultValueIn;
			ConfigHandler.allValues.add(this);
		}

		public boolean getValueDefault() {
			return this.defaultValue;
		}

		public void setDefaultValue(boolean value) {
			this.defaultValue = value;
		}

		public boolean getValue() {

			if (this.configObj != null)
				return this.configObj.get();
			else
				return this.defaultValue;
		}

		@Override
		public void reset() {
			if (this.configObj != null) {
				this.configObj.set(this.defaultValue);
				this.configObj.save();
			}
		}

	}

	public static class IntParameter implements IComplexParameter<IntParameter> {
		public ForgeConfigSpec.IntValue configObj;
		private int defaultValue;

		public IntParameter(int defaultValueIn) {
			this.defaultValue = defaultValueIn;
			ConfigHandler.allValues.add(this);
		}

		public int getValueDefault() {
			return this.defaultValue;
		}

		public void setDefaultValue(int value) {
			this.defaultValue = value;
		}

		public int getValue() {

			if (this.configObj != null)
				return this.configObj.get();
			else
				return this.defaultValue;
		}

		@Override
		public void reset() {
			if (this.configObj != null) {
				this.configObj.set(this.defaultValue);
				this.configObj.save();
			}
		}
	}

	public static class DoubleParameter implements IComplexParameter<DoubleParameter> {
		public ForgeConfigSpec.DoubleValue configObj;
		private double defaultValue;

		public DoubleParameter(double defaultValueIn) {
			this.defaultValue = defaultValueIn;
			ConfigHandler.allValues.add(this);
		}

		public double getValueDefault() {
			return this.defaultValue;
		}

		public void setDefaultValue(double value) {
			this.defaultValue = value;
		}

		public double getValue() {

			if (this.configObj != null)
				return this.configObj.get();
			else
				return this.defaultValue;
		}

		@Override
		public void reset() {
			if (this.configObj != null) {
				this.configObj.set(this.defaultValue);
				this.configObj.save();
			}
		}
	}

	public static class PerhapsParameter implements IComplexParameter<PerhapsParameter> {
		public ForgeConfigSpec.IntValue configObj;
		private Perhaps defaultValue;

		public PerhapsParameter(int defaultValueIn) {
			this.defaultValue = new Perhaps(defaultValueIn);

			ConfigHandler.allValues.add(this);
		}

		public Perhaps getValueDefault() {
			return this.defaultValue;
		}

		public void setDefaultValue(int value) {
			this.defaultValue = new Perhaps(value);
		}

		public Perhaps getValue() {

			if (this.configObj != null)
				return new Perhaps(this.configObj.get());
			else
				return this.defaultValue;
		}

		@Override
		public void reset() {
			if (this.configObj != null) {
				this.configObj.set(this.defaultValue.asPercentage());
				this.configObj.save();
			}
		}
	}

	public static class GenericParameter<Whatever> implements IComplexParameter<Whatever> {
		public ForgeConfigSpec.ConfigValue<Whatever> configObj;
		private Whatever defaultValue;

		public GenericParameter(Whatever defaultValueIn) {
			this.defaultValue = defaultValueIn;
			ConfigHandler.allValues.add(this);
		}

		public Whatever getValueDefault() {
			return this.defaultValue;
		}

		public void setDefaultValue(Whatever value) {
			this.defaultValue = value;
		}

		public Whatever getValue() {

			if (this.configObj != null)
				return this.configObj.get();
			else
				return this.defaultValue;
		}

		@Override
		public void reset() {
			if (this.configObj != null) {
				this.configObj.set(this.defaultValue);
				this.configObj.save();
			}
		}

	}
}
