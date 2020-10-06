package com.integral.omniconfig.wrappers;

import com.integral.enigmaticlegacy.objects.Perhaps;
import com.integral.omniconfig.Configuration;

public class EnigmaConfig {

	public static final int STANDART_INTEGER_LIMIT = 32768;

	public static abstract class GenericParameter {
		protected String name = "unknownKey";
		protected String comment = "undefinedComment";
		protected String category = "undefinedCategory";

		public GenericParameter() {
			// NO-OP
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCategory() {
			return this.category;
		}

		public String getName() {
			return this.name;
		}

		public String getComment() {
			return this.comment;
		}

		public abstract GenericParameter invoke(Configuration config);
	}

	public static class BooleanParameter extends GenericParameter {
		private boolean defaultValue;
		private boolean value = false;

		public BooleanParameter(boolean defaultValue) {
			super();
			this.defaultValue = defaultValue;
		}

		public boolean getDefaultValue() {
			return this.defaultValue;
		}

		public void setDefaultValue(boolean defaultValue) {
			this.defaultValue = defaultValue;
		}

		public boolean getValue() {
			return this.value;
		}

		public void setValue(boolean value) {
			this.value = value;
		}

		@Override
		public BooleanParameter invoke(Configuration config) {
			this.value = config.getBoolean(this.name, this.category, this.defaultValue, this.comment);
			return this;
		}

	}

	public static class StringParameter extends GenericParameter {
		private String defaultValue;
		private String value = "undefinedValue";

		public StringParameter(String defaultValue) {
			super();
			this.defaultValue = defaultValue;
		}

		public String getDefaultValue() {
			return this.defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getValue() {
			return this.value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public StringParameter invoke(Configuration config) {
			this.value = config.getString(this.name, this.category, this.defaultValue, this.comment);
			return this;
		}

	}

	public static class IntParameter extends GenericParameter {
		private int defaultValue;
		private int value = 0;
		private int minValue = 0;
		private int maxValue = STANDART_INTEGER_LIMIT;

		public IntParameter(int defaultValue) {
			super();
			this.defaultValue = defaultValue;
		}

		public int getDefaultValue() {
			return this.defaultValue;
		}

		public void setDefaultValue(int defaultValue) {
			this.defaultValue = defaultValue;
		}

		public int getValue() {
			return this.value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public int getMaxValue() {
			return this.maxValue;
		}

		public int getMinValue() {
			return this.minValue;
		}

		public void setMinValue(int minValue) {
			this.minValue = minValue;
		}

		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}

		@Override
		public IntParameter invoke(Configuration config) {
			this.value = config.getInt(this.name, this.category, this.defaultValue, this.minValue, this.maxValue, this.comment);
			return this;
		}

	}

	public static class FloatParameter extends GenericParameter {
		private float defaultValue;
		private float value = 0F;
		private float minValue = 0F;
		private float maxValue = STANDART_INTEGER_LIMIT;

		public FloatParameter(float defaultValue) {
			super();
			this.defaultValue = defaultValue;
		}

		public float getDefaultValue() {
			return this.defaultValue;
		}

		public void setDefaultValue(float defaultValue) {
			this.defaultValue = defaultValue;
		}

		public float getValue() {
			return this.value;
		}

		public void setValue(float value) {
			this.value = value;
		}

		public float getMaxValue() {
			return this.maxValue;
		}

		public float getMinValue() {
			return this.minValue;
		}

		public void setMinValue(float minValue) {
			this.minValue = minValue;
		}

		public void setMaxValue(float maxValue) {
			this.maxValue = maxValue;
		}

		@Override
		public FloatParameter invoke(Configuration config) {
			this.value = config.getFloat(this.name, this.category, this.defaultValue, this.minValue, this.maxValue, this.comment);
			return this;
		}

	}

	public static class PerhapsParameter extends GenericParameter {
		private Perhaps defaultValue;
		private Perhaps value = new Perhaps(0);
		private int minValue = 0;
		private int maxValue = 100;

		public PerhapsParameter(int defaultValue) {
			super();
			this.defaultValue = new Perhaps(defaultValue);
		}

		public Perhaps getDefaultValue() {
			return this.defaultValue;
		}

		public void setDefaultValue(int defaultValue) {
			this.defaultValue = new Perhaps(defaultValue);
		}

		public Perhaps getValue() {
			return this.value;
		}

		public void setValue(int value) {
			this.value = new Perhaps(value);
		}

		public int getMinValue() {
			return this.minValue;
		}

		public int getMaxValue() {
			return this.maxValue;
		}

		public void setMinValue(int minValue) {
			this.minValue = minValue;
		}

		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}

		@Override
		public PerhapsParameter invoke(Configuration config) {
			this.value = new Perhaps(config.getInt(this.name, this.category, this.defaultValue.asPercentage(), this.minValue, this.maxValue, this.comment));
			return this;
		}

	}

}
