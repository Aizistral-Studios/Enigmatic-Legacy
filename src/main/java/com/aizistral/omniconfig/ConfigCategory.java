package com.aizistral.omniconfig;

import static com.aizistral.omniconfig.Configuration.COMMENT_SEPARATOR;
import static com.aizistral.omniconfig.Configuration.NEW_LINE;
import static com.aizistral.omniconfig.Configuration.allowedProperties;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class ConfigCategory implements Map<String, Property>
{
	private String name;
	private String comment;
	private String languagekey;
	private ArrayList<ConfigCategory> children = new ArrayList<ConfigCategory>();
	private Map<String, Property> properties = new TreeMap<String, Property>();
	private int propNumber = 0;
	public final ConfigCategory parent;
	private boolean changed = false;
	private boolean requiresWorldRestart = false;
	private boolean showInGui = true;
	private boolean requiresMcRestart = false;
	private List<String> propertyOrder = null;
	public boolean initialized = false;

	public ConfigCategory(String name)
	{
		this(name, null);
	}

	public ConfigCategory(String name, ConfigCategory parent)
	{
		this.name = name;
		this.parent = parent;
		if (parent != null)
		{
			parent.children.add(this);
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ConfigCategory)
		{
			ConfigCategory cat = (ConfigCategory)obj;
			return this.name.equals(cat.name) && this.children.equals(cat.children);
		}

		return false;
	}

	public String getName()
	{
		return this.name;
	}

	public String getQualifiedName()
	{
		return getQualifiedName(this.name, this.parent);
	}

	public static String getQualifiedName(String name, ConfigCategory parent)
	{
		return (parent == null ? name : parent.getQualifiedName() + Configuration.CATEGORY_SPLITTER + name);
	}

	public ConfigCategory getFirstParent()
	{
		return (this.parent == null ? this : this.parent.getFirstParent());
	}

	public boolean isChild()
	{
		return this.parent != null;
	}

	public Map<String, Property> getValues()
	{
		return ImmutableMap.copyOf(this.properties);
	}

	public List<Property> getOrderedValues()
	{
		if (this.propertyOrder != null)
		{
			ArrayList<Property> set = new ArrayList<Property>();
			for (String key : this.propertyOrder)
				if (this.properties.containsKey(key)) {
					set.add(this.properties.get(key));
				}

			return ImmutableList.copyOf(set);
		}
		else
			return ImmutableList.copyOf(this.properties.values());
	}

	public ConfigCategory setLanguageKey(String languagekey)
	{
		this.languagekey = languagekey;
		return this;
	}

	public String getLanguagekey()
	{
		if (this.languagekey != null)
			return this.languagekey;
		else
			return this.getQualifiedName();
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getComment()
	{
		return this.comment;
	}

	/**
	 * Sets the flag for whether or not this category can be edited while a world is running. Care should be taken to ensure
	 * that only properties that are truly dynamic can be changed from the in-game options menu. Only set this flag to
	 * true if all child properties/categories are unable to be modified while a world is running.
	 */
	public ConfigCategory setRequiresWorldRestart(boolean requiresWorldRestart)
	{
		this.requiresWorldRestart = requiresWorldRestart;
		return this;
	}

	/**
	 * Returns whether or not this category is able to be edited while a world is running using the in-game Mod Options screen
	 * as well as the Mods list screen, or only from the Mods list screen.
	 */
	public boolean requiresWorldRestart()
	{
		return this.requiresWorldRestart;
	}

	/**
	 * Sets whether or not this ConfigCategory should be allowed to show on config GUIs.
	 * Defaults to true.
	 */
	public ConfigCategory setShowInGui(boolean showInGui)
	{
		this.showInGui = showInGui;
		return this;
	}

	/**
	 * Gets whether or not this ConfigCategory should be allowed to show on config GUIs.
	 * Defaults to true unless set to false.
	 */
	public boolean showInGui()
	{
		return this.showInGui;
	}

	/**
	 * Sets whether or not this ConfigCategory requires Minecraft to be restarted when changed.
	 * Defaults to false. Only set this flag to true if ALL child properties/categories require
	 * Minecraft to be restarted when changed. Setting this flag will also prevent modification
	 * of the child properties/categories while a world is running.
	 */
	public ConfigCategory setRequiresMcRestart(boolean requiresMcRestart)
	{
		this.requiresMcRestart = this.requiresWorldRestart = requiresMcRestart;
		return this;
	}

	/**
	 * Gets whether or not this ConfigCategory requires Minecraft to be restarted when changed.
	 * Defaults to false unless set to true.
	 */
	public boolean requiresMcRestart()
	{
		return this.requiresMcRestart;
	}

	public ConfigCategory setPropertyOrder(List<String> propertyOrder)
	{
		this.propertyOrder = propertyOrder;
		for (String s : this.properties.keySet())
			if (!propertyOrder.contains(s)) {
				propertyOrder.add(s);
			}
		return this;
	}

	public List<String> getPropertyOrder()
	{
		if (this.propertyOrder != null)
			return ImmutableList.copyOf(this.propertyOrder);
		else
			return ImmutableList.copyOf(this.properties.keySet());
	}

	public boolean containsKey(String key)
	{
		return this.properties.containsKey(key);
	}

	public Property get(String key)
	{
		return this.properties.get(key);
	}

	private void write(BufferedWriter out, String... data) throws IOException
	{
		this.write(out, true, data);
	}

	private void write(BufferedWriter out, boolean new_line, String... data) throws IOException
	{
		for (String element : data) {
			out.write(element);
		}
		if (new_line) {
			out.write(NEW_LINE);
		}
	}

	public void write(BufferedWriter out, int indent) throws IOException
	{
		String pad0 = this.getIndent(indent);
		String pad1 = this.getIndent(indent + 1);
		String pad2 = this.getIndent(indent + 2);

		if (this.comment != null && !this.comment.isEmpty())
		{
			this.write(out, pad0, COMMENT_SEPARATOR);
			this.write(out, pad0, "# ", this.name);
			this.write(out, pad0, "#--------------------------------------------------------------------------------------------------------#");
			Splitter splitter = Splitter.onPattern("\r?\n");

			for (String line : splitter.split(this.comment))
			{
				this.write(out, pad0, "# ", line);
			}

			this.write(out, pad0, COMMENT_SEPARATOR, NEW_LINE);
		}

		String displayName = this.name;

		if (!allowedProperties.matchesAllOf(this.name))
		{
			displayName = '"' + this.name + '"';
		}

		this.write(out, pad0, displayName, " {");

		Property[] props = this.getOrderedValues().toArray(new Property[] {});

		for (int x = 0; x < props.length; x++)
		{
			Property prop = props[x];

			if (!prop.initialized) {
				continue;
			}

			if (prop.comment != null && !prop.comment.isEmpty())
			{
				if (x != 0)
				{
					out.newLine();
				}

				Splitter splitter = Splitter.onPattern("\r?\n");
				for (String commentLine : splitter.split(prop.comment))
				{
					this.write(out, pad1, "# ", commentLine);
				}
			}

			String propName = prop.getName();

			if (!allowedProperties.matchesAllOf(propName))
			{
				propName = '"' + propName + '"';
			}

			if (prop.isList())
			{
				char type = prop.getType().getID();

				this.write(out, pad1, String.valueOf(type), ":", propName, " <");

				for (String line : prop.getStringList())
				{
					this.write(out, pad2, line);
				}

				this.write(out, pad1, " >");
			}
			else if (prop.getType() == null)
			{
				this.write(out, pad1, propName, "=", prop.getString());
			}
			else
			{
				char type = prop.getType().getID();
				//System.out.println("Property: " + propName + ", value: " + prop.getString());
				this.write(out, pad1, String.valueOf(type), ":", propName, "=", prop.getString());
			}
		}

		if (this.children.size() > 0) {
			out.newLine();
		}

		for (ConfigCategory child : this.children)
		{
			if (child.initialized) {
				child.write(out, indent + 1);
			}
		}

		this.write(out, pad0, "}", NEW_LINE);
	}

	private String getIndent(int indent)
	{
		StringBuilder buf = new StringBuilder("");
		for (int x = 0; x < indent; x++)
		{
			buf.append("    ");
		}
		return buf.toString();
	}

	public boolean hasChanged()
	{
		if (this.changed) return true;
		for (Property prop : this.properties.values())
		{
			if (prop.hasChanged()) return true;
		}
		return false;
	}

	void resetChangedState()
	{
		this.changed = false;
		for (Property prop : this.properties.values())
		{
			prop.resetChangedState();
		}
	}


	//Map bouncer functions for compatibility with older mods, to be removed once all mods stop using it.
	@Override public int size(){ return this.properties.size(); }
	@Override public boolean isEmpty() { return this.properties.isEmpty(); }
	@Override public boolean containsKey(Object key) { return this.properties.containsKey(key); }
	@Override public boolean containsValue(Object value){ return this.properties.containsValue(value); }
	@Override public Property get(Object key) { return this.properties.get(key); }
	@Override public Property put(String key, Property value)
	{
		this.changed = true;
		if (this.propertyOrder != null && !this.propertyOrder.contains(key)) {
			this.propertyOrder.add(key);
		}
		return this.properties.put(key, value);
	}
	@Override public Property remove(Object key)
	{
		this.changed = true;
		return this.properties.remove(key);
	}
	@Override public void putAll(Map<? extends String, ? extends Property> m)
	{
		this.changed = true;
		if (this.propertyOrder != null) {
			for (String key : m.keySet())
				if (!this.propertyOrder.contains(key)) {
					this.propertyOrder.add(key);
				}
		}
		this.properties.putAll(m);
	}
	@Override public void clear()
	{
		this.changed = true;
		this.properties.clear();
	}
	@Override public Set<String> keySet() { return this.properties.keySet(); }
	@Override public Collection<Property> values() { return this.properties.values(); }

	@Override //Immutable copy, changes will NOT be reflected in this category
	public Set<java.util.Map.Entry<String, Property>> entrySet()
	{
		return ImmutableSet.copyOf(this.properties.entrySet());
	}

	public Set<ConfigCategory> getChildren(){ return ImmutableSet.copyOf(this.children); }

	public void removeChild(ConfigCategory child)
	{
		if (this.children.contains(child))
		{
			this.children.remove(child);
			this.changed = true;
		}
	}
}