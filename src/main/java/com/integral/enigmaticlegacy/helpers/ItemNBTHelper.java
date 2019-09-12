package com.integral.enigmaticlegacy.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * A couple of methods for mork convenient work with NBT of ItemStacks.
 * Originated from Botania's code.
 * @author Integral
 */

public final class ItemNBTHelper
{
    public static boolean detectNBT(final ItemStack stack) {
        return stack.hasTag();
    }
    
    public static void initNBT(final ItemStack stack) {
        if (!detectNBT(stack)) {
            injectNBT(stack, new CompoundNBT());
        }
    }
    
    public static void injectNBT(final ItemStack stack, final CompoundNBT nbt) {
        stack.setTag(nbt);
    }
    
    public static CompoundNBT getNBT(final ItemStack stack) {
        initNBT(stack);
        return stack.getTag();
    }
    
    public static void setBoolean(final ItemStack stack, final String tag, final boolean b) {
        getNBT(stack).putBoolean(tag, b);
    }
    
    public static void setByte(final ItemStack stack, final String tag, final byte b) {
        getNBT(stack).putByte(tag, b);
    }
    
    public static void setShort(final ItemStack stack, final String tag, final short s) {
        getNBT(stack).putShort(tag, s);
    }
    
    public static void setInt(final ItemStack stack, final String tag, final int i) {
        getNBT(stack).putInt(tag, i);
    }
    
    public static void setLong(final ItemStack stack, final String tag, final long l) {
        getNBT(stack).putLong(tag, l);
    }
    
    public static void setFloat(final ItemStack stack, final String tag, final float f) {
        getNBT(stack).putFloat(tag, f);
    }
    
    public static void setDouble(final ItemStack stack, final String tag, final double d) {
        getNBT(stack).putDouble(tag, d);
    }
    
    public static void setString(final ItemStack stack, final String tag, final String s) {
        getNBT(stack).putString(tag, s);
    }
    
    public static boolean verifyExistance(final ItemStack stack, final String tag) {
        return stack != null && getNBT(stack).contains(tag);
    }
    
    public static boolean getBoolean(final ItemStack stack, final String tag, final boolean defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getBoolean(tag) : defaultExpected;
    }
    
    public static byte getByte(final ItemStack stack, final String tag, final byte defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getByte(tag) : defaultExpected;
    }
    
    public static short getShort(final ItemStack stack, final String tag, final short defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getShort(tag) : defaultExpected;
    }
    
    public static int getInt(final ItemStack stack, final String tag, final int defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getInt(tag) : defaultExpected;
    }
    
    public static long getLong(final ItemStack stack, final String tag, final long defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getLong(tag) : defaultExpected;
    }
    
    public static float getFloat(final ItemStack stack, final String tag, final float defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getFloat(tag) : defaultExpected;
    }
    
    public static double getDouble(final ItemStack stack, final String tag, final double defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getDouble(tag) : defaultExpected;
    }
    
    public static CompoundNBT getCompound(final ItemStack stack, final String tag, final boolean nullifyOnFail) {
        return verifyExistance(stack, tag) ? getNBT(stack).getCompound(tag) : (nullifyOnFail ? null : new CompoundNBT());
    }
    
    public static String getString(final ItemStack stack, final String tag, final String defaultExpected) {
        return verifyExistance(stack, tag) ? getNBT(stack).getString(tag) : defaultExpected;
    }
    
}
