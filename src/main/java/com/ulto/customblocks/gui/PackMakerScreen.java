package com.ulto.customblocks.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class PackMakerScreen extends CottonClientScreen {
    public PackMakerScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public void onClose() {
        this.client.setScreen(new BlockMakerScreen(new PackTypeSelectorGUI()));
    }
}
