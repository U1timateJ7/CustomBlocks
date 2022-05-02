package com.ulto.customblocks.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class BlockMakerScreen extends CottonClientScreen {
    public BlockMakerScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public void close() {
        this.client.setScreen(new MakerSelectorScreen(new MakerSelectorGUI()));
    }
}
