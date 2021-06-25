package com.ulto.customblocks.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ulto.customblocks.GenerateCustomElements;
import com.ulto.customblocks.util.NumberConverter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import net.minecraft.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockMakerGUI extends LightweightGuiDescription {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File blockFile;

    public BlockMakerGUI() {
        WPlainPanel root = new WPlainPanel();
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        setRootPanel(scroll);
        root.setSize(300, 200);
        scroll.setSize(310, 210);
        WLabel topLabel = new WLabel(new TranslatableText("gui.block_maker.label.top"));
        root.add(topLabel, 120, 10);
        WButton infoButton = new WButton(new TranslatableText("gui.block_maker.button.info"));
        infoButton.setOnClick(() -> MinecraftClient.getInstance().openScreen(new BlockMakerInfoScreen(new BlockMakerInfoGUI())));
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) root.add(infoButton, 260, 0, 40, 20);
        WButton openFolderButton = new WButton(new TranslatableText("gui.maker.button.open_folder"));
        openFolderButton.setOnClick(() -> Util.getOperatingSystem().open(GenerateCustomElements.blocksFolder));
        root.add(openFolderButton, 0, 0, 70, 20);

        //Widgets
        WTextField namespaceField = new WTextField(new LiteralText("namespace"));
        namespaceField.setHost(this);
        namespaceField.setEditable(true);
        namespaceField.setMaxLength(50);
        root.add(namespaceField, 10, 50, 90, 20);
        WLabel namespaceLabel = new WLabel(new LiteralText("(like \"minecraft\" in"));
        root.add(namespaceLabel, 10, 30);
        WLabel namespaceLabel2 = new WLabel(new LiteralText("\"minecraft:stone\")"));
        root.add(namespaceLabel2, 10, 40);
        WTextField idField = new WTextField(new LiteralText("id"));
        idField.setHost(this);
        idField.setEditable(true);
        idField.setMaxLength(50);
        root.add(idField, 110, 50, 90, 20);
        WLabel idLabel = new WLabel(new LiteralText("(like \"stone\" in"));
        root.add(idLabel, 110, 30);
        WLabel idLabel2 = new WLabel(new LiteralText("\"minecraft:stone\")"));
        root.add(idLabel2, 110, 40);
        WTextField displayNameField = new WTextField(new LiteralText("display name"));
        displayNameField.setHost(this);
        displayNameField.setEditable(true);
        displayNameField.setMaxLength(50);
        root.add(displayNameField, 210, 50, 90, 20);
        WLabel displayNameLabel = new WLabel(new LiteralText("the display name"));
        root.add(displayNameLabel, 210, 30);
        WLabel displayNameLabel2 = new WLabel(new LiteralText("of the block"));
        root.add(displayNameLabel2, 210, 40);
        WTextField materialField = new WTextField(new LiteralText("material"));
        materialField.setHost(this);
        materialField.setEditable(true);
        materialField.setMaxLength(10);
        root.add(materialField, 10, 100, 90, 20);
        WLabel materialLabel = new WLabel(new LiteralText("the base material"));
        root.add(materialLabel, 10, 80);
        WLabel materialLabel2 = new WLabel(new LiteralText("of the block"));
        root.add(materialLabel2, 10, 90);
        WTextField hardnessField = new WTextField(new LiteralText("hardness"));
        hardnessField.setHost(this);
        hardnessField.setEditable(true);
        hardnessField.setMaxLength(10);
        root.add(hardnessField, 110, 100, 90, 20);
        WLabel hardnessLabel = new WLabel(new LiteralText("how long it takes"));
        root.add(hardnessLabel, 110, 80);
        WLabel hardnessLabel2 = new WLabel(new LiteralText("to break the block"));
        root.add(hardnessLabel2, 110, 90);
        WTextField resistanceField = new WTextField(new LiteralText("resistance"));
        resistanceField.setHost(this);
        resistanceField.setEditable(true);
        resistanceField.setMaxLength(10);
        root.add(resistanceField, 210, 100, 90, 20);
        WLabel resistanceLabel = new WLabel(new LiteralText("how blast"));
        root.add(resistanceLabel, 210, 80);
        WLabel resistanceLabel2 = new WLabel(new LiteralText("resistant it is"));
        root.add(resistanceLabel2, 210, 90);
        WTextField slipperinessField = new WTextField(new LiteralText("slipperiness"));
        slipperinessField.setHost(this);
        slipperinessField.setEditable(true);
        slipperinessField.setMaxLength(10);
        root.add(slipperinessField, 10, 150, 90, 20);
        WLabel slipperinessLabel = new WLabel(new LiteralText("how slippery the"));
        root.add(slipperinessLabel, 10, 130);
        WLabel slipperinessLabel2 = new WLabel(new LiteralText("block is (default: 0.6)"));
        root.add(slipperinessLabel2, 10, 140);
        WToggleButton hasGravityToggle = new WToggleButton(new LiteralText("has gravity"));
        root.add(hasGravityToggle, 110, 150, 20, 20);
        WTextField rotationTypeField = new WTextField(new LiteralText("rotation type"));
        rotationTypeField.setHost(this);
        rotationTypeField.setEditable(true);
        rotationTypeField.setMaxLength(11);
        root.add(rotationTypeField, 210, 150, 90, 20);
        WLabel rotationTypeLabel = new WLabel(new LiteralText("the rotation type of"));
        root.add(rotationTypeLabel, 210, 130, 110, 10);
        WLabel rotationTypeLabel2 = new WLabel(new LiteralText("the block (ex: none, axis)"));
        root.add(rotationTypeLabel2, 210, 140, 130, 10);
        WTextField mapColorField = new WTextField(new LiteralText("map color"));
        mapColorField.setHost(this);
        mapColorField.setEditable(true);
        mapColorField.setMaxLength(20);
        root.add(mapColorField, 10, 200, 90, 20);
        WLabel mapColorLabel = new WLabel(new LiteralText("the color shown on"));
        root.add(mapColorLabel, 0, 180);
        WLabel mapColorLabel2 = new WLabel(new LiteralText("the map (ex: white)"));
        root.add(mapColorLabel2, 0, 190);
        WTextField soundsField = new WTextField(new LiteralText("block sounds"));
        soundsField.setHost(this);
        soundsField.setEditable(true);
        soundsField.setMaxLength(10);
        root.add(soundsField, 110, 200, 90, 20);
        WLabel soundsLabel = new WLabel(new LiteralText("the sounds the block"));
        root.add(soundsLabel, 110, 180);
        WLabel soundsLabel2 = new WLabel(new LiteralText("make (ex: stone, wood)"));
        root.add(soundsLabel2, 110, 190);
        WTextField efficientToolField = new WTextField(new LiteralText("efficient tool"));
        efficientToolField.setHost(this);
        efficientToolField.setEditable(true);
        efficientToolField.setMaxLength(10);
        root.add(efficientToolField, 210, 200, 90, 20);
        WTextField harvestLevelField = new WTextField(new LiteralText("harvest level"));
        harvestLevelField.setHost(this);
        harvestLevelField.setEditable(true);
        harvestLevelField.setMaxLength(2);
        root.add(harvestLevelField, 10, 250, 90, 20);
        WLabel harvestLevelLabel = new WLabel(new LiteralText("the harvest level"));
        root.add(harvestLevelLabel, 10, 230);
        WLabel harvestLevelLabel2 = new WLabel(new LiteralText("of the block"));
        root.add(harvestLevelLabel2, 10, 240);
        WTextField luminanceField = new WTextField(new LiteralText("luminance"));
        luminanceField.setHost(this);
        luminanceField.setEditable(true);
        luminanceField.setMaxLength(2);
        root.add(luminanceField, 110, 250, 90, 20);
        WLabel luminanceLabel = new WLabel(new LiteralText("the light level"));
        root.add(luminanceLabel, 110, 230);
        WLabel luminanceLabel2 = new WLabel(new LiteralText("the block makes"));
        root.add(luminanceLabel2, 110, 240);
        WTextField maxStackSizeField = new WTextField(new LiteralText("max stack size"));
        maxStackSizeField.setHost(this);
        maxStackSizeField.setEditable(true);
        maxStackSizeField.setMaxLength(2);
        root.add(maxStackSizeField, 210, 250, 90, 20);
        WLabel maxStackSizeLabel = new WLabel(new LiteralText("the max stack size"));
        root.add(maxStackSizeLabel, 210, 230);
        WLabel maxStackSizeLabel2 = new WLabel(new LiteralText("of the block item"));
        root.add(maxStackSizeLabel2, 210, 240);
        WToggleButton fireproofToggle = new WToggleButton(new LiteralText("fireproof"));
        root.add(fireproofToggle, 10, 300, 20, 20);
        WLabel fireproofLabel = new WLabel(new LiteralText("is the block"));
        root.add(fireproofLabel, 10, 280);
        WLabel fireproofLabel2 = new WLabel(new LiteralText("item fireproof"));
        root.add(fireproofLabel2, 10, 290);
        WTextField itemGroupField = new WTextField(new LiteralText("item group"));
        itemGroupField.setHost(this);
        itemGroupField.setEditable(true);
        itemGroupField.setMaxLength(15);
        root.add(itemGroupField, 100, 300, 90, 20);
        WLabel itemGroupLabel = new WLabel(new LiteralText("the tab that the"));
        root.add(itemGroupLabel, 100, 280);
        WLabel itemGroupLabel2 = new WLabel(new LiteralText("block item is in"));
        root.add(itemGroupLabel2, 100, 290);
        WTextField textureNamespaceField = new WTextField(new LiteralText("texture namespace"));
        textureNamespaceField.setHost(this);
        textureNamespaceField.setEditable(true);
        textureNamespaceField.setMaxLength(50);
        root.add(textureNamespaceField, 200, 300, 100, 20);
        WLabel textureNamespaceLabel = new WLabel(new LiteralText("the namespace that"));
        root.add(textureNamespaceLabel, 200, 280);
        WLabel textureNamespaceLabel2 = new WLabel(new LiteralText("the textures use"));
        root.add(textureNamespaceLabel2, 200, 290);
        WTextField topTextureField = new WTextField(new LiteralText("top texture"));
        topTextureField.setHost(this);
        topTextureField.setEditable(true);
        topTextureField.setMaxLength(50);
        root.add(topTextureField, 10, 350, 90, 20);
        WTextField bottomTextureField = new WTextField(new LiteralText("bottom texture"));
        bottomTextureField.setHost(this);
        bottomTextureField.setEditable(true);
        bottomTextureField.setMaxLength(50);
        root.add(bottomTextureField, 110, 350, 90, 20);
        WTextField frontTextureField = new WTextField(new LiteralText("front texture"));
        frontTextureField.setHost(this);
        frontTextureField.setEditable(true);
        frontTextureField.setMaxLength(50);
        root.add(frontTextureField, 210, 350, 90, 20);
        WTextField backTextureField = new WTextField(new LiteralText("back texture"));
        backTextureField.setHost(this);
        backTextureField.setEditable(true);
        backTextureField.setMaxLength(50);
        root.add(backTextureField, 10, 400, 90, 20);
        WTextField rightTextureField = new WTextField(new LiteralText("right texture"));
        rightTextureField.setHost(this);
        rightTextureField.setEditable(true);
        rightTextureField.setMaxLength(50);
        root.add(rightTextureField, 110, 400, 90, 20);
        WTextField leftTextureField = new WTextField(new LiteralText("left texture"));
        leftTextureField.setHost(this);
        leftTextureField.setEditable(true);
        leftTextureField.setMaxLength(50);
        root.add(leftTextureField, 210, 400, 90, 20);

        WButton createButton = new WButton(new TranslatableText("gui.block_maker.button.create"));
        createButton.setOnClick(() -> {
            blockFile = new File(GenerateCustomElements.blocksFolder.getName() + File.separator + idField.getText() + ".json");
            try {
                JsonObject block = new JsonObject();
                block.addProperty("namespace", namespaceField.getText());
                block.addProperty("id", idField.getText());
                block.addProperty("display_name", displayNameField.getText());
                if (materialField.getText().length() > 0) block.addProperty("material", materialField.getText());
                if (hardnessField.getText().length() > 0) block.addProperty("hardness", NumberConverter.convertFloat(hardnessField.getText()));
                if (resistanceField.getText().length() > 0) block.addProperty("resistance", NumberConverter.convertFloat(resistanceField.getText()));
                if (slipperinessField.getText().length() > 0) block.addProperty("slipperiness", NumberConverter.convertFloat(slipperinessField.getText()));
                if (hasGravityToggle.getToggle()) block.addProperty("has_gravity", hasGravityToggle.getToggle());
                if (rotationTypeField.getText().length() > 0) block.addProperty("rotation_type", rotationTypeField.getText());
                if (mapColorField.getText().length() > 0) block.addProperty("map_color", mapColorField.getText());
                if (soundsField.getText().length() > 0) block.addProperty("sounds", soundsField.getText());
                if (efficientToolField.getText().length() > 0) block.addProperty("efficient_tool", efficientToolField.getText());
                if (harvestLevelField.getText().length() > 0) block.addProperty("harvest_level", NumberConverter.convertInt(harvestLevelField.getText()));
                if (luminanceField.getText().length() > 0) block.addProperty("luminance", NumberConverter.convertInt(luminanceField.getText()));
                if (maxStackSizeField.getText().length() > 0) block.addProperty("max_stack_size", NumberConverter.convertInt(maxStackSizeField.getText()));
                if (fireproofToggle.getToggle()) block.addProperty("fireproof", fireproofToggle.getToggle());
                if (itemGroupField.getText().length() > 0) block.addProperty("item_group", itemGroupField.getText());
                if (textureNamespaceField.getText().length() > 0) block.addProperty("texture_namespace", textureNamespaceField.getText());
                block.add("textures", new JsonObject());
                block.getAsJsonObject("textures").addProperty("top_texture", topTextureField.getText());
                block.getAsJsonObject("textures").addProperty("bottom_texture", bottomTextureField.getText());
                block.getAsJsonObject("textures").addProperty("front_texture", frontTextureField.getText());
                block.getAsJsonObject("textures").addProperty("back_texture", backTextureField.getText());
                block.getAsJsonObject("textures").addProperty("right_texture", rightTextureField.getText());
                block.getAsJsonObject("textures").addProperty("left_texture", leftTextureField.getText());
                FileWriter blockFileWriter = new FileWriter(blockFile);
                blockFileWriter.write(gson.toJson(block));
                blockFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        root.add(createButton, 10, 450, 80, 20);
        WTextField packNameField = new WTextField(new LiteralText("Pack Name"));
        packNameField.setHost(this);
        packNameField.setEditable(true);
        packNameField.setMaxLength(50);
        root.add(packNameField, 200, 450, 100, 20);
        WButton addToPackButton = new WButton(new TranslatableText("gui.block_maker.button.add_to_pack"));
        addToPackButton.setOnClick(() -> {
            blockFile = new File(GenerateCustomElements.blocksFolder.getName() + File.separator + idField.getText() + ".json");
            List<File> packs = new ArrayList<>();
            listFiles(GenerateCustomElements.packsFolder, packs);
            JsonObject block = new JsonObject();
            block.addProperty("namespace", namespaceField.getText());
            block.addProperty("id", idField.getText());
            block.addProperty("display_name", displayNameField.getText());
            if (materialField.getText().length() > 0) block.addProperty("material", materialField.getText());
            if (hardnessField.getText().length() > 0) block.addProperty("hardness", NumberConverter.convertFloat(hardnessField.getText()));
            if (resistanceField.getText().length() > 0) block.addProperty("resistance", NumberConverter.convertFloat(resistanceField.getText()));
            if (slipperinessField.getText().length() > 0) block.addProperty("slipperiness", NumberConverter.convertFloat(slipperinessField.getText()));
            if (hasGravityToggle.getToggle()) block.addProperty("has_gravity", hasGravityToggle.getToggle());
            if (rotationTypeField.getText().length() > 0) block.addProperty("rotation_type", rotationTypeField.getText());
            if (mapColorField.getText().length() > 0) block.addProperty("map_color", mapColorField.getText());
            if (soundsField.getText().length() > 0) block.addProperty("sounds", soundsField.getText());
            if (efficientToolField.getText().length() > 0) block.addProperty("efficient_tool", efficientToolField.getText());
            if (harvestLevelField.getText().length() > 0) block.addProperty("harvest_level", NumberConverter.convertInt(harvestLevelField.getText()));
            if (luminanceField.getText().length() > 0) block.addProperty("luminance", NumberConverter.convertInt(luminanceField.getText()));
            if (maxStackSizeField.getText().length() > 0) block.addProperty("max_stack_size", NumberConverter.convertInt(maxStackSizeField.getText()));
            if (fireproofToggle.getToggle()) block.addProperty("fireproof", fireproofToggle.getToggle());
            if (itemGroupField.getText().length() > 0) block.addProperty("item_group", itemGroupField.getText());
            if (textureNamespaceField.getText().length() > 0) block.addProperty("texture_namespace", textureNamespaceField.getText());
            block.add("textures", new JsonObject());
            block.getAsJsonObject("textures").addProperty("top_texture", topTextureField.getText());
            block.getAsJsonObject("textures").addProperty("bottom_texture", bottomTextureField.getText());
            block.getAsJsonObject("textures").addProperty("front_texture", frontTextureField.getText());
            block.getAsJsonObject("textures").addProperty("back_texture", backTextureField.getText());
            block.getAsJsonObject("textures").addProperty("right_texture", rightTextureField.getText());
            block.getAsJsonObject("textures").addProperty("left_texture", leftTextureField.getText());
            for (File value : packs) {
                try {
                    BufferedReader packReader = new BufferedReader(new FileReader(value));
                    StringBuilder json = new StringBuilder();
                    String line = "";
                    while ((line = packReader.readLine()) != null) {
                        json.append(line);
                    }
                    JsonObject pack = new Gson().fromJson(json.toString(), JsonObject.class);
                    if (pack.get("name").getAsString().equals(packNameField.getText())) {
                        if (pack.has("blocks")) {
                            pack.getAsJsonArray("blocks").add(block);
                        }
                        FileWriter valuefw = new FileWriter(value);
                        valuefw.write(gson.toJson(pack));
                        valuefw.close();
                    }
                    packReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        root.add(addToPackButton, 110, 450, 80, 20);
        WLabel orLabel = new WLabel(new TranslatableText("gui.block_maker.label.or"));
        root.add(orLabel, 95, 455);
    }

    private static void listFiles(final File folder, List list) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                listFiles(fileEntry, list);
            } else {
                list.add(fileEntry);
            }
        }
    }
}
