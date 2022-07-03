package com.ulto.customblocks.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ulto.customblocks.GenerateCustomElements;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        WLabel topLabel = new WLabel(Text.translatable("gui.block_item_pack_maker.label.top"));
        root.add(topLabel, 120, 10);

        //Widgets
        WTextField nameField = new WTextField(Text.literal("name"));
        nameField.setHost(this);
        nameField.setEditable(true);
        nameField.setMaxLength(50);
        root.add(nameField, 100, 50, 100, 20);
        WLabel nameLabel = new WLabel(Text.literal("the name of"));
        root.add(nameLabel, 100, 30);
        WLabel nameLabel2 = new WLabel(Text.literal("the pack."));
        root.add(nameLabel2, 100, 40);

        WLabel helpLabel = new WLabel(Text.literal("You can add blocks/items to this pack by clicking"));
        root.add(helpLabel, 0, 80);
        WLabel helpLabel2 = new WLabel(Text.literal("\"Add to Pack\" after putting in the name"));
        root.add(helpLabel2, 0, 90);

        WButton createButton = new WButton(Text.translatable("gui.block_maker.button.create"));
        createButton.setOnClick(() -> {
            packFile = new File(GenerateCustomElements.packsFolder + File.separator + nameField.getText() + ".json");
            try {
                JsonObject pack = new JsonObject();
                pack.addProperty("name", nameField.getText());
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
