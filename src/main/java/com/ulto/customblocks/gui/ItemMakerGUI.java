package com.ulto.customblocks.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ulto.customblocks.CustomResourceCreator;
import com.ulto.customblocks.GenerateCustomElements;
import com.ulto.customblocks.util.NumberConverter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemMakerGUI extends LightweightGuiDescription {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File itemFile;

    public ItemMakerGUI() {
        WPlainPanel root = new WPlainPanel();
        WPlainPanel panel = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        WPlainPanel foodRoot = new WPlainPanel();
        foodRoot.setInsets(Insets.ROOT_PANEL);
        WScrollPanel foodScroll = new WScrollPanel(foodRoot);
        foodScroll.setHost(this);
        WTabPanel main = new WTabPanel();
        main.add(new WTabPanel.Tab.Builder(scroll).title(Text.translatable("gui.item_maker.tab.item")).icon(new ItemIcon(Items.DIAMOND)).build());
        main.add(new WTabPanel.Tab.Builder(foodScroll).title(Text.translatable("gui.item_maker.tab.food")).icon(new ItemIcon(Items.APPLE)).build());
        setRootPanel(panel);
        root.setSize(300, 200);
        scroll.setSize(324, 224);
        foodRoot.setSize(300, 200);
        foodScroll.setSize(324, 224);
        main.setSize(324, 254);
        panel.setSize(411, 254);
        panel.add(main, 0, 0, 324, 254);
        WLabel topLabel = new WLabel(Text.translatable("gui.item_maker.label.top"));
        root.add(topLabel, 120, 10);
        WLabel foodTopLabel = new WLabel(Text.translatable("gui.item_maker.food.label.top"));
        foodRoot.add(foodTopLabel, 120, 10);
        WButton openFolderButton = new WButton(Text.translatable("gui.maker.button.open_folder"));
        openFolderButton.setOnClick(() -> Util.getOperatingSystem().open(GenerateCustomElements.itemsFolder));
        root.add(openFolderButton, 0, 0, 70, 20);

        //Widgets
        WTextField namespaceField = new WTextField(Text.literal("namespace"));
        namespaceField.setHost(this);
        namespaceField.setEditable(true);
        namespaceField.setMaxLength(50);
        root.add(namespaceField, 10, 50, 90, 20);
        WLabel namespaceLabel = new WLabel(Text.literal("(like \"minecraft\" in"));
        root.add(namespaceLabel, 10, 30);
        WLabel namespaceLabel2 = new WLabel(Text.literal("\"minecraft:apple\")"));
        root.add(namespaceLabel2, 10, 40);
        WTextField idField = new WTextField(Text.literal("id"));
        idField.setHost(this);
        idField.setEditable(true);
        idField.setMaxLength(50);
        root.add(idField, 110, 50, 90, 20);
        WLabel idLabel = new WLabel(Text.literal("(like \"apple\" in"));
        root.add(idLabel, 110, 30);
        WLabel idLabel2 = new WLabel(Text.literal("\"minecraft:apple\")"));
        root.add(idLabel2, 110, 40);
        WTextField displayNameField = new WTextField(Text.literal("display name"));
        displayNameField.setHost(this);
        displayNameField.setEditable(true);
        displayNameField.setMaxLength(50);
        root.add(displayNameField, 210, 50, 90, 20);
        WLabel displayNameLabel = new WLabel(Text.literal("the display name"));
        root.add(displayNameLabel, 210, 30);
        WLabel displayNameLabel2 = new WLabel(Text.literal("of the item"));
        root.add(displayNameLabel2, 210, 40);
        WTextField maxStackSizeField = new WTextField(Text.literal("max stack size"));
        maxStackSizeField.setHost(this);
        maxStackSizeField.setEditable(true);
        maxStackSizeField.setMaxLength(2);
        root.add(maxStackSizeField, 10, 100, 90, 20);
        WLabel maxStackSizeLabel = new WLabel(Text.literal("the max stack size"));
        root.add(maxStackSizeLabel, 10, 80);
        WLabel maxStackSizeLabel2 = new WLabel(Text.literal("of the item"));
        root.add(maxStackSizeLabel2, 10, 90);
        WToggleButton fireproofToggle = new WToggleButton(Text.literal("fireproof"));
        root.add(fireproofToggle, 110, 100, 20, 20);
        WLabel fireproofLabel = new WLabel(Text.literal("is the item"));
        root.add(fireproofLabel, 110, 80);
        WLabel fireproofLabel2 = new WLabel(Text.literal("fireproof"));
        root.add(fireproofLabel2, 110, 90);
        WTextField itemGroupField = new WTextField(Text.literal("item group"));
        itemGroupField.setHost(this);
        itemGroupField.setEditable(true);
        itemGroupField.setMaxLength(15);
        root.add(itemGroupField, 210, 100, 90, 20);
        WLabel itemGroupLabel = new WLabel(Text.literal("the tab that the"));
        root.add(itemGroupLabel, 210, 80);
        WLabel itemGroupLabel2 = new WLabel(Text.literal("item is in"));
        root.add(itemGroupLabel2, 210, 90);
        WTextField textureNamespaceField = new WTextField(Text.literal("texture namespace"));
        textureNamespaceField.setHost(this);
        textureNamespaceField.setEditable(true);
        textureNamespaceField.setMaxLength(50);
        root.add(textureNamespaceField, 10, 150, 100, 20);
        WLabel textureNamespaceLabel = new WLabel(Text.literal("the namespace that"));
        root.add(textureNamespaceLabel, 10, 130);
        WLabel textureNamespaceLabel2 = new WLabel(Text.literal("the texture uses"));
        root.add(textureNamespaceLabel2, 10, 140);
        WTextField textureField = new WTextField(Text.literal("texture"));
        textureField.setHost(this);
        textureField.setEditable(true);
        textureField.setMaxLength(50);
        root.add(textureField, 120, 150, 90, 20);

        //Food Widgets
        foodRoot.add(openFolderButton, 0, 0, 70, 20);
        foodRoot.add(namespaceField, 10, 50, 90, 20);
        foodRoot.add(namespaceLabel, 10, 30);
        foodRoot.add(namespaceLabel2, 10, 40);
        foodRoot.add(idField, 110, 50, 90, 20);
        foodRoot.add(idLabel, 110, 30);
        foodRoot.add(idLabel2, 110, 40);
        foodRoot.add(displayNameField, 210, 50, 90, 20);
        foodRoot.add(displayNameLabel, 210, 30);
        foodRoot.add(displayNameLabel2, 210, 40);
        foodRoot.add(maxStackSizeField, 10, 100, 90, 20);
        foodRoot.add(maxStackSizeLabel, 10, 80);
        foodRoot.add(maxStackSizeLabel2, 10, 90);
        foodRoot.add(fireproofToggle, 110, 100, 20, 20);
        foodRoot.add(fireproofLabel, 110, 80);
        foodRoot.add(fireproofLabel2, 110, 90);
        foodRoot.add(itemGroupField, 210, 100, 90, 20);
        foodRoot.add(itemGroupLabel, 210, 80);
        foodRoot.add(itemGroupLabel2, 210, 90);
        foodRoot.add(textureNamespaceField, 10, 150, 100, 20);
        foodRoot.add(textureNamespaceLabel, 10, 130);
        foodRoot.add(textureNamespaceLabel2, 10, 140);
        foodRoot.add(textureField, 120, 150, 90, 20);

        //Food Specific Widgets
        WTextField nutritionField = new WTextField(Text.literal("nutrition"));
        nutritionField.setHost(this);
        nutritionField.setEditable(true);
        nutritionField.setMaxLength(2);
        foodRoot.add(nutritionField, 210, 150, 90, 20);
        WLabel nutritionLabel = new WLabel(Text.literal("the amount of hunger"));
        foodRoot.add(nutritionLabel, 210, 130);
        WLabel nutritionLabel2 = new WLabel(Text.literal("points the food gives"));
        foodRoot.add(nutritionLabel2, 210, 140);
        WTextField saturationField = new WTextField(Text.literal("saturation"));
        saturationField.setHost(this);
        saturationField.setEditable(true);
        saturationField.setMaxLength(10);
        foodRoot.add(saturationField, 10, 200, 90, 20);
        WLabel saturationLabel = new WLabel(Text.literal("the saturation"));
        foodRoot.add(saturationLabel, 10, 180);
        WLabel saturationLabel2 = new WLabel(Text.literal("given (ex: 1.2)"));
        foodRoot.add(saturationLabel2, 10, 190);
        WToggleButton isMeatToggle = new WToggleButton(Text.literal("is meat"));
        foodRoot.add(isMeatToggle, 110, 200, 90, 20);
        WLabel isMeatLabel = new WLabel(Text.literal("can you feed this"));
        foodRoot.add(isMeatLabel, 110, 180);
        WLabel isMeatLabel2 = new WLabel(Text.literal("food to dogs"));
        foodRoot.add(isMeatLabel2, 110, 190);
        WTextField eatingSpeedField = new WTextField(Text.literal("eating speed"));
        eatingSpeedField.setHost(this);
        eatingSpeedField.setEditable(true);
        eatingSpeedField.setMaxLength(2);
        foodRoot.add(eatingSpeedField, 210, 200, 90, 20);
        WLabel eatingSpeedLabel = new WLabel(Text.literal("the speed in ticks"));
        foodRoot.add(eatingSpeedLabel, 210, 180);
        WLabel eatingSpeedLabel2 = new WLabel(Text.literal("of eating (default: 32)"));
        foodRoot.add(eatingSpeedLabel2, 210, 190);
        WToggleButton alwaysEdibleToggle = new WToggleButton(Text.literal("always edible"));
        foodRoot.add(alwaysEdibleToggle, 110, 250, 90, 20);
        WLabel alwaysEdibleLabel = new WLabel(Text.literal("is this always edible"));
        foodRoot.add(alwaysEdibleLabel, 110, 230);
        WLabel alwaysEdibleLabel2 = new WLabel(Text.literal(""));
        foodRoot.add(alwaysEdibleLabel2, 110, 240);
        WButton foodCreateButton = new WButton(Text.translatable("gui.block_maker.button.create"));
        foodCreateButton.setOnClick(() -> {
            itemFile = new File(GenerateCustomElements.itemsFolder.getName() + File.separator + idField.getText() + ".json");
            try {
                JsonObject block = new JsonObject();
                block.addProperty("namespace", namespaceField.getText());
                block.addProperty("id", idField.getText());
                block.addProperty("display_name", displayNameField.getText());
                if (maxStackSizeField.getText().length() > 0) block.addProperty("max_stack_size", NumberConverter.convertInt(maxStackSizeField.getText()));
                if (fireproofToggle.getToggle()) block.addProperty("fireproof", fireproofToggle.getToggle());
                if (itemGroupField.getText().length() > 0) block.addProperty("item_group", itemGroupField.getText());
                if (textureNamespaceField.getText().length() > 0) block.addProperty("texture_namespace", textureNamespaceField.getText());
                block.addProperty("texture", textureField.getText());
                block.add("food", new JsonObject());
                JsonObject food = block.getAsJsonObject("food");
                if (nutritionField.getText().length() > 0) food.addProperty("nutrition", NumberConverter.convertInt(nutritionField.getText()));
                if (saturationField.getText().length() > 0) food.addProperty("saturation", NumberConverter.convertFloat(saturationField.getText()));
                if (isMeatToggle.getToggle()) food.addProperty("meat", isMeatToggle.getToggle());
                if (alwaysEdibleToggle.getToggle()) food.addProperty("always_edible", alwaysEdibleToggle.getToggle());
                if (eatingSpeedField.getText().length() > 0) food.addProperty("eating_speed", NumberConverter.convertInt(eatingSpeedField.getText()));
                FileWriter blockFileWriter = new FileWriter(itemFile);
                blockFileWriter.write(gson.toJson(block));
                blockFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        foodRoot.add(foodCreateButton, 10, 300, 80, 20);

        //Other Widgets
        List<Item> itemList = new ArrayList<>();
        for (Item item : Registry.ITEM.stream().toList()) if (!(item instanceof BlockItem) && !item.getDefaultStack().isEmpty()) itemList.add(item);

        WListPanel<Item, WButton> presets = new WListPanel<>(itemList, WButton::new, ((item, button) -> {
            button.setIcon(new ItemIcon(item));
            button.setLabel(Text.translatable(item.getTranslationKey()));
            button.setOnClick(() -> {
                maxStackSizeField.setText(String.valueOf(item.getMaxCount()));
                fireproofToggle.setToggle(item.isFireproof());
                itemGroupField.setText(item.getGroup() != null ? item.getGroup().getName() : "none");
                if (item.isFood()) {
                    FoodComponent food = item.getFoodComponent();
                    nutritionField.setText(String.valueOf(food.getHunger()));
                    saturationField.setText(String.valueOf(food.getSaturationModifier()));
                    isMeatToggle.setToggle(food.isMeat());
                    eatingSpeedField.setText(food.isSnack() ? "16" : "32");
                    alwaysEdibleToggle.setToggle(food.isAlwaysEdible());
                } else {
                    nutritionField.setText("");
                    saturationField.setText("");
                    isMeatToggle.setToggle(false);
                    eatingSpeedField.setText("");
                    alwaysEdibleToggle.setToggle(false);
                }
            });
        }));
        presets.getScrollBar().setHost(this);
        final boolean[] showPresets = {false};
        WButton togglePresets = new WButton(Text.translatable("gui.block_maker.button.presets"));
        togglePresets.setOnClick(() -> {
            if (!showPresets[0]) {
                panel.setSize(464, 254);
                MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
                panel.add(presets, 324, 28, 140, 180);
                showPresets[0] = true;
            } else {
                panel.remove(presets);
                panel.setSize(411, 254);
                MinecraftClient.getInstance().setScreen(MinecraftClient.getInstance().currentScreen);
                showPresets[0] = false;
            }
        });
        panel.add(togglePresets, 324, 7, 80, 20);
        WButton openTextureFolderButton = new WButton(Text.translatable("gui.maker.button.textures_folder"));
        openTextureFolderButton.setOnClick(() -> {
            if (!textureNamespaceField.getText().isEmpty()) {
                File destination = CustomResourceCreator.assets.toPath().resolve(textureNamespaceField.getText()).resolve("textures").resolve("item").toFile();
                destination.mkdirs();
                Util.getOperatingSystem().open(destination);
            }
            else if (!namespaceField.getText().isEmpty()) {
                File destination = CustomResourceCreator.assets.toPath().resolve(namespaceField.getText()).resolve("textures").resolve("item").toFile();
                destination.mkdirs();
                Util.getOperatingSystem().open(destination);
            }
        });
        root.add(openTextureFolderButton, 210, 0, 100, 20);
        foodRoot.add(openTextureFolderButton, 210, 0, 100, 20);
        WButton createButton = new WButton(Text.translatable("gui.block_maker.button.create"));
        createButton.setOnClick(() -> {
            itemFile = new File(GenerateCustomElements.itemsFolder.getName() + File.separator + idField.getText() + ".json");
            try {
                JsonObject item = new JsonObject();
                item.addProperty("namespace", namespaceField.getText());
                item.addProperty("id", idField.getText());
                item.addProperty("display_name", displayNameField.getText());
                if (maxStackSizeField.getText().length() > 0) item.addProperty("max_stack_size", NumberConverter.convertInt(maxStackSizeField.getText()));
                if (fireproofToggle.getToggle()) item.addProperty("fireproof", fireproofToggle.getToggle());
                if (itemGroupField.getText().length() > 0) item.addProperty("item_group", itemGroupField.getText());
                if (textureNamespaceField.getText().length() > 0) item.addProperty("texture_namespace", textureNamespaceField.getText());
                item.addProperty("texture", textureField.getText());
                FileWriter blockFileWriter = new FileWriter(itemFile);
                blockFileWriter.write(gson.toJson(item));
                blockFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        root.add(createButton, 10, 200, 80, 20);
        WTextField packNameField = new WTextField(Text.literal("Pack Name"));
        packNameField.setHost(this);
        packNameField.setEditable(true);
        packNameField.setMaxLength(50);
        root.add(packNameField, 200, 200, 100, 20);
        WButton addToPackButton = new WButton(Text.translatable("gui.block_maker.button.add_to_pack"));
        addToPackButton.setOnClick(() -> {
            itemFile = new File(GenerateCustomElements.itemsFolder.getName() + File.separator + idField.getText() + ".json");
            List<File> packs = new ArrayList<>();
            listFiles(GenerateCustomElements.packsFolder, packs);
            JsonObject item = new JsonObject();
            item.addProperty("namespace", namespaceField.getText());
            item.addProperty("id", idField.getText());
            item.addProperty("display_name", displayNameField.getText());
            if (maxStackSizeField.getText().length() > 0) item.addProperty("max_stack_size", NumberConverter.convertInt(maxStackSizeField.getText()));
            if (fireproofToggle.getToggle()) item.addProperty("fireproof", fireproofToggle.getToggle());
            if (itemGroupField.getText().length() > 0) item.addProperty("item_group", itemGroupField.getText());
            if (textureNamespaceField.getText().length() > 0) item.addProperty("texture_namespace", textureNamespaceField.getText());
            item.addProperty("texture", textureField.getText());
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
                        if (!pack.has("items")) {
                            pack.add("items", new JsonArray());
                        }
                        pack.getAsJsonArray("items").add(item);
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
        root.add(addToPackButton, 110, 200, 80, 20);
        WLabel orLabel = new WLabel(Text.translatable("gui.block_maker.label.or"));
        root.add(orLabel, 95, 205);
        WTextField foodPackNameField = new WTextField(Text.literal("Pack Name"));
        foodPackNameField.setHost(this);
        foodPackNameField.setEditable(true);
        foodPackNameField.setMaxLength(50);
        foodRoot.add(foodPackNameField, 200, 300, 100, 20);
        WLabel foodOrLabel = new WLabel(Text.translatable("gui.block_maker.label.or"));
        foodRoot.add(foodOrLabel, 95, 305);
        WButton foodAddToPackButton = new WButton(Text.translatable("gui.block_maker.button.add_to_pack"));
        foodAddToPackButton.setOnClick(() -> {
            itemFile = new File(GenerateCustomElements.itemsFolder.getName() + File.separator + idField.getText() + ".json");
            List<File> packs = new ArrayList<>();
            listFiles(GenerateCustomElements.packsFolder, packs);
            JsonObject block = new JsonObject();
            block.addProperty("namespace", namespaceField.getText());
            block.addProperty("id", idField.getText());
            block.addProperty("display_name", displayNameField.getText());
            if (maxStackSizeField.getText().length() > 0) block.addProperty("max_stack_size", NumberConverter.convertInt(maxStackSizeField.getText()));
            if (fireproofToggle.getToggle()) block.addProperty("fireproof", fireproofToggle.getToggle());
            if (itemGroupField.getText().length() > 0) block.addProperty("item_group", itemGroupField.getText());
            if (textureNamespaceField.getText().length() > 0) block.addProperty("texture_namespace", textureNamespaceField.getText());
            block.addProperty("texture", textureField.getText());
            block.add("food", new JsonObject());
            JsonObject food = block.getAsJsonObject("food");
            if (nutritionField.getText().length() > 0) food.addProperty("nutrition", NumberConverter.convertInt(nutritionField.getText()));
            if (saturationField.getText().length() > 0) food.addProperty("saturation", NumberConverter.convertFloat(saturationField.getText()));
            if (isMeatToggle.getToggle()) food.addProperty("meat", isMeatToggle.getToggle());
            if (alwaysEdibleToggle.getToggle()) food.addProperty("always_edible", alwaysEdibleToggle.getToggle());
            if (eatingSpeedField.getText().length() > 0) food.addProperty("eating_speed", NumberConverter.convertInt(eatingSpeedField.getText()));
            for (File value : packs) {
                try {
                    BufferedReader packReader = new BufferedReader(new FileReader(value));
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = packReader.readLine()) != null) {
                        json.append(line);
                    }
                    JsonObject pack = new Gson().fromJson(json.toString(), JsonObject.class);
                    if (pack.get("name").getAsString().equals(foodPackNameField.getText())) {
                        if (pack.has("items")) {
                            pack.getAsJsonArray("items").add(block);
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
        foodRoot.add(foodAddToPackButton, 110, 300, 80, 20);
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
