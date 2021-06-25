package com.ulto.customblocks.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.GenerateCustomElements;
import com.ulto.customblocks.util.NumberConverter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockItemPackMakerGUI extends LightweightGuiDescription {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File packFile;

    public BlockItemPackMakerGUI() {
        WPlainPanel root = new WPlainPanel();
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        setRootPanel(scroll);
        root.setSize(300, 150);
        scroll.setSize(310, 160);
        WLabel topLabel = new WLabel(new TranslatableText("gui.block_item_pack_maker.label.top"));
        root.add(topLabel, 120, 10);

        //Widgets
        WTextField nameField = new WTextField(new LiteralText("name"));
        nameField.setHost(this);
        nameField.setEditable(true);
        nameField.setMaxLength(50);
        root.add(nameField, 50, 50, 100, 20);
        WLabel nameLabel = new WLabel(new LiteralText("the name of"));
        root.add(nameLabel, 50, 30);
        WLabel nameLabel2 = new WLabel(new LiteralText("the pack."));
        root.add(nameLabel2, 50, 40);
        WTextField typeField = new WTextField(new LiteralText("block, item or both"));
        typeField.setHost(this);
        typeField.setEditable(true);
        typeField.setMaxLength(50);
        root.add(typeField, 155, 50, 100, 20);
        WLabel typeLabel = new WLabel(new LiteralText("if anything else"));
        root.add(typeLabel, 155, 30);
        WLabel typeLabel2 = new WLabel(new LiteralText("it is block"));
        root.add(typeLabel2, 155, 40);

        WLabel helpLabel = new WLabel(new LiteralText("You can add blocks/items to this pack by clicking"));
        root.add(helpLabel, 0, 80);
        WLabel helpLabel2 = new WLabel(new LiteralText("\"Add to Pack\" after putting in the name"));
        root.add(helpLabel2, 0, 90);

        WButton createButton = new WButton(new TranslatableText("gui.block_maker.button.create"));
        createButton.setOnClick(() -> {
            packFile = new File(GenerateCustomElements.packsFolder.getName() + File.separator + nameField.getText() + ".json");
            try {
                JsonObject pack = new JsonObject();
                pack.addProperty("name", nameField.getText());
                if (typeField.getText().equals("item")) pack.add("items", new JsonArray());
                if (typeField.getText().equals("both")) pack.add("items", new JsonArray());
                if (!typeField.getText().equals("item")) pack.add("blocks", new JsonArray());
                FileWriter packFileWriter = new FileWriter(packFile);
                packFileWriter.write(gson.toJson(pack));
                packFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        root.add(createButton, 100, 100, 100, 20);
    }
}
