package com.ulto.customblocks.gui;

import com.ulto.customblocks.GenerateCustomElements;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

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
        blockItemPackButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new PackMakerScreen(new BlockItemPackMakerGUI())));
        root.add(blockItemPackButton, 110, 50, 90, 20);
        WButton openFolderButton = new WButton(new TranslatableText("gui.maker.button.open_folder"));
        openFolderButton.setOnClick(() -> Util.getOperatingSystem().open(GenerateCustomElements.packsFolder));
        root.add(openFolderButton, 120, 80, 70, 20);
    }
}