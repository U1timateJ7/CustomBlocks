package com.ulto.customblocks.util;

public class NumberConverter {
    public static int convertInt(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    public static float convertFloat(String s) {
        try {
            return Float.parseFloat(s.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    public static double convertDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    public static short convertShort(String s) {
        try {
            return Short.parseShort(s.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    public static long convertLong(String s) {
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    public static byte convertByte(String s) {
        try {
            return Byte.parseByte(s.trim());
        } catch (Exception e) {
            return 1;
        }
    }
}
