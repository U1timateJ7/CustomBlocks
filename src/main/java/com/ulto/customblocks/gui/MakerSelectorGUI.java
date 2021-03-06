package com.ulto.customblocks.gui;

import com.ulto.customblocks.CustomBlocksMod;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class MakerSelectorGUI extends LightweightGuiDescription {
    public MakerSelectorGUI() {
        WPlainPanel root = new WPlainPanel();
        WScrollPanel scroll = new WScrollPanel(root);
        scroll.setHost(this);
        setRootPanel(scroll);
        root.setSize(300, 100);
        scroll.setSize(310, 110);
        WLabel topLabel = new WLabel(Text.translatable("gui.maker_selector.label.top"));
        root.add(topLabel, 120, 10);
        WButton blockButton = new WButton(Text.translatable("gui.maker_selector.button.block"));
        blockButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new BlockMakerScreen(new BlockMakerGUI())));
        root.add(blockButton, 10, 50, 90, 20);
        WButton itemButton = new WButton(Text.translatable("gui.maker_selector.button.item"));
        itemButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new BlockMakerScreen(new ItemMakerGUI())));
        root.add(itemButton, 110, 50, 90, 20);
        WButton packButton = new WButton(Text.translatable("gui.maker_selector.button.pack"));
        packButton.setOnClick(() -> MinecraftClient.getInstance().setScreen(new BlockMakerScreen(new PackTypeSelectorGUI())));
        root.add(packButton, 210, 50, 90, 20);
        WButton configButton = new WButton(Text.translatable("gui.maker_selector.button.config"));
        configButton.setOnClick(() -> Util.getOperatingSystem().open(CustomBlocksMod.customBlocksConfig));
        root.add(configButton, 110, 80, 90, 20);
    }
}
