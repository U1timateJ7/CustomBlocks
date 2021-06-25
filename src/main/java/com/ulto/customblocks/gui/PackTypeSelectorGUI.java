package com.ulto.customblocks.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.ulto.customblocks.GenerateCustomElements;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.dynamic.RegistryReadingOps;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class PackTypeSelectorGUI extends LightweightGuiDescription {
    public PackTypeSelectorGUI() {
        WPlainPanel root = new WPlainPanel();
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        setRootPanel(scroll);
        root.setSize(300, 100);
        scroll.setSize(310, 110);
        WLabel topLabel = new WLabel(new TranslatableText("gui.pack_type_selector.label.top"));
        root.add(topLabel, 108, 10);
        WButton blockItemPackButton = new WButton(new TranslatableText("gui.pack_type_selector.button.block_item_pack"));
        blockItemPackButton.setOnClick(() -> MinecraftClient.getInstance().openScreen(new PackMakerScreen(new BlockItemPackMakerGUI())));
        root.add(blockItemPackButton, 110, 50, 90, 20);
        //WButton woodPackButton = new WButton(new TranslatableText("gui.pack_type_selector.button.wood_pack"));
        //woodPackButton.setOnClick(() -> MinecraftClient.getInstance().openScreen(new PackMakerScreen(new WoodPackMakerGUI())));
        //root.add(woodPackButton, 110, 50, 90, 20);
        WButton openFolderButton = new WButton(new TranslatableText("gui.maker.button.open_folder"));
        openFolderButton.setOnClick(() -> Util.getOperatingSystem().open(GenerateCustomElements.packsFolder));
        root.add(openFolderButton, 120, 80, 70, 20);
    }
}