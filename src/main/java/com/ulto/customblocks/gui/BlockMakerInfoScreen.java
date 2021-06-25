package com.ulto.customblocks.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class BlockMakerInfoScreen extends CottonClientScreen {
    public BlockMakerInfoScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public void onClose() {
        this.client.openScreen(new BlockMakerScreen(new BlockMakerGUI()));
    }
}
