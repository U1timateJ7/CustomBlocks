package com.ulto.customblocks.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.CustomResourceCreator;
import com.ulto.customblocks.GenerateCustomElements;
import com.ulto.customblocks.util.BlockUtils;
import com.ulto.customblocks.util.NumberConverter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockMakerGUI extends LightweightGuiDescription {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File blockFile;

    public BlockMakerGUI() {
        WPlainPanel root = new WPlainPanel();
        WPlainPanel panel = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        panel.setSize(411, 224);
        panel.add(scroll, 0, 0, 324, 224);
        setRootPanel(panel);
        root.setSize(300, 200);
        scroll.setSize(324, 224);
        WLabel topLabel = new WLabel(Text.translatable("gui.block_maker.label.top"));
        root.add(topLabel, 120, 10);
        WButton openFolderButton = new WButton(Text.translatable("gui.maker.button.open_folder"));
        openFolderButton.setOnClick(() -> Util.getOperatingSystem().open(GenerateCustomElements.blocksFolder));
        root.add(openFolderButton, 0, 0, 70, 20);

        //Widgets
        WTextField namespaceField = new WTextField(Text.literal("namespace"));
        namespaceField.setHost(this);
        namespaceField.setEditable(true);
        namespaceField.setMaxLength(50);
        root.add(namespaceField, 10, 50, 90, 20);
        WLabel namespaceLabel = new WLabel(Text.literal("(like \"minecraft\" in"));
        root.add(namespaceLabel, 10, 30);
        WLabel namespaceLabel2 = new WLabel(Text.literal("\"minecraft:stone\")"));
        root.add(namespaceLabel2, 10, 40);
        WTextField idField = new WTextField(Text.literal("id"));
        idField.setHost(this);
        idField.setEditable(true);
        idField.setMaxLength(50);
        root.add(idField, 110, 50, 90, 20);
        WLabel idLabel = new WLabel(Text.literal("(like \"stone\" in"));
        root.add(idLabel, 110, 30);
        WLabel idLabel2 = new WLabel(Text.literal("\"minecraft:stone\")"));
        root.add(idLabel2, 110, 40);
        WTextField displayNameField = new WTextField(Text.literal("display name"));
        displayNameField.setHost(this);
        displayNameField.setEditable(true);
        displayNameField.setMaxLength(50);
        root.add(displayNameField, 210, 50, 90, 20);
        WLabel displayNameLabel = new WLabel(Text.literal("the display name"));
        root.add(displayNameLabel, 210, 30);
        WLabel displayNameLabel2 = new WLabel(Text.literal("of the block"));
        root.add(displayNameLabel2, 210, 40);
        WTextField materialField = new WTextField(Text.literal("material"));
        materialField.setHost(this);
        materialField.setEditable(true);
        materialField.setMaxLength(10);
        root.add(materialField, 10, 100, 90, 20);
        WLabel materialLabel = new WLabel(Text.literal("the base material"));
        root.add(materialLabel, 10, 80);
        WLabel materialLabel2 = new WLabel(Text.literal("of the block"));
        root.add(materialLabel2, 10, 90);
        WTextField hardnessField = new WTextField(Text.literal("hardness"));
        hardnessField.setHost(this);
        hardnessField.setEditable(true);
        hardnessField.setMaxLength(10);
        root.add(hardnessField, 110, 100, 90, 20);
        WLabel hardnessLabel = new WLabel(Text.literal("how long it takes"));
        root.add(hardnessLabel, 110, 80);
        WLabel hardnessLabel2 = new WLabel(Text.literal("to break the block"));
        root.add(hardnessLabel2, 110, 90);
        WTextField resistanceField = new WTextField(Text.literal("resistance"));
        resistanceField.setHost(this);
        resistanceField.setEditable(true);
        resistanceField.setMaxLength(10);
        root.add(resistanceField, 210, 100, 90, 20);
        WLabel resistanceLabel = new WLabel(Text.literal("how blast"));
        root.add(resistanceLabel, 210, 80);
        WLabel resistanceLabel2 = new WLabel(Text.literal("resistant it is"));
        root.add(resistanceLabel2, 210, 90);
        WTextField slipperinessField = new WTextField(Text.literal("slipperiness"));
        slipperinessField.setHost(this);
        slipperinessField.setEditable(true);
        slipperinessField.setMaxLength(10);
        root.add(slipperinessField, 10, 150, 90, 20);
        WLabel slipperinessLabel = new WLabel(Text.literal("how slippery the"));
        root.add(slipperinessLabel, 10, 130);
        WLabel slipperinessLabel2 = new WLabel(Text.literal("block is (default: 0.6)"));
        root.add(slipperinessLabel2, 10, 140);
        WToggleButton hasGravityToggle = new WToggleButton(Text.literal("has gravity"));
        root.add(hasGravityToggle, 110, 150, 20, 20);
        WTextField rotationTypeField = new WTextField(Text.literal("rotation type"));
        rotationTypeField.setHost(this);
        rotationTypeField.setEditable(true);
        rotationTypeField.setMaxLength(13);
        root.add(rotationTypeField, 210, 150, 90, 20);
        WLabel rotationTypeLabel = new WLabel(Text.literal("the rotation type of"));
        root.add(rotationTypeLabel, 210, 130, 110, 10);
        WLabel rotationTypeLabel2 = new WLabel(Text.literal("the block (ex: none, axis)"));
        root.add(rotationTypeLabel2, 210, 140, 130, 10);
        WTextField mapColorField = new WTextField(Text.literal("map color"));
        mapColorField.setHost(this);
        mapColorField.setEditable(true);
        mapColorField.setMaxLength(20);
        root.add(mapColorField, 10, 200, 90, 20);
        WLabel mapColorLabel = new WLabel(Text.literal("the color shown on"));
        root.add(mapColorLabel, 0, 180);
        WLabel mapColorLabel2 = new WLabel(Text.literal("the map (ex: white)"));
        root.add(mapColorLabel2, 0, 190);
        WTextField soundsField = new WTextField(Text.literal("block sounds"));
        soundsField.setHost(this);
        soundsField.setEditable(true);
        soundsField.setMaxLength(23);
        root.add(soundsField, 110, 200, 90, 20);
        WLabel soundsLabel = new WLabel(Text.literal("the sounds the block"));
        root.add(soundsLabel, 110, 180);
        WLabel soundsLabel2 = new WLabel(Text.literal("make (ex: stone, wood)"));
        root.add(soundsLabel2, 110, 190);
        WTextField efficientToolField = new WTextField(Text.literal("efficient tool"));
        efficientToolField.setHost(this);
        efficientToolField.setEditable(true);
        efficientToolField.setMaxLength(10);
        root.add(efficientToolField, 210, 200, 90, 20);
        WTextField harvestLevelField = new WTextField(Text.literal("harvest level"));
        harvestLevelField.setHost(this);
        harvestLevelField.setEditable(true);
        harvestLevelField.setMaxLength(2);
        root.add(harvestLevelField, 10, 250, 90, 20);
        WLabel harvestLevelLabel = new WLabel(Text.literal("the harvest level"));
        root.add(harvestLevelLabel, 10, 230);
        WLabel harvestLevelLabel2 = new WLabel(Text.literal("of the block"));
        root.add(harvestLevelLabel2, 10, 240);
        WTextField luminanceField = new WTextField(Text.literal("luminance"));
        luminanceField.setHost(this);
        luminanceField.setEditable(true);
        luminanceField.setMaxLength(2);
        root.add(luminanceField, 110, 250, 90, 20);
        WLabel luminanceLabel = new WLabel(Text.literal("the light level"));
        root.add(luminanceLabel, 110, 230);
        WLabel luminanceLabel2 = new WLabel(Text.literal("the block makes"));
        root.add(luminanceLabel2, 110, 240);
        WTextField maxStackSizeField = new WTextField(Text.literal("max stack size"));
        maxStackSizeField.setHost(this);
        maxStackSizeField.setEditable(true);
        maxStackSizeField.setMaxLength(2);
        root.add(maxStackSizeField, 210, 250, 90, 20);
        WLabel maxStackSizeLabel = new WLabel(Text.literal("the max stack size"));
        root.add(maxStackSizeLabel, 210, 230);
        WLabel maxStackSizeLabel2 = new WLabel(Text.literal("of the block item"));
        root.add(maxStackSizeLabel2, 210, 240);
        WToggleButton fireproofToggle = new WToggleButton(Text.literal("fireproof"));
        root.add(fireproofToggle, 10, 300, 20, 20);
        WLabel fireproofLabel = new WLabel(Text.literal("is the block"));
        root.add(fireproofLabel, 10, 280);
        WLabel fireproofLabel2 = new WLabel(Text.literal("item fireproof"));
        root.add(fireproofLabel2, 10, 290);
        WTextField itemGroupField = new WTextField(Text.literal("item group"));
        itemGroupField.setHost(this);
        itemGroupField.setEditable(true);
        itemGroupField.setMaxLength(15);
        root.add(itemGroupField, 100, 300, 90, 20);
        WLabel itemGroupLabel = new WLabel(Text.literal("the tab that the"));
        root.add(itemGroupLabel, 100, 280);
        WLabel itemGroupLabel2 = new WLabel(Text.literal("block item is in"));
        root.add(itemGroupLabel2, 100, 290);
        WTextField textureNamespaceField = new WTextField(Text.literal("texture namespace"));
        textureNamespaceField.setHost(this);
        textureNamespaceField.setEditable(true);
        textureNamespaceField.setMaxLength(50);
        root.add(textureNamespaceField, 200, 300, 100, 20);
        WLabel textureNamespaceLabel = new WLabel(Text.literal("the namespace that"));
        root.add(textureNamespaceLabel, 200, 280);
        WLabel textureNamespaceLabel2 = new WLabel(Text.literal("the textures use"));
        root.add(textureNamespaceLabel2, 200, 290);
        WToggleButton useSingleTextureToggle = new WToggleButton(Text.literal("Use single texture."));
        root.add(useSingleTextureToggle, 10, 330);
        WTextField allTextureField = new WTextField(Text.literal("texture"));
        allTextureField.setHost(this);
        allTextureField.setEditable(true);
        allTextureField.setMaxLength(50);
        WTextField topTextureField = new WTextField(Text.literal("top texture"));
        topTextureField.setHost(this);
        topTextureField.setEditable(true);
        topTextureField.setMaxLength(50);
        root.add(topTextureField, 10, 350, 90, 20);
        WTextField bottomTextureField = new WTextField(Text.literal("bottom texture"));
        bottomTextureField.setHost(this);
        bottomTextureField.setEditable(true);
        bottomTextureField.setMaxLength(50);
        root.add(bottomTextureField, 110, 350, 90, 20);
        WTextField frontTextureField = new WTextField(Text.literal("front texture"));
        frontTextureField.setHost(this);
        frontTextureField.setEditable(true);
        frontTextureField.setMaxLength(50);
        root.add(frontTextureField, 210, 350, 90, 20);
        WTextField backTextureField = new WTextField(Text.literal("back texture"));
        backTextureField.setHost(this);
        backTextureField.setEditable(true);
        backTextureField.setMaxLength(50);
        root.add(backTextureField, 10, 400, 90, 20);
        WTextField rightTextureField = new WTextField(Text.literal("right texture"));
        rightTextureField.setHost(this);
        rightTextureField.setEditable(true);
        rightTextureField.setMaxLength(50);
        root.add(rightTextureField, 110, 400, 90, 20);
        WTextField leftTextureField = new WTextField(Text.literal("left texture"));
        leftTextureField.setHost(this);
        leftTextureField.setEditable(true);
        leftTextureField.setMaxLength(50);
        root.add(leftTextureField, 210, 400, 90, 20);
        useSingleTextureToggle.setOnToggle((toggle) -> {
            if (toggle) {
                root.add(allTextureField, 10, 350, 90, 20);
                root.remove(topTextureField);
                root.remove(bottomTextureField);
                root.remove(frontTextureField);
                root.remove(backTextureField);
                root.remove(rightTextureField);
                root.remove(leftTextureField);
            } else {
                root.remove(allTextureField);
                root.add(topTextureField, 10, 350, 90, 20);
                root.add(bottomTextureField, 110, 350, 90, 20);
                root.add(frontTextureField, 210, 350, 90, 20);
                root.add(backTextureField, 10, 400, 90, 20);
                root.add(rightTextureField, 110, 400, 90, 20);
                root.add(leftTextureField, 210, 400, 90, 20);
            }
        });

        WListPanel<Block, WButton> presets = new WListPanel<>(Registry.BLOCK.stream().toList().subList(1, Registry.BLOCK.stream().toList().size()), WButton::new, ((block, button) -> {
            button.setIcon(new ItemIcon(block.asItem()));
            button.setLabel(Text.translatable(block.getTranslationKey()));
            button.setOnClick(() -> {
                materialField.setText(BlockUtils.materialNameFromMaterial(block.settings.material));
                hardnessField.setText(String.valueOf(block.settings.hardness));
                resistanceField.setText(String.valueOf(block.settings.resistance));
                slipperinessField.setText(String.valueOf(block.settings.slipperiness));
                rotationTypeField.setText(BlockUtils.rotationTypeFromBlock(block));
                mapColorField.setText(BlockUtils.mapColorFromBlock(block));
                soundsField.setText(BlockUtils.blockSoundGroupfromBlock(block));
                efficientToolField.setText(BlockUtils.efficientToolFromBlock(block));
                harvestLevelField.setText(BlockUtils.harvestLevelFromBlock(block));
                luminanceField.setText(String.valueOf(block.settings.luminance.applyAsInt(block.getDefaultState())));
                maxStackSizeField.setText(String.valueOf(block.asItem().getMaxCount()));
                fireproofToggle.setToggle(block.asItem().isFireproof());
                itemGroupField.setText(block.asItem().getGroup() != null ? block.asItem().getGroup().getName() : "none");
            });
        }));
        presets.getScrollBar().setHost(this);
        final boolean[] showPresets = {false};
        WButton togglePresets = new WButton(Text.translatable("gui.block_maker.button.presets"));
        togglePresets.setOnClick(() -> {
            if (!showPresets[0]) {
                panel.setSize(464, 224);
                MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
                panel.add(presets, 324, 28, 140, 180);
                showPresets[0] = true;
            } else {
                panel.remove(presets);
                panel.setSize(411, 224);
                MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
                showPresets[0] = false;
            }
        });
        panel.add(togglePresets, 324, 7, 80, 20);
        WButton openTextureFolderButton = new WButton(Text.translatable("gui.maker.button.textures_folder"));
        openTextureFolderButton.setOnClick(() -> {
            if (!textureNamespaceField.getText().isEmpty()) {
                File destination = CustomResourceCreator.assets.toPath().resolve(textureNamespaceField.getText()).resolve("textures").resolve("block").toFile();
                destination.mkdirs();
                Util.getOperatingSystem().open(destination);
            }
            else if (!namespaceField.getText().isEmpty()) {
                File destination = CustomResourceCreator.assets.toPath().resolve(namespaceField.getText()).resolve("textures").resolve("block").toFile();
                destination.mkdirs();
                Util.getOperatingSystem().open(destination);
            }
        });
        root.add(openTextureFolderButton, 203, 0, 100, 20);
        WButton createButton = new WButton(Text.translatable("gui.block_maker.button.create"));
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
                if (!useSingleTextureToggle.getToggle()) {
                    block.getAsJsonObject("textures").addProperty("top_texture", topTextureField.getText());
                    block.getAsJsonObject("textures").addProperty("bottom_texture", bottomTextureField.getText());
                    block.getAsJsonObject("textures").addProperty("front_texture", frontTextureField.getText());
                    block.getAsJsonObject("textures").addProperty("back_texture", backTextureField.getText());
                    block.getAsJsonObject("textures").addProperty("right_texture", rightTextureField.getText());
                    block.getAsJsonObject("textures").addProperty("left_texture", leftTextureField.getText());
                } else {
                    block.getAsJsonObject("textures").addProperty("all", allTextureField.getText());
                }
                FileWriter blockFileWriter = new FileWriter(blockFile);
                blockFileWriter.write(gson.toJson(block));
                blockFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        root.add(createButton, 10, 450, 80, 20);
        WTextField packNameField = new WTextField(Text.literal("Pack Name"));
        packNameField.setHost(this);
        packNameField.setEditable(true);
        packNameField.setMaxLength(50);
        root.add(packNameField, 200, 450, 100, 20);
        WButton addToPackButton = new WButton(Text.translatable("gui.block_maker.button.add_to_pack"));
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
            for (File file : packs) {
                try {
                    BufferedReader packReader = new BufferedReader(new FileReader(file));
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = packReader.readLine()) != null) {
                        json.append(line);
                    }
                    JsonObject pack = new Gson().fromJson(json.toString(), JsonObject.class);
                    if (pack.get("name").getAsString().equals(packNameField.getText())) {
                        if (!pack.has("blocks")) {
                            pack.add("blocks", new JsonArray());
                        }
                        pack.getAsJsonArray("blocks").add(block);
                        FileWriter valuefw = new FileWriter(file);
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
        WLabel orLabel = new WLabel(Text.translatable("gui.block_maker.label.or"));
        root.add(orLabel, 95, 455);
    }

    private static void listFiles(final File folder, List<File> list) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                listFiles(fileEntry, list);
            } else {
                list.add(fileEntry);
            }
        }
    }
}
