package com.ulto.customblocks.util;

import com.google.gson.JsonObject;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MiscConverter {
    public static List<Text> stringListToTextList(List<String> stringList) {
        List<Text> list = new ArrayList<>();
        for (String string : stringList) {
            list.add(Text.literal(string));
        }
        return list;
    }

    public static int rgbToColor(int r, int b, int g) {
        return r<<16 | g<<8 | b;
    }

    public static int jsonObjectToColor(JsonObject color) {
        int r = 255;
        if (color.has("red")) r = color.get("red").getAsInt();
        if (color.has("r")) r = color.get("r").getAsInt();
        int g = 255;
        if (color.has("green")) g = color.get("green").getAsInt();
        if (color.has("g")) g = color.get("g").getAsInt();
        int b = 255;
        if (color.has("blue")) b = color.get("blue").getAsInt();
        if (color.has("b")) b = color.get("b").getAsInt();
        return rgbToColor(r, g, b);
    }
}
