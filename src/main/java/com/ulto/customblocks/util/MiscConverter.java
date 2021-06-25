package com.ulto.customblocks.util;

import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MiscConverter {
    public static List<LiteralText> stringListToLiteralTextList(List<String> stringList) {
        List<LiteralText> list = new ArrayList<>();
        for (String string : stringList) {
            list.add(new LiteralText(string));
        }
        return list;
    }

    public static int rgbToColor(int r, int b, int g) {
        return r<<16 | g<<8 | b;
    }
}
