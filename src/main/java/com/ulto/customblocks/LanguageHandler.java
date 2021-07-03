package com.ulto.customblocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LanguageHandler {
	static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	static JsonObject languageObject = new JsonObject();
	static File langNamespace = new File(CustomResourceCreator.assets, File.separator + "lang");
	static File lang = new File(langNamespace, File.separator + "lang");
	static File en_us = new File(lang, File.separator + "en_us.json");

	public static void setupLanguage() {
		langNamespace.mkdirs();
		lang.mkdirs();

		if (!en_us.exists()) {
			try {
				en_us.createNewFile();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	public static boolean addBlockKey(JsonObject block) {
		if (block.has("namespace") && block.has("id") && block.has("display_name")) {
			String namespace = block.get("namespace").getAsString();
			String id = block.get("id").getAsString();
			String text = block.get("display_name").getAsString();
			languageObject.addProperty("block." + namespace + "." + id, text);
			return true;
		}
		return false;
	}

	public static boolean addItemKey(JsonObject item) {
		if (item.has("namespace") && item.has("id") && item.has("display_name")) {
			String namespace = item.get("namespace").getAsString();
			String id = item.get("id").getAsString();
			String text = item.get("display_name").getAsString();
			languageObject.addProperty("item." + namespace + "." + id, text);
			return true;
		}
		return false;
	}

	public static boolean addItemGroupKey(JsonObject itemGroup) {
		if (itemGroup.has("namespace") && itemGroup.has("id") && itemGroup.has("name")) {
			String namespace = itemGroup.get("namespace").getAsString();
			String id = itemGroup.get("id").getAsString();
			String text = itemGroup.get("name").getAsString();
			languageObject.addProperty("itemGroup." + namespace + "." + id, text);
			return true;
		}
		return false;
	}

	public static void saveLang() {
		try {
			FileWriter en_usfw = new FileWriter(en_us);
			en_usfw.write(gson.toJson(languageObject));
			en_usfw.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
