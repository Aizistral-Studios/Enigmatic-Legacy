package com.aizistral.omniconfig.wrappers;

import com.aizistral.enigmaticlegacy.EnigmaticLegacy;
import com.aizistral.enigmaticlegacy.objects.Perhaps;
import com.aizistral.omniconfig.Configuration;

public class Omniconfig {

	public static final int STANDART_INTEGER_LIMIT = 32768;

	public static abstract class GenericParameter {
		protected String name = "unknownKey";
		protected String comment = "undefinedComment";
		protected String category = "undefinedCategory";
		protected boolean isSynchornized = false;
		protected boolean clientOnly = false;

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

		public boolean isSynchronized() {
			return this.isSynchornized;
		}

		public String getId() {
			return this.category + "$" + this.name;
		}

		public void setSynchronized(boolean isSyncable) {
			this.isSynchornized = isSyncable;
		}

		public boolean isClientOnly() {
			return this.clientOnly;
		}

		public void setClientOnly(boolean clientOnly) {
			this.clientOnly = clientOnly;
		}

		protected void logGenericParserError(String value) {
			EnigmaticLegacy.LOGGER.error("Error when parsing value of '" + this.name + "' in '" + this.category + "': " + value);
		}

		public abstract String valueToString();

		public abstract void parseFromString(String value);

		public abstract GenericParameter invoke(Configuration config);
	}

	public static class BooleanParameter extends GenericParameter {
		private boolean defaultValue;
		private boolean value;

		public BooleanParameter(boolean defaultValue) {
			super();
			this.defaultValue = defaultValue;
			this.value = this.defaultValue;
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
			if (!this.isClientOnly() || config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
				config.pushSynchronized(this.isSynchornized);
				this.value = config.getBoolean(this.name, this.category, this.defaultValue, this.comment);
			}
			return this;
		}

		@Override
		public String valueToString() {
			return Boolean.toString(this.value);
		}

		@Override
		public void parseFromString(String value) {
			try {
				this.value = Boolean.parseBoolean(value);
			} catch (Exception e) {
				this.logGenericParserError(value);
			}
		}

		@Override
		public String toString() {
			return Boolean.toString(this.value);
		}

	}

	public static class StringParameter extends GenericParameter {
		private String defaultValue;
		private String value;
		private String[] validValues;

		public StringParameter(String defaultValue) {
			super();
			this.defaultValue = defaultValue;
			this.value = this.defaultValue;

			this.validValues = null;
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

		public void setValidValues(String... validValues) {
			this.validValues = validValues;
		}

		public String[] getValidValues() {
			return this.validValues;
		}

		@Override
		public StringParameter invoke(Configuration config) {
			if (!this.isClientOnly() || config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
				config.pushSynchronized(this.isSynchornized);
				if (this.validValues == null) {
					this.value = config.getString(this.name, this.category, this.defaultValue, this.comment);
				} else {
					this.value = config.getString(this.name, this.category, this.defaultValue, this.comment, this.validValues);
				}
			}
			return this;
		}

		@Override
		public String valueToString() {
			return this.value;
		}

		@Override
		public void parseFromString(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return super.toString();
		}

	}

	public static class IntParameter extends GenericParameter {
		private int defaultValue;
		private int value;
		private int minValue = 0;
		private int maxValue = STANDART_INTEGER_LIMIT;

		public IntParameter(int defaultValue) {
			super();
			this.defaultValue = defaultValue;
			this.value = this.defaultValue;
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
			if (!this.isClientOnly() || config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
				config.pushSynchronized(this.isSynchornized);
				this.value = config.getInt(this.name, this.category, this.defaultValue, this.minValue, this.maxValue, this.comment);
			}
			return this;
		}

		@Override
		public String valueToString() {
			return Integer.toString(this.value);
		}

		@Override
		public void parseFromString(String value) {
			try {
				int parsed = Integer.parseInt(value);
				this.value = parsed < this.minValue ? this.minValue : (parsed > this.maxValue ? this.maxValue : parsed);
			} catch (Exception e) {
				this.logGenericParserError(value);
			}
		}

		@Override
		public String toString() {
			return Integer.toString(this.value);
		}

	}

