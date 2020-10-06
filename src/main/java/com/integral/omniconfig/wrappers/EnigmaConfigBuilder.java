package com.integral.omniconfig.wrappers;

import com.google.common.collect.ImmutableList;
import com.integral.omniconfig.Configuration;
import com.integral.omniconfig.wrappers.EnigmaConfig.BooleanParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.FloatParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.GenericParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.IntParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.PerhapsParameter;
import com.integral.omniconfig.wrappers.EnigmaConfig.StringParameter;

import net.minecraftforge.fml.loading.FMLPaths;

import static com.integral.omniconfig.wrappers.EnigmaConfig.STANDART_INTEGER_LIMIT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnigmaConfigBuilder {
	private final Configuration config;
	private String currentCategory;
	private String currentComment;
	private float currentMin;
	private float currentMax;
	private final List<GenericParameter> invokationList;
	private boolean deferInvokation;

	private EnigmaConfigBuilder(Configuration config) {
		this.config = config;

		this.invokationList = new ArrayList<>();
		this.deferInvokation = false;

		this.resetCategory();
		this.resetComment();
		this.resetCurrentMin();
		this.resetCurrentMax();
	}

	public static EnigmaConfigBuilder setupBuilder(String fileName) {
		return setupBuilder(fileName, null);
	}

	public static EnigmaConfigBuilder setupBuilder(String fileName, String version) {
		return setupBuilder(fileName, false, version);
	}

	public static EnigmaConfigBuilder setupBuilder(String fileName, boolean caseSensitive, String version) {
		try {
			return setupBuilder(new File(FMLPaths.CONFIGDIR.get().toFile().getCanonicalFile(), fileName+".omniconf"), caseSensitive, version);
		} catch (IOException ex) {
			new RuntimeException("Something screwed up when loading config.", ex).printStackTrace();
			return null;
		}
	}

	public static EnigmaConfigBuilder setupBuilder(File file) {
		return setupBuilder(file, null);
	}

	public static EnigmaConfigBuilder setupBuilder(File file, String version) {
		return setupBuilder(file, false, version);
	}

	public static EnigmaConfigBuilder setupBuilder(File file, boolean caseSensitive, String version) {
		return new EnigmaConfigBuilder(new Configuration(file, version, caseSensitive));
	}

	public static EnigmaConfigBuilder setupBuilder(Configuration config) {
		return new EnigmaConfigBuilder(config);
	}

	private void resetCurrentMin() {
		this.currentMin = 0F;
	}

	private void resetCurrentMax() {
		this.currentMax = STANDART_INTEGER_LIMIT;
	}

	private void resetComment() {
		this.currentComment = "Undocumented property";
	}

	private void resetCategory() {
		this.currentCategory = Configuration.CATEGORY_GENERAL;
	}

	public void deferInvocation(boolean whetherOrNot) {
		this.deferInvokation = whetherOrNot;
	}

	public boolean isInvokationDeferred() {
		return this.deferInvokation;
	}

	public EnigmaConfigBuilder pushOverloadingAction(Consumer<Configuration> action) {
		this.config.attachOverloadingAction(action);
		return this;
	}

	public EnigmaConfigBuilder pushBeholderAttachment() {
		this.config.attachBeholder();
		return this;
	}

	public EnigmaConfigBuilder pushTerminateNonInvokedKeys(boolean whetherOrNot) {
		this.config.setTerminateNonInvokedKeys(whetherOrNot);
		return this;
	}

	public EnigmaConfigBuilder pushVersioningPolicy(Configuration.VersioningPolicy policy) {
		this.config.setVersioningPolicy(policy);
		return this;
	}

	public EnigmaConfigBuilder loadConfigFile() {
		this.config.load();
		return this;
	}

	public EnigmaConfigBuilder pushCategory(String name) {
		this.currentCategory = name;
		return this;
	}

	public EnigmaConfigBuilder pushCategory(String name, String comment) {
		this.pushCategory(name);
		this.config.addCustomCategoryComment(name, comment);
		return this;
	}

	public EnigmaConfigBuilder popCategory() {
		this.currentCategory = Configuration.CATEGORY_GENERAL;
		return this;
	}

	public EnigmaConfigBuilder pushComment(String comment) {
		this.currentComment = comment;
		return this;
	}

	public EnigmaConfigBuilder pushMin(int minValue) {
		return this.pushMin((float)minValue);
	}

	public EnigmaConfigBuilder pushMax(int maxValue) {
		return this.pushMax((float)maxValue);
	}

	public EnigmaConfigBuilder pushMin(float minValue) {
		this.currentMin = minValue;
		return this;
	}

	public EnigmaConfigBuilder pushMax(float maxValue) {
		this.currentMax = maxValue;
		return this;
	}

	public BooleanParameter generate(String name, boolean defaultValue) {
		BooleanParameter returned = new BooleanParameter(defaultValue);
		returned.setName(name);
		returned.setCategory(this.currentCategory);
		returned.setComment(this.currentComment);

		this.resetComment();

		this.invokationList.add(returned);
		if (!this.deferInvokation) {
			returned.invoke(this.config);
		}

		return returned;
	}

	public StringParameter generate(String name, String defaultValue) {
		StringParameter returned = new StringParameter(defaultValue);
		returned.setName(name);
		returned.setCategory(this.currentCategory);
		returned.setComment(this.currentComment);

		this.resetComment();

		this.invokationList.add(returned);
		if (!this.deferInvokation) {
			returned.invoke(this.config);
		}

		return returned;
	}

	public IntParameter generate(String name, int defaultValue) {
		IntParameter returned = new IntParameter(defaultValue);
		returned.setName(name);
		returned.setCategory(this.currentCategory);
		returned.setComment(this.currentComment);
		returned.setMinValue((int) this.currentMin);
		returned.setMaxValue((int) this.currentMax);

		this.resetComment();
		this.resetCurrentMin();
		this.resetCurrentMax();

		this.invokationList.add(returned);
		if (!this.deferInvokation) {
			returned.invoke(this.config);
		}

		return returned;
	}

	public FloatParameter generate(String name, float defaultValue) {
		FloatParameter returned = new FloatParameter(defaultValue);
		returned.setName(name);
		returned.setCategory(this.currentCategory);
		returned.setComment(this.currentComment);
		returned.setMinValue(this.currentMin);
		returned.setMaxValue(this.currentMax);

		this.resetComment();
		this.resetCurrentMin();
		this.resetCurrentMax();

		this.invokationList.add(returned);
		if (!this.deferInvokation) {
			returned.invoke(this.config);
		}

		return returned;
	}

	public PerhapsParameter perhaps(String name, int defaultPercentage) {
		PerhapsParameter returned = new PerhapsParameter(defaultPercentage);
		returned.setName(name);
		returned.setCategory(this.currentCategory);
		returned.setComment(this.currentComment);
		returned.setMinValue((int) this.currentMin);
		returned.setMaxValue((int) this.currentMax);

		this.resetComment();
		this.resetCurrentMin();
		this.resetCurrentMax();

		this.invokationList.add(returned);
		if (!this.deferInvokation) {
			returned.invoke(this.config);
		}

		return returned;
	}

	public Configuration build() {
		if (!this.deferInvokation) {
			this.config.save();
		}

		return this.config;
	}

	public List<GenericParameter> retrieveInvocationList() {
		return this.invokationList;
	}

}
