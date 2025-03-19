package com.aizistral.omniconfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class ConfigElement<T>
{
	private Property prop;
	private Property.Type type;
	private boolean isProperty;
	private ConfigCategory ctgy;
	private boolean categoriesFirst = true;

	public ConfigElement(ConfigCategory ctgy)
	{
		this.ctgy = ctgy;
		this.isProperty = false;
	}

	public ConfigElement(Property prop)
	{
		this.prop = prop;
		this.type = prop.getType();
		this.isProperty = true;
	}

	public ConfigElement<T> listCategoriesFirst(boolean categoriesFirst)
	{
		this.categoriesFirst = categoriesFirst;
		return this;
	}


	public List<ConfigElement> getChildElements()
	{
		if (!this.isProperty)
		{
			List<ConfigElement> elements = new ArrayList<ConfigElement>();
			Iterator<ConfigCategory> ccI = this.ctgy.getChildren().iterator();
			Iterator<Property> pI = this.ctgy.getOrderedValues().iterator();
			int index = 0;

			if (this.categoriesFirst) {
				while (ccI.hasNext())
				{
					ConfigElement temp = new ConfigElement(ccI.next());
					if (temp.showInGui()) {
						elements.add(temp);
					}
				}
			}

			while (pI.hasNext())
			{
				ConfigElement<?> temp = getTypedElement(pI.next());
				if (temp.showInGui()) {
					elements.add(temp);
				}
			}

			if (!this.categoriesFirst) {
				while (ccI.hasNext())
				{
					ConfigElement temp = new ConfigElement(ccI.next());
					if (temp.showInGui()) {
						elements.add(temp);
					}
				}
			}

			return elements;
		}
		return null;
	}

	public static ConfigElement<?> getTypedElement(Property prop)
	{
		switch (getType(prop))
		{
			case BOOLEAN:
				return new ConfigElement<Boolean>(prop);
			case DOUBLE:
				return new ConfigElement<Double>(prop);
			case INTEGER:
				return new ConfigElement<Integer>(prop);
			default:
				return new ConfigElement<String>(prop);
		}
	}


	public String getName()
	{
		return this.isProperty ? this.prop.getName() : this.ctgy.getName();
	}


	public boolean isProperty()
	{
		return this.isProperty;
	}


	public String getQualifiedName()
	{
		return this.isProperty ? this.prop.getName() : this.ctgy.getQualifiedName();
	}


	public ConfigSupertype getType()
	{
		return this.isProperty ? getType(this.prop) : ConfigSupertype.CONFIG_CATEGORY;
	}

	public static ConfigSupertype getType(Property prop)
	{
		return prop.getType() == Property.Type.BOOLEAN ? ConfigSupertype.BOOLEAN : prop.getType() == Property.Type.DOUBLE ? ConfigSupertype.DOUBLE :
			prop.getType() == Property.Type.INTEGER ? ConfigSupertype.INTEGER : prop.getType() == Property.Type.COLOR ? ConfigSupertype.COLOR :
				prop.getType() == Property.Type.MOD_ID ? ConfigSupertype.MOD_ID : ConfigSupertype.STRING;
	}


	public boolean isList()
	{
		return this.isProperty && this.prop.isList();
	}


	public boolean isListLengthFixed()
	{
		return this.isProperty && this.prop.isListLengthFixed();
	}


	public int getMaxListLength()
	{
		return this.isProperty ? this.prop.getMaxListLength() : -1;
	}


	public String getComment()
	{
		return this.isProperty ? this.prop.comment : this.ctgy.getComment();
	}


	public boolean isDefault()
	{
		return !this.isProperty || this.prop.isDefault();
	}


	public void setToDefault()
	{
		if (this.isProperty) {
			this.prop.setToDefault();
		}
	}


	public boolean requiresWorldRestart()
	{
		return this.isProperty ? this.prop.requiresWorldRestart() : this.ctgy.requiresWorldRestart();
	}


	public boolean showInGui()
	{
		return this.isProperty ? this.prop.showInGui() : this.ctgy.showInGui();
	}


	public boolean requiresMcRestart()
	{
		return this.isProperty ? this.prop.requiresMcRestart() : this.ctgy.requiresMcRestart();
	}


	public String[] getValidValues()
	{
		return this.isProperty ? this.prop.getValidValues() : null;
	}


	public String getLanguageKey()
	{
		return this.isProperty ? this.prop.getLanguageKey() : this.ctgy.getLanguagekey();
	}


	public Object getDefault()
	{
		return this.isProperty ? (T) this.prop.getDefault() : null;
	}


	public Object[] getDefaults()
	{
		if (this.isProperty)
		{
			String[] aVal = this.prop.getDefaults();
			if (this.type == Property.Type.BOOLEAN)
			{
				Boolean[] ba = new Boolean[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					ba[i] = Boolean.valueOf(aVal[i]);
				}
				return ba;
			}
			else if (this.type == Property.Type.DOUBLE)
			{
				Double[] da = new Double[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					da[i] = Double.valueOf(aVal[i].toString());
				}
				return da;
			}
			else if (this.type == Property.Type.INTEGER)
			{
				Integer[] ia = new Integer[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					ia[i] = Integer.valueOf(aVal[i].toString());
				}
				return ia;
			}
			else
				return aVal;
		}
		return null;
	}


	public Pattern getValidationPattern()
	{
		return this.isProperty ? this.prop.getValidationPattern() : null;
	}


	public Object get()
	{
		return this.isProperty ? (T) this.prop.getString() : null;
	}


	public Object[] getList()
	{
		if (this.isProperty)
		{
			String[] aVal = this.prop.getStringList();
			if (this.type == Property.Type.BOOLEAN)
			{
				Boolean[] ba = new Boolean[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					ba[i] = Boolean.valueOf(aVal[i]);
				}
				return ba;
			}
			else if (this.type == Property.Type.DOUBLE)
			{
				Double[] da = new Double[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					da[i] = Double.valueOf(aVal[i].toString());
				}
				return da;
			}
			else if (this.type == Property.Type.INTEGER)
			{
				Integer[] ia = new Integer[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					ia[i] = Integer.valueOf(aVal[i].toString());
				}
				return ia;
			}
			else
				return aVal;
		}
		return null;
	}


	public void set(T value)
	{
		if (this.isProperty)
		{
			if (this.type == Property.Type.BOOLEAN) {
				this.prop.set(Boolean.parseBoolean(value.toString()));
			} else if (this.type == Property.Type.DOUBLE) {
				this.prop.set(Double.parseDouble(value.toString()));
			} else if (this.type == Property.Type.INTEGER) {
				this.prop.set(Integer.parseInt(value.toString()));
			} else {
				this.prop.set(value.toString());
			}
		}
	}


	public void set(T[] aVal)
	{
		if (this.isProperty)
		{
			if (this.type == Property.Type.BOOLEAN)
			{
				boolean[] ba = new boolean[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					ba[i] = Boolean.valueOf(aVal[i].toString());
				}
				this.prop.set(ba);
			}
			else if (this.type == Property.Type.DOUBLE)
			{
				double[] da = new double[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					da[i] = Double.valueOf(aVal[i].toString());
				}
				this.prop.set(da);
			}
			else if (this.type == Property.Type.INTEGER)
			{
				int[] ia = new int[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					ia[i] = Integer.valueOf(aVal[i].toString());
				}
				this.prop.set(ia);
			}
			else
			{
				String[] is = new String[aVal.length];
				for(int i = 0; i < aVal.length; i++) {
					is[i] = aVal[i].toString();
				}
				this.prop.set(is);
			}
		}
	}


	public T getMinValue()
	{
		return this.isProperty ? (T) this.prop.getMinValue() : null;
	}


	public T getMaxValue()
	{
		return this.isProperty ? (T) this.prop.getMaxValue() : null;
	}
}