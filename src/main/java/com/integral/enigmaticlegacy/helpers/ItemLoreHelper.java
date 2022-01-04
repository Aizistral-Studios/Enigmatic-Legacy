package com.integral.enigmaticlegacy.helpers;

import java.util.List;

import javax.annotation.Nullable;

import com.integral.enigmaticlegacy.handlers.SuperpositionHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemLoreHelper {

	@OnlyIn(Dist.CLIENT)
	public static void indicateCursedOnesOnly(List<Component> list) {
		ChatFormatting format;

		if (Minecraft.getInstance().player != null) {
			format = SuperpositionHandler.isTheCursedOne(Minecraft.getInstance().player) ? ChatFormatting.GOLD : ChatFormatting.DARK_RED;
		} else {
			format = ChatFormatting.DARK_RED;
		}

		list.add(new TranslatableComponent("tooltip.enigmaticlegacy.cursedOnesOnly1").withStyle(format));
		list.add(new TranslatableComponent("tooltip.enigmaticlegacy.cursedOnesOnly2").withStyle(format));

	}

	public static void addLocalizedFormattedString(List<Component> list, String str, ChatFormatting format) {
		list.add(new TranslatableComponent(str).withStyle(format));
	}

	public static void addLocalizedString(List<Component> list, String str) {
		list.add(new TranslatableComponent(str));
	}

	public static void addLocalizedString(List<Component> list, String str, @Nullable ChatFormatting format, Object... values) {
		TextComponent[] stringValues = new TextComponent[values.length];

		int counter = 0;
		for (Object value : values) {
			TextComponent comp;

			if (value instanceof TextComponent) {
				comp = (TextComponent)value;
			} else {
				comp = new TextComponent(value.toString());
			}

			if (format != null) {
				comp.withStyle(format);
			}

			stringValues[counter] = comp;
			counter++;
		}

		list.add(new TranslatableComponent(str, (Object[])stringValues));
	}

	public static ItemStack mergeDisplayData(ItemStack from, ItemStack to) {
		CompoundTag nbt = from.getOrCreateTagElement("display");
		ListTag loreList = nbt.getList("Lore", 8).size() > 0 ? nbt.getList("Lore", 8) : to.getOrCreateTagElement("display").getList("Lore", 8);
		StringTag displayName = nbt.getString("Name").length() > 0 ? StringTag.valueOf(nbt.getString("Name")) : StringTag.valueOf(to.getOrCreateTagElement("display").getString("Name"));

		CompoundTag mergedData = new CompoundTag();
		mergedData.put("Lore", loreList.copy());
		mergedData.put("Name", displayName.copy());

		to.getOrCreateTag().put("display", mergedData);

		return to;
	}

	public static ItemStack addLoreString(ItemStack stack, String string) {
		CompoundTag nbt = stack.getOrCreateTagElement("display");

		ListTag loreList = nbt.getList("Lore", 8);
		loreList.add(StringTag.valueOf(Component.Serializer.toJson(new TextComponent(string))));

		nbt.put("Lore", loreList);

		return stack;
	}

	public static ItemStack setLoreString(ItemStack stack, String string, int index) {
		CompoundTag nbt = stack.getOrCreateTagElement("display");

		ListTag loreList = nbt.getList("Lore", 8);
		if (loreList.size() - 1 >= index) {
			loreList.set(index, StringTag.valueOf(Component.Serializer.toJson(new TextComponent(string))));
		} else {
			loreList.add(StringTag.valueOf(Component.Serializer.toJson(new TextComponent(string))));
		}

		nbt.put("Lore", loreList);

		return stack;
	}

	public static ItemStack removeLoreString(ItemStack stack, int index) {
		CompoundTag nbt = stack.getOrCreateTagElement("display");

		ListTag loreList = nbt.getList("Lore", 8);

		if (index == -1 && loreList.size() > 0) {
			loreList.remove(loreList.size() - 1);
		} else if (loreList.size() - 1 >= index) {
			loreList.remove(index);
		}

		nbt.put("Lore", loreList);

		return stack;
	}

	public static ItemStack setLastLoreString(ItemStack stack, String string) {
		CompoundTag nbt = stack.getOrCreateTagElement("display");

		ListTag loreList = nbt.getList("Lore", 8);

		if (loreList.size() > 0) {
			loreList.set(loreList.size() - 1, StringTag.valueOf(Component.Serializer.toJson(new TextComponent(string))));
		} else {
			loreList.add(StringTag.valueOf(Component.Serializer.toJson(new TextComponent(string))));
		}

		nbt.put("Lore", loreList);

		return stack;
	}

	public static ItemStack setDisplayName(ItemStack stack, String name) {
		CompoundTag nbt = stack.getOrCreateTagElement("display");

		nbt.putString("Name", Component.Serializer.toJson(new TextComponent(name)));

		return stack;
	}

	public static class AnvilParser {

		private boolean isLore;
		private int loreIndex;
		private boolean removeString;
		private String handledString;

		private AnvilParser(String string) {
			this.loreIndex = -1;
			this.handledString = string.toString();
			this.isLore = this.handledString.startsWith("!");
			this.removeString = this.handledString.startsWith("-!");

			if (this.isLore) {
				this.handledString = this.handledString.replaceFirst("!", "");
				String index = AnvilParser.parseIndex(this.handledString);
				this.loreIndex = Integer.parseInt(index);

				if (this.loreIndex != -1) {
					this.handledString = this.handledString.replaceFirst(index, "");
				}
			} else if (this.removeString) {
				this.handledString = this.handledString.replaceFirst("-!", "");
				String index = AnvilParser.parseIndex(this.handledString);
				this.loreIndex = Integer.parseInt(index);

				if (this.loreIndex != -1) {
					this.handledString = this.handledString.replaceFirst(index, "");
				}
			}

			this.handledString = AnvilParser.parseFormatting(this.handledString);

		}

		public static AnvilParser parseField(String field) {
			return new AnvilParser(field);
		}

		private static String parseFormatting(String field) {
			String formatter = new TranslatableComponent("tooltip.enigmaticlegacy.paragraph").getString();
			String subformat = new TranslatableComponent("tooltip.enigmaticlegacy.subformat").getString();

			return field.replace(subformat, formatter);
		}

		private static String parseIndex(String field) {
			String number = "";
			int index = -1;

			for (char symbol : field.toCharArray()) {
				if (Character.isDigit(symbol)) {
					number = number + symbol;
				} else {
					break;
				}

				if (number.length() >= 2) {
					break;
				}
			}

			if (!number.equals(""))
				return number;

			return "" + index;
		}

		public boolean isLoreString() {
			return this.isLore;
		}

		public boolean shouldRemoveString() {
			return this.removeString;
		}

		public int getLoreIndex() {
			return this.loreIndex;
		}

		public String getFormattedString() {
			return this.handledString;
		}

	}

}