	public static class DoubleParameter extends GenericParameter {
		private double defaultValue;
		private double value;
		private double minValue = 0;
		private double maxValue = STANDART_INTEGER_LIMIT;

		public DoubleParameter(double defaultValue) {
			super();
			this.defaultValue = defaultValue;
			this.value = this.defaultValue;
		}

		public double getDefaultValue() {
			return this.defaultValue;
		}

		public void setDefaultValue(double defaultValue) {
			this.defaultValue = defaultValue;
		}

		public double getValue() {
			return this.value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public double getMaxValue() {
			return this.maxValue;
		}

		public double getMinValue() {
			return this.minValue;
		}

		public void setMinValue(double minValue) {
			this.minValue = minValue;
		}

		public void setMaxValue(double maxValue) {
			this.maxValue = maxValue;
		}

		@Override
		public DoubleParameter invoke(Configuration config) {
			if (!this.isClientOnly() || config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
				config.pushSynchronized(this.isSynchornized);
				this.value = config.getDouble(this.name, this.category, this.defaultValue, this.minValue, this.maxValue, this.comment);
			}
			return this;
		}

		@Override
		public String valueToString() {
			return Double.toString(this.value);
		}

		@Override
		public void parseFromString(String value) {
			try {
				double parsed = Double.parseDouble(value);
				this.value = parsed < this.minValue ? this.minValue : (parsed > this.maxValue ? this.maxValue : parsed);
			} catch (Exception e) {
				this.logGenericParserError(value);
			}
		}

		@Override
		public String toString() {
			return Double.toString(this.value);
		}

	}

	public static class PerhapsParameter extends GenericParameter {
		private Perhaps defaultValue;
		private Perhaps value;
		private int minValue = 0;
		private int maxValue = 100;

		public PerhapsParameter(int defaultValue) {
			super();
			this.defaultValue = new Perhaps(defaultValue);
			this.value = this.defaultValue;
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
			if (!this.isClientOnly() || config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
				config.pushSynchronized(this.isSynchornized);
				this.value = new Perhaps(config.getInt(this.name, this.category, this.defaultValue.asPercentage(), this.minValue, this.maxValue, this.comment));
			}
			return this;
		}

		@Override
		public String valueToString() {
			return Integer.toString(this.value.asPercentage());
		}

		@Override
		public void parseFromString(String value) {
			try {
				int parsed = Integer.parseInt(value);
				int percentage = parsed < this.minValue ? this.minValue : (parsed > this.maxValue ? this.maxValue : parsed);

				this.value = new Perhaps(percentage);
			} catch (Exception e) {
				this.logGenericParserError(value);
			}
		}

		@Override
		public String toString() {
			return this.valueToString();
		}

	}

	public static class EnumParameter<T extends Enum<T>> extends GenericParameter {
		private final Class<T> clazz;
		private T defaultValue;
		private T[] validValues;
		private T value;

		public EnumParameter(T defaultValue) {
			super();
			this.clazz = defaultValue.getDeclaringClass();
			this.defaultValue = defaultValue;
			this.value = this.defaultValue;

			this.validValues = this.clazz.getEnumConstants();
		}

		public T getDefaultValue() {
			return this.defaultValue;
		}

		public void setDefaultValue(T defaultValue) {
			this.defaultValue = defaultValue;
		}

		public T getValue() {
			return this.value;
		}

		public void setValue(T value) {
			this.value = value;
		}

		@SuppressWarnings("unchecked")
		public void setValidValues(T... values) {
			this.validValues = values;
		}

		public T[] getValidValues() {
			return this.validValues;
		}

		@Override
		public EnumParameter<T> invoke(Configuration config) {
			// <V extends Enum<V>> ForgeConfigSpec
			if (!this.isClientOnly() || config.getSidedType() == Configuration.SidedConfigType.CLIENT) {
				config.pushSynchronized(this.isSynchornized);
				this.value = config.getEnum(this.name, this.category, this.defaultValue, this.comment, this.validValues);
			}
			return this;
		}

		@Override
		public String valueToString() {
			return this.value.toString();
		}

		@Override
		public void parseFromString(String value) {
			this.value = Enum.valueOf(this.clazz, value);
		}

		@Override
		public String toString() {
			return this.value.toString();
		}

	}

}
