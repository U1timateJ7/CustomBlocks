package com.ulto.customblocks.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ulto.customblocks.GenerateCustomElements;
import com.ulto.customblocks.util.NumberConverter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BlockMakerInfoGUI extends LightweightGuiDescription {
    public BlockMakerInfoGUI() {
        WPlainPanel root = new WPlainPanel();
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        setRootPanel(scroll);
        root.setSize(300, 200);
        scroll.setSize(310, 210);
        WLabel topLabel = new WLabel(new TranslatableText("gui.block_maker_info.label.top"));
        root.add(topLabel, 120, 10);

        //Widgets
        WLabel firstLabel = new WLabel(new TranslatableText("gui.block_maker_info.label.first"));
        root.add(firstLabel, 10, 50);
    }
}
