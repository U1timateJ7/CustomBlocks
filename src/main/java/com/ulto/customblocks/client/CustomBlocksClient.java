package com.ulto.customblocks.client;

import com.ulto.customblocks.GenerateCustomElements;
import com.ulto.customblocks.gui.MakerSelectorGUI;
import com.ulto.customblocks.gui.MakerSelectorScreen;
import com.ulto.customblocks.resource.IMutableResourcePackManager;
import com.ulto.customblocks.resource.CustomResourcePackProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CustomBlocksClient implements ClientModInitializer {
    private static KeyBinding openBlockMenu;

    @Override
    public void onInitializeClient() {
        //Client Generation
        GenerateCustomElements.generateClient();
        //Keybindings
        openBlockMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.customblocks.open_menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.misc"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openBlockMenu.wasPressed()) {
                MinecraftClient.getInstance().openScreen(new MakerSelectorScreen(new MakerSelectorGUI()));
            }
        });
        //Resource Injection
        final IMutableResourcePackManager manager = (IMutableResourcePackManager) MinecraftClient.getInstance().getResourcePackManager();
        manager.customblocks$addProvider(new CustomResourcePackProvider(CustomResourcePackProvider.Type.RESOURCES));
    }
}
