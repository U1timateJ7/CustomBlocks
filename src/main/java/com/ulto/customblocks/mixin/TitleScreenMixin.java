package com.ulto.customblocks.mixin;

import com.ulto.customblocks.gui.MakerSelectorGUI;
import com.ulto.customblocks.gui.MakerSelectorScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addCustomButtons(CallbackInfo ci) {
        int jj = this.height / 4 + 48;
        ButtonWidget.TooltipSupplier tooltipSupplier = (buttonWidget, matrixStack, i, j) -> this.renderOrderedTooltip(matrixStack, this.client.textRenderer.wrapLines(Text.translatable("title.maker.tooltip"), Math.max(this.width / 2 - 43, 170)), i, j);
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, jj + 72 + 12 - 36, 20, 20, 0, 0, 20, new Identifier("custom_blocks", "textures/gui/block_menu_icon.png"), 20, 40, (buttonWidget) -> {
            this.client.setScreen(new MakerSelectorScreen(new MakerSelectorGUI()));
        }, tooltipSupplier, Text.translatable("narrator.button.blockmenu")));
    }
}
