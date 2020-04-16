package com.integral.enigmaticlegacy.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ItemLoreHelper {

	public static ItemStack mergeDisplayData(ItemStack from, ItemStack to) {
		CompoundNBT nbt = from.getOrCreateChildTag("display");
		ListNBT loreList = nbt.getList("Lore", 8).size() > 0 ? nbt.getList("Lore", 8) : to.getOrCreateChildTag("display").getList("Lore", 8);
		StringNBT displayName = nbt.getString("Name").length() > 0 ? StringNBT.valueOf(nbt.getString("Name")) : StringNBT.valueOf(to.getOrCreateChildTag("display").getString("Name"));

		CompoundNBT mergedData = new CompoundNBT();
		mergedData.put("Lore", loreList.copy());
		mergedData.put("Name", displayName.copy());

		to.getOrCreateTag().put("display", mergedData);

		return to;
	}

	public static ItemStack addLoreString(ItemStack stack, String string) {
		CompoundNBT nbt = stack.getOrCreateChildTag("display");

	    ListNBT loreList = nbt.getList("Lore", 8);
	    loreList.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent(string))));

	    nbt.put("Lore", loreList);

	    return stack;
	}

	public static ItemStack setLoreString(ItemStack stack, String string, int index) {
		CompoundNBT nbt = stack.getOrCreateChildTag("display");

	    ListNBT loreList = nbt.getList("Lore", 8);
	    if (loreList.size()-1 >= index)
	    	loreList.set(index, StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent(string))));
	    else
	    	loreList.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent(string))));

	    nbt.put("Lore", loreList);

	    return stack;
	}

	public static ItemStack removeLoreString(ItemStack stack, int index) {
		CompoundNBT nbt = stack.getOrCreateChildTag("display");

	    ListNBT loreList = nbt.getList("Lore", 8);

	    if (index == -1 && loreList.size() > 0)
	    	loreList.remove(loreList.size()-1);
	    else if (loreList.size()-1 >= index)
	    	loreList.remove(index);

	    nbt.put("Lore", loreList);

	    return stack;
	}

	public static ItemStack setLastLoreString(ItemStack stack, String string) {
		CompoundNBT nbt = stack.getOrCreateChildTag("display");

	    ListNBT loreList = nbt.getList("Lore", 8);

	    if (loreList.size() > 0)
	    	loreList.set(loreList.size()-1, StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent(string))));
	    else
	    	loreList.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(new StringTextComponent(string))));

	    nbt.put("Lore", loreList);

	    return stack;
	}

	public static ItemStack setDisplayName(ItemStack stack, String name) {
		CompoundNBT nbt = stack.getOrCreateChildTag("display");

	    nbt.putString("Name", ITextComponent.Serializer.toJson(new StringTextComponent(name)));

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

				if (this.loreIndex != -1)
					this.handledString = this.handledString.replaceFirst(index, "");
			} else if (this.removeString) {
				this.handledString = this.handledString.replaceFirst("-!", "");
				String index = AnvilParser.parseIndex(this.handledString);
				this.loreIndex = Integer.parseInt(index);

				if (this.loreIndex != -1)
					this.handledString = this.handledString.replaceFirst(index, "");
			}

			this.handledString = AnvilParser.parseFormatting(this.handledString);

		}

		public static AnvilParser parseField(String field) {

			return new AnvilParser(field);
		}

		public static String parseFormatting(String field) {
			String formatter = new TranslationTextComponent("tooltip.enigmaticlegacy.paragraph").getFormattedText();
			String subformat = new TranslationTextComponent("tooltip.enigmaticlegacy.subformat").getFormattedText();

			return field.replace(subformat, formatter);
		}

		public static String parseIndex(String field) {
			String number = "";
			int index = -1;


			for (char symbol : field.toCharArray()) {
				if (Character.isDigit(symbol)) {
					number = number+symbol;
				} else
					break;

				if (number.length() >= 2)
					break;
			}

			if (!number.equals("")) {
				return number;
			}

			return ""+index;
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
