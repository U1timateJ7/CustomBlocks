package com.ulto.customblocks.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

public class BlockMakerScreen extends CottonClientScreen {
    public BlockMakerScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public void onClose() {
        this.client.openScreen(new MakerSelectorScreen(new MakerSelectorGUI()));
    }
}
