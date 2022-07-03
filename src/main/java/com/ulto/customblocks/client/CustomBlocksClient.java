package com.ulto.customblocks.client;

import com.ulto.customblocks.GenerateCustomElements;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CustomBlocksClient {
    public static File blocksFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "blocks");
    public static List<File> blocksFv0 = GenerateCustomElements.listFiles(blocksFolderFv0, ".json");
    public static File itemsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "items");
    public static List<File> itemsFv0 = GenerateCustomElements.listFiles(itemsFolderFv0, ".json");
    public static File entitiesFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "entities");
    public static List<File> entitiesFv0 = GenerateCustomElements.listFiles(entitiesFolderFv0, ".json", "models");
    public static File entityModelsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "models");
    public static List<File> entityModelsFv0 = GenerateCustomElements.listFiles(entityModelsFolderFv0, ".json");
    public static File itemGroupsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "itemgroups");
    public static List<File> itemGroupsFv0 = GenerateCustomElements.listFiles(itemGroupsFolderFv0, ".json");
    public static File paintingsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "paintings");
    public static List<File> paintingsFv0 = GenerateCustomElements.listFiles(paintingsFolderFv0, ".json");
    public static File recipesFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "recipes");
    public static List<File> recipesFv0 = GenerateCustomElements.listFiles(recipesFolderFv0, ".json");
    public static File globalEventsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "global_events");
    public static List<File> globalEventsFv0 = GenerateCustomElements.listFiles(globalEventsFolderFv0, ".json");
    public static File packsFolderFv0 = new File(Minecraft.getInstance().gameDirectory, File.separator + "packs");
    public static List<File> packsFv0 = GenerateCustomElements.listFiles(packsFolderFv0, ".json");

    public static void copyOldFiles() {
        for (File fv0 : blocksFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.blocksFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : itemsFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.itemsFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : entitiesFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.entitiesFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : entityModelsFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.entityModelsFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : itemGroupsFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.itemGroupsFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : paintingsFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.paintingsFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : recipesFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.recipesFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : globalEventsFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.globalEventsFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File fv0 : packsFv0) {
            try {
                Files.copy(fv0.toPath(), GenerateCustomElements.packsFolder.toPath().resolve(fv0.getName()));
                fv0.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blocksFolderFv0.delete();
        itemsFolderFv0.delete();
        entityModelsFolderFv0.delete();
        entitiesFolderFv0.delete();
        itemGroupsFolderFv0.delete();
        paintingsFolderFv0.delete();
        recipesFolderFv0.delete();
        globalEventsFolderFv0.delete();
        packsFolderFv0.delete();
    }
}
