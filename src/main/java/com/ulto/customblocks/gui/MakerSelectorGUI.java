package com.ulto.customblocks.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

public class MakerSelectorGUI extends LightweightGuiDescription {
    public MakerSelectorGUI() {
        WPlainPanel root = new WPlainPanel();
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        setRootPanel(scroll);
        root.setSize(300, 100);
        scroll.setSize(310, 110);
        WLabel topLabel = new WLabel(new TranslatableText("gui.maker_selector.label.top"));
        root.add(topLabel, 120, 10);
        WButton blockButton = new WButton(new TranslatableText("gui.maker_selector.button.block"));
        blockButton.setOnClick(() -> MinecraftClient.getInstance().openScreen(new BlockMakerScreen(new BlockMakerGUI())));
        root.add(blockButton, 10, 50, 90, 20);
        WButton itemButton = new WButton(new TranslatableText("gui.maker_selector.button.item"));
        itemButton.setOnClick(() -> MinecraftClient.getInstance().openScreen(new BlockMakerScreen(new ItemMakerGUI())));
        root.add(itemButton, 110, 50, 90, 20);
        WButton packButton = new WButton(new TranslatableText("gui.maker_selector.button.pack"));
        packButton.setOnClick(() -> MinecraftClient.getInstance().openScreen(new BlockMakerScreen(new PackTypeSelectorGUI())));
        root.add(packButton, 210, 50, 90, 20);
    }
}
